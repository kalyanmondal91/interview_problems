package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Time Based Key-Value Store
 * Difficulty: Medium
 *
 * Description: Design a time-based key-value store that can store multiple values for the
 * same key at different timestamps and retrieve the key's value at a certain timestamp.
 * set(key, value, timestamp) stores the value associated with the key at the given timestamp.
 * get(key, timestamp) returns the value with the largest timestamp <= the given timestamp.
 *
 * Example:
 *   Input: set("foo","bar",1), get("foo",1), get("foo",3), set("foo","bar2",4), get("foo",4), get("foo",5)
 *   Output: "", "bar", "bar", "bar2", "bar2"
 *
 * Approach: Store values in a HashMap mapping each key to a list of (timestamp, value) pairs,
 * kept in sorted order by timestamp (since set is always called with increasing timestamps).
 * For get(), perform binary search on the list to find the largest timestamp <= the query timestamp.
 * This gives O(log n) lookup per get() call.
 *
 * Time Complexity: O(1) for set, O(log n) for get
 * Space Complexity: O(n) total stored entries
 *
 * Test Cases:
 *   1. set("foo","bar",1), get("foo",1) → "bar"
 *   2. get("foo",3) → "bar" (returns most recent before timestamp 3)
 *   3. Edge: get("foo",0) → "" (no entry at or before timestamp 0)
 */
public class TimeBasedKeyValueStore {

    private final Map<String, List<AbstractMap.SimpleEntry<Integer, String>>> map;

    public TimeBasedKeyValueStore() {
        map = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        map.computeIfAbsent(key, k -> new ArrayList<>())
           .add(new AbstractMap.SimpleEntry<>(timestamp, value));
    }

    public String get(String key, int timestamp) {
        List<AbstractMap.SimpleEntry<Integer, String>> list = map.get(key);
        if (list == null || list.isEmpty()) return "";

        int lo = 0, hi = list.size() - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid).getKey() <= timestamp) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans == -1 ? "" : list.get(ans).getValue();
    }

    public static void main(String[] args) {
        // Test Case 1: Basic set and get at exact timestamp
        TimeBasedKeyValueStore store = new TimeBasedKeyValueStore();
        store.set("foo", "bar", 1);
        System.out.println("get(foo,1) = " + store.get("foo", 1));   // bar
        System.out.println("get(foo,3) = " + store.get("foo", 3));   // bar (most recent before 3)

        // Test Case 2: Multiple values for same key
        store.set("foo", "bar2", 4);
        System.out.println("get(foo,4) = " + store.get("foo", 4));   // bar2
        System.out.println("get(foo,5) = " + store.get("foo", 5));   // bar2

        // Test Case 3: Edge - no entry at or before given timestamp
        System.out.println("get(foo,0) = '" + store.get("foo", 0) + "'"); // ""
        System.out.println("get(unknown,1) = '" + store.get("unknown", 1) + "'"); // ""
    }
}
