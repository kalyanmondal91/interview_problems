package org.interview.system_design.lld.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete cache implementation backed by a LinkedHashMap.
 * Eviction is delegated to the configured EvictionStrategy.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class SimpleCache<K, V> extends Cache<K, V> {

    private final Map<K, CacheEntry<K, V>> store = new LinkedHashMap<>();
    private final CacheConfig config;
    private final EvictionStrategy<K, V> evictionStrategy;

    public SimpleCache(CacheConfig config, EvictionStrategy<K, V> evictionStrategy) {
        this.config = config;
        this.evictionStrategy = evictionStrategy;
    }

    @Override
    protected Optional<V> doGet(K key) {
        CacheEntry<K, V> entry = store.get(key);
        if (entry == null) return Optional.empty();

        // Evict expired entries lazily
        if (entry.isExpired()) {
            store.remove(key);
            return Optional.empty();
        }

        entry.recordAccess();
        return Optional.of(entry.getValue());
    }

    @Override
    protected void doPut(K key, V value) {
        if (store.containsKey(key)) {
            // Replace existing entry
            store.put(key, new CacheEntry<>(key, value, config.getTtlSeconds()));
            return;
        }

        if (store.size() >= config.getMaxCapacity()) {
            K evictKey = evictionStrategy.evict(store);
            store.remove(evictKey);
        }

        store.put(key, new CacheEntry<>(key, value, config.getTtlSeconds()));
    }

    @Override
    public void remove(K key) {
        store.remove(key);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public int size() {
        return store.size();
    }

    /** Exposes the raw entry map for inspection (e.g., by CacheService stats). */
    public Map<K, CacheEntry<K, V>> getStore() {
        return store;
    }
}
