package org.interview.system_design.lld.ratelimiter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite Rate Limiter — applies ALL child rate limiters.
 * A request is allowed only if every child permits it.
 * This enables layered policies, e.g. "100 req/min AND 1000 req/hour".
 * Pattern: Composite
 */
public class CompositeRateLimiter implements RateLimiter {

    private final List<RateLimiter> limiters;

    public CompositeRateLimiter(List<RateLimiter> limiters) {
        if (limiters == null || limiters.isEmpty()) {
            throw new IllegalArgumentException("CompositeRateLimiter requires at least one child");
        }
        this.limiters = new ArrayList<>(limiters);
    }

    public CompositeRateLimiter(RateLimiter... limiters) {
        this(Arrays.asList(limiters));
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        RateLimitResult lastResult = null;
        for (RateLimiter limiter : limiters) {
            lastResult = limiter.tryAcquire(clientId);
            if (!lastResult.isAllowed()) {
                // Short-circuit: first denial wins
                return RateLimitResult.deny(
                        "[Composite] Denied by " + limiter.getClass().getSimpleName()
                        + ": " + lastResult.getReason(),
                        lastResult.getResetTimeMs());
            }
        }
        // All passed — return the most conservative remaining count
        return lastResult;
    }

    @Override
    public void reset(String clientId) {
        limiters.forEach(l -> l.reset(clientId));
    }

    public List<RateLimiter> getLimiters() {
        return List.copyOf(limiters);
    }
}
