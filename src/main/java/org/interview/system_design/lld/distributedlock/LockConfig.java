package org.interview.system_design.lld.distributedlock;

/**
 * Configuration for acquiring a distributed lock.
 */
public class LockConfig {

    private final String lockKey;
    private final long   ttlMs;
    private final int    maxRetries;
    private final long   retryDelayMs;
    private final String ownerId;

    public LockConfig(String lockKey, long ttlMs, int maxRetries, long retryDelayMs, String ownerId) {
        this.lockKey      = lockKey;
        this.ttlMs        = ttlMs;
        this.maxRetries   = maxRetries;
        this.retryDelayMs = retryDelayMs;
        this.ownerId      = ownerId;
    }

    /** Convenience constructor with sensible defaults. */
    public LockConfig(String lockKey, String ownerId) {
        this(lockKey, 30_000L, 3, 100L, ownerId);
    }

    public String getLockKey()     { return lockKey; }
    public long   getTtlMs()       { return ttlMs; }
    public int    getMaxRetries()  { return maxRetries; }
    public long   getRetryDelayMs(){ return retryDelayMs; }
    public String getOwnerId()     { return ownerId; }

    @Override
    public String toString() {
        return String.format("LockConfig[key=%s, ttl=%dms, owner=%s]", lockKey, ttlMs, ownerId);
    }
}
