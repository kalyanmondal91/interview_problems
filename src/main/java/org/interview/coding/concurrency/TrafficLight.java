package org.interview.coding.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * Problem: Traffic Light Controlled Intersection
 * Difficulty: Medium
 *
 * Description: Control a traffic light for two groups of cars (A and B) at an intersection.
 * carArrived(carId, directionGroup, turn, greenLight, turnGreen) is called when a car arrives.
 * Only the current green group may proceed; otherwise the light must switch.
 *
 * Example:
 *   Input: cars from group A then B arrive at alternate turns
 *   Output: Each group proceeds when it's their turn, light switches when needed
 *
 * Approach: Use a ReentrantLock to synchronize state transitions. Track which group currently
 * has the green light. When a car from the green group arrives, call greenLight.run() directly.
 * When a car from the red group arrives, call turnGreen.run() to switch, update state, then
 * call greenLight.run() for the new green group.
 *
 * Time Complexity: O(1) per car arrival
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Cars arrive from alternating groups → light switches at each turn
 *   2. Multiple cars from same group → proceed without switching
 *   3. Edge: 6 cars in correct sequence → all pass in order
 */
public class TrafficLight {

    private int currentGreen; // 1 = group A, 2 = group B
    private final ReentrantLock lock = new ReentrantLock();

    public TrafficLight() {
        this.currentGreen = 1; // Group A starts green
    }

    public void carArrived(
            int carId,
            int directionGroup,
            int turn,
            Runnable greenLight,
            Runnable turnGreen) {

        lock.lock();
        try {
            if (currentGreen != directionGroup) {
                turnGreen.run();
                currentGreen = directionGroup;
            }
            greenLight.run();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Test Case 1: Alternating groups
        System.out.println("=== Test 1: Alternating groups ===");
        TrafficLight light1 = new TrafficLight();
        int[][] cars = {{1, 2, 1}, {2, 1, 2}, {3, 2, 3}, {4, 1, 4}};
        for (int[] car : cars) {
            int carId = car[0];
            int group = car[1];
            int turn = car[2];
            light1.carArrived(carId, group, turn,
                () -> System.out.println("Car " + carId + " (group " + group + ") passed GREEN"),
                () -> System.out.println("  [Light switched to group " + group + "]"));
        }

        // Test Case 2: Multiple cars same group
        System.out.println("=== Test 2: Same group, no light switch ===");
        TrafficLight light2 = new TrafficLight();
        for (int i = 1; i <= 3; i++) {
            final int id = i;
            light2.carArrived(id, 1, i,
                () -> System.out.println("Car " + id + " (group 1) passed"),
                () -> System.out.println("  [Light switched!]"));
        }

        // Test Case 3: Edge - concurrent cars (thread-safe check)
        System.out.println("=== Test 3: Concurrent arrivals ===");
        TrafficLight light3 = new TrafficLight();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 1; i <= 6; i++) {
            final int id = i;
            final int group = (i % 2 == 0) ? 2 : 1;
            executor.submit(() -> {
                try { latch.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                light3.carArrived(id, group, id,
                    () -> System.out.println("Car " + id + " (group " + group + ") passed"),
                    () -> System.out.println("  [Light switched to group " + group + "]"));
            });
        }
        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
