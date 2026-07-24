package org.interview.system_design.lld.ratelimiter;

/**
 * Core rate-limiter interface.
 * Pattern: Strategy (each algorithm is a concrete implementation)
 */
public interface RateLimiter {

    /**
     * Attempts to acquire a request slot for the given client.
     *
     * @param clientId identifies the caller (user, IP, API key)
     * @return result indicating whether the request is allowed
     */
    RateLimitResult tryAcquire(String clientId);

    /**
     * Resets the rate-limit state for a specific client.
     * Useful for administrative resets or testing.
     *
     * @param clientId the client whose state to clear
     */
    void reset(String clientId);
}
