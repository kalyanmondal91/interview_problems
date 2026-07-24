# System Design: WhatsApp

## 1. Requirements

### Functional Requirements
- 1:1 messaging with text, emoji, links
- Group chat (up to 256 members)
- Media sharing: photos, videos, voice notes, documents
- Delivery receipts: single tick (sent to server), double tick (delivered to device), blue tick (read)
- Online/last-seen presence indicators
- End-to-end encryption (E2EE) using Signal Protocol
- Message history stored on device; minimal server-side history
- Push notifications for offline users

### Non-Functional Requirements
- **Scale**: 100B messages/day (~1.16M messages/sec), 2B registered users, ~500M DAU
- **Latency**: < 100ms message delivery for online users
- **Availability**: 99.999% (5 nines) — messages must never be lost
- **Durability**: Messages stored until delivered; then deleted from server (E2EE model)
- **Storage**: Media stored on CDN/object store; text on message queue per user
- **Consistency**: At-least-once delivery with deduplication

---

## 2. Capacity Estimation

### QPS
- 100B messages/day ÷ 86,400s ≈ **1.16M msg/sec** (avg), ~3M/sec peak
- 500M DAU, avg 200 messages/day = 100B confirmed
- Group messages: assume 20% of messages, avg group size 50 → fanout ~10× = 200B deliveries/day

### Storage (server-side transient)
- Undelivered message queue: assume 5% offline at any time = 25M users
  - Avg 50 messages queued × 2KB = ~2.5 TB queue storage at any time
- Media: 100B messages × 5% with media × avg 500KB = **2.5 PB/day** (CDN-backed)
- Metadata per message: ~200B → 100B/day × 200B = 20 TB/day (metadata DB)

### Bandwidth
- Inbound: 1.16M msg/sec × avg 1KB = ~1.16 GB/s
- Outbound (with group fanout): ~12 GB/s across all delivery servers

---

## 3. High-Level Architecture

```
  Client A (iOS/Android)                    Client B (iOS/Android)
       │                                          │
       │ TLS/WebSocket                            │ TLS/WebSocket
       ▼                                          ▼
  ┌──────────────────────────────────────────────────────┐
  │              Connection Servers (CoS)                │
  │   (Maintain persistent WS; ~500K connections each)   │
  └──────────────────┬───────────────────────────────────┘
                     │
          ┌──────────▼──────────┐
          │    Message Router   │  ◄── Kafka topics per user
          └──────────┬──────────┘
                     │
     ┌───────────────┼────────────────┐
     ▼               ▼                ▼
 ┌────────┐    ┌──────────┐    ┌───────────┐
 │  1:1   │    │  Group   │    │  Media    │
 │  Svc   │    │  Fanout  │    │  Upload   │
 └────┬───┘    └────┬─────┘    └─────┬─────┘
      │              │                │
      ▼              ▼                ▼
 ┌─────────┐  ┌───────────┐  ┌────────────┐
 │Cassandra│  │Cassandra  │  │  S3 / CDN  │
 │(messages│  │(group msg)│  │  (media)   │
 └─────────┘  └───────────┘  └────────────┘
      │
      ▼
 ┌─────────────┐
 │ Push Notif  │
 │ (APNS/FCM)  │
 └─────────────┘
```

---

## 4. Core Components

### 4.1 Connection Servers (CoS)
- Each server maintains ~500K persistent WebSocket connections
- At 2B users with 25% online = 500M connections → ~1000 CoS nodes
- Responsible for: connection auth, heartbeat, message receive/send, presence updates
- Register connection mapping: `user_id → CoS_node_id` in Redis (with TTL = session lifetime)

### 4.2 Message Router
- Looks up destination user's CoS node from Redis
- If user is online: forward directly to their CoS node via internal gRPC
- If user is offline: write message to Kafka topic `offline:{user_id}` for async delivery
- Assigns globally unique message_id (Snowflake-style)

### 4.3 Message Storage Service (Cassandra)
- Stores messages until ACK received from recipient device
- Partitioned by `(chat_id)`, clustered by `(timestamp, message_id)` for ordered retrieval
- TTL on messages after delivery confirmation (30-day default for legal/backup)
- Multi-datacenter replication with LOCAL_QUORUM reads/writes

