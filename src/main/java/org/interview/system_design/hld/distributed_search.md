# System Design: Uber / Lyft

## 1. Requirements

### Functional Requirements
- Rider can request a ride from current location to destination
- System matches rider to nearest available driver
- Real-time GPS tracking of driver/trip on a map
- Fare estimation before booking; final fare calculation after trip
- Trip state machine: REQUESTED → MATCHED → DRIVER_ARRIVING → IN_PROGRESS → COMPLETED → PAID
- Rider and driver can rate each other after trip
- Driver can go online/offline; rider can cancel before pickup

### Non-Functional Requirements
- **Scale**: 15M rides/day (~175 rides/sec peak ~2×), 3M active drivers globally
- **Driver location updates**: every 10 seconds while online (~300K location events/sec)
- **Matching latency**: < 2 seconds to find and notify a driver
- **Tracking latency**: < 5 seconds end-to-end GPS refresh on rider's screen
- **Availability**: 99.99% uptime (ride requests must never silently fail)
- **Consistency**: Strong consistency for trip state; eventual for driver location
- **Geo coverage**: global, multi-region

---

## 2. Capacity Estimation

### QPS
- 15M rides/day → ~175 rides/sec (avg), ~500 rides/sec (peak)
- 3M online drivers × 1 update/10s = **300,000 location writes/sec**
- 15M riders polling map every 3s during trip ≈ 5M concurrent trips at peak → ~1.7M map reads/sec

### Storage
- **Trip records**: 15M/day × 365 × 5 years × 1KB = ~27 TB
- **Driver location (in-memory)**: 3M drivers × 100B = ~300 MB (fits in Redis)
- **Historical GPS traces**: 15M trips × avg 30 min × 1 update/10s × 50B = ~135 GB/day

### Bandwidth
- Location updates: 300K/s × 100B = ~30 MB/s inbound
- Map tile + driver icon pushes: 1.7M reads/s × 500B = ~850 MB/s outbound

---

## 3. High-Level Architecture

```
Rider App          Driver App
    |                  |
    |  HTTPS/WS        |  HTTPS/WS
    v                  v
+-------------------------------------------+
|           API Gateway / Load Balancer      |
+-------------------------------------------+
     |           |            |          |
     v           v            v          v
  Ride       Location      Trip       Payment
 Request     Service       Service    Service
 Service         |            |
     |       Geo Index     Trip DB
     |      (Redis/S2)    (Postgres)
     |           |
     v           v
  Matching    Kafka
  Service    (location
     |         stream)
     |
  Driver     Notification
  Pool       Service
             (WebSocket/
              Push)
```

```
                        ┌────────────────────────────────────────────────┐
                        │                 UBER SYSTEM                    │
                        └────────────────────────────────────────────────┘

  ┌──────────┐  WS/HTTP  ┌─────────────┐      ┌─────────────────────┐
  │ Rider    │◄─────────►│  API GW +   │      │   Ride Request Svc  │
  │ Mobile   │           │  Auth (JWT) │─────►│   (stateless pods)  │
  └──────────┘           └─────────────┘      └────────┬────────────┘
                                  │                     │
  ┌──────────┐  WS/HTTP           │            ┌────────▼────────────┐
  │ Driver   │◄──────────────────►│            │   Matching Service  │
  │ Mobile   │                   │            │  (Geo query + score)│
  └──────────┘                   │            └────────┬────────────┘
        │                        │                     │
        │ location update        │            ┌────────▼────────────┐
        ▼                        │            │  Driver Location DB │
  ┌─────────────┐                │            │  Redis Geo / S2     │
  │ Location Svc│──►Kafka───────►│            └─────────────────────┘
  └─────────────┘                │
                                 │            ┌─────────────────────┐
                                 └───────────►│   Trip Service      │
                                              │   Postgres + FSM    │
                                              └─────────────────────┘
```

---

## 4. Core Components

### 4.1 API Gateway
- Handles auth (JWT validation), rate limiting, TLS termination
- Routes to downstream microservices
- Maintains persistent WebSocket connections for drivers and active riders

### 4.2 Ride Request Service
- Accepts rider request (origin, destination, ride type)
- Calls ETA service (Google Maps or internal routing) for fare estimate
- Creates a trip record in state REQUESTED
- Publishes ride_requested event to Kafka

### 4.3 Location Service
- Receives GPS pings from driver apps every 10 seconds
- Writes to Kafka topic `driver-location` (driver_id, lat, lng, heading, timestamp)
- Consumers update Redis GeoSet (GEOADD) keyed by city/zone

