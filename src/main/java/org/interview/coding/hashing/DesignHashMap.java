package org.interview.coding.hashing;

import java.util.LinkedList;

/**
 * Problem: Design HashMap
 * Difficulty: Easy
 *
 * Description:
 * Design a HashMap without using any built-in hash table libraries. Implement the MyHashMap
 * class supporting put(key, value), get(key), and remove(key) operations. Keys and values
 * are non-negative integers.
 *
 * Example:
 *   Input: put(1,1), put(2,2), get(1), get(3), put(2,1), get(2), remove(2), get(2)
 *   Output: 1, -1, 1, -1
 *
 * Constraints:
 *   - 0 <= key, value <= 10^6
 *   - At most 10^4 calls to put, get, and remove
 *
 * Approach:
 *   Use an array of LinkedLists (separate chaining). The array size is a prime number (1009)
 *   to minimize collisions. Each bucket holds key-value pair entries. For each operation,
 *   hash the key to find the bucket, then search the linked list in that bucket for the key.
 *   This provides O(1) average case for all operations.
 *
 * Time Complexity: O(n/k) average per operation, where k is number of buckets
 * Space Complexity: O(n + k)
 *
 * Test Cases:
 *   1. put(1,1), get(1) → Output: 1
 *   2. get(3) → Output: -1 (key not present)
 *   3. Edge case: put(2,2), remove(2), get(2) → Output: -1
 */
public class DesignHashMap {

    private static final int SIZE = 1009;
    private LinkedList<int[]>[] buckets;

    public DesignHashMap() {
        buckets = new LinkedList[SIZE];
    }

    private int hash(int key) {
        return key % SIZE;
    }

    public void put(int key, int value) {
        int h = hash(key);
        if (buckets[h] == null) buckets[h] = new LinkedList<>();
        for (int[] entry : buckets[h]) {
            if (entry[0] == key) {
                entry[1] = value;
                return;
            }
        }
        buckets[h].add(new int[]{key, value});
    }

    public int get(int key) {
        int h = hash(key);
        if (buckets[h] == null) return -1;
        for (int[] entry : buckets[h]) {
            if (entry[0] == key) return entry[1];
        }
        return -1;
    }

    public void remove(int key) {
        int h = hash(key);
        if (buckets[h] == null) return;
        buckets[h].removeIf(entry -> entry[0] == key);
    }

    public static void main(String[] args) {
        DesignHashMap sol = new DesignHashMap();
        // Test 1
        sol.put(1, 1);
        System.out.println(sol.get(1));  // 1
        // Test 2
        System.out.println(sol.get(3));  // -1
        // Test 3 (edge case: remove then get)
        sol.put(2, 2);
        sol.remove(2);
        System.out.println(sol.get(2));  // -1
    }
}