### 4.4 Group Fanout Service
- For group message: fetch all member user_ids from Group Service
- Fan out delivery to each member's CoS or offline queue
- For large groups (>50 members): async fanout via Kafka to avoid head-of-line blocking
- Track per-member delivery state in group_delivery table

### 4.5 Media Service
- Client encrypts media locally before upload (E2EE)
- Upload to pre-signed S3 URL directly (bypasses app servers)
- Message contains media URL + encryption key (only shared with recipient)
- CDN serves media; CDN edge cached by region for popular media

### 4.6 Presence Service
- Users send heartbeat every 30s when app is foreground
- Presence stored in Redis with 60s TTL: `presence:{user_id}` → {online, last_seen}
- "Last seen" timestamp persisted to Cassandra on disconnect
- Privacy setting: users can hide last seen (filter at read time)

### 4.7 Push Notification Service
- Triggered when message written to offline queue
- Sends silent push via APNS/FCM to wake client app
- Client establishes WebSocket → drains offline queue
- Rate limiting: max 1 push/second per user to avoid APNS throttling

### 4.8 ACK & Receipt Engine
- Sender → Server: single tick after server accepts message
- Server → Recipient CoS → Recipient Device: double tick sent back to sender
- Recipient opens chat → read receipt event → blue tick sent to sender
- All receipt events flow through Kafka; persisted to receipts table

---

## 5. Data Models

### messages (Cassandra)
```
Table: messages
Partition Key: chat_id (TEXT)  -- "user1_user2" sorted, or group_id
Clustering Key: created_at (TIMEUUID) DESC, message_id (UUID)

Columns:
  message_id    UUID
  sender_id     BIGINT
  chat_id       TEXT
  message_type  TEXT  -- text|image|video|audio|document
  content       BLOB  -- encrypted ciphertext
  media_url     TEXT
  status        TEXT  -- SENT|DELIVERED|READ
  created_at    TIMEUUID
  ttl           INT   -- Cassandra TTL for auto-expiry
```

### group_members (Cassandra)
```
Table: group_members
Partition Key: group_id (UUID)
Clustering Key: user_id (BIGINT)

Columns:
  group_id     UUID
  user_id      BIGINT
  role         TEXT  -- ADMIN|MEMBER
  joined_at    TIMESTAMP
  muted        BOOLEAN
```

### delivery_receipts (Cassandra)
```
Table: delivery_receipts
Partition Key: message_id (UUID)
Clustering Key: recipient_id (BIGINT)

Columns:
  delivered_at  TIMESTAMP
  read_at       TIMESTAMP
```

### user_sessions (Redis)
```
Key: session:{user_id}
Value: { cos_node: "cos-node-42", device_token: "...", connected_at: epoch }
TTL: 3600s (refreshed on heartbeat)
```

---

## 6. API Design

### WebSocket Protocol (client ↔ CoS)
```json
// Client → Server: send message
{
  "type": "MSG_SEND",
  "client_msg_id": "abc123",
  "to": "user_456",
  "chat_id": "chat_123_456",
  "content": "<encrypted_base64>",
  "media_url": null,
  "timestamp": 1700000000
}

// Server → Client: message delivery
{
  "type": "MSG_RECEIVE",
  "message_id": "server_uuid",
  "client_msg_id": "abc123",
  "from": "user_123",
  "chat_id": "chat_123_456",
  "content": "<encrypted_base64>",
  "timestamp": 1700000001
}

// Client → Server: delivery receipt
{
  "type": "MSG_ACK",
  "message_id": "server_uuid",
  "ack_type": "DELIVERED" | "READ"
}

// Server → Sender: receipt notification
{
  "type": "RECEIPT",
  "message_id": "server_uuid",
  "receipt_type": "DELIVERED" | "READ",
  "by": "user_456",
  "timestamp": 1700000002
}
```

