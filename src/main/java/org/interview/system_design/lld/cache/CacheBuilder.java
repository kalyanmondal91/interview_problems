package org.interview.system_design.lld.cache;

/**
 * Fluent builder for constructing a configured Cache instance.
 * Pattern: Builder
 *
 * <pre>
 * Cache&lt;String, String&gt; cache = new CacheBuilder&lt;String, String&gt;()
 *     .maxCapacity(100)
 *     .ttlSeconds(300)
 *     .evictionPolicy(EvictionPolicy.LRU)
 *     .build();
 * </pre>
 *
 * @param <K> key type
 * @param <V> value type
 */
public class CacheBuilder<K, V> {

    private int maxCapacity = 100;
    private long ttlSeconds = 0;
    private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;

    public CacheBuilder<K, V> maxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public CacheBuilder<K, V> ttlSeconds(long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
        return this;
    }

    public CacheBuilder<K, V> evictionPolicy(EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        return this;
    }

    /** Builds and returns a configured SimpleCache. */
    public Cache<K, V> build() {
        CacheConfig config = new CacheConfig(maxCapacity, ttlSeconds, evictionPolicy);
        EvictionStrategy<K, V> strategy = createStrategy(evictionPolicy);
        return new SimpleCache<>(config, strategy);
    }

    @SuppressWarnings("unchecked")
    private EvictionStrategy<K, V> createStrategy(EvictionPolicy policy) {
        switch (policy) {
            case LRU:    return new LRUEvictionStrategy<>();
            case LFU:    return new LFUEvictionStrategy<>();
            case FIFO:   return new FIFOEvictionStrategy<>();
            case RANDOM: return new RandomEvictionStrategy<>();
            default:     throw new IllegalArgumentException("Unknown policy: " + policy);
        }
    }
}
