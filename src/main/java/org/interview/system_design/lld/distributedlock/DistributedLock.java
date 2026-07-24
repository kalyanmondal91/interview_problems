package org.interview.system_design.lld.distributedlock;

import java.util.concurrent.TimeUnit;

/**
 * High-level distributed lock interface exposed to application code.
 * Implementations wrap a DistributedLockStrategy and add retry, reentrance, and lifecycle management.
 */
public interface DistributedLock {

    /**
     * Acquires the lock, blocking until it is available or the configured retries are exhausted.
     *
     * @return LockResult indicating success or failure
     */
    LockResult acquire();

    /**
     * Releases the lock held by the current thread/owner.
     *
     * @return true if released successfully
     */
    boolean release();

    /**
     * Tries to acquire the lock within the specified timeout.
     *
     * @param timeout  maximum wait time
     * @param timeUnit unit of timeout
     * @return LockResult indicating success or failure
     */
    LockResult tryAcquire(long timeout, TimeUnit timeUnit);

    /**
     * Returns true if the current thread/owner holds this lock.
     */
    boolean isHeldByCurrentThread();
}
