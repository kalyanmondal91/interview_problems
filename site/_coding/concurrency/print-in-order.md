---
layout: problem
title: "Print In Order"
category: concurrency
category_display: "Concurrency"
difficulty: Easy
time_complexity: "O(1) per method"
space_complexity: "O(1)"
tags: [concurrency]
render_with_liquid: false
---

## Problem

Print in Order Three methods first(), second(), third() must execute in order (first, then second, then third) regardless of which thread calls them or in what order threads are started. The class is shared across three threads.

## Approach

Use two Semaphores initialized to 0. first() executes, then releases sem1. second() acquires sem1 (blocks until first() completes), executes, then releases sem2. third() acquires sem2 (blocks until second() completes), then executes. This enforces the ordering regardless of thread scheduling.

## Solution

```java
package org.interview.coding.concurrency;

import java.util.concurrent.*;

/**
 * Problem: Print in Order
 * Difficulty: Easy
 *
 * Description: Three methods first(), second(), third() must execute in order (first, then second,
 * then third) regardless of which thread calls them or in what order threads are started. The class
 * is shared across three threads.
 *
 * Example:
 *   Input: Threads started in order [third, second, first]
 *   Output: "first" then "second" then "third" always printed in order
 *
 * Approach: Use two Semaphores initialized to 0. first() executes, then releases sem1.
 * second() acquires sem1 (blocks until first() completes), executes, then releases sem2.
 * third() acquires sem2 (blocks until second() completes), then executes. This enforces
 * the ordering regardless of thread scheduling.
 *
 * Time Complexity: O(1) per method
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Threads started in natural order → first, second, third
 *   2. Threads started in reverse order [3,2,1] → still first, second, third
 *   3. Edge: all three threads started simultaneously → correct order maintained
 */
public class PrintInOrder {

    private final Semaphore sem1 = new Semaphore(0);
    private final Semaphore sem2 = new Semaphore(0);

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        sem1.release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        sem1.acquire();
        printSecond.run();
        sem2.release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        sem2.acquire();
        printThird.run();
    }

    public static void main(String[] args) throws InterruptedException {
        // Test Case 1: Threads started in reverse order still print in order
        System.out.println("=== Test 1: Reverse thread start order ===");
        PrintInOrder printer1 = new PrintInOrder();
        Thread t3 = new Thread(() -> {
            try { printer1.third(() -> System.out.print("third ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread t2 = new Thread(() -> {
            try { printer1.second(() -> System.out.print("second ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread t1 = new Thread(() -> {
            try { printer1.first(() -> System.out.print("first ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        t3.start(); t2.start(); t1.start();
        t1.join(); t2.join(); t3.join();
        System.out.println();

        // Test Case 2: Natural order
        System.out.println("=== Test 2: Natural thread start order ===");
        PrintInOrder printer2 = new PrintInOrder();
        Thread a1 = new Thread(() -> {
            try { printer2.first(() -> System.out.print("first ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread a2 = new Thread(() -> {
            try { printer2.second(() -> System.out.print("second ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread a3 = new Thread(() -> {
            try { printer2.third(() -> System.out.print("third ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        a1.start(); a2.start(); a3.start();
        a1.join(); a2.join(); a3.join();
        System.out.println();

        // Test Case 3: Edge - simultaneous start
        System.out.println("=== Test 3: Simultaneous start ===");
        PrintInOrder printer3 = new PrintInOrder();
        CountDownLatch latch = new CountDownLatch(1);
        Thread b1 = new Thread(() -> {
            try { latch.await(); printer3.first(() -> System.out.print("first ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread b2 = new Thread(() -> {
            try { latch.await(); printer3.second(() -> System.out.print("second ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread b3 = new Thread(() -> {
            try { latch.await(); printer3.third(() -> System.out.print("third ")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        b3.start(); b2.start(); b1.start();
        latch.countDown();
        b1.join(); b2.join(); b3.join();
        System.out.println();
    }
}
```

## Complexity

- **Time:** O(1) per method
- **Space:** O(1)
