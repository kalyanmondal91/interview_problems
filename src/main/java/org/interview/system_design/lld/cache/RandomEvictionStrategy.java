package org.interview.system_design.lld.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Evicts a randomly chosen entry.
 */
public class RandomEvictionStrategy<K, V> implements EvictionStrategy<K, V> {

    private final Random random = new Random();

    @Override
    public K evict(Map<K, CacheEntry<K, V>> entries) {
        List<K> keys = new ArrayList<>(entries.keySet());
        if (keys.isEmpty()) throw new IllegalStateException("Cache is empty");
        return keys.get(random.nextInt(keys.size()));
    }
}
