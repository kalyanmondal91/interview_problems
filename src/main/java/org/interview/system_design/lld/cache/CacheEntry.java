package org.interview.system_design.lld.cache;

import java.time.Instant;

/**
 * Wrapper around a cached value with metadata used by eviction strategies.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class CacheEntry<K, V> {

    private final K key;
    private final V value;
    private final Instant createdAt;
    private Instant lastAccessedAt;
    private long accessCount;
    private final long ttlSeconds; // 0 = no TTL

    public CacheEntry(K key, V value, long ttlSeconds) {
        this.key = key;
        this.value = value;
        this.createdAt = Instant.now();
        this.lastAccessedAt = this.createdAt;
        this.accessCount = 0;
        this.ttlSeconds = ttlSeconds;
    }

    /** Records an access, updating lastAccessedAt and incrementing count. */
    public void recordAccess() {
        this.lastAccessedAt = Instant.now();
        this.accessCount++;
    }

    /** Returns true if this entry has a TTL and has expired. */
    public boolean isExpired() {
        if (ttlSeconds <= 0) return false;
        return Instant.now().isAfter(createdAt.plusSeconds(ttlSeconds));
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastAccessedAt() { return lastAccessedAt; }
    public long getAccessCount() { return accessCount; }
    public long getTtlSeconds() { return ttlSeconds; }
}
