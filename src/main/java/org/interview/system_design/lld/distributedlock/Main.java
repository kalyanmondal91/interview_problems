package org.interview.system_design.lld.distributedlock;

/**
 * Demo runner for the Distributed Lock LLD.
 * Patterns: Decorator (MetricsLockDecorator, AutoRenewLockDecorator), Strategy (lock backend)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) throws InterruptedException {

        InMemoryLockStrategy strategy = new InMemoryLockStrategy();
        LockManager manager = new LockManager(strategy);

        banner("Scenario 1: Single-thread acquire -> work -> release");
        LockConfig config1 = new LockConfig("resource-A", 5000, 3, 100, "owner-1");
        DistributedLock lock1 = manager.getLock("resource-A", config1);

        System.out.println("  isLocked before: " + strategy.isLocked("resource-A"));
        LockResult result1 = lock1.acquire();
        System.out.println("  acquire(): " + result1.isAcquired());
        System.out.println("  isLocked  after: " + strategy.isLocked("resource-A"));
        System.out.println("  isHeldByCurrentThread: " + lock1.isHeldByCurrentThread());

        Thread.sleep(100);
        boolean released = lock1.release();
        System.out.println("  release(): " + released);
        System.out.println("  isLocked  after release: " + strategy.isLocked("resource-A"));

        banner("Scenario 2: Two threads compete for same lock");
        LockConfig config2  = new LockConfig("resource-B", 3000, 5, 200, "thread-1");
        LockConfig config2b = new LockConfig("resource-B", 3000, 5, 200, "thread-2");
        DistributedLock lockA = manager.getLock("resource-B", config2);
        DistributedLock lockB = manager.getLock("resource-B", config2b);

        LockResult resA = lockA.acquire();
        System.out.println("  Thread-1 acquired lock: " + resA.isAcquired());

        Thread t2 = new Thread(() -> {
            System.out.println("  Thread-2 attempting acquire (will retry)...");
            LockResult res = lockB.acquire();
            System.out.println("  Thread-2 acquire result: " + res.isAcquired());
            lockB.release();
        });

        t2.start();
        Thread.sleep(500);
        lockA.release();
        System.out.println("  Thread-1 released lock");
        t2.join(2000);

        banner("Scenario 3: ReentrantDistributedLock -- same thread acquires twice");
        LockConfig reentrantConfig = new LockConfig("reentrant-res", 5000, 3, 100, "main-thread");
        DistributedLock reentrant = manager.getLock("reentrant-res", reentrantConfig);

        LockResult r1 = reentrant.acquire();
        LockResult r2 = reentrant.acquire();
        System.out.println("  First  acquire: " + r1.isAcquired());
        System.out.println("  Second acquire (reentrant): " + r2.isAcquired());
        reentrant.release();
        System.out.println("  isLocked after 1st release: " + strategy.isLocked("reentrant-res"));
        reentrant.release();
        System.out.println("  isLocked after 2nd release: " + strategy.isLocked("reentrant-res"));

        banner("Scenario 4: MetricsLockDecorator -- track acquire count and wait time");
        LockConfig mConfig = new LockConfig("metrics-res", 5000, 3, 50, "metrics-owner");
        DistributedLock baseLock = manager.getLock("metrics-res", mConfig);
        MetricsLockDecorator metricLock = new MetricsLockDecorator(baseLock);

        for (int i = 0; i < 3; i++) {
            metricLock.acquire();
            Thread.sleep(50);
            metricLock.release();
        }
        System.out.printf("  Acquire count: %d | Avg wait time: %d ms%n",
                metricLock.getAcquireCount(), metricLock.getAverageWaitMs());

        banner("Scenario 5: TTL expiry -- lock expires after 300ms");
        LockConfig ttlConfig = new LockConfig("ttl-res", 300, 3, 50, "owner-ttl");
        DistributedLock ttlLock = manager.getLock("ttl-res", ttlConfig);
        LockResult ttlResult = ttlLock.acquire();
        System.out.println("  Lock acquired: " + ttlResult.isAcquired() + ". isLocked: " + strategy.isLocked("ttl-res"));
        Thread.sleep(400);
        System.out.println("  After 400ms (TTL=300ms). isLocked: " + strategy.isLocked("ttl-res"));

        LockConfig newOwnerConfig = new LockConfig("ttl-res", 5000, 3, 50, "new-owner");
        DistributedLock newOwnerLock = manager.getLock("ttl-res", newOwnerConfig);
        LockResult newAcquire = newOwnerLock.acquire();
        System.out.println("  New owner acquired expired lock: " + newAcquire.isAcquired());
        newOwnerLock.release();

        System.out.println();
        System.out.println("=== Distributed Lock Demo Complete ===");
    }
}
