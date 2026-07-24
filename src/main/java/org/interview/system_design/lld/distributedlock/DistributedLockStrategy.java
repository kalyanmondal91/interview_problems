package org.interview.system_design.lld.distributedlock;

/**
 * Strategy interface for the low-level lock storage backend.
 * Concrete implementations: InMemoryLockStrategy (simulates Redis), DatabaseLockStrategy.
 * Pattern: Strategy
 */
public interface DistributedLockStrategy {

    /**
     * Attempts to acquire the lock described by config.
     *
     * @param config lock parameters including key, TTL, owner
     * @return result of the attempt
     */
    LockResult tryAcquire(LockConfig config);

    /**
     * Releases a previously acquired lock.
     *
     * @param lockKey   the resource key
     * @param lockToken the unique token issued when the lock was acquired
     * @return true if released successfully, false if token mismatch or already released
     */
    boolean release(String lockKey, String lockToken);

    /**
     * Checks whether the given key is currently locked (and not expired).
     *
     * @param lockKey the resource key
     * @return true if the key is locked
     */
    boolean isLocked(String lockKey);

    /**
     * Renews the TTL of an existing lock.
     *
     * @param lockKey   the resource key
     * @param lockToken the token of the current holder
     * @param ttlMs     the new TTL in milliseconds
     * @return true if renewal succeeded
     */
    boolean renew(String lockKey, String lockToken, long ttlMs);
}
