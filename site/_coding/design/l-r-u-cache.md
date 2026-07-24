---
layout: problem
title: "L R U Cache"
category: design
category_display: "Design"
difficulty: Medium
time_complexity: "O(1) for get and put"
space_complexity: "O(capacity)"
leetcode: 146
tags: [design]
render_with_liquid: false
---

## Problem

LRU Cache Design a data structure that follows the Least Recently Used (LRU) cache eviction policy. It should support get(key) which returns the value if key exists (else -1), and put(key, value) which inserts or updates the key-value pair, evicting the LRU entry when capacity is exceeded.

## Approach

Use LinkedHashMap with accessOrder=true so that every get/put moves the accessed entry to the tail, making the head always the least recently used. Override removeEldestEntry to automatically evict the eldest (LRU) entry when size exceeds capacity. Both get and put run in O(1) amortized time.

## Solution

```java
package org.interview.coding.design;

import java.util.*;

/**
 * Problem: LRU Cache
 * Difficulty: Medium
 *
 * Description: Design a data structure that follows the Least Recently Used (LRU) cache
 * eviction policy. It should support get(key) which returns the value if key exists (else -1),
 * and put(key, value) which inserts or updates the key-value pair, evicting the LRU entry
 * when capacity is exceeded.
 *
 * Example:
 *   Input: LRUCache(2), put(1,1), put(2,2), get(1), put(3,3), get(2), put(4,4), get(1), get(3), get(4)
 *   Output: 1, -1, -1, 3, 4
 *
 * Approach: Use LinkedHashMap with accessOrder=true so that every get/put moves the accessed
 * entry to the tail, making the head always the least recently used. Override removeEldestEntry
 * to automatically evict the eldest (LRU) entry when size exceeds capacity. Both get and put
 * run in O(1) amortized time.
 *
 * Time Complexity: O(1) for get and put
 * Space Complexity: O(capacity)
 *
 * Test Cases:
 *   1. capacity=2, put(1,1), put(2,2), get(1) → 1
 *   2. put(3,3) evicts key=2, get(2) → -1
 *   3. Edge: get on non-existent key → -1
 */
public class LRUCache {

    private final int capacity;
    private final LinkedHashMap<Integer, Integer> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    public int get(int key) {
        return cache.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        cache.put(key, value);
    }

    public static void main(String[] args) {
        // Test Case 1: Basic LRU eviction
        LRUCache cache1 = new LRUCache(2);
        cache1.put(1, 1);
        cache1.put(2, 2);
        System.out.println("get(1)=" + cache1.get(1));   // 1
        cache1.put(3, 3);  // evicts key 2
        System.out.println("get(2)=" + cache1.get(2));   // -1 (evicted)
        System.out.println("get(3)=" + cache1.get(3));   // 3

        // Test Case 2: Update existing key
        LRUCache cache2 = new LRUCache(2);
        cache2.put(1, 1);
        cache2.put(2, 2);
        cache2.put(1, 10); // update key 1
        cache2.put(3, 3);  // evicts key 2 (LRU), not key 1
        System.out.println("get(1)=" + cache2.get(1));   // 10
        System.out.println("get(2)=" + cache2.get(2));   // -1 (evicted)

        // Test Case 3: Edge - get on non-existent key
        LRUCache cache3 = new LRUCache(1);
        System.out.println("get(99)=" + cache3.get(99)); // -1
        cache3.put(1, 100);
        cache3.put(2, 200); // evicts key 1
        System.out.println("get(1)=" + cache3.get(1));   // -1 (evicted)
        System.out.println("get(2)=" + cache3.get(2));   // 200
    }
}
```

## Complexity

- **Time:** O(1) for get and put
- **Space:** O(capacity)
