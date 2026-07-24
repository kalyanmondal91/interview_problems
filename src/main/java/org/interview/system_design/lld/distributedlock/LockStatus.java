package org.interview.system_design.lld.distributedlock;

/**
 * Possible states of a distributed lock.
 */
public enum LockStatus {
    ACQUIRED,
    RELEASED,
    EXPIRED,
    FAILED
}
