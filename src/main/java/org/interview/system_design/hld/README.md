# 🌐 High-Level Design (HLD) — 20 Problems

> **Goal:** For each system, articulate requirements, capacity estimates, architecture
> diagrams, data models, API contracts, and informed trade-off decisions.

---

## 🧠 HLD Interview Framework

Use this structured approach for every design question:

```
1. Clarify Requirements    → Functional + Non-Functional (scale, latency, consistency)
2. Capacity Estimation     → QPS, storage, bandwidth (back-of-envelope)
3. API Design              → REST/gRPC endpoints, request/response shapes
4. High-Level Architecture → Component diagram with data flows
5. Data Model              → Schema, partitioning strategy
6. Deep Dives              → Bottlenecks, caching, sharding, replication
7. Trade-offs              → CAP theorem, consistency models, cost
```

---

## 📋 Problem Index

| # | Problem | File | Core Themes |
|---|---------|------|-------------|
| 1 | **Design Uber** | [`uber.md`](uber.md) | Geospatial indexing · Driver matching · Surge pricing · WebSocket tracking |
| 2 | **Design WhatsApp** | [`whatsapp.md`](whatsapp.md) | XMPP/WebSocket · Message ordering · E2E encryption · Presence |
| 3 | **Design Google Drive** | [`google_drive.md`](google_drive.md) | Chunked upload · OT/CRDT collaboration · ACL · Versioning |
| 4 | **Design Dropbox** | [`dropbox.md`](dropbox.md) | Content-addressable blocks · Delta sync · LAN sync · Dedup |
| 5 | **Design Instagram Feed** | [`instagram_feed.md`](instagram_feed.md) | Fanout-on-write · Hybrid push/pull · Redis sorted sets · CDN |
| 6 | **Design YouTube** | *(coming soon)* | HLS/DASH transcoding · CDN POPs · HyperLogLog view counts |
| 7 | **Design Amazon Product Catalog** | *(coming soon)* | Elasticsearch · Faceted search · Inventory · Price management |
| 8 | **Design Rate Limiter** | *(coming soon)* | Token bucket · Sliding window · Redis Lua · Distributed counters |
| 9 | **Design Notification System** | *(coming soon)* | Kafka per channel · Priority queues · Template engine · Retry |
| 10 | **Design Distributed Cache** | *(coming soon)* | Consistent hashing · LRU/LFU eviction · AOF/RDB · Cache stampede |
| 11 | **Design URL Shortener** | *(coming soon)* | Base62 · Counter vs hash · Redirect analytics · Hot URL cache |
| 12 | **Design Distributed Search** | [`distributed_search.md`](distributed_search.md) | Inverted index · BM25 · Scatter-gather · Shard sizing |
| 13 | **Design Payment System** | [`payment_system.md`](payment_system.md) | Idempotency · Double-entry ledger · Saga · PCI DSS · Reconciliation |
| 14 | **Design Kafka** | *(coming soon)* | Log-structured storage · ISR · Exactly-once · KRaft leader election |
| 15 | **Design Feature Flags** | *(coming soon)* | SSE streaming · Consistent % rollout · SDK evaluation · Audit log |
| 16 | **Design API Gateway** | *(coming soon)* | Reverse proxy · Plugin chain · Circuit breaker · JWT validation |
| 17 | **Design Recommendations** | *(coming soon)* | Two-tower model · Feature store · Explore/exploit · Cold start |
| 18 | **Design Inventory System** | *(coming soon)* | Optimistic locking · Event sourcing · CQRS · Saga for oversell |
| 19 | **Design Global Product Catalog** | *(coming soon)* | EAV vs JSON schema · Localisation · Import pipeline · Versioning |
| 20 | **Design Real-time Analytics** | *(coming soon)* | Lambda/Kappa arch · Flink · ClickHouse · HLL/Count-Min Sketch |

---

## 🗂️ Architecture Patterns Covered

