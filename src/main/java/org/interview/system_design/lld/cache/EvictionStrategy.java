package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Strategy interface for cache eviction algorithms.
 * Pattern: Strategy
 *
 * @param <K> key type
 * @param <V> value type
 */
public interface EvictionStrategy<K, V> {
    /**
     * Selects and returns the key that should be evicted from the cache.
     *
     * @param entries current cache entries
     * @return the key to evict
     */
    K evict(Map<K, CacheEntry<K, V>> entries);
}
