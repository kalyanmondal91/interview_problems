package org.interview.system_design.lld.ratelimiter;

import java.time.LocalDateTime;

/**
 * Decorator that logs every tryAcquire call with its result.
 * Pattern: Decorator (concrete)
 */
public class LoggingRateLimiterDecorator extends RateLimiterDecorator {

    public LoggingRateLimiterDecorator(RateLimiter delegate) {
        super(delegate);
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        RateLimitResult result = delegate.tryAcquire(clientId);
        System.out.printf("[RateLimiter][%s] client=%s allowed=%s remaining=%d reason=%s%n",
                LocalDateTime.now(), clientId,
                result.isAllowed(), result.getRemainingRequests(), result.getReason());
        return result;
    }
}
