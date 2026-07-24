package org.interview.coding.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * Problem: Custom Read-Write Lock
 * Difficulty: Medium
 *
 * Description: Implement a read-write lock allowing multiple concurrent readers but exclusive
 * writers. readLock()/readUnlock() manage concurrent reads. writeLock()/writeUnlock() ensure
 * exclusive write access. The first reader acquires an underlying mutex; the last reader releases it.
 *
 * Example:
 *   Input: 3 reader threads + 1 writer thread on a shared resource
 *   Output: Readers run concurrently, writer gets exclusive access
 *
 * Approach: Track readerCount with a synchronized counter. readLock: increment readerCount;
 * if first reader (count=1), acquire the write mutex. readUnlock: decrement count; if last reader
 * (count=0), release mutex. writeLock: directly acquire the mutex (blocks all new readers and
 * other writers). writeUnlock: release mutex.
 *
 * Time Complexity: O(1) for all lock/unlock operations
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. 3 concurrent readers → all run simultaneously
 *   2. Writer waits for active readers to finish
 *   3. Edge: No active readers → writer acquires lock immediately
 */
public class ReadWriteLock {

    private int readerCount = 0;
    private final ReentrantLock mutex = new ReentrantLock();
    private final Object readerCountLock = new Object();

    public void readLock() {
        synchronized (readerCountLock) {
            readerCount++;
            if (readerCount == 1) mutex.lock(); // first reader blocks writers
        }
    }

    public void readUnlock() {
        synchronized (readerCountLock) {
            readerCount--;
            if (readerCount == 0) mutex.unlock(); // last reader unblocks writers
        }
    }

    public void writeLock() {
        mutex.lock();
    }

    public void writeUnlock() {
        mutex.unlock();
    }

    static int sharedResource = 0;

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLock rwLock = new ReadWriteLock();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Test Case 1 & 2: 3 readers and 1 writer
        System.out.println("=== Test 1: 3 Concurrent Readers ===");
        CountDownLatch readersDone = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            final int id = i;
            executor.submit(() -> {
                rwLock.readLock();
                try {
                    System.out.println("Reader " + id + " reading: " + sharedResource);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.readUnlock();
                    readersDone.countDown();
                }
            });
        }
        readersDone.await();

        // Test Case 3: Edge - writer gets exclusive access
        System.out.println("=== Test 2: Writer exclusive access ===");
        executor.submit(() -> {
            rwLock.writeLock();
            try {
                sharedResource = 42;
                System.out.println("Writer wrote: " + sharedResource);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.writeUnlock();
            }
        });

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Final resource value: " + sharedResource);
    }
}
