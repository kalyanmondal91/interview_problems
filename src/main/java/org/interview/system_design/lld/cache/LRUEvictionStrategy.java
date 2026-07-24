package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Evicts the entry with the oldest lastAccessedAt timestamp.
 */
public class LRUEvictionStrategy<K, V> implements EvictionStrategy<K, V> {

    @Override
    public K evict(Map<K, CacheEntry<K, V>> entries) {
        return entries.entrySet().stream()
                .min((a, b) -> a.getValue().getLastAccessedAt()
                        .compareTo(b.getValue().getLastAccessedAt()))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("Cache is empty"));
    }
}
