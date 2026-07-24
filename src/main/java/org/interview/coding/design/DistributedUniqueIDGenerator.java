package org.interview.coding.design;

import java.util.*;

/**
 * Problem: Distributed Unique ID Generator (Snowflake)
 * Difficulty: Hard
 *
 * Description: Generate globally unique 64-bit IDs in a distributed system. Each ID encodes a
 * 41-bit millisecond timestamp, 10-bit machine ID, and 12-bit per-ms sequence number. IDs are
 * monotonically increasing and sortable by time.
 *
 * Example:
 *   Input: nextId() called multiple times rapidly
 *   Output: Unique long IDs, increasing over time
 *
 * Approach: On each nextId() call, get current milliseconds. If same as last, increment the
 * 12-bit sequence (if overflow at 4095, wait for next millisecond). If different ms, reset
 * sequence to 0. Compose result as: (timestamp << 22) | (machineId << 12) | sequence.
 * Handle clock rollback by waiting until time catches up. Use synchronized for thread safety.
 *
 * Time Complexity: O(1) amortized per ID generation
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Generate 5 IDs rapidly → all unique, all increasing
 *   2. Generate 4096 IDs in the same ms → sequence wraps, waits for next ms
 *   3. Edge: extract timestamp/machineId/sequence from generated ID to verify correctness
 */
public class DistributedUniqueIDGenerator {

    private static final long EPOCH = 1700000000000L; // custom epoch
    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1; // 4095
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1; // 1023

    private final long machineId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public DistributedUniqueIDGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("Machine ID must be 0-" + MAX_MACHINE_ID);
        }
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currentMs = System.currentTimeMillis() - EPOCH;

        // Handle clock rollback
        if (currentMs < lastTimestamp) {
            long diff = lastTimestamp - currentMs;
            if (diff <= 5) {
                try { Thread.sleep(diff); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                currentMs = System.currentTimeMillis() - EPOCH;
            } else {
                throw new RuntimeException("Clock moved backwards by " + diff + " ms");
            }
        }

        if (currentMs == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // Wait for next millisecond
                while (currentMs <= lastTimestamp) {
                    currentMs = System.currentTimeMillis() - EPOCH;
                }
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentMs;
        return (currentMs << (MACHINE_ID_BITS + SEQUENCE_BITS))
             | (machineId << SEQUENCE_BITS)
             | sequence;
    }

    public static void main(String[] args) throws InterruptedException {
        DistributedUniqueIDGenerator gen = new DistributedUniqueIDGenerator(1);

        // Test Case 1: Generate 5 IDs and verify they are unique and increasing
        System.out.println("=== Test 1: 5 unique IDs ===");
        long prev = -1;
        for (int i = 0; i < 5; i++) {
            long id = gen.nextId();
            System.out.println("ID: " + id + " (increasing=" + (id > prev) + ")");
            prev = id;
        }

        // Test Case 2: Generate 10 IDs rapidly in same millisecond
        System.out.println("=== Test 2: 10 rapid IDs ===");
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 10; i++) ids.add(gen.nextId());
        System.out.println("All 10 unique: " + (ids.size() == 10));

        // Test Case 3: Edge - verify decomposition of an ID
        System.out.println("=== Test 3: ID decomposition ===");
        long id = gen.nextId();
        long ts = id >> (MACHINE_ID_BITS + SEQUENCE_BITS);
        long mach = (id >> SEQUENCE_BITS) & MAX_MACHINE_ID;
        long seq = id & MAX_SEQUENCE;
        System.out.println("ID=" + id + " => ts=" + ts + ", machineId=" + mach + ", seq=" + seq);
        System.out.println("Machine ID correct: " + (mach == 1));
    }
}
