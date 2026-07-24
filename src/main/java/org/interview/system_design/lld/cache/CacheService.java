package org.interview.system_design.lld.cache;

import java.util.Optional;

/**
 * Service wrapper around a Cache that tracks statistics.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class CacheService<K, V> {

    private final Cache<K, V> cache;
    private final CacheStats stats = new CacheStats();

    public CacheService(Cache<K, V> cache) {
        this.cache = cache;
    }

    /**
     * Gets a value, recording hit or miss statistics.
     */
    public Optional<V> get(K key) {
        Optional<V> result = cache.get(key);
        if (result.isPresent()) {
            stats.recordHit();
        } else {
            stats.recordMiss();
        }
        return result;
    }

    /**
     * Puts a value; detects evictions by comparing sizes before/after.
     */
    public void put(K key, V value) {
        int sizeBefore = cache.size();
        cache.put(key, value);
        int sizeAfter = cache.size();
        // If size didn't grow, something was evicted (and the key is new)
        if (sizeAfter <= sizeBefore && !get(key).isPresent()) {
            stats.recordEviction();
        }
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
        stats.reset();
    }

    public int size() { return cache.size(); }

    public CacheStats getStats() { return stats; }
}
