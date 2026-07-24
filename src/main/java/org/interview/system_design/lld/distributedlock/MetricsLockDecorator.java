package org.interview.system_design.lld.distributedlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Decorator that tracks acquire count, total wait time, and total hold time.
 * Pattern: Decorator (concrete)
 */
public class MetricsLockDecorator extends LockDecorator {

    private final AtomicLong acquireCount   = new AtomicLong(0);
    private final AtomicLong totalWaitMs    = new AtomicLong(0);
    private final AtomicLong totalHoldMs    = new AtomicLong(0);

    private volatile long acquireStartTime;
    private volatile long holdStartTime;

    public MetricsLockDecorator(DistributedLock delegate) {
        super(delegate);
    }

    @Override
    public LockResult acquire() {
        acquireStartTime = System.currentTimeMillis();
        LockResult result = delegate.acquire();
        if (result.isAcquired()) {
            long waitMs = System.currentTimeMillis() - acquireStartTime;
            totalWaitMs.addAndGet(waitMs);
            acquireCount.incrementAndGet();
            holdStartTime = System.currentTimeMillis();
        }
        return result;
    }

    @Override
    public LockResult tryAcquire(long timeout, TimeUnit timeUnit) {
        acquireStartTime = System.currentTimeMillis();
        LockResult result = delegate.tryAcquire(timeout, timeUnit);
        if (result.isAcquired()) {
            long waitMs = System.currentTimeMillis() - acquireStartTime;
            totalWaitMs.addAndGet(waitMs);
            acquireCount.incrementAndGet();
            holdStartTime = System.currentTimeMillis();
        }
        return result;
    }

    @Override
    public boolean release() {
        boolean released = delegate.release();
        if (released && holdStartTime > 0) {
            long holdMs = System.currentTimeMillis() - holdStartTime;
            totalHoldMs.addAndGet(holdMs);
        }
        return released;
    }

    public long getAcquireCount()      { return acquireCount.get(); }
    public long getAverageWaitMs()     {
        long count = acquireCount.get();
        return count == 0 ? 0 : totalWaitMs.get() / count;
    }
    public long getAverageHoldMs()     {
        long count = acquireCount.get();
        return count == 0 ? 0 : totalHoldMs.get() / count;
    }

    public void printStats() {
        System.out.printf("[MetricsLockDecorator] acquires=%d avgWait=%dms avgHold=%dms%n",
                getAcquireCount(), getAverageWaitMs(), getAverageHoldMs());
    }
}