### REST APIs
```
POST /v1/media/upload-url
  Body: { file_size, mime_type, chat_id }
  Response: { upload_url (pre-signed S3), media_id }

GET  /v1/chats/{chat_id}/messages?before=<timestamp>&limit=50
  Response: [ { message_id, sender_id, content, type, timestamp, status } ]

POST /v1/groups
  Body: { name, member_ids: [], icon_url }
  Response: { group_id, created_at }

PUT  /v1/groups/{group_id}/members
  Body: { add: [user_id], remove: [user_id] }

GET  /v1/users/{user_id}/presence
  Response: { online: true|false, last_seen: timestamp }
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 End-to-End Encryption (Signal Protocol)
- Each device has an Identity Key, Signed PreKey, and One-Time PreKeys uploaded to server
- Sender fetches recipient's public keys → performs X3DH key agreement → derives AES-256 session key
- Messages encrypted locally; server never sees plaintext
- Key rotation via Double Ratchet algorithm for forward secrecy

### 7.2 Message Queue per User (Offline Delivery)
- Kafka topic per user causes too many topics (2B); instead:
  - Segment users into ~10K Kafka partitions
  - User offline messages written to Cassandra with status=PENDING
  - CoS drains pending messages on reconnect in order

### 7.3 ACK Mechanism (At-Least-Once Delivery)
```
Sender ──send──► CoS-A ──store──► Cassandra ──OK──► ACK to Sender (tick 1)
                                      │
                              CoS-B (recipient) ──deliver──► Client B
                                      │
                             Client B ──ACK──► CoS-B ──► Cassandra (status=DELIVERED)
                                      │
                             CoS-B ──receipt──► CoS-A ──► Sender (tick 2)
```

### 7.4 Fanout for Group Messages
- **Small groups** (< 50): inline fanout — write N individual delivery records synchronously
- **Large groups** (50-256): async via Kafka `group-fanout` topic; workers process member batches
- Avoid thundering herd: stagger notifications with small random delay (0-2s)

### 7.5 Cassandra Schema Design
- Partition by `chat_id` ensures messages for a conversation are co-located
- TIMEUUID as clustering key gives natural time ordering without explicit sort column
- Wide rows: one partition per chat (all messages) — may grow large → apply TTL aggressively
- For 1:1: chat_id = "min(user_a, user_b)_max(user_a, user_b)" deterministic

### 7.6 Presence Scalability
- Naive approach: broadcast every presence change → O(friends × users) = unsustainable
- **Pull model**: client fetches presence only for chats currently open (lazy load)
- **Push model for active chats**: subscribe to presence events only for currently viewed contacts
- Redis pub/sub channel per user: `presence:{user_id}` → only subscribed CoS nodes receive

---

## 8. Scalability & Bottlenecks

| Component | Scale Challenge | Solution |
|---|---|---|
| Connection Servers | 500M concurrent WS | Horizontal scale; ~1000 nodes @ 500K each |
| Message routing | Lookup user→CoS | Redis Cluster; 100M key lookups/sec |
| Cassandra writes | 3M msg/sec | Partitioning + multi-DC; token-aware routing |
| Group fanout | 256 members × fanout | Async Kafka; parallel worker pools |
| Media upload | PB/day scale | Direct S3 upload; no proxying through app server |
| Presence updates | 500M user changes | Coalesce updates; pull model for inactive chats |

---

## 9. Fault Tolerance & Reliability

- **Message durability**: Cassandra RF=3 with LOCAL_QUORUM ensures no message lost even with node failure
- **CoS crash recovery**: Redis session expires; client reconnects to any CoS; drains offline queue
- **Kafka retention**: 7-day retention allows replay of unprocessed offline messages
- **Idempotency**: client_msg_id deduplicates re-sent messages (network retry)
- **Circuit breakers**: wrap Cassandra, Redis, push notification calls
- **Multi-region**: active-active with conflict-free replicated data types (CRDT) for presence
- **Backpressure**: CoS applies rate limiting per user (1000 msg/min) to prevent abuse flooding

---

## 10. Trade-offs & Alternatives

| Decision | Chosen | Alternative | Reason |
|---|---|---|---|
| Transport | WebSocket | XMPP | WS simpler to load balance; XMPP rich protocol but overhead |
| Message store | Cassandra | MySQL sharded | Cassandra: time-series append, no hot partitions |
| E2EE | Signal Protocol | TLS only | Signal: true E2EE, server can't read; TLS only stops network MITM |
| Group fanout | Async Kafka | Sync write | Async avoids blocking sender on 256-member fanout |
| Presence | Pull + selective push | Broadcast | Broadcast: O(n²); pull: on-demand + push only for active screens |
| Media | S3 direct upload | Via app server | Avoids bandwidth bottleneck on app servers for large files |
