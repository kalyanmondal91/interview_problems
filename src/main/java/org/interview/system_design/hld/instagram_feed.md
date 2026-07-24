# System Design: Instagram Feed

## 1. Requirements

### Functional Requirements
- Users can post photos and videos
- Users can follow/unfollow other users
- News feed shows recent posts from followed users
- Like and comment on posts
- Stories (24-hour ephemeral content)
- Explore/discover page (trending content)
- Notifications (likes, comments, new followers)

### Non-Functional Requirements
- 500M DAU, 1B MAU
- 100M posts/day (photos + videos)
- Feed generation < 200ms P99
- 99.99% availability
- Feed should feel real-time (new posts visible within seconds)
- Eventual consistency acceptable for feed

---

## 2. Capacity Estimation

**Posts:**
- 100M posts/day = ~1,150 posts/sec
- Average post size: 1MB photo + 1KB metadata
- Storage: 100M × 1MB = 100TB/day, ~36PB/year

**Feed reads:**
- 500M DAU × 10 feed refreshes/day = 5B reads/day = ~58,000 RPS

**Follows:**
- Average: 200 followers per user
- 500M users × 200 avg followers = 100B follow edges

**Fanout:**
- 1,150 posts/sec × 200 avg followers = 230,000 feed writes/sec

---

## 3. High-Level Architecture

```
+----------+   +----------+   +----------+
|  iOS App |   |Android App|  | Web App  |
+----+-----+   +-----+----+   +----+-----+
     |               |             |
     +---------------+-------------+
                     |
           +---------v----------+
           |   API Gateway/LB   |
           +---------+----------+
                     |
    +----------------+----------------+
    |                |                |
+---v---+     +------v-----+    +-----v------+
| Post  |     | Feed       |    | Media      |
|Service|     | Service    |    | Service    |
+---+---+     +------+-----+    +-----+------+
    |                |                |
    |          +-----v------+         |
    |          | Feed Store |         |
    |          | (Redis)    |         |
    |          +------------+         |
    |                                 |
+---v-----------+          +----------v----+
| Post DB       |          | CDN           |
| (Cassandra)   |          | (CloudFront)  |
+---------------+          +---------------+
    |
+---v-----------+
| Fanout Worker |
| (Kafka+async) |
+---------------+
    |
+---v-----------+
| Follow Graph  |
| (social graph |
|  service)     |
+---------------+
```

---

## 4. Core Components

### 4.1 Post Service
- Handles photo/video upload
- Generates post_id, extracts metadata, triggers media processing
- Writes post to Cassandra (partitioned by user_id)
- Publishes PostCreated event to Kafka topic

### 4.2 Media Service
- Receives raw media from upload
- Generates multiple resolutions (thumbnail, medium, full)
- Uploads processed images to S3
- Returns CDN URLs for each resolution

### 4.3 Feed Service
- Primary read path for news feed
- Reads pre-computed feed from Redis (sorted set per user)
- Falls back to pull-based fanout for users not in cache
- Assembles feed items with post details + author info

### 4.4 Fanout Workers (Kafka Consumers)
- Consume PostCreated events
- For regular users (<10K followers): fanout-on-write (push to all follower feeds)
- For celebrities (>10K followers): skip fanout; use pull-on-read
- Write post_id to Redis sorted set: ZADD user:{follower_id}:feed {timestamp} {post_id}
- Fan-out rate: 230,000 writes/sec requires ~50 worker instances

### 4.5 Feed Store (Redis)
- Sorted set per user: key=`feed:{user_id}`, score=post_timestamp, value=post_id
- Max feed size: 1000 most recent post IDs per user (trim with ZREMRANGEBYRANK)
- Read: ZREVRANGE feed:{user_id} 0 19 → last 20 post IDs
- TTL: 7 days inactivity evicts feed (re-computed on next login)

### 4.6 Social Graph Service
- Stores follow relationships (user_id → Set of follower_ids)
- Used by fanout workers to enumerate followers
- Backed by graph DB or MySQL + Redis cache
- Read-heavy: cache follower lists in Redis

---

## 5. Data Models

```sql
-- Posts (Cassandra, partition by user_id)
CREATE TABLE posts (
    post_id     UUID,
    user_id     BIGINT,
    media_urls  LIST<TEXT>,
    caption     TEXT,
    location    TEXT,
    like_count  COUNTER,  -- Cassandra counter column
    comment_count INT,
    created_at  TIMESTAMP,
    PRIMARY KEY (user_id, created_at, post_id)
) WITH CLUSTERING ORDER BY (created_at DESC);

-- Follows (MySQL sharded by follower_id)
CREATE TABLE follows (
    follower_id BIGINT,
    followee_id BIGINT,
    created_at  TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id)
);

-- User profiles (MySQL)
CREATE TABLE users (
    user_id          BIGINT PRIMARY KEY,
    username         VARCHAR(50) UNIQUE,
    display_name     VARCHAR(100),
    profile_pic_url  VARCHAR(500),
    bio              TEXT,
    follower_count   INT,
    following_count  INT,
    post_count       INT,
    is_verified      BOOLEAN,
    created_at       TIMESTAMP
);

-- Redis feed (sorted set, not SQL)
-- Key: feed:{user_id}
-- Score: epoch timestamp (float)
-- Value: post_id
```

