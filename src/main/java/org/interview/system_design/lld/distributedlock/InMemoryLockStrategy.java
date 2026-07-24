package org.interview.system_design.lld.distributedlock;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory lock strategy that simulates a Redis SET NX PX pattern.
 * Uses ConcurrentHashMap for thread-safe operations.
 * Pattern: Strategy (concrete)
 */
public class InMemoryLockStrategy implements DistributedLockStrategy {

    private final ConcurrentHashMap<String, LockEntry> store = new ConcurrentHashMap<>();

    @Override
    public synchronized LockResult tryAcquire(LockConfig config) {
        LockEntry existing = store.get(config.getLockKey());

        // If an unexpired entry exists, fail fast
        if (existing != null && existing.expiresAt.isAfter(Instant.now())) {
            return LockResult.failed("Lock already held: " + config.getLockKey()
                    + " by " + existing.ownerId);
        }

        // Either no entry, or existing entry is expired — claim the lock
        String token = UUID.randomUUID().toString();
        Instant now  = Instant.now();
        Instant exp  = now.plusMillis(config.getTtlMs());
        store.put(config.getLockKey(), new LockEntry(token, config.getOwnerId(), exp));
        return new LockResult(LockStatus.ACQUIRED, token, now, exp, "Lock acquired");
    }

    @Override
    public synchronized boolean release(String lockKey, String lockToken) {
        LockEntry entry = store.get(lockKey);
        if (entry == null) return false;
        if (!entry.token.equals(lockToken)) return false; // token mismatch — reject
        store.remove(lockKey);
        return true;
    }

    @Override
    public boolean isLocked(String lockKey) {
        LockEntry entry = store.get(lockKey);
        return entry != null && entry.expiresAt.isAfter(Instant.now());
    }

    @Override
    public synchronized boolean renew(String lockKey, String lockToken, long ttlMs) {
        LockEntry entry = store.get(lockKey);
        if (entry == null || !entry.token.equals(lockToken)) return false;
        if (entry.expiresAt.isBefore(Instant.now())) return false; // already expired
        store.put(lockKey, new LockEntry(entry.token, entry.ownerId, Instant.now().plusMillis(ttlMs)));
        return true;
    }

    // ---- Inner entry ----

    private static class LockEntry {
        final String  token;
        final String  ownerId;
        final Instant expiresAt;

        LockEntry(String token, String ownerId, Instant expiresAt) {
            this.token     = token;
            this.ownerId   = ownerId;
            this.expiresAt = expiresAt;
        }
    }
}
