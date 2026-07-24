package org.interview.coding.design;

import java.util.*;
import java.util.concurrent.*;

/**
 * Problem: Key-Value Store with TTL
 * Difficulty: Medium
 *
 * Description: Implement a key-value store with time-to-live (TTL) expiration. set(key, value, ttlMillis)
 * stores a value with a TTL. get(key) returns the value or null if expired/absent. delete(key) removes
 * the key. Expired keys are lazily removed on get() and also by a background cleanup thread.
 *
 * Example:
 *   Input: set("a","hello",500), get("a"), sleep(600), get("a")
 *   Output: "hello", null (expired)
 *
 * Approach: Store entries as Entry{value, expiryTime} in a ConcurrentHashMap. get() checks
 * System.currentTimeMillis() against expiryTime; if expired, removes the key and returns null.
 * A background daemon thread periodically scans and removes expired entries to prevent memory leaks.
 * delete() simply removes from the map.
 *
 * Time Complexity: O(1) for get/set/delete, O(N) for cleanup sweep
 * Space Complexity: O(N) for N entries
 *
 * Test Cases:
 *   1. set("key","val",500), get("key") → "val" (before expiry)
 *   2. sleep(600), get("key") → null (after expiry)
 *   3. Edge: set then delete before expiry → get returns null immediately
 */
public class KeyValueStoreWithTTL {

    static class Entry {
        String value;
        long expiryTime;

        Entry(String value, long ttlMillis) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
        }
    }

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    public KeyValueStoreWithTTL() {
        // Background cleanup thread (daemon so it doesn't block JVM exit)
        Thread cleaner = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    long now = System.currentTimeMillis();
                    store.entrySet().removeIf(e -> now > e.getValue().expiryTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleaner.setDaemon(true);
        cleaner.start();
    }

    public void set(String key, String value, long ttlMillis) {
        store.put(key, new Entry(value, ttlMillis));
    }

    public String get(String key) {
        Entry entry = store.get(key);
        if (entry == null) return null;
        if (System.currentTimeMillis() > entry.expiryTime) {
            store.remove(key);
            return null;
        }
        return entry.value;
    }

    public void delete(String key) {
        store.remove(key);
    }

    public static void main(String[] args) throws InterruptedException {
        KeyValueStoreWithTTL kvStore = new KeyValueStoreWithTTL();

        // Test Case 1: Value accessible before TTL expiry
        kvStore.set("key1", "hello", 1000);
        System.out.println("get(key1)=" + kvStore.get("key1")); // hello

        // Test Case 2: Value expires after TTL
        kvStore.set("key2", "world", 300);
        System.out.println("get(key2) before expiry=" + kvStore.get("key2")); // world
        Thread.sleep(400);
        System.out.println("get(key2) after expiry=" + kvStore.get("key2")); // null

        // Test Case 3: Edge - delete before expiry
        kvStore.set("key3", "temp", 5000);
        kvStore.delete("key3");
        System.out.println("get(key3) after delete=" + kvStore.get("key3")); // null
    }
}
