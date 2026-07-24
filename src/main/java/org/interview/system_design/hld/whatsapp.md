# System Design: Distributed Search Engine (Elasticsearch)

## 1. Requirements

### Functional Requirements
- **Full-text search**: Query across billions of documents using keywords, phrases, wildcards
- **Faceted filtering**: Filter results by structured fields (category, price range, date, tags)
- **Relevance ranking**: Score results by BM25/TF-IDF; custom boosting rules
- **Near-real-time indexing**: Documents searchable within 1 second of ingestion
- **Aggregations**: Count, avg, histogram, top-N per field
- **Multi-index search**: Search across multiple indices simultaneously
- **Geo search**: Find documents within radius of a lat/lon point

### Non-Functional Requirements
- **Data volume**: 1 PB of raw data across all indices
- **Query throughput**: 100K QPS at p99 < 200ms
- **Indexing throughput**: 500K documents/sec sustained
- **Availability**: 99.99% (active-active multi-AZ)
- **NRT latency**: Documents visible within 1 second of indexing
- **Storage efficiency**: Inverted index compression ~70% reduction vs raw text

---

## 2. Capacity Estimation

### Data Scale
```
Total data:          1 PB raw text
Avg document size:   1 KB
Total documents:     1 PB / 1 KB = 1 trillion documents

Inverted index size: ~30% of raw data = 300 TB
With replication(2x): 600 TB stored on nodes
```

### QPS & Throughput
```
Search QPS:       100,000 req/sec
Indexing rate:    500,000 docs/sec

Per-shard search: assuming 200 shards → 500 QPS/shard (manageable)
Per-shard index:  500K / 200 = 2,500 docs/sec (Lucene handles ~10K/sec/shard)
```

### Node Sizing
```
Data nodes:   600 TB / 2 TB per node = 300 data nodes
              (with 50% disk headroom → 600 nodes)
Master nodes: 3 dedicated (odd number for quorum)
Coord nodes:  20 dedicated query coordinators
Ingest nodes: 10 for pre-processing pipeline
```

---

## 3. High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Clients / Applications                         │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │  REST/HTTP or Java client
                ┌──────────────▼──────────────────┐
                │     Coordinating Nodes (×20)     │
                │  (parse query, scatter-gather)   │
                └──────────────┬──────────────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
┌─────────▼──────┐  ┌──────────▼──────┐  ┌─────────▼───────┐
│  Shard P1 (M)  │  │  Shard P2 (M)   │  │  Shard P3 (M)   │  ... ×200
│  Data Node     │  │  Data Node      │  │  Data Node      │
│  Lucene Index  │  │  Lucene Index   │  │  Lucene Index   │
└─────────┬──────┘  └──────────┬──────┘  └─────────┬───────┘
          │                    │                    │
┌─────────▼──────┐  ┌──────────▼──────┐  ┌─────────▼───────┐
│  Shard R1 (R)  │  │  Shard R2 (R)   │  │  Shard R3 (R)   │  (replicas)
│  Data Node     │  │  Data Node      │  │  Data Node      │
└────────────────┘  └─────────────────┘  └─────────────────┘
          │
┌─────────▼──────────────────────────────────────────────────┐
│                  Master Nodes (×3)                          │
│    Cluster state, shard allocation, index management        │
└─────────────────────────────────────────────────────────────┘
          │
┌─────────▼──────────────────────────────────────────────────┐
│                  Ingest Pipeline                            │
│    Kafka → Ingest Nodes → Bulk API → Data Nodes            │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Core Components

### 4.1 Coordinating Nodes
- Receive search requests from clients (no data stored locally)
- Parse and validate query DSL
- Determine which shards to query (all shards for global search, or subset for filtered)
- **Scatter**: fan out sub-queries to relevant shard primaries or replicas
- **Gather**: collect top-N results from each shard, merge, re-rank globally
- Return final ranked results to client
- CPU-intensive: dedicated nodes with high-core CPUs

### 4.2 Data Nodes (Lucene Shards)
- Each shard = one Lucene index on disk
- Stores inverted index, document values (doc_values), stored fields
- Handles local search: execute query against local Lucene index, return top-K hits with scores
- Handles indexing: write to in-memory index buffer, periodically flush (refresh)
- **Segment structure**: immutable segments; periodic merging in background

### 4.3 Master Nodes
- Maintain cluster state: list of nodes, shard allocation, index metadata/mappings
- Shard allocation algorithm: distribute shards evenly, avoid same-node primary+replica
- Leader election via Raft (KRaft) or Bully algorithm
- Never handle search/indexing (dedicated = no GC pauses affecting cluster state)

### 4.4 Ingest Nodes
- Pre-processing pipeline before documents hit data nodes
- Pipelines: language detection, tokenization, NER extraction, geo-encoding IP → lat/lon
- Transform, enrich, filter documents using processors
- Decouple heavy pre-processing from indexing path

