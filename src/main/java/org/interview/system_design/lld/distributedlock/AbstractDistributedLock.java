package org.interview.system_design.lld.distributedlock;

import java.util.concurrent.TimeUnit;

/**
 * Base class that implements the retry logic for acquiring a distributed lock.
 * Subclasses override {@link #doAcquire()} to provide specific behaviour.
 * Pattern: Template Method
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    protected final LockConfig config;
    protected final DistributedLockStrategy strategy;

    /** Token held by the current owner; null when not held. */
    protected volatile String currentToken;

    protected AbstractDistributedLock(LockConfig config, DistributedLockStrategy strategy) {
        this.config   = config;
        this.strategy = strategy;
    }

    @Override
    public LockResult acquire() {
        int attempts = 0;
        while (attempts <= config.getMaxRetries()) {
            LockResult result = doAcquire();
            if (result.isAcquired()) {
                currentToken = result.getLockToken();
                return result;
            }
            attempts++;
            if (attempts <= config.getMaxRetries()) {
                System.out.printf("[%s] Retry %d/%d for key=%s%n",
                        getClass().getSimpleName(), attempts,
                        config.getMaxRetries(), config.getLockKey());
                sleep(config.getRetryDelayMs());
            }
        }
        return LockResult.failed("Failed to acquire lock after "
                + config.getMaxRetries() + " retries: " + config.getLockKey());
    }

    @Override
    public LockResult tryAcquire(long timeout, TimeUnit timeUnit) {
        long deadline = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        while (System.currentTimeMillis() < deadline) {
            LockResult result = doAcquire();
            if (result.isAcquired()) {
                currentToken = result.getLockToken();
                return result;
            }
            sleep(Math.min(config.getRetryDelayMs(), deadline - System.currentTimeMillis()));
        }
        return LockResult.failed("Timed out waiting for lock: " + config.getLockKey());
    }

    @Override
    public boolean release() {
        if (currentToken == null) return false;
        boolean released = strategy.release(config.getLockKey(), currentToken);
        if (released) currentToken = null;
        return released;
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return currentToken != null && strategy.isLocked(config.getLockKey());
    }

    /**
     * Template method: perform one acquire attempt without retry logic.
     * Subclasses may override to handle reentrance or other specializations.
     */
    protected LockResult doAcquire() {
        return strategy.tryAcquire(config);
    }

    protected void sleep(long ms) {
        if (ms <= 0) return;
        try { Thread.sleep(ms); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
