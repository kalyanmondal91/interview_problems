package org.interview.coding.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * Problem: Dining Philosophers
 * Difficulty: Medium
 *
 * Description: Five philosophers sit at a round table with five forks between them. Each philosopher
 * alternates between thinking and eating. To eat, a philosopher must acquire both the fork to their
 * left and right. Design a solution that prevents deadlock.
 *
 * Example:
 *   Input: 5 philosophers, each eating 3 times
 *   Output: All philosophers eat 3 times without deadlock or starvation
 *
 * Approach: Assign a global ordering to forks (0-4). Each philosopher picks up the lower-numbered
 * fork first, then the higher-numbered fork. This breaks the circular wait condition that causes
 * deadlock. Philosopher i picks up fork min(i, (i+1)%5) first, then max(i, (i+1)%5).
 *
 * Time Complexity: O(philosophers * meals) overall
 * Space Complexity: O(N) for N philosopher threads and N locks
 *
 * Test Cases:
 *   1. 5 philosophers each eating 3 times → all complete without deadlock
 *   2. Verify no philosopher starves (all finish)
 *   3. Edge: single philosopher → eats without contention
 */
public class DiningPhilosophers {

    private static final int N = 5;
    private final ReentrantLock[] forks = new ReentrantLock[N];

    public DiningPhilosophers() {
        for (int i = 0; i < N; i++) forks[i] = new ReentrantLock();
    }

    class Philosopher implements Runnable {
        private final int id;
        private final int leftFork;
        private final int rightFork;
        private final int firstFork;
        private final int secondFork;

        Philosopher(int id) {
            this.id = id;
            this.leftFork = id;
            this.rightFork = (id + 1) % N;
            // Pick lower-numbered fork first to avoid deadlock
            this.firstFork = Math.min(leftFork, rightFork);
            this.secondFork = Math.max(leftFork, rightFork);
        }

        @Override
        public void run() {
            try {
                for (int meal = 0; meal < 3; meal++) {
                    // Think
                    System.out.println("Philosopher " + id + " is thinking");
                    Thread.sleep(100 + (int)(Math.random() * 100));

                    // Pick up forks in order (lower index first)
                    forks[firstFork].lock();
                    forks[secondFork].lock();

                    // Eat
                    System.out.println("Philosopher " + id + " is eating (meal " + (meal + 1) + ")");
                    Thread.sleep(100);

                    // Put down forks
                    forks[secondFork].unlock();
                    forks[firstFork].unlock();
                }
                System.out.println("Philosopher " + id + " is done!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DiningPhilosophers dp = new DiningPhilosophers();
        ExecutorService executor = Executors.newFixedThreadPool(N);

        // Test Case 1 & 2: 5 philosophers each eating 3 times
        System.out.println("=== Dining Philosophers (5 philosophers, 3 meals each) ===");
        for (int i = 0; i < N; i++) {
            executor.submit(dp.new Philosopher(i));
        }

        executor.shutdown();
        boolean completed = executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("All philosophers finished: " + completed);

        // Test Case 3: Edge - single philosopher
        System.out.println("=== Single Philosopher Test ===");
        DiningPhilosophers single = new DiningPhilosophers();
        Thread t = new Thread(single.new Philosopher(0));
        t.start();
        t.join(5000);
        System.out.println("Single philosopher done: " + !t.isAlive());
    }
}
