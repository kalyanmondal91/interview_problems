---
layout: problem
title: "L F U Cache"
category: design
category_display: "Design"
difficulty: Hard
time_complexity: "O(1) for get and put"
space_complexity: "O(capacity)"
leetcode: 460
tags: [design]
render_with_liquid: false
---

## Problem

LFU Cache Design a Least Frequently Used (LFU) cache. get(key) returns the value or -1. put(key, value) inserts/updates; when capacity is reached, the least frequently used key is evicted. Ties in frequency are broken by LRU order (evict the least recently used among ties).

## Approach

Maintain three maps: keyVal (key→value), keyFreq (key→frequency), and freqKeys (frequency→LinkedHashSet of keys in LRU order). Track minFreq. On get, increment key's frequency and update freqKeys. On put, if key exists update it; if over capacity evict the LRU entry from freqKeys[minFreq]; then insert new key with freq=1 and set minFreq=1.

## Solution

```java
package org.interview.coding.design;

import java.util.*;

/**
 * Problem: LFU Cache
 * Difficulty: Hard
 *
 * Description: Design a Least Frequently Used (LFU) cache. get(key) returns the value or -1.
 * put(key, value) inserts/updates; when capacity is reached, the least frequently used key is
 * evicted. Ties in frequency are broken by LRU order (evict the least recently used among ties).
 *
 * Example:
 *   Input: LFUCache(2), put(1,1), put(2,2), get(1), put(3,3), get(2), get(3), put(4,4), get(1), get(3), get(4)
 *   Output: 1, -1, 3, -1, 3, 4
 *
 * Approach: Maintain three maps: keyVal (key→value), keyFreq (key→frequency), and freqKeys
 * (frequency→LinkedHashSet of keys in LRU order). Track minFreq. On get, increment key's
 * frequency and update freqKeys. On put, if key exists update it; if over capacity evict the
 * LRU entry from freqKeys[minFreq]; then insert new key with freq=1 and set minFreq=1.
 *
 * Time Complexity: O(1) for get and put
 * Space Complexity: O(capacity)
 *
 * Test Cases:
 *   1. capacity=2, put(1,1), put(2,2), get(1) → 1, put(3,3) evicts key 2 (LFU), get(2) → -1
 *   2. get(3) → 3, put(4,4) evicts key 1 (freq=1, LRU), get(1) → -1
 *   3. Edge: capacity=1, put(1,1), put(2,2) evicts key 1, get(1) → -1
 */
public class LFUCache {

    private final int capacity;
    private int minFreq;
    private final Map<Integer, Integer> keyVal;
    private final Map<Integer, Integer> keyFreq;
    private final Map<Integer, LinkedHashSet<Integer>> freqKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyVal = new HashMap<>();
        this.keyFreq = new HashMap<>();
        this.freqKeys = new HashMap<>();
    }

    public int get(int key) {
        if (!keyVal.containsKey(key)) return -1;
        incrementFreq(key);
        return keyVal.get(key);
    }

    public void put(int key, int value) {
        if (capacity <= 0) return;
        if (keyVal.containsKey(key)) {
            keyVal.put(key, value);
            incrementFreq(key);
            return;
        }
        if (keyVal.size() >= capacity) {
            evict();
        }
        keyVal.put(key, value);
        keyFreq.put(key, 1);
        freqKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    private void incrementFreq(int key) {
        int freq = keyFreq.get(key);
        keyFreq.put(key, freq + 1);
        freqKeys.get(freq).remove(key);
        if (freqKeys.get(freq).isEmpty()) {
            freqKeys.remove(freq);
            if (minFreq == freq) minFreq++;
        }
        freqKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    private void evict() {
        LinkedHashSet<Integer> keys = freqKeys.get(minFreq);
        int evictKey = keys.iterator().next();
        keys.remove(evictKey);
        if (keys.isEmpty()) freqKeys.remove(minFreq);
        keyVal.remove(evictKey);
        keyFreq.remove(evictKey);
    }

    public static void main(String[] args) {
        // Test Case 1: Basic LFU eviction
        LFUCache cache1 = new LFUCache(2);
        cache1.put(1, 1);
        cache1.put(2, 2);
        System.out.println("get(1)=" + cache1.get(1));   // 1, freq(1)=2
        cache1.put(3, 3);  // evicts key 2 (LFU, freq=1)
        System.out.println("get(2)=" + cache1.get(2));   // -1 (evicted)
        System.out.println("get(3)=" + cache1.get(3));   // 3

        // Test Case 2: LRU tiebreaker among same frequency
        LFUCache cache2 = new LFUCache(3);
        cache2.put(1, 1); cache2.put(2, 2); cache2.put(3, 3);
        cache2.get(1); cache2.get(2); // freq(1)=2, freq(2)=2, freq(3)=1
        cache2.put(4, 4); // evicts key 3 (LFU)
        System.out.println("get(3)=" + cache2.get(3));   // -1 (evicted)
        System.out.println("get(4)=" + cache2.get(4));   // 4

        // Test Case 3: Edge - capacity=1
        LFUCache cache3 = new LFUCache(1);
        cache3.put(1, 1);
        cache3.put(2, 2); // evicts key 1
        System.out.println("get(1)=" + cache3.get(1));   // -1
        System.out.println("get(2)=" + cache3.get(2));   // 2
    }
}
```

## Complexity

- **Time:** O(1) for get and put
- **Space:** O(capacity)
