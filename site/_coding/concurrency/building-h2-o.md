---
layout: problem
title: "Building H2 O"
category: concurrency
category_display: "Concurrency"
difficulty: Medium
time_complexity: "O(N) where N = number of molecules"
space_complexity: "O(1)"
tags: [concurrency]
render_with_liquid: false
---

## Problem

Building H2O Synchronize hydrogen and oxygen threads to form H2O molecules. Each molecule requires exactly 2 hydrogen atoms and 1 oxygen atom. hydrogen() and oxygen() are called by different threads; they must synchronize so that each molecule is formed with the correct ratio.

## Approach

Use two Semaphores to control how many H and O threads can proceed before forming a molecule. hydrogenSem allows 2 H per molecule; oxygenSem allows 1 O per molecule. A CyclicBarrier of size 3 synchronizes all 3 atoms before they "bond". The barrier's action releases the semaphores for the next molecule. Each thread acquires its semaphore, calls barrier.await(), then prints its atom.

## Solution

```java
package org.interview.coding.concurrency;

import java.util.concurrent.*;

/**
 * Problem: Building H2O
 * Difficulty: Medium
 *
 * Description: Synchronize hydrogen and oxygen threads to form H2O molecules. Each molecule
 * requires exactly 2 hydrogen atoms and 1 oxygen atom. hydrogen() and oxygen() are called by
 * different threads; they must synchronize so that each molecule is formed with the correct ratio.
 *
 * Example:
 *   Input: 4 hydrogen threads and 2 oxygen threads
 *   Output: "HHO" twice (in some valid order) - each molecule has 2H and 1O
 *
 * Approach: Use two Semaphores to control how many H and O threads can proceed before forming
 * a molecule. hydrogenSem allows 2 H per molecule; oxygenSem allows 1 O per molecule.
 * A CyclicBarrier of size 3 synchronizes all 3 atoms before they "bond". The barrier's action
 * releases the semaphores for the next molecule. Each thread acquires its semaphore, calls
 * barrier.await(), then prints its atom.
 *
 * Time Complexity: O(N) where N = number of molecules
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. 4H + 2O threads → 2 molecules formed (HHO, HHO in some order)
 *   2. 2H + 1O threads → 1 molecule
 *   3. Edge: excess H threads beyond molecules → remaining H block
 */
public class BuildingH2O {

    private final Semaphore hydrogenSem;
    private final Semaphore oxygenSem;
    private final CyclicBarrier barrier;

    public BuildingH2O() {
        hydrogenSem = new Semaphore(2);
        oxygenSem = new Semaphore(1);
        barrier = new CyclicBarrier(3, () -> {
            // Barrier action: release for next molecule after 3 atoms have gathered
            hydrogenSem.release(2);
            oxygenSem.release(1);
        });
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException, BrokenBarrierException {
        hydrogenSem.acquire();
        releaseHydrogen.run();
        barrier.await();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException, BrokenBarrierException {
        oxygenSem.acquire();
        releaseOxygen.run();
        barrier.await();
    }

    public static void main(String[] args) throws InterruptedException {
        // Test Case 1: 4H + 2O → 2 H2O molecules
        System.out.println("=== Test 1: 4H + 2O = 2 molecules ===");
        BuildingH2O water1 = new BuildingH2O();
        StringBuilder output1 = new StringBuilder();
        Object lock1 = new Object();

        Thread[] hThreads1 = new Thread[4];
        Thread[] oThreads1 = new Thread[2];
        for (int i = 0; i < 4; i++) {
            hThreads1[i] = new Thread(() -> {
                try {
                    water1.hydrogen(() -> { synchronized(lock1) { output1.append("H"); } });
                } catch (Exception e) { Thread.currentThread().interrupt(); }
            });
        }
        for (int i = 0; i < 2; i++) {
            oThreads1[i] = new Thread(() -> {
                try {
                    water1.oxygen(() -> { synchronized(lock1) { output1.append("O"); } });
                } catch (Exception e) { Thread.currentThread().interrupt(); }
            });
        }
        for (Thread t : hThreads1) t.start();
        for (Thread t : oThreads1) t.start();
        for (Thread t : hThreads1) t.join();
        for (Thread t : oThreads1) t.join();
        System.out.println("Output: " + output1 + " (length=" + output1.length() + ", expected 6)");

        // Test Case 2: 2H + 1O → 1 H2O molecule
        System.out.println("=== Test 2: 2H + 1O = 1 molecule ===");
        BuildingH2O water2 = new BuildingH2O();
        StringBuilder output2 = new StringBuilder();
        Object lock2 = new Object();
        Thread h1 = new Thread(() -> { try { water2.hydrogen(() -> { synchronized(lock2) { output2.append("H"); }}); } catch (Exception e) {} });
        Thread h2 = new Thread(() -> { try { water2.hydrogen(() -> { synchronized(lock2) { output2.append("H"); }}); } catch (Exception e) {} });
        Thread o1 = new Thread(() -> { try { water2.oxygen(() -> { synchronized(lock2) { output2.append("O"); }}); } catch (Exception e) {} });
        h1.start(); h2.start(); o1.start();
        h1.join(); h2.join(); o1.join();
        System.out.println("Output: " + output2 + " (length=" + output2.length() + ", expected 3)");

        // Test Case 3: Verify H count and O count are correct
        long hCount = output1.chars().filter(c -> c == 'H').count();
        long oCount = output1.chars().filter(c -> c == 'O').count();
        System.out.println("Test 1 ratio: H=" + hCount + ", O=" + oCount + " (2:1 = " + (hCount == 2*oCount) + ")");
    }
}
```

## Complexity

- **Time:** O(N) where N = number of molecules
- **Space:** O(1)
