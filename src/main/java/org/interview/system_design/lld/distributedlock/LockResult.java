package org.interview.system_design.lld.distributedlock;

import java.time.Instant;

/**
 * Immutable result returned by a distributed lock acquisition attempt.
 */
public class LockResult {

    private final LockStatus status;
    private final String     lockToken;   // unique token for the lock holder
    private final Instant    acquiredAt;
    private final Instant    expiresAt;
    private final String     message;

    public LockResult(LockStatus status, String lockToken,
                      Instant acquiredAt, Instant expiresAt, String message) {
        this.status     = status;
        this.lockToken  = lockToken;
        this.acquiredAt = acquiredAt;
        this.expiresAt  = expiresAt;
        this.message    = message;
    }

    public static LockResult acquired(String token, long ttlMs) {
        Instant now = Instant.now();
        return new LockResult(LockStatus.ACQUIRED, token,
                now, now.plusMillis(ttlMs), "Lock acquired");
    }

    public static LockResult failed(String message) {
        return new LockResult(LockStatus.FAILED, null, null, null, message);
    }

    public LockStatus getStatus()    { return status; }
    public String     getLockToken() { return lockToken; }
    public Instant    getAcquiredAt(){ return acquiredAt; }
    public Instant    getExpiresAt() { return expiresAt; }
    public String     getMessage()   { return message; }

    public boolean isAcquired() { return status == LockStatus.ACQUIRED; }

    @Override
    public String toString() {
        return String.format("LockResult[status=%s, token=%s, expires=%s, msg=%s]",
                status, lockToken, expiresAt, message);
    }
}
