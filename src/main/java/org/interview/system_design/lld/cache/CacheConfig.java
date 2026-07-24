package org.interview.system_design.lld.cache;

/**
 * Immutable configuration for a cache instance.
 * Created via CacheBuilder.
 */
public class CacheConfig {

    private final int maxCapacity;
    private final long ttlSeconds;       // 0 = no TTL
    private final EvictionPolicy evictionPolicy;

    public CacheConfig(int maxCapacity, long ttlSeconds, EvictionPolicy evictionPolicy) {
        if (maxCapacity <= 0) throw new IllegalArgumentException("maxCapacity must be positive");
        this.maxCapacity = maxCapacity;
        this.ttlSeconds = ttlSeconds;
        this.evictionPolicy = evictionPolicy;
    }

    public int getMaxCapacity() { return maxCapacity; }
    public long getTtlSeconds() { return ttlSeconds; }
    public EvictionPolicy getEvictionPolicy() { return evictionPolicy; }

    @Override
    public String toString() {
        return String.format("CacheConfig{maxCapacity=%d, ttl=%ds, policy=%s}",
                maxCapacity, ttlSeconds, evictionPolicy);
    }
}
