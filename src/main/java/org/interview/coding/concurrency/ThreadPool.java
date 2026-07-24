package org.interview.coding.concurrency;

import java.util.concurrent.*;

/**
 * Problem: Custom Thread Pool
 * Difficulty: Medium
 *
 * Description: Implement a fixed-size thread pool with submit(Runnable) and shutdown() methods.
 * Worker threads continuously pull tasks from a shared queue. shutdown() signals all workers to
 * terminate gracefully after completing current tasks.
 *
 * Example:
 *   Input: pool of 4 workers, submit 10 tasks
 *   Output: All 10 tasks executed, workers terminate on shutdown
 *
 * Approach: Maintain a LinkedBlockingQueue of Runnables. N worker threads loop calling queue.take()
 * and running the task. submit() adds to the queue. shutdown() sends N poison-pill tasks (empty
 * Runnables with a termination flag) — one per worker — so each worker exits after receiving one.
 * Use a sentinel null or a special PoisonPill Runnable.
 *
 * Time Complexity: O(1) for submit, O(N) for shutdown
 * Space Complexity: O(queue_size + N) for queued tasks and worker threads
 *
 * Test Cases:
 *   1. Submit 10 tasks to 4-worker pool → all tasks run
 *   2. Shutdown after tasks complete → workers terminate cleanly
 *   3. Edge: Submit tasks after shutdown → tasks may be ignored (queue closed)
 */
public class ThreadPool {

    private static final Runnable POISON_PILL = () -> {};
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final Thread[] workers;
    private final int numWorkers;
    private volatile boolean shutdown = false;

    public ThreadPool(int numWorkers) {
        this.numWorkers = numWorkers;
        this.workers = new Thread[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            final int id = i;
            workers[i] = new Thread(() -> {
                while (true) {
                    try {
                        Runnable task = taskQueue.take();
                        if (task == POISON_PILL) break;
                        task.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("Worker-" + id + " terminated");
            }, "Worker-" + i);
            workers[i].start();
        }
    }

    public void submit(Runnable task) {
        if (!shutdown) {
            taskQueue.offer(task);
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown = true;
        // Send one poison pill per worker
        for (int i = 0; i < numWorkers; i++) {
            taskQueue.put(POISON_PILL);
        }
        for (Thread worker : workers) {
            worker.join();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool pool = new ThreadPool(4);

        // Test Case 1 & 2: Submit 10 tasks
        System.out.println("=== Submitting 10 tasks to 4-worker pool ===");
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName());
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                latch.countDown();
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        System.out.println("All tasks completed");

        // Test Case 3: Shutdown
        System.out.println("=== Shutting down pool ===");
        pool.shutdown();
        System.out.println("Pool shut down successfully");
    }
}
