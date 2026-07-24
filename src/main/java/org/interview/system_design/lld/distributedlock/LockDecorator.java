package org.interview.system_design.lld.distributedlock;

import java.util.concurrent.TimeUnit;

/**
 * Abstract decorator for DistributedLock.
 * Subclasses add cross-cutting concerns (metrics, auto-renewal) transparently.
 * Pattern: Decorator
 */
public abstract class LockDecorator implements DistributedLock {

    protected final DistributedLock delegate;

    protected LockDecorator(DistributedLock delegate) {
        this.delegate = delegate;
    }

    @Override
    public LockResult acquire() {
        return delegate.acquire();
    }

    @Override
    public boolean release() {
        return delegate.release();
    }

    @Override
    public LockResult tryAcquire(long timeout, TimeUnit timeUnit) {
        return delegate.tryAcquire(timeout, timeUnit);
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return delegate.isHeldByCurrentThread();
    }
}