| Pattern | Problems |
|---------|---------|
| **Event-driven / Kafka** | Notification, Kafka, Analytics, Instagram Feed |
| **CQRS** | Inventory, Search, Analytics |
| **Event Sourcing** | Payment, Inventory, Catalog |
| **Saga (distributed transactions)** | Payment, Inventory, Cab Booking |
| **Two-phase fanout** | WhatsApp, Instagram Feed |
| **Content-addressable storage** | Dropbox, Google Drive |
| **Consistent hashing** | Distributed Cache, Rate Limiter, URL Shortener |
| **Two-tower ML** | Recommendations, YouTube |
| **Columnar OLAP** | Analytics (ClickHouse/Druid) |
| **Read replicas + write sharding** | All databases at scale |

---

## 📊 Key Capacity Numbers (Memorise These)

| Metric | Value |
|--------|-------|
| 1 day in seconds | **~86,400** |
| 1 month in seconds | **~2.5M** |
| 1 billion requests/day → QPS | **~11,600 QPS** |
| 1M QPS → requests/day | **~86B** |
| 1KB × 1M rows | **~1 GB** |
| 1MB × 1M files | **~1 TB** |
| SSD random read | **~100 µs** |
| Network round-trip (same DC) | **~0.5 ms** |
| Network round-trip (cross-region) | **~100–200 ms** |
| MySQL single-node RPS | **~5,000–10,000** |
| Redis single-node RPS | **~100,000–1,000,000** |
| Kafka partition throughput | **~100 MB/s** |

---

## 🔑 Key Topics to Discuss in Every Interview

### Database & Storage
- [ ] SQL vs NoSQL selection rationale
- [ ] Sharding strategy (hash, range, directory)
- [ ] Replication (sync vs async, quorum writes)
- [ ] Indexing choices and query patterns
- [ ] Read replicas for read-heavy workloads

### Caching
- [ ] Cache-aside vs read-through vs write-through vs write-behind
- [ ] TTL and eviction policy (LRU, LFU)
- [ ] Cache invalidation strategy
- [ ] Hot-key problem and local L1 cache mitigation

### Messaging & Event Streaming
- [ ] At-most-once / at-least-once / exactly-once semantics
- [ ] Kafka partition ordering guarantees
- [ ] Consumer group and offset management
- [ ] Dead letter queues for poison messages

### Scalability
- [ ] Horizontal vs vertical scaling
- [ ] Stateless services (easier to scale)
- [ ] CDN for static assets and media
- [ ] Auto-scaling policies (CPU, RPS, queue depth)

### Consistency & Availability
- [ ] CAP theorem: pick two — Consistency, Availability, Partition Tolerance
- [ ] Consistency models: strong → linearisable → sequential → causal → eventual
- [ ] Idempotency keys for safe retries
- [ ] Distributed transactions: 2PC vs Saga (choreography vs orchestration)

### Reliability & Fault Tolerance
- [ ] Circuit breaker pattern (closed → open → half-open)
- [ ] Retry with exponential backoff + jitter
- [ ] Bulkhead isolation
- [ ] Multi-region active-active vs active-passive
- [ ] Health checks and load balancer failover

### Observability
- [ ] Metrics: latency P50/P95/P99, error rate, throughput (USE/RED method)
- [ ] Distributed tracing (trace ID propagation, spans)
- [ ] Structured logging with correlation IDs
- [ ] Alerting on SLO breach

### Security
- [ ] Authentication: JWT, OAuth 2.0, API keys
- [ ] Authorisation: RBAC, ABAC
- [ ] Encryption in transit (TLS 1.3) and at rest (AES-256)
- [ ] PCI DSS compliance (tokenisation, no raw PANs)
- [ ] DDoS protection (rate limiting, WAF, Cloudflare)

---

## 📐 CAP Theorem Quick Reference

```
         Consistency
              /
             /  
            /    
           /  CA  
          /--------
         / CP |  AP 
        /_____|______
   Partition Tolerance
```

| System | CAP Choice | Example |
|--------|-----------|---------|
| Traditional RDBMS | CA | MySQL (single node) |
| HBase, Zookeeper | CP | Financial ledgers |
| Cassandra, DynamoDB | AP | Shopping carts, feed |
| MongoDB (configurable) | CP or AP | Flexible |

---

> **See also:** [`../lld/README.md`](../lld/README.md) for low-level design and OOP patterns.
