---
layout: hld
title: "Dropbox"
system: dropbox
description: "- Upload and download files from any device"
---

# System Design: Dropbox

## 1. Requirements

### Functional Requirements
- Upload and download files from any device
- Sync files across multiple devices automatically
- Selective sync (choose which folders to sync)
- Offline access to synced files
- File sharing with read/write permissions
- Version history (30 days)
- Delta sync (only changed portions)
- Folder hierarchy support

### Non-Functional Requirements
- 500M registered users, 100M DAU
- Average file size: 1MB; max file size: 5GB
- 1M uploads/min, 10M downloads/min
- 500PB total storage
- Sync latency < 10 seconds
- 99.99% availability
- Durability: 99.999999999% (11 nines)

---

## 2. Capacity Estimation

**Storage:**
- 500M users × 10GB average = 5EB total (with 3× replication = 15EB raw)
- Deduplication ratio ~30% savings ≈ 10.5EB raw

**Bandwidth:**
- 1M uploads/min × 1MB avg = ~16 GB/s upload bandwidth
- 10M downloads/min × 1MB avg = ~167 GB/s download bandwidth

**Metadata:**
- Per file: 1KB metadata × 500M users × 200 files avg = 100TB metadata

**QPS:**
- Upload: ~17,000 QPS
- Download: ~167,000 QPS
- Notification (sync events): ~500,000 QPS (file change events)

---

## 3. High-Level Architecture

```
+------------------+        +-------------------+        +---------------------+
|   Desktop Client |        |   Mobile Client   |        |    Web Browser      |
+--------+---------+        +---------+---------+        +-----------+---------+
         |                            |                              |
         +----------------------------+------------------------------+
                                      |
                            +---------v----------+
                            |    Load Balancer   |
                            +---------+----------+
                                      |
              +-----------------------+-----------------------+
              |                       |                       |
    +---------v--------+   +----------v-------+   +----------v-------+
    |   Upload Service |   |  Download/CDN    |   | Notification Svc |
    +---------+--------+   +------------------+   +----------+-------+
              |                       |                       |
    +---------v--------+   +----------v-------+   +----------v-------+
    | Block Store (S3) |   |   CDN Edge POPs  |   |   Message Queue  |
    +------------------+   +------------------+   +----------+-------+
                                                              |
    +------------------+   +------------------+   +----------v-------+
    | Metadata Service |   |  Auth Service    |   |   Sync Service   |
    +--------+---------+   +------------------+   +------------------+
             |
    +--------v---------+
    |  Metadata DB     |
    | (MySQL sharded)  |
    +------------------+
```

---

## 4. Core Components

### 4.1 Client-Side Chunker
- Splits files into 4MB fixed-size blocks
- Computes SHA-256 hash per block
- Checks which blocks already exist on server (avoids re-uploading)
- Maintains local SQLite DB of block hashes and file state

### 4.2 Upload Service
- Receives block uploads from clients
- Validates block hashes on receipt
- Stores blocks in content-addressable block store (S3)
- Block key = SHA-256 hash → deduplication is automatic
- Updates metadata DB after successful block storage
- Returns upload tokens for resumable uploads

### 4.3 Block Store
- S3-compatible object store
- Each object = one 4MB block, keyed by SHA-256 hash
- Cross-region replication for durability
- Lifecycle policies: move old versions to Glacier after 30 days

### 4.4 Metadata Service
- Stores file/folder hierarchy, version history, sharing info
- MySQL with sharding by user_id
- Schema: files, file_blocks (maps file version to blocks), folders, shares, devices

### 4.5 Notification Service
- Long-polling or WebSocket per device
- When a file changes on device A, server notifies all other devices of user
- Uses message queue (Kafka) to fan out change events
- Clients receive minimal event: {file_id, version, changed_by_device}

### 4.6 Sync Service
- Client polls for changes since last sync timestamp
- Server returns list of changed files and their block manifests
- Client downloads only changed blocks (delta sync)

### 4.7 CDN
- Hot files served from edge CDN nodes
- Reduces latency for downloads
- Invalidation on file updates

---

## 5. Data Models

```sql
-- Files table (sharded by user_id)
CREATE TABLE files (
    file_id     BIGINT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    folder_id   BIGINT,
    name        VARCHAR(255),
    size        BIGINT,
    mime_type   VARCHAR(100),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    is_deleted  BOOLEAN DEFAULT FALSE,
    INDEX(user_id, folder_id)
);

-- File versions
CREATE TABLE file_versions (
    version_id   BIGINT PRIMARY KEY,
    file_id      BIGINT NOT NULL,
    version_num  INT,
    size         BIGINT,
    block_count  INT,
    created_at   TIMESTAMP,
    device_id    BIGINT,
    INDEX(file_id, version_num DESC)
);

-- Block manifest (which blocks make up a version)
CREATE TABLE version_blocks (
    version_id  BIGINT,
    block_index INT,
    block_hash  CHAR(64),  -- SHA-256
    PRIMARY KEY(version_id, block_index)
);

-- Blocks (content-addressable, no user_id, shared globally)
CREATE TABLE blocks (
    block_hash     CHAR(64) PRIMARY KEY,
    size           INT,
    storage_path   VARCHAR(500),  -- S3 key
    created_at     TIMESTAMP
);
```