### 4.5 Lucene Internals (per Shard)
```
Document arrives → IndexWriter
    → Analyzed by Analyzer (tokenize, lowercase, stem, remove stop words)
    → Written to in-memory buffer (IndexBuffer, default 512MB JVM heap)
    → Refresh (every 1 sec): in-memory → immutable Segment (searchable)
    → Flush: segments committed to disk with fsync
    → Merge: background thread merges small segments → fewer, larger segments
         (reduces per-segment search overhead; merge factor = 10 by default)
```

### 4.6 Query Router
- Routes to correct index based on request URL
- Cross-index queries fan out to shards across multiple indices
- Supports aliases (index alias → one or many indices, zero-downtime reindex)

---

## 5. Data Models

### Index Mapping (Schema)
```json
{
  "mappings": {
    "properties": {
      "doc_id":       { "type": "keyword" },
      "title":        { "type": "text", "analyzer": "english",
                        "fields": { "raw": { "type": "keyword" } } },
      "body":         { "type": "text", "analyzer": "english" },
      "tags":         { "type": "keyword" },
      "author_id":    { "type": "keyword" },
      "published_at": { "type": "date", "format": "strict_date_optional_time" },
      "price":        { "type": "scaled_float", "scaling_factor": 100 },
      "location":     { "type": "geo_point" },
      "embedding":    { "type": "dense_vector", "dims": 768 }
    }
  },
  "settings": {
    "number_of_shards":   20,
    "number_of_replicas": 1,
    "refresh_interval":   "1s"
  }
}
```

### Inverted Index Structure
```
Term          → Posting List (doc_id: position_list)
─────────────────────────────────────────────────────
"distributed" → [doc1:(3,7), doc45:(1), doc892:(2,9,15)]
"search"      → [doc1:(4), doc12:(1,3), doc45:(2), ...]
"engine"      → [doc1:(5), doc45:(3), ...]

doc_values (columnar, for sorting/aggregation):
doc_id │ price │ published_at │ author_id
───────┼───────┼──────────────┼──────────
doc1   │ 29.99 │ 2025-01-10   │ user_123
doc45  │ 9.99  │ 2025-01-08   │ user_456
```

---

## 6. API Design

### Index Document
```
PUT /products/_doc/doc_123
{
  "doc_id": "doc_123",
  "title":  "Wireless Headphones",
  "body":   "Premium noise-canceling headphones with 40hr battery",
  "tags":   ["electronics", "audio"],
  "price":  149.99,
  "published_at": "2025-01-10T12:00:00Z"
}

Response 201: { "_id": "doc_123", "result": "created", "_version": 1 }
```

### Bulk Index
```
POST /_bulk
{"index": {"_index": "products", "_id": "doc_124"}}
{"title": "...", "price": 99.99}
{"index": {"_index": "products", "_id": "doc_125"}}
{"title": "...", "price": 49.99}

Response: { "took": 12, "errors": false, "items": [...] }
```

### Search
```
POST /products/_search
{
  "query": {
    "bool": {
      "must":   { "match": { "body": "noise canceling headphones" } },
      "filter": [
        { "range":  { "price": { "gte": 50, "lte": 200 } } },
        { "terms":  { "tags":  ["electronics"] } }
      ],
      "should": { "match": { "title": "wireless" } }
    }
  },
  "aggs": {
    "price_histogram": {
      "histogram": { "field": "price", "interval": 25 }
    }
  },
  "sort": [{ "_score": "desc" }, { "price": "asc" }],
  "from": 0,
  "size": 20
}
```

### Scroll / PIT (Deep Pagination)
```
// Create Point-in-Time snapshot
POST /products/_pit?keep_alive=1m
→ { "id": "pit_token_xyz" }

// Paginate using search_after
POST /products/_search
{
  "pit": { "id": "pit_token_xyz", "keep_alive": "1m" },
  "sort": [{ "_score": "desc" }, { "_id": "asc" }],
  "search_after": [0.87, "doc_456"],
  "size": 20
}
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 Inverted Index & BM25 Scoring
```
BM25 Score(q, d) = Σ IDF(t) × [tf(t,d) × (k1+1)] / [tf(t,d) + k1×(1 - b + b×|d|/avgdl)]

IDF(t) = log(1 + (N - df(t) + 0.5) / (df(t) + 0.5))
  N     = total documents
  df(t) = documents containing term t

Parameters: k1=1.2 (term frequency saturation), b=0.75 (field length normalization)
```

### 7.2 Segment Merge Strategy
```
New doc write → tiny segment (e.g., 100 docs)
Background merge (tiered policy):
  - Merge 10 segments of similar size → 1 larger segment
  - Target segment size: 5GB max
  - Force merge on read-only indices (aliases): 1 segment/shard for optimal performance