### 4.4 Matching Service
- Consumes ride_requested events
- Queries Redis GEORADIUS to find all online drivers within radius (start 2km, expand to 5km if none)
- Scores drivers by ETA + acceptance rate + rating
- Sends push notification to top-N drivers; first to accept wins
- Updates trip state to MATCHED, writes driver_id to trip record

### 4.5 Trip Service
- Owns the trip state machine (FSM)
- Persists all state transitions to Postgres with timestamps
- Emits events on each transition for downstream services (billing, analytics)

### 4.6 Real-Time Tracking Service
- Maintains WebSocket connections (or SSE) to active riders
- Subscribes to Kafka `driver-location`; filters by trip's active driver_id
- Pushes location diffs to rider's WebSocket every 5s

### 4.7 Fare / Surge Pricing Engine
- Base fare = base + per_minute × time + per_km × distance
- Surge multiplier calculated per geohash zone: demand / supply ratio
- Surge zones recomputed every 1 minute using Lambda architecture

### 4.8 Payment Service
- Triggered on COMPLETED transition
- Calls Stripe/Braintree to charge rider's saved card
- Handles refunds, promotions, driver payouts (weekly batch)

### 4.9 Notification Service
- Driver match requests: APNS / FCM push
- Trip events: push + in-app WebSocket messages
- Retry with exponential backoff for push failures

---

## 5. Data Models

### trips table (Postgres, sharded by city_id)
```sql
CREATE TABLE trips (
  trip_id       UUID PRIMARY KEY,
  rider_id      BIGINT NOT NULL,
  driver_id     BIGINT,
  city_id       INT NOT NULL,
  status        VARCHAR(20) NOT NULL,   -- REQUESTED|MATCHED|IN_PROGRESS|COMPLETED|CANCELLED
  origin_lat    DOUBLE PRECISION,
  origin_lng    DOUBLE PRECISION,
  dest_lat      DOUBLE PRECISION,
  dest_lng      DOUBLE PRECISION,
  requested_at  TIMESTAMPTZ NOT NULL,
  matched_at    TIMESTAMPTZ,
  picked_up_at  TIMESTAMPTZ,
  completed_at  TIMESTAMPTZ,
  fare_estimate NUMERIC(8,2),
  fare_final    NUMERIC(8,2),
  surge_mult    NUMERIC(4,2) DEFAULT 1.0,
  distance_km   NUMERIC(6,2),
  duration_sec  INT
);
```

### driver_locations (Redis GeoSet per zone)
```
Key: geo:zone:{zone_id}
GEOADD geo:zone:NYC driver_id lng lat
GEORADIUSBYMEMBER geo:zone:NYC origin_point 3 km ASC COUNT 50
```

### drivers table (Postgres)
```sql
CREATE TABLE drivers (
  driver_id     BIGINT PRIMARY KEY,
  status        VARCHAR(10),   -- ONLINE | OFFLINE | ON_TRIP
  vehicle_type  VARCHAR(20),
  rating        NUMERIC(3,2),
  zone_id       INT,
  last_seen_at  TIMESTAMPTZ
);
```

### ratings table
```sql
CREATE TABLE ratings (
  trip_id    UUID,
  rater_id   BIGINT,
  ratee_id   BIGINT,
  score      SMALLINT CHECK (score BETWEEN 1 AND 5),
  comment    TEXT,
  rated_at   TIMESTAMPTZ
);
```

---

## 6. API Design

### Rider APIs
```
POST /v1/rides/estimate
  Body: { origin: {lat, lng}, destination: {lat, lng}, ride_type: "UberX" }
  Response: { fare_estimate, surge_multiplier, eta_minutes }

POST /v1/rides
  Body: { origin, destination, ride_type, payment_method_id }
  Response: { trip_id, status: "REQUESTED" }

GET  /v1/rides/{trip_id}
  Response: { trip_id, status, driver: {name, rating, location, eta}, fare_estimate }

DELETE /v1/rides/{trip_id}
  Response: { status: "CANCELLED", cancellation_fee }
```

### Driver APIs
```
PUT  /v1/drivers/me/location
  Body: { lat, lng, heading, speed }
  Response: 204

PUT  /v1/drivers/me/status
  Body: { status: "ONLINE" | "OFFLINE" }

POST /v1/drivers/me/trips/{trip_id}/accept
POST /v1/drivers/me/trips/{trip_id}/reject
POST /v1/drivers/me/trips/{trip_id}/arrive
POST /v1/drivers/me/trips/{trip_id}/start
POST /v1/drivers/me/trips/{trip_id}/complete
```

