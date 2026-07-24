package org.interview.system_design.lld.distributedlock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Decorator that automatically renews the lock's TTL while it is held.
 * A background thread renews the TTL at half the TTL interval to prevent expiry.
 * Pattern: Decorator (concrete)
 */
public class AutoRenewLockDecorator extends LockDecorator {

    private final DistributedLockStrategy strategy;
    private final LockConfig config;
    private final ScheduledExecutorService scheduler;

    private volatile String currentToken;
    private volatile ScheduledFuture<?> renewalTask;

    public AutoRenewLockDecorator(DistributedLock delegate,
                                  DistributedLockStrategy strategy,
                                  LockConfig config) {
        super(delegate);
        this.strategy  = strategy;
        this.config    = config;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "lock-renew-" + config.getLockKey());
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public LockResult acquire() {
        LockResult result = delegate.acquire();
        if (result.isAcquired()) {
            currentToken = result.getLockToken();
            scheduleRenewal();
        }
        return result;
    }

    @Override
    public LockResult tryAcquire(long timeout, TimeUnit timeUnit) {
        LockResult result = delegate.tryAcquire(timeout, timeUnit);
        if (result.isAcquired()) {
            currentToken = result.getLockToken();
            scheduleRenewal();
        }
        return result;
    }

    @Override
    public boolean release() {
        cancelRenewal();
        boolean released = delegate.release();
        if (released) currentToken = null;
        return released;
    }

    // ---- Private helpers ----

    private void scheduleRenewal() {
        long renewalIntervalMs = config.getTtlMs() / 2;
        renewalTask = scheduler.scheduleAtFixedRate(() -> {
            if (currentToken != null) {
                boolean renewed = strategy.renew(config.getLockKey(), currentToken, config.getTtlMs());
                System.out.printf("[AutoRenew] Renewed lock key=%s success=%s%n",
                        config.getLockKey(), renewed);
                if (!renewed) cancelRenewal();
            }
        }, renewalIntervalMs, renewalIntervalMs, TimeUnit.MILLISECONDS);
    }

    private void cancelRenewal() {
        if (renewalTask != null && !renewalTask.isCancelled()) {
            renewalTask.cancel(false);
            renewalTask = null;
        }
    }

    /** Shutdown the renewal scheduler when the lock is no longer needed. */
    public void shutdown() {
        cancelRenewal();
        scheduler.shutdownNow();
    }
}
