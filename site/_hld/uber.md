---
layout: hld
title: "Uber"
system: uber
description: "- **Shorten URL**: Given a long URL, generate a unique short URL (e.g., `https://short.ly/aB3kR2x`)"
---

# System Design: URL Shortener (bit.ly / TinyURL)

## 1. Requirements

### Functional Requirements
- **Shorten URL**: Given a long URL, generate a unique short URL (e.g., `https://short.ly/aB3kR2x`)
- **Redirect**: GET `/<short_code>` → redirect to original long URL (301 or 302)
- **Custom Aliases**: Users can optionally provide a custom short code (e.g., `/my-promo`)
- **Expiry**: URLs can have a TTL (time-to-live); expired URLs return 410 Gone
- **Analytics**: Track click count, referrer, geo-location, device type per short URL
- **Delete / Update**: URL owners can deactivate or update destination

### Non-Functional Requirements
- **Scale**: 100M new URLs/day writes; 10:1 read:write → 1B redirects/day
- **Latency**: Redirect p99 < 10ms (cache-heavy path)
- **Availability**: 99.99% uptime (< 53 min downtime/year)
- **Durability**: No data loss for stored URLs
- **Consistency**: Eventual consistency acceptable for analytics; strong consistency for URL creation (no duplicates)
- **Storage**: ~500 bytes/URL → 100M × 500B = 50GB/day → ~18TB/year

---

## 2. Capacity Estimation

### QPS
```
Write QPS  = 100M / 86,400 ≈ 1,160 writes/sec   (peak ~3x = 3,500/sec)
Read  QPS  = 1B   / 86,400 ≈ 11,600 reads/sec   (peak ~3x = 35,000/sec)
```

### Storage
```
Short code:    7 chars  =   7 B
Long URL:      avg 200B = 200 B
Metadata:      user_id, created_at, expiry, custom_flag ≈ 100 B
Analytics row: per click ≈ 100 B

Per URL record: ~500 B
5-year total:  100M × 365 × 5 × 500B ≈ 91 TB (URLs table)
Analytics:     1B clicks/day × 100B × 365 × 5 ≈ 180 TB
```

### Bandwidth
```
Inbound  (write):   3,500 req/s × 2KB  ≈  7 MB/s
Outbound (redirect): 35,000 req/s × 1KB ≈ 35 MB/s
```

### Cache
```
80/20 rule: 20% of URLs drive 80% of traffic
Hot set: top 20M URLs × 500B = 10GB → fits in a single Redis node
```

---

## 3. High-Level Architecture

```
                            ┌──────────────────────────────────────────────┐
                            │                   Clients                    │
                            └───────────┬───────────────────┬──────────────┘
                                        │                   │
                              POST /shorten             GET /<code>
                                        │                   │
                            ┌───────────▼───────────────────▼──────────────┐
                            │              API Gateway / LB                 │
                            │        (rate limiting, SSL termination)       │
                            └───────────┬───────────────────┬──────────────┘
                                        │                   │
                         ┌──────────────▼──────┐  ┌────────▼────────────────┐
                         │  Write Service       │  │   Redirect Service       │
                         │  (URL Shortener)     │  │   (Read-heavy)           │
                         └──────────────┬───────┘  └──────┬──────────────────┘
                                        │                  │
                         ┌──────────────▼───────┐  ┌──────▼──────┐
                         │   ID Generator        │  │  Redis Cache │
                         │   (Zookeeper/Snowflake)│  │  (Hot URLs)  │
                         └──────────────┬────────┘  └──────┬───────┘
                                        │                  │ (miss)
                         ┌──────────────▼──────────────────▼──────────────┐
                         │               Primary DB (Cassandra / MySQL)    │
                         │               url_mappings table                │
                         └─────────────────────────┬───────────────────────┘
                                                    │
                                        ┌───────────▼───────────┐
                                        │   Analytics Pipeline   │
                                        │  Kafka → Flink → OLAP  │
                                        └───────────────────────┘
```

---

## 4. Core Components

### 4.1 API Gateway / Load Balancer
- **Nginx / AWS ALB** handles SSL termination, distributes traffic across stateless services
- Rate limiting per IP/user to prevent abuse (10 shortens/min for free tier)
- Routes `/shorten` to Write Service, `/<code>` to Redirect Service

### 4.2 Write Service (URL Shortener)
- Validates input URL (regex + reachability check optional)
- Generates short code via **ID Generator** (see §7)
- Stores mapping in primary DB
- Optionally validates custom alias uniqueness
- Returns short URL to client

### 4.3 ID Generator
- **Counter-based**: Zookeeper maintains a global counter; each Write Service gets a range (e.g., 1000–2000). Encode counter to Base62 → guaranteed unique, sequential, predictable length
- **Hash-based**: MD5(long_url) → take first 7 chars of Base62 encoding. Risk: collisions (resolve by appending +1 or checking DB)
- **Snowflake-style**: 64-bit ID = timestamp + machine_id + sequence. Sortable by time