### WebSocket Events (server → client)
```json
// To rider during matching
{ "type": "DRIVER_MATCHED", "driver": { "name": "John", "lat": 37.77, "lng": -122.4, "eta": 4 } }

// Location update during trip
{ "type": "LOCATION_UPDATE", "lat": 37.78, "lng": -122.41, "eta_minutes": 2 }

// Trip completed
{ "type": "TRIP_COMPLETED", "fare": 18.50, "duration_sec": 720, "distance_km": 8.3 }
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 Geospatial Indexing
- **Redis GeoSet** uses Geohash (52-bit integer) internally: O(log N) GEORADIUS queries
- For global scale: partition GeoSets by **city / zone** to avoid single hot key
- Alternative: **S2 Geometry library** (Google) — hierarchical cells, used by Uber internally
  - H3 (Uber's open-source hexagonal grid) used for surge pricing zones (uniform area cells)
- **Quad-tree** for offline indexing in analytics, but too slow for real-time updates

### 7.2 Driver Location Updates
- 300K writes/sec — Kafka absorbs the burst; consumers update Redis
- Each update is idempotent (latest wins); no need for strong ordering
- Drivers that haven't sent update in 30s marked OFFLINE automatically (TTL or heartbeat check)

### 7.3 Matching Algorithm
- GEORADIUS returns N candidates sorted by distance
- Score = α × ETA + β × (1 - acceptance_rate) + γ × (5 - avg_rating)
- Dispatch to top-3 drivers simultaneously; start a 15s timer
- If no acceptance: expand radius and retry (up to 3 rounds)
- Prevent double-dispatch: Redis SET NX lock on driver_id during pending state

### 7.4 Trip State Machine
```
REQUESTED ──► MATCHED ──► DRIVER_ARRIVING ──► IN_PROGRESS ──► COMPLETED
    │              │                                                │
    └──CANCELLED◄──┘                                         ──► PAID
```
- Transitions validated server-side (only allowed transitions accepted)
- Optimistic locking on `version` column to prevent concurrent state corruption

### 7.5 Surge Pricing
- Zones defined by H3 hex grid at resolution 7 (~1.2 km² cells)
- Every 60s: count ride requests vs online drivers per zone
- Surge = max(1.0, demand/supply × 1.2), capped at 5.0
- Stored in Redis with 90s TTL; riders shown surge on estimate

### 7.6 WebSocket Scalability
- Sticky sessions via consistent hashing on trip_id at load balancer
- Each tracking server node subscribes to Kafka partition subset
- Heartbeat every 30s; reconnect logic on client with exponential backoff

---

## 8. Scalability & Bottlenecks

| Component | Bottleneck | Solution |
|---|---|---|
| Location ingestion | 300K writes/sec | Kafka partitioned by driver_id; Redis Cluster |
| Matching | Geo query on hot zone | Shard GeoSet by zone; read replica Redis |
| Trip DB writes | High TPS on Postgres | Shard by city_id; CQRS with read replicas |
| WebSocket connections | 5M concurrent | Horizontal scale; multiplexed on nginx/Envoy |
| Fare calc | CPU-bound | Pre-cache route matrices; async recalc |

- **Hot zones** (concerts, airports): pre-warm surge calculations, increase driver incentives
- **Database sharding**: trips sharded by city_id; cross-shard queries via aggregation service
- **Read replicas** for trip history queries; primary only for writes

---

## 9. Fault Tolerance & Reliability

- **Kafka**: replicas=3, acks=all for location stream; replayable on consumer failure
- **Redis Sentinel / Cluster**: automatic failover; driver locations reconstructed from Kafka replay
- **Postgres**: synchronous streaming replication; automated failover via Patroni
- **Circuit breakers** (Hystrix/Resilience4j) on all inter-service calls
- **Idempotent APIs**: all state transitions include idempotency key to prevent double-processing
- **Trip recovery**: on app restart, client fetches current trip state to resync
- **Payment retries**: failed charges retried 3× with backoff; fallback to manual review queue
- **Multi-region active-passive**: global traffic manager routes to healthy region; RTO < 30s

---

## 10. Trade-offs & Alternatives

| Decision | Chosen | Alternative | Reason |
|---|---|---|---|
| Location store | Redis GeoSet | PostGIS | Sub-ms reads; PostGIS for analytics only |
| Geo indexing | S2/H3 | Quad-tree | Dynamic updates; quad-tree expensive to rebuild |
| Messaging | WebSocket | Long Poll | Lower latency; Long Poll simpler but 2× requests |
| Trip DB | Postgres (sharded) | Cassandra | Strong consistency for FSM; Cassandra for history |
| Matching | Greedy nearest | Hungarian algo | Latency; Hungarian optimal but O(n³) |
| Feed model | Push (Kafka) | Pull (polling) | Real-time; polling increases latency and load |
