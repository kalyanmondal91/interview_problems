package org.interview.system_design.lld.distributedlock;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages named distributed locks.
 * Creates and caches DistributedLock instances keyed by lock name.
 * Supports swappable strategies (in-memory or database backend).
 */
public class LockManager {

    private final DistributedLockStrategy strategy;
    private final ConcurrentHashMap<String, DistributedLock> lockCache = new ConcurrentHashMap<>();

    public LockManager(DistributedLockStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Returns an existing or newly created lock for the given name.
     * The lock is wrapped with metrics tracking.
     *
     * @param name   logical name of the resource
     * @param config lock configuration (TTL, retries, owner)
     * @return a DistributedLock ready to be acquired
     */
    public DistributedLock getLock(String name, LockConfig config) {
        return lockCache.computeIfAbsent(name, k -> {
            ReentrantDistributedLock base = new ReentrantDistributedLock(config, strategy);
            return new MetricsLockDecorator(base);
        });
    }

    /**
     * Convenience: get a reentrant lock with default config for the given owner.
     */
    public DistributedLock getLock(String name, String ownerId) {
        LockConfig config = new LockConfig(name, ownerId);
        return getLock(name, config);
    }

    /** Removes the cached lock entry (does NOT release an acquired lock). */
    public void evict(String name) {
        lockCache.remove(name);
    }

    /** Returns whether the given key is currently locked in the backend. */
    public boolean isLocked(String lockKey) {
        return strategy.isLocked(lockKey);
    }
}
