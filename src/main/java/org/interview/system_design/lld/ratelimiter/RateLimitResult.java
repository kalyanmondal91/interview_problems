package org.interview.system_design.lld.ratelimiter;

/**
 * Result of a rate-limit check.
 * Immutable value object returned by every RateLimiter implementation.
 */
public class RateLimitResult {

    private final boolean allowed;
    private final int remainingRequests;
    private final long resetTimeMs;
    private final String reason;

    public RateLimitResult(boolean allowed, int remainingRequests, long resetTimeMs, String reason) {
        this.allowed = allowed;
        this.remainingRequests = remainingRequests;
        this.resetTimeMs = resetTimeMs;
        this.reason = reason;
    }

    public static RateLimitResult allow(int remaining, long resetTimeMs) {
        return new RateLimitResult(true, remaining, resetTimeMs, "OK");
    }

    public static RateLimitResult deny(String reason, long resetTimeMs) {
        return new RateLimitResult(false, 0, resetTimeMs, reason);
    }

    public boolean isAllowed()          { return allowed; }
    public int getRemainingRequests()   { return remainingRequests; }
    public long getResetTimeMs()        { return resetTimeMs; }
    public String getReason()           { return reason; }

    @Override
    public String toString() {
        return String.format("RateLimitResult[allowed=%s, remaining=%d, resetMs=%d, reason=%s]",
                allowed, remainingRequests, resetTimeMs, reason);
    }
}
