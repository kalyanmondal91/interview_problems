---
layout: hld
title: "Google Drive"
system: google_drive
description: "- Upload and download files of any type (up to 5GB per file)"
---

# System Design: Google Drive

## 1. Requirements

### Functional Requirements
- Upload and download files of any type (up to 5GB per file)
- Organize files in folder hierarchy
- Share files/folders with specific users (view / comment / edit permissions)
- Version history: restore any previous version of a file
- Real-time collaborative editing for Google Docs/Sheets/Slides
- Sync client: changes on one device propagate to all others automatically
- Search files by name and content

### Non-Functional Requirements
- **Scale**: 1B registered users, 15M concurrent uploads at peak
- **Storage**: ~2TB average per user → 1B × 2TB = 2 Exabytes total (across all tiers)
- **Upload throughput**: 15M × avg 1MB/s = ~15 PB/s burst (handled by chunking + CDN)
- **Availability**: 99.99% for file access; 99.9% for real-time collaboration
- **Durability**: 11 nines (same as S3) — files never lost
- **Consistency**: Strong for metadata (file tree); eventual for replica propagation
- **Latency**: Metadata operations < 50ms; file open < 500ms (first byte)

---

## 2. Capacity Estimation

### QPS
- 1B users × avg 10 file ops/day = 10B ops/day → ~115K ops/sec
- 15M concurrent uploads at peak; ~50M downloads
- Collaboration: 100M active Docs users, avg 5 edits/min = ~8.3M OT ops/sec

### Storage
- Raw: 1B users × 2TB = 2 EB
- With replication (3×): 6 EB
- Block deduplication saves ~30% → effective ~4.2 EB
- Metadata DB: 1B users × 10K files × 500B metadata = ~5 PB

### Bandwidth
- Upload: 15M concurrent × 1MB/s = 15 TB/s (handled by multipart upload to regional clusters)
- Download: 50M concurrent × 2MB/s = 100 TB/s (CDN absorbs ~85%)

---

## 3. High-Level Architecture

```
  Web Browser / Desktop Client / Mobile App
         │              │              │
         │              │              │
  ┌──────▼──────────────▼──────────────▼──────┐
  │           API Gateway (Global LB)          │
  └─────┬────────────────────────────┬─────────┘
        │                            │
  ┌─────▼──────┐              ┌──────▼──────┐
  │  Metadata  │              │  File/Block │
  │  Service   │              │  Service    │
  │            │              │             │
  │ Postgres   │              │  Chunker    │
  │ (sharded)  │              │  +  S3      │
  └─────┬──────┘              └──────┬──────┘
        │                            │
  ┌─────▼──────┐              ┌──────▼──────┐
  │  Change    │              │  CDN        │
  │  Log       │              │  (CloudFront│
  │  (Kafka)   │              │  /Akamai)   │
  └─────┬──────┘              └─────────────┘
        │
  ┌─────▼─────────────────────────────────┐
  │         Sync Service                  │
  │   (delta detection, change push)      │
  └─────┬─────────────────────────────────┘
        │
  ┌─────▼──────────────────┐
  │  Notification Service  │
  │  (WebSocket / SSE)     │
  └────────────────────────┘
```

```
                Upload Flow
  Client ──chunk──► Block Service ──► S3 (block by hash)
                         │
                    ──metadata──► Metadata DB (file record + block refs)
                         │
                    ──event──► Kafka ──► Sync Service ──► Other devices
```

---

## 4. Core Components

### 4.1 Metadata Service
- Manages file/folder tree: parent_id, name, owner, permissions, version pointer
- CRUD on file metadata; resolves path → file_id lookups
- Postgres sharded by user_id; read replicas for listings
- Caches hot file metadata in Redis (file_id → metadata)

### 4.2 Block / Chunk Service
- Splits incoming files into fixed-size chunks (default 4MB per block)
- Computes SHA-256 of each block for content-addressable storage (CAS)
- Deduplication: if block_hash already exists in S3, skip upload (link only)
- Issues pre-signed S3 URLs for direct client-to-S3 upload (avoids proxying large data)
- Stores block manifest: file_version → ordered list of block hashes

### 4.3 Sync Engine (Client-side)
- Watches file system for changes (inotify on Linux, FSEvents on macOS, ReadDirectoryChangesW on Windows)
- On change: compute block diff (only changed blocks are uploaded)
- Downloads only delta blocks when remote changes arrive
- Maintains local SQLite index of file_id → local_path, block_hashes, version

### 4.4 Change Detection & Notification
- Every file mutation publishes to Kafka `file-changes` topic (user_id, file_id, version, change_type)
- Sync Service subscribes; looks up which devices the user has registered
- Pushes lightweight change notification (not the file itself) via WebSocket/SSE to all user devices
- Client pulls specific changed blocks on notification receipt

