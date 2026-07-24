package org.interview.coding.concurrency;

import java.util.*;
import java.util.concurrent.*;

/**
 * Problem: Distributed Lock Simulation
 * Difficulty: Hard
 *
 * Description: Simulate a distributed lock where multiple clients compete for the same lock key.
 * tryLock(lockKey, ownerId, ttlMillis) atomically acquires the lock if available (or if expired).
 * unlock(lockKey, ownerId) releases the lock only if the caller is the owner.
 *
 * Example:
 *   Input: Client A acquires lock, Client B tries and fails, A releases, B acquires
 *   Output: tryLock returns true/false appropriately
 *
 * Approach: Use ConcurrentHashMap<lockKey, LockEntry{ownerId, expiryTime}>. tryLock() uses
 * compute() for atomic check-and-set: if absent or TTL expired, write new entry and return true;
 * otherwise return false. unlock() uses compute() to remove only if ownerId matches. This avoids
 * race conditions without explicit synchronization.
 *
 * Time Complexity: O(1) for tryLock and unlock
 * Space Complexity: O(L) where L = number of distinct lock keys
 *
 * Test Cases:
 *   1. ClientA locks, ClientB tryLock fails, ClientA unlocks, ClientB locks → true,false,true
 *   2. Lock expires by TTL → next tryLock succeeds
 *   3. Edge: unlock with wrong ownerId → lock remains held
 */
public class DistributedLock {

    static class LockEntry {
        final String ownerId;
        final long expiryTime;

        LockEntry(String ownerId, long ttlMillis) {
            this.ownerId = ownerId;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final ConcurrentHashMap<String, LockEntry> locks = new ConcurrentHashMap<>();

    public boolean tryLock(String lockKey, String ownerId, long ttlMillis) {
        // Use atomic compute for check-and-set
        boolean[] acquired = {false};
        locks.compute(lockKey, (key, existing) -> {
            if (existing == null || existing.isExpired()) {
                acquired[0] = true;
                return new LockEntry(ownerId, ttlMillis);
            }
            return existing; // keep existing lock
        });
        return acquired[0];
    }

    public boolean unlock(String lockKey, String ownerId) {
        boolean[] released = {false};
        locks.compute(lockKey, (key, existing) -> {
            if (existing != null && existing.ownerId.equals(ownerId) && !existing.isExpired()) {
                released[0] = true;
                return null; // remove the lock
            }
            return existing;
        });
        return released[0];
    }

    public static void main(String[] args) throws InterruptedException {
        DistributedLock dLock = new DistributedLock();

        // Test Case 1: Sequential lock/unlock
        System.out.println("=== Test 1: Sequential lock/unlock ===");
        boolean r1 = dLock.tryLock("resource-1", "ClientA", 5000);
        boolean r2 = dLock.tryLock("resource-1", "ClientB", 5000); // should fail
        System.out.println("ClientA tryLock: " + r1);  // true
        System.out.println("ClientB tryLock: " + r2);  // false
        dLock.unlock("resource-1", "ClientA");
        boolean r3 = dLock.tryLock("resource-1", "ClientB", 5000); // should succeed now
        System.out.println("ClientB after A releases: " + r3); // true
        dLock.unlock("resource-1", "ClientB");

        // Test Case 2: TTL expiry
        System.out.println("=== Test 2: TTL expiry ===");
        dLock.tryLock("resource-2", "ClientC", 300); // short TTL
        Thread.sleep(400); // wait for expiry
        boolean r4 = dLock.tryLock("resource-2", "ClientD", 5000);
        System.out.println("ClientD after TTL expiry: " + r4); // true

        // Test Case 3: Edge - concurrent threads competing for same lock
        System.out.println("=== Test 3: Concurrent competition ===");
        DistributedLock concLock = new DistributedLock();
        int[] successCount = {0};
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            final String clientId = "Client-" + i;
            executor.submit(() -> {
                if (concLock.tryLock("shared", clientId, 1000)) {
                    synchronized (successCount) { successCount[0]++; }
                    System.out.println(clientId + " acquired lock");
                }
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        System.out.println("Only 1 client acquired lock: " + (successCount[0] == 1));
    }
}
