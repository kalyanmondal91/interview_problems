package org.interview.system_design.lld.distributedlock;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simulated database-based pessimistic lock strategy.
 * In a real system this would use SELECT … FOR UPDATE or an advisory lock.
 * Here it simulates with a HashMap (not thread-safe by design — DB provides atomicity).
 * Pattern: Strategy (concrete)
 */
public class DatabaseLockStrategy implements DistributedLockStrategy {

    // Simulates a DB table: lock_key → row
    private final Map<String, DbLockRow> lockTable = new HashMap<>();

    @Override
    public synchronized LockResult tryAcquire(LockConfig config) {
        DbLockRow row = lockTable.get(config.getLockKey());
        if (row != null && row.expiresAt.isAfter(Instant.now())) {
            return LockResult.failed("[DB] Lock held by " + row.ownerId
                    + " until " + row.expiresAt);
        }

        String token = "db_" + UUID.randomUUID().toString().substring(0, 8);
        Instant now  = Instant.now();
        Instant exp  = now.plusMillis(config.getTtlMs());
        lockTable.put(config.getLockKey(), new DbLockRow(token, config.getOwnerId(), exp));
        System.out.printf("[DB] Acquired lock key=%s owner=%s token=%s%n",
                config.getLockKey(), config.getOwnerId(), token);
        return new LockResult(LockStatus.ACQUIRED, token, now, exp, "[DB] Lock acquired");
    }

    @Override
    public synchronized boolean release(String lockKey, String lockToken) {
        DbLockRow row = lockTable.get(lockKey);
        if (row == null || !row.token.equals(lockToken)) {
            System.out.printf("[DB] Release failed: key=%s token mismatch%n", lockKey);
            return false;
        }
        lockTable.remove(lockKey);
        System.out.printf("[DB] Released lock key=%s%n", lockKey);
        return true;
    }

    @Override
    public boolean isLocked(String lockKey) {
        DbLockRow row = lockTable.get(lockKey);
        return row != null && row.expiresAt.isAfter(Instant.now());
    }

    @Override
    public synchronized boolean renew(String lockKey, String lockToken, long ttlMs) {
        DbLockRow row = lockTable.get(lockKey);
        if (row == null || !row.token.equals(lockToken)) return false;
        lockTable.put(lockKey, new DbLockRow(row.token, row.ownerId, Instant.now().plusMillis(ttlMs)));
        System.out.printf("[DB] Renewed lock key=%s newExpiry=%s%n",
                lockKey, lockTable.get(lockKey).expiresAt);
        return true;
    }

    // ---- Inner row ----

    private static class DbLockRow {
        final String  token;
        final String  ownerId;
        final Instant expiresAt;

        DbLockRow(String token, String ownerId, Instant expiresAt) {
            this.token     = token;
            this.ownerId   = ownerId;
            this.expiresAt = expiresAt;
        }
    }
}