### 4.5 Versioning Service
- Each upload creates a new file_version record (immutable snapshot of block list)
- Version history: up to 100 versions for free tier, unlimited for Workspace
- Soft-delete + 30-day trash for deleted files
- Version restore: atomic metadata pointer swap (no data copy needed)

### 4.6 Sharing & Permission Service
- ACL model: (file_id, principal_id, role) — role ∈ {VIEWER, COMMENTER, EDITOR, OWNER}
- Public link sharing: generates signed token; token → permission lookup
- Inheritance: folder permission propagates to children unless overridden
- Stored in Postgres; heavily cached in Redis (ACL lookups on every file access)

### 4.7 Real-Time Collaboration Service (for Docs)
- Uses Operational Transformation (OT) or CRDT
- Collaboration server maintains authoritative document state
- Each edit is a delta op (insert/delete at position)
- OT server serializes concurrent edits; transforms conflicting ops
- Document state snapshotted every 100 ops and persisted

### 4.8 CDN Layer
- Static file downloads served from CDN edge nodes
- Cache-key = block_hash (content-addressable → immutable, cacheable forever)
- Popular files (shared publicly) pre-warmed to edge; others fetched on first request

---

## 5. Data Models

### files (Postgres, sharded by owner_id)
```sql
CREATE TABLE files (
  file_id         UUID PRIMARY KEY,
  owner_id        BIGINT NOT NULL,
  parent_folder_id UUID,
  name            VARCHAR(255) NOT NULL,
  mime_type       VARCHAR(100),
  size_bytes      BIGINT,
  current_version INT DEFAULT 1,
  is_folder       BOOLEAN DEFAULT FALSE,
  is_trashed      BOOLEAN DEFAULT FALSE,
  created_at      TIMESTAMPTZ,
  modified_at     TIMESTAMPTZ,
  INDEX (owner_id, parent_folder_id)
);
```

### file_versions (Postgres)
```sql
CREATE TABLE file_versions (
  file_id     UUID NOT NULL,
  version     INT  NOT NULL,
  created_by  BIGINT,
  created_at  TIMESTAMPTZ,
  size_bytes  BIGINT,
  block_list  TEXT,  -- JSON array of block hashes in order
  checksum    VARCHAR(64),
  PRIMARY KEY (file_id, version)
);
```

### blocks (S3 / object store)
```
Key pattern: blocks/{first2_of_hash}/{block_sha256_hex}
Content: raw 4MB block data (compressed + encrypted at rest)
Metadata: ref_count (managed separately in Postgres)

Table: block_refs (Postgres)
  block_hash    VARCHAR(64) PRIMARY KEY
  size_bytes    INT
  ref_count     INT          -- deduplication reference count
  stored_at     TIMESTAMPTZ
```

### file_acls (Postgres)
```sql
CREATE TABLE file_acls (
  file_id      UUID NOT NULL,
  principal_id BIGINT,          -- user_id or group_id
  principal_type VARCHAR(10),    -- USER | GROUP | LINK
  role         VARCHAR(10),      -- VIEWER|COMMENTER|EDITOR|OWNER
  granted_at   TIMESTAMPTZ,
  PRIMARY KEY (file_id, principal_id)
);
```

---

## 6. API Design

### File Operations
```
POST /v1/files/upload-session
  Body: { name, parent_id, size_bytes, mime_type }
  Response: { upload_id, chunk_urls: [ { chunk_index, upload_url } ] }

PUT  <pre-signed-s3-url>   (direct client → S3, per chunk)

POST /v1/files/upload-session/{upload_id}/complete
  Body: { block_hashes: [ "sha256_1", "sha256_2", ... ] }
  Response: { file_id, version: 1 }

GET  /v1/files/{file_id}
  Response: { file_id, name, size, mime_type, version, download_url }

GET  /v1/files/{file_id}/download
  Response: 302 → CDN pre-signed URL

DELETE /v1/files/{file_id}
  Response: 204  (moves to trash)

POST /v1/files/{file_id}/versions/{version}/restore
  Response: { new_version: 5 }
```

### Folder Operations
```
POST /v1/folders
  Body: { name, parent_id }
  Response: { folder_id }

GET  /v1/folders/{folder_id}/children?page_token=&limit=100
  Response: { items: [ file|folder ], next_page_token }
```