### 4.4 Redirect Service
- Stateless, horizontally scaled
- Lookup order: **L1 local cache (in-process, 5s TTL)** → **Redis** → **DB**
- Returns HTTP **302** (analytics tracking; browser won't cache) or **301** (browser caches; reduces load but kills analytics)
- Checks URL expiry; returns 410 if expired

### 4.5 Redis Cache (Hot URL Cache)
- Caches `short_code → long_url` mappings
- LRU eviction; TTL matches URL expiry
- Cluster mode: 3 shards × 2 replicas = 6 nodes
- Write-through on URL creation; write-invalidate on URL update/delete

### 4.6 Primary Database
- **Cassandra**: Natural partition key = `short_code`; scales horizontally; excellent read/write throughput
- Schema: `url_mappings` (short_code PK, long_url, user_id, created_at, expires_at, custom)
- Replication factor 3 across AZs; `LOCAL_QUORUM` for consistency

### 4.7 Analytics Pipeline
- Click events emitted to **Kafka** (topic: `click_events`)
- **Flink** job aggregates by short_code, hour, geo, referrer
- Results stored in **ClickHouse** for dashboard queries
- Real-time click counter in Redis `INCR short_code:clicks`

---

## 5. Data Models

### url_mappings (Cassandra)
```
short_code    TEXT         PRIMARY KEY   -- e.g., "aB3kR2x"
long_url      TEXT                       -- original URL
user_id       UUID                       -- owner
created_at    TIMESTAMP
expires_at    TIMESTAMP                  -- NULL = never expires
is_active     BOOLEAN
custom_alias  BOOLEAN
click_count   COUNTER                    -- approximate
```

### click_events (Kafka / ClickHouse)
```
event_id      UUID
short_code    TEXT
clicked_at    TIMESTAMP
ip_address    TEXT       -- hashed for privacy
country       TEXT       -- geo-resolved
city          TEXT
referrer      TEXT
user_agent    TEXT
device_type   TEXT       -- mobile/desktop/bot
```

### users (MySQL)
```
user_id       UUID        PRIMARY KEY
email         VARCHAR(255) UNIQUE
api_key       VARCHAR(64)  UNIQUE INDEX
tier          ENUM('free','pro','enterprise')
created_at    TIMESTAMP
```

---

## 6. API Design

### Shorten URL
```
POST /api/v1/shorten
Authorization: Bearer <api_key>
Content-Type: application/json

{
  "long_url":    "https://www.example.com/very/long/path?query=value",
  "custom_alias": "my-promo",           // optional
  "expires_in":   86400                 // seconds, optional
}

Response 201:
{
  "short_url":  "https://short.ly/aB3kR2x",
  "short_code": "aB3kR2x",
  "expires_at": "2025-01-15T10:00:00Z"
}
```

### Redirect
```
GET /aB3kR2x

Response 302 Found
Location: https://www.example.com/very/long/path?query=value

Response 410 Gone   (expired)
Response 404        (not found)
```

### Get Analytics
```
GET /api/v1/urls/{short_code}/analytics?from=2025-01-01&to=2025-01-07&granularity=day
Authorization: Bearer <api_key>

Response 200:
{
  "short_code":    "aB3kR2x",
  "total_clicks":  142857,
  "timeseries":    [{"date": "2025-01-01", "clicks": 12000}, ...],
  "top_countries": [{"country": "US", "clicks": 80000}, ...],
  "top_referrers": [{"referrer": "twitter.com", "clicks": 45000}, ...]
}
```

### Delete URL
```
DELETE /api/v1/urls/{short_code}
Authorization: Bearer <api_key>

Response 204 No Content
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 Short Code Generation: Counter vs Hash

| Approach       | Pros                               | Cons                               |
|----------------|------------------------------------|------------------------------------|
| Counter+Base62 | Guaranteed unique, 7 chars at 62^7=3.5T capacity | Single point of failure if counter service down; sequential (predictable) |
| MD5 Hash       | No central dependency              | Collisions possible (handle with retry or append); not sequential |
| Snowflake ID   | Distributed, time-sortable, unique | Slightly longer code (10+ chars)   |

**Recommended**: Counter with Zookeeper range allocation (each server gets batch of 1000 IDs → reduces Zookeeper calls 1000x).

### 7.2 Base62 Encoding
```
Characters: [0-9a-zA-Z] = 62 chars
7 characters → 62^7 = 3,521,614,606,208 ≈ 3.5 trillion unique codes
At 100M/day: 3.5T / 100M = 35,000 days ≈ 95 years capacity
```

### 7.3 Redirect: 301 vs 302

| Code | Name              | Browser Caches? | Analytics Tracked? | Use Case                  |
|------|-------------------|-----------------|--------------------|---------------------------|
| 301  | Moved Permanently | Yes             | Only first visit   | Static URLs, reduce load  |
| 302  | Found (Temporary) | No              | Every visit        | Analytics required        |

**Decision**: Use **302** by default to capture all click analytics. Offer 301 as opt-in for enterprise (CDN-friendly links).

### 7.4 Caching Strategy (80/20 Rule)
- 20% of URLs → 80% of traffic
- Cache top 20M URLs in Redis (10GB)
- **Cache-aside** pattern for redirect service
- TTL aligned with URL expiry; proactive invalidation on delete
- In-process LRU cache (Caffeine, 100K entries, 5s TTL) → Redis → DB

### 7.5 Sharding Strategy
- **Partition by `short_code` hash** in Cassandra → even distribution
- No hot spots since short codes are uniformly distributed
- Avoid user_id as partition key (celebrity user problem)

### 7.6 Custom Alias Handling
- Check uniqueness before commit: `SELECT short_code FROM url_mappings WHERE short_code = 'my-promo'`
- Cassandra lightweight transactions (LWT) for compare-and-set
- Alternatively: distributed lock via Redis `SET NX` with 5s TTL

---

## 8. Scalability & Bottlenecks

### Bottleneck 1: Redirect Latency
- **Solution**: Multi-tier caching (in-process → Redis → DB)
- Deploy redirect service in multiple regions; route via geo-DNS
- CDN edge caching for 301 redirects

### Bottleneck 2: Write Throughput
- ID Generator range allocation reduces Zookeeper calls
- Write Service sharded by short code prefix
- Async analytics write (Kafka decouples from critical path)

### Bottleneck 3: Analytics Aggregation
- Pre-aggregate per hour in Flink; store in ClickHouse
- For real-time counters: Redis INCR (atomic, fast)
- For historical analysis: batch jobs on ClickHouse

### Horizontal Scaling
```
Write Service:    Stateless → add instances behind LB
Redirect Service: Stateless → add instances; auto-scale on CPU
Redis:            Cluster mode with 3+ shards
Cassandra:        Add nodes; automatic data rebalancing
Kafka:            Add partitions/brokers for higher throughput
```

---

## 9. Fault Tolerance & Reliability

### Single Points of Failure
| Component       | Failure Mode                | Mitigation                                      |
|-----------------|-----------------------------|-------------------------------------------------|
| Zookeeper       | Counter unavailable         | Range pre-allocation; switch to hash-based      |
| Redis           | Cache miss storm            | Circuit breaker; fallback to DB; replica reads  |
| Cassandra nodes | Node failure                | RF=3; `LOCAL_QUORUM`; automatic repair          |
| Kafka           | Broker failure              | Replication factor 3; ISR                       |
| API Gateway     | Single LB failure           | Active-active multi-AZ LB                       |

### Health Checks & Circuit Breakers
- Redirect Service: circuit breaker to Redis (fallback: direct DB)
- Write Service: circuit breaker to Zookeeper (fallback: hash mode)
- Kafka consumer lag monitoring → alert if > 1M messages

### Data Durability
- Cassandra: commitlog fsync before ack; RF=3
- Kafka: `acks=all`, `min.insync.replicas=2`
- Daily snapshots to S3 for Cassandra

### Multi-Region Active-Active
```
Region US-East  ←──── Global LB (latency-based) ────→  Region EU-West
    │                                                        │
Cassandra DC1  ────── async cross-DC replication ────  Cassandra DC2
Redis Primary  ────── (separate cluster per region)    Redis Primary
```

---

## 10. Trade-offs & Alternatives

### Trade-off 1: Cassandra vs MySQL
- **Cassandra**: Better write scalability, horizontal sharding built-in. Cons: no joins, eventual consistency
- **MySQL**: ACID, easier joins for analytics. Cons: vertical scaling limit, sharding complexity
- **Decision**: Cassandra for URL mappings (write-heavy, simple lookups); MySQL for user management

### Trade-off 2: Analytics Accuracy vs Performance
- **Exact counts**: Every click hits DB counter → OLTP bottleneck
- **Approximate**: Redis INCR (fast but may lose data on crash) + Kafka async aggregation
- **Decision**: Redis for real-time approximate counts; Kafka+Flink for accurate historical data

### Trade-off 3: In-Process Cache vs Redis Only
- In-process cache eliminates network hop (< 0.1ms vs 1ms)
- Risk: stale data window (5s); memory pressure per instance
- **Decision**: 2-tier cache acceptable given eventual consistency for analytics

### Alternative: Precomputed Short Codes
- Offline batch job pre-generates millions of unique codes, stores in a "pool" table
- Write service pops from pool → no synchronization needed at write time
- Complexity: managing pool size, replenishment jobs, expiry of unused codes

### Alternative: Geographically Local Short Codes
- Embed region prefix in short code (e.g., `us-aB3kR2x`)
- Route redirects to nearest region's DB
- Reduces cross-region latency but complicates global deduplication