---

## 6. API Design

```
# Post operations
POST /api/v1/posts
Body: multipart/form-data (media file + caption + location)
Response: { post_id, media_urls, created_at }

GET /api/v1/posts/{post_id}
Response: full post object with like/comment counts

DELETE /api/v1/posts/{post_id}

# Feed
GET /api/v1/feed?limit=20&cursor={next_cursor}
Response: { posts: [...], next_cursor, has_more }

# Follow
POST /api/v1/follows/{user_id}          # Follow user
DELETE /api/v1/follows/{user_id}        # Unfollow

GET /api/v1/users/{user_id}/followers?cursor=...
GET /api/v1/users/{user_id}/following?cursor=...

# Likes
POST /api/v1/posts/{post_id}/likes
DELETE /api/v1/posts/{post_id}/likes

# Comments
POST /api/v1/posts/{post_id}/comments
Body: { text }
GET /api/v1/posts/{post_id}/comments?limit=20&cursor=...
```

---

## 7. Deep Dive: Key Design Decisions

### 7.1 Push vs Pull vs Hybrid Fanout

**Pure Push (fanout-on-write):**
- On post creation, push post_id to all follower feeds immediately
- Pro: O(1) feed read (already precomputed)
- Con: Celebrities with 100M followers = 100M writes per post

**Pure Pull (fanout-on-read):**
- No precomputed feed; query posts from all followees on read
- Pro: No write amplification
- Con: O(n) on read where n = number of followees; too slow

**Hybrid (Instagram's approach):**
- Regular users (< 10K followers): push-on-write
- Celebrities (> 10K followers): pull-on-read (merge into feed at query time)
- Active users: push; inactive users: don't push (lazy compute)

### 7.2 Feed Pagination
- Cursor-based pagination (not offset) using `last_post_id` or timestamp
- Prevents missing/duplicate posts when new posts arrive during pagination

### 7.3 Feed Ranking
- Pure chronological: simple but misses personalization
- EdgeRank-like score: `score = affinity × weight × time_decay`
  - `affinity`: how often you interact with the poster
  - `weight`: post type (video > photo > share)
  - `time_decay`: exponential decay: 1 / (1 + λ × hours_since_post)
- Store pre-ranked scores in Redis sorted set (score = EdgeRank score)

### 7.4 Hot Users (Celebrities)
- Celebrity posts fetched at read time and merged with pre-computed friend feed
- Keep separate "celebrity feed" Redis key per celebrity
- On feed read: merge user_feed + union of celebrity feeds they follow (top-K merge)

---

## 8. Scalability & Bottlenecks

| Bottleneck | Scale Impact | Solution |
|-----------|-------------|---------|
| Fanout write amplification | 230K writes/sec for regular users | Async Kafka workers, horizontal scale |
| Celebrity posts | 100M+ writes per post | Pull-on-read hybrid; celebrity feed cache |
| Redis memory | 500M users × 1KB feed = 500GB | Evict inactive user feeds (7-day TTL) |
| Post read | 58K RPS | Cassandra read path is fast; Redis L1 cache for hot posts |
| Media delivery | 167 GB/s download | CDN edge caching; multi-CDN strategy |

---

## 9. Fault Tolerance & Reliability

- **Fanout failure**: Kafka consumer lag monitored; retry on worker crash
- **Redis failure**: Feed rebuilt from Cassandra post table (slower but correct)
- **Post DB**: Cassandra multi-datacenter replication (LOCAL_QUORUM writes)
- **Media uploads**: S3 cross-region replication; CDN failover between origin regions
- **Circuit breaker**: Feed service degrades gracefully (shows cached feed if live queries fail)

---

## 10. Trade-offs & Alternatives

| Decision | Chosen | Alternative | Reason |
|----------|--------|-------------|--------|
| Feed storage | Redis sorted set | Cassandra timeline | Redis O(log n) insert, O(1) range scan; Cassandra cheaper at scale |
| Post storage | Cassandra | MySQL | Cassandra scales writes better; no ACID needed for posts |
| Fanout model | Hybrid push/pull | Pure push | Pure push breaks for celebrities |
| Ranking | EdgeRank-style | ML-based (two-tower) | EdgeRank is fast to compute; ML improves quality at cost |
| Stories | Separate service + Redis TTL | Posts with expiry field | Stories have different access patterns (sequential, expire exactly 24h) |
