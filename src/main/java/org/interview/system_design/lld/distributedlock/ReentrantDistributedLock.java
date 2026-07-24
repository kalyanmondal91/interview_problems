package org.interview.system_design.lld.distributedlock;

/**
 * Reentrant distributed lock.
 * The same owner can acquire the lock multiple times without deadlocking.
 * It is fully released only when the hold count drops back to zero.
 * Pattern: Template Method (overrides doAcquire)
 */
public class ReentrantDistributedLock extends AbstractDistributedLock {

    private volatile int holdCount = 0;

    public ReentrantDistributedLock(LockConfig config, DistributedLockStrategy strategy) {
        super(config, strategy);
    }

    @Override
    protected synchronized LockResult doAcquire() {
        // If already held by this owner, increment count and return success
        if (currentToken != null && strategy.isLocked(config.getLockKey())) {
            holdCount++;
            System.out.printf("[ReentrantLock] Reentrant acquire key=%s holdCount=%d%n",
                    config.getLockKey(), holdCount);
            return LockResult.acquired(currentToken, config.getTtlMs());
        }
        // First acquisition
        LockResult result = strategy.tryAcquire(config);
        if (result.isAcquired()) {
            holdCount = 1;
        }
        return result;
    }

    @Override
    public synchronized boolean release() {
        if (currentToken == null || holdCount == 0) return false;
        holdCount--;
        System.out.printf("[ReentrantLock] Released (holdCount now %d) key=%s%n",
                holdCount, config.getLockKey());
        if (holdCount == 0) {
            boolean released = strategy.release(config.getLockKey(), currentToken);
            if (released) currentToken = null;
            return released;
        }
        return true; // still held
    }

    public int getHoldCount() { return holdCount; }
}