Write amplification: each byte written ~log(n) times during merges
Read benefit: fewer segments → fewer file handles → faster search
```

### 7.3 Shard Sizing
```
Rule of thumb: 10-50 GB per shard (Elasticsearch recommendation)
Too small shards: overhead of shard management
Too large shards: slow merges, expensive shard recovery

Index: 1 TB data → 20-100 shards × 10-50 GB each
Number of shards = max(primary_shards, ceil(data_size / target_shard_size))
```

### 7.4 Query Execution: Scatter-Gather
```
Client → Coordinating Node
  ↓
Phase 1 (Query): broadcast to all N shards
  Each shard returns top-K doc IDs + scores (lightweight)
  Coordinating node merges: global top-K via priority queue
  ↓
Phase 2 (Fetch): request full documents for global top-K from respective shards
  Return full _source documents to client

Deep pagination problem: from=10000&size=10 → each shard returns 10,010 results
Solution: Point-in-Time + search_after (cursor-based, O(1) memory)
```

### 7.5 Relevance Tuning
- **Field boosting**: `"title^3"` (title matches worth 3× body matches)
- **Function score**: decay functions (recency, geo proximity), popularity boost
- **Learning-to-rank**: ML model as rescoring stage (XGBoost on click data)
- **Synonyms**: synonym token filter at index or query time

### 7.6 Near-Real-Time Indexing
```
Refresh interval (default 1s):
  IndexBuffer → refresh → new Lucene Segment (RAM) → searchable
  
Explicit refresh: POST /index/_refresh (for immediate visibility)
  
Trade-off: refresh_interval="30s" during bulk indexing
           → fewer segments → faster indexing throughput
           → less visible during bulk load window
```

---

## 8. Scalability & Bottlenecks

### Bottleneck 1: Hot Shards
- A single shard receiving disproportionate writes (e.g., time-based index)
- **Solution**: Index per day/week; rollover alias; ILM (Index Lifecycle Management)
- Hot-warm-cold architecture: hot (SSD) → warm (HDD) → cold (S3 snapshots)

### Bottleneck 2: JVM Heap Pressure
- Large heaps cause long GC pauses → cluster instability
- **Solution**: Heap ≤ 31GB (compressed OOPs); use OS page cache for Lucene I/O
- Dedicated heap: 50% of RAM for JVM; 50% for OS file cache

### Bottleneck 3: Deep Aggregations
- Cardinality aggregations on high-cardinality fields extremely memory-intensive
- **Solution**: HyperLogLog for approximate cardinality (HLL++ ≈1% error, O(1) memory)
- Pre-compute aggregations for dashboards; store in materialized view

### Read Scaling
```
Add more replicas: each shard has N replicas → N×read throughput
Coordinating nodes: scale independently → handle fan-out overhead
Circuit breaker: Elasticsearch built-in memory circuit breaker
```

---

## 9. Fault Tolerance & Reliability

### Shard Recovery
- **Primary shard failure**: promote replica → select new replica from remaining nodes
- **Node failure**: cluster reallocates shards within 60s (delayed_timeout config)
- **Network partition**: split-brain prevented by master quorum (N/2 + 1 master-eligible nodes)

### Data Durability
- **Translog**: append-only write-ahead log; fsync every 5s or 512MB
- **Snapshot**: full index snapshot to S3 (incremental); restore in minutes
- **Cross-cluster replication**: async replication to DR cluster

### Monitoring
```
Key metrics:
  - indexing_rate (docs/sec)
  - search_rate (req/sec), search_latency p99
  - segment_count (high = need merge)
  - JVM heap_used (>85% → danger)
  - unassigned_shards (>0 → degraded)
  - cluster_status: green/yellow/red
```

---

## 10. Trade-offs & Alternatives

### Trade-off 1: NRT vs Throughput
- Refresh every 1s → good NRT but overhead per refresh
- Refresh every 30s → higher bulk throughput, stale reads
- **Decision**: 1s for user-facing search; 30s for background indexing jobs

### Trade-off 2: Inverted Index vs Column Store
- Inverted index: excellent for full-text, poor for range scans on high-cardinality numeric fields
- ClickHouse/Druid: better for time-series aggregations
- **Decision**: Use Elasticsearch for text search; ClickHouse for analytics aggregations

### Trade-off 3: Schema-on-write vs Dynamic Mapping
- Dynamic mapping: flexible but can lead to mapping explosions (thousands of fields)
- Strict mapping: requires schema definition upfront; prevents surprises
- **Decision**: Strict mapping in production; explicit field limits; dynamic=false or dynamic=strict

### Alternatives
- **Apache Solr**: Similar Lucene-based, better for static datasets
- **Typesense**: Simpler, faster for exact search (no Lucene overhead)
- **Meilisearch**: Developer-friendly, typo-tolerant; less scalable
- **pgvector + PostgreSQL**: Vector similarity search integrated with RDBMS
