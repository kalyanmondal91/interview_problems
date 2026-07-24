---
layout: problem
title: "Producer Consumer"
category: concurrency
category_display: "Concurrency"
difficulty: Medium
time_complexity: "O(N) total for N items"
space_complexity: "O(capacity) for buffer"
tags: [concurrency]
render_with_liquid: false
---

## Problem

Producer-Consumer Pattern Implement the classic producer-consumer pattern using a bounded buffer. Multiple producer threads generate items and add them to the buffer. Multiple consumer threads take items from the buffer and process them. The buffer blocks producers when full and consumers when empty.

## Approach

Use ArrayBlockingQueue which internally provides thread-safe bounded blocking semantics. queue.put(item) blocks when the queue is full; queue.take() blocks when empty. Producers run in separate threads via ExecutorService. After all items are produced, send sentinel values (-1) to signal consumers to stop.

## Solution

```java
package org.interview.coding.concurrency;

import java.util.*;
import java.util.concurrent.*;

/**
 * Problem: Producer-Consumer Pattern
 * Difficulty: Medium
 *
 * Description: Implement the classic producer-consumer pattern using a bounded buffer.
 * Multiple producer threads generate items and add them to the buffer. Multiple consumer threads
 * take items from the buffer and process them. The buffer blocks producers when full and consumers
 * when empty.
 *
 * Example:
 *   Input: 2 producers generating items 1-10, 2 consumers processing items, buffer capacity=5
 *   Output: All items produced and consumed without data loss or deadlock
 *
 * Approach: Use ArrayBlockingQueue which internally provides thread-safe bounded blocking semantics.
 * queue.put(item) blocks when the queue is full; queue.take() blocks when empty. Producers run in
 * separate threads via ExecutorService. After all items are produced, send sentinel values (-1) to
 * signal consumers to stop.
 *
 * Time Complexity: O(N) total for N items
 * Space Complexity: O(capacity) for buffer
 *
 * Test Cases:
 *   1. 1 producer + 1 consumer, 5 items, capacity=3 → all items consumed
 *   2. 2 producers + 2 consumers, 10 items → all consumed, no duplicates
 *   3. Edge: producer faster than consumer (buffer fills up) → producer blocks, no data loss
 */
public class ProducerConsumer {

    private static final int CAPACITY = 5;
    private static final int SENTINEL = -1;

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final int start;
        private final int end;
        private final String name;

        Producer(BlockingQueue<Integer> queue, int start, int end, String name) {
            this.queue = queue;
            this.start = start;
            this.end = end;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = start; i <= end; i++) {
                    queue.put(i);
                    System.out.println(name + " produced: " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final String name;

        Consumer(BlockingQueue<Integer> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int item = queue.take();
                    if (item == SENTINEL) break;
                    System.out.println(name + " consumed: " + item);
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(CAPACITY);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Test Case 1 & 2: 2 producers + 2 consumers
        executor.submit(new Producer(queue, 1, 5, "Producer-1"));
        executor.submit(new Producer(queue, 6, 10, "Producer-2"));
        executor.submit(new Consumer(queue, "Consumer-1"));
        executor.submit(new Consumer(queue, "Consumer-2"));

        // Wait for producers to finish, then send sentinels
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        // Send sentinels for 2 consumers
        queue.put(SENTINEL);
        queue.put(SENTINEL);

        // Test Case 3: Edge - verify queue drains properly
        Thread.sleep(500);
        System.out.println("Queue empty: " + queue.isEmpty());
    }
}
```

## Complexity

- **Time:** O(N) total for N items
- **Space:** O(capacity) for buffer
