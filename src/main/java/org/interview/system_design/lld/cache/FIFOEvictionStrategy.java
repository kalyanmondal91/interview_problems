package org.interview.system_design.lld.cache;

import java.util.Map;

/**
 * Evicts the entry that was created earliest (first in, first out).
 */
public class FIFOEvictionStrategy<K, V> implements EvictionStrategy<K, V> {

    @Override
    public K evict(Map<K, CacheEntry<K, V>> entries) {
        return entries.entrySet().stream()
                .min((a, b) -> a.getValue().getCreatedAt().compareTo(b.getValue().getCreatedAt()))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("Cache is empty"));
    }
}