---

## 6. API Design

```
# Upload
POST /api/v1/upload/initiate
Body: { file_name, file_size, parent_folder_id }
Response: { upload_id, missing_blocks: [hash1, hash2...] }

POST /api/v1/upload/block/{upload_id}
Body: binary block data
Headers: X-Block-Hash: sha256

POST /api/v1/upload/commit/{upload_id}
Body: { block_manifest: [hash1, hash2...] }
Response: { file_id, version_id }

# Download
GET /api/v1/files/{file_id}/manifest?version=latest
Response: { blocks: [{index, hash, url}...] }

GET /api/v1/blocks/{hash}
Response: binary block data (or redirect to CDN)

# Sync
GET /api/v1/sync/changes?since={timestamp}&device_id={id}
Response: { changes: [{file_id, action, version_id}...], next_cursor }

# Sharing
POST /api/v1/shares
Body: { file_id, shared_with_email, permission: READ|WRITE }

# Notifications (long poll)
GET /api/v1/notifications/poll?device_id={id}&timeout=30
Response: { events: [...] } or 204 No Content on timeout
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 Client-Side Chunking Strategy
- Fixed 4MB blocks: simple, predictable, compatible with S3 multipart upload
- Rolling hash (Rabin fingerprinting): variable-size chunks based on content, better for small edits in large files (used by rsync)
- Dropbox uses fixed-size with a twist: first chunk can vary to align with natural edit points

### 7.2 Content-Addressable Storage & Deduplication
- Block key = SHA-256(block_content) → same content stored once globally
- Before uploading, client sends list of block hashes; server replies with which are missing
- Only missing blocks are transmitted (saves 30-50% bandwidth on average)
- Cross-user deduplication: if two users have same file, only one copy in block store

### 7.3 Delta Sync
- On file edit, only changed blocks are uploaded (typically 1-2 blocks for small edits)
- Client maintains local SQLite with block hashes and last sync timestamp
- On sync: GET /changes since last cursor → download only new/changed blocks

### 7.4 Conflict Resolution
- Last-write-wins by default (timestamp from server)
- On conflict (two devices modified same file offline): create a "conflicted copy" with device name suffix
- User manually merges (Dropbox does not auto-merge binary files)

### 7.5 LAN Sync
- Clients on same LAN discover each other via UDP broadcast
- Transfer blocks directly over LAN (peer-to-peer) instead of through server
- Reduces server bandwidth by ~60% in office environments

---

## 8. Scalability & Bottlenecks

| Component | Bottleneck | Solution |
|-----------|-----------|---------|
| Upload throughput | Network I/O on upload servers | Horizontal scale + multipart upload |
| Metadata reads | MySQL hot shards | Redis cache for file metadata |
| Block store | S3 throttling | Exponential backoff + S3 request rate guidelines |
| Notifications | Too many long-poll connections | Connection multiplexing, WebSocket |
| Sync storm | All devices sync after server restart | Jittered reconnect backoff |

---

## 9. Fault Tolerance & Reliability

- **Block durability**: S3 with cross-region replication (11 nines durability)
- **Upload resume**: Resumable uploads with upload_id; client retries missing blocks
- **Metadata DB**: MySQL with synchronous replication + automated failover (MHA/Orchestrator)
- **Notification service**: At-least-once delivery; clients handle duplicate events (idempotent sync)
- **Block store failures**: Client retries with exponential backoff; partial uploads committed only when all blocks uploaded
- **Data corruption**: SHA-256 hash verification on every block read and write

---

## 10. Trade-offs & Alternatives

| Decision | Chosen | Alternative | Reason |
|----------|--------|-------------|--------|
| Chunking | Fixed 4MB | Rolling hash (rsync-style) | Simpler, S3 compatible; rolling hash better for large binary edits |
| Conflict strategy | Conflicted copies | CRDT / OT auto-merge | Binary files can't be auto-merged; OT only works for text |
| Sync protocol | Long poll + cursor | WebSocket, Server-Sent Events | Long poll simpler to scale, WebSocket better for low latency |
| Deduplication scope | Global (cross-user) | Per-user only | Global saves more storage but raises privacy questions |
| Metadata store | MySQL (sharded) | Cassandra | MySQL provides ACID for consistency; Cassandra better for massive write scale |