### Sharing
```
POST /v1/files/{file_id}/permissions
  Body: { email: "user@example.com", role: "EDITOR" }
  Response: { permission_id }

GET  /v1/files/{file_id}/permissions
  Response: [ { principal, role, granted_at } ]

POST /v1/files/{file_id}/share-link
  Body: { role: "VIEWER", expiry: "2024-12-31" }
  Response: { link: "https://drive.google.com/file/d/abc/view?usp=sharing" }
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 Chunked Upload & Deduplication
```
File (10GB)
  │
  ├── Block 0: [bytes 0–4MB]    SHA256 = aaa → exists in S3? YES → skip upload
  ├── Block 1: [bytes 4–8MB]    SHA256 = bbb → exists? NO  → upload to S3
  ├── Block 2: [bytes 8–12MB]   SHA256 = ccc → exists? YES → skip
  └── ...

file_version record: { file_id, version, blocks: ["aaa", "bbb", "ccc", ...] }
```
- Deduplication across all users saves ~30-40% storage (common blocks in popular files)
- Rolling hash (Rabin fingerprinting) used for variable-size blocks in sync client for better diff

### 7.2 Delta Sync Algorithm
1. Client watches FS; on change: compute current block list via rolling hash
2. Compare with last-known block list (stored in local SQLite)
3. Upload only new/changed blocks; metadata update is atomic pointer swap
4. Download: on notification, fetch new block list → download only missing blocks
- Network usage reduced ~10× vs full file transfer for small edits

### 7.3 Conflict Resolution
- **Last-write-wins** for binary files (Office docs): timestamp-based; older version kept as sibling
- **OT for Docs**: server serializes all operations; clients apply transforms
- **CRDT for Sheets**: conflict-free distributed data structure; no server coordination needed
- On sync conflict: create both versions ("filename (1).docx" copy) and notify user

### 7.4 Metadata Sharding
- Shard by user_id (range sharding for hot user isolation)
- Cross-shard joins (shared files): denormalize — store shared file reference in both users' shards
- Global file_id index maintained in a separate lookup table for sharing

### 7.5 S3 Multipart Upload
- Files > 100MB use S3 multipart upload (up to 10K parts, 5MB–5GB each)
- Parallel chunk upload: client uploads 4 chunks simultaneously
- Server-side encryption: AES-256 at S3 layer; optionally client-side for sensitive files

### 7.6 Real-Time Collaboration (OT Detail)
```
Initial state: "Hello"
Client A: insert "World" at pos 5 → op_A = Insert(5, "World")
Client B: delete char at pos 0    → op_B = Delete(0, 1)

Without OT: applying both gives different results on A and B
With OT:    Transform(op_B, op_A) → Delete(0, 1) becomes valid on new doc
            Server applies op_A first, then transformed op_B → "Hello World" on both
```

---

## 8. Scalability & Bottlenecks

| Component | Challenge | Solution |
|---|---|---|
| Block storage | 2 EB total | S3-compatible object store; tiered (hot/warm/cold) |
| Metadata DB | 5 PB metadata | Shard by user_id; Redis cache hot metadata |
| Upload bandwidth | 15 TB/s peak | Direct-to-S3 uploads; bypass app servers |
| CDN download | 100 TB/s | 200+ edge PoPs; content-addressed cache (infinite TTL) |
| Sync notifications | 500M devices | Kafka fan-out; WebSocket servers sharded by user_id |
| Collaboration | 8.3M OT ops/sec | Collab servers per document; actor model |

---

## 9. Fault Tolerance & Reliability

- **S3 durability**: 11 nines with cross-region replication; versioning enabled
- **Block ref counting**: garbage collection process removes unreferenced blocks (refcount=0)
- **Upload resumability**: multipart upload state stored; client can resume after crash
- **Metadata DB**: Postgres streaming replication; automatic failover via Patroni
- **Change log**: Kafka retention 7 days; sync service can replay missed notifications
- **Collaboration server crash**: document state snapshotted every 100 ops; replay remaining ops from Kafka
- **Client offline**: sync queue persisted locally; applies changes on reconnect with conflict detection

---

## 10. Trade-offs & Alternatives

| Decision | Chosen | Alternative | Reason |
|---|---|---|---|
| Block size | 4MB fixed | Variable (Rabin) | Fixed simpler; Rabin better dedup for edits |
| Dedup scope | Global (all users) | Per-user | Global saves more; requires cross-user block sharing |
| Collab | OT | CRDT | OT: server-authoritative, simpler; CRDT: fully P2P |
| Metadata store | Postgres sharded | Spanner | Postgres cheaper; Spanner for true global strong consistency |
| Notifications | Kafka + WebSocket | Long Poll | Kafka durable; long poll wastes connections |
| CDN cache | Content-addressed forever | TTL-based | Block hash immutable → can cache forever safely |
