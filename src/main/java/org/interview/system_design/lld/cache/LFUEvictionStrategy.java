package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Evicts the entry with the lowest access count (ties broken by oldest access time).
 */
public class LFUEvictionStrategy<K, V> implements EvictionStrategy<K, V> {

    @Override
    public K evict(Map<K, CacheEntry<K, V>> entries) {
        return entries.entrySet().stream()
                .min((a, b) -> {
                    int cmp = Long.compare(a.getValue().getAccessCount(), b.getValue().getAccessCount());
                    if (cmp != 0) return cmp;
                    return a.getValue().getLastAccessedAt().compareTo(b.getValue().getLastAccessedAt());
                })
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("Cache is empty"));
    }
}
