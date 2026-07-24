---
layout: problem
title: "Bounded Blocking Queue"
category: concurrency
category_display: "Concurrency"
difficulty: Medium
time_complexity: "O(1) for push/pop/peek (amortized, ignoring block time)"
space_complexity: "O(capacity)"
tags: [concurrency]
render_with_liquid: false
---

## Problem

Bounded Blocking Queue Implement a thread-safe bounded blocking queue. push(val) blocks if queue is at capacity. pop() blocks if queue is empty and returns the head value. peek() returns the head without removing, blocks if empty. size() returns current element count.

## Approach

Use ReentrantLock with two Condition variables: notFull (signal when space available) and notEmpty (signal when element available). push() waits on notFull while at capacity, adds element, then signals notEmpty. pop() waits on notEmpty while empty, removes element, signals notFull. peek() similarly waits on notEmpty and reads without removing.

## Solution

```java
package org.interview.coding.concurrency;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Problem: Bounded Blocking Queue
 * Difficulty: Medium
 *
 * Description: Implement a thread-safe bounded blocking queue. push(val) blocks if queue is at
 * capacity. pop() blocks if queue is empty and returns the head value. peek() returns the head
 * without removing, blocks if empty. size() returns current element count.
 *
 * Example:
 *   Input: capacity=3, push(1), push(2), pop() in parallel threads
 *   Output: pop returns 1 (FIFO order)
 *
 * Approach: Use ReentrantLock with two Condition variables: notFull (signal when space available)
 * and notEmpty (signal when element available). push() waits on notFull while at capacity, adds
 * element, then signals notEmpty. pop() waits on notEmpty while empty, removes element, signals
 * notFull. peek() similarly waits on notEmpty and reads without removing.
 *
 * Time Complexity: O(1) for push/pop/peek (amortized, ignoring block time)
 * Space Complexity: O(capacity)
 *
 * Test Cases:
 *   1. push(1),push(2),push(3),pop() → 1 (FIFO)
 *   2. Concurrent pushes beyond capacity → pusher blocks until consumer pops
 *   3. Edge: pop on empty queue blocks until a push occurs
 */
public class BoundedBlockingQueue {

    private final LinkedList<Integer> queue = new LinkedList<>();
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public void push(int val) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.addLast(val);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int pop() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            int val = queue.removeFirst();
            notFull.signalAll();
            return val;
        } finally {
            lock.unlock();
        }
    }

    public int peek() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            return queue.peekFirst();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try { return queue.size(); } finally { lock.unlock(); }
    }

    public static void main(String[] args) throws InterruptedException {
        // Test Case 1: Basic push/pop FIFO
        BoundedBlockingQueue q = new BoundedBlockingQueue(3);
        q.push(1); q.push(2); q.push(3);
        System.out.println("pop()=" + q.pop()); // 1
        System.out.println("peek()=" + q.peek()); // 2
        System.out.println("size()=" + q.size()); // 2

        // Test Case 2: Concurrent push beyond capacity
        BoundedBlockingQueue q2 = new BoundedBlockingQueue(2);
        Thread producer = new Thread(() -> {
            try {
                q2.push(10); q2.push(20); q2.push(30); // 30 will block
                System.out.println("All pushed");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(200);
                System.out.println("Consumer popped: " + q2.pop());
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        producer.start(); consumer.start();
        producer.join(); consumer.join();

        // Test Case 3: Edge - pop blocks until push
        BoundedBlockingQueue q3 = new BoundedBlockingQueue(1);
        Thread popThread = new Thread(() -> {
            try { System.out.println("Popped: " + q3.pop()); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        popThread.start();
        Thread.sleep(200);
        q3.push(99);
        popThread.join();
    }
}
```

## Complexity

- **Time:** O(1) for push/pop/peek (amortized, ignoring block time)
- **Space:** O(capacity)
