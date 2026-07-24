package org.interview.system_design.lld.ratelimiter;

/**
 * Abstract decorator for RateLimiter.
 * Subclasses add cross-cutting concerns (logging, metrics) without modifying algorithms.
 * Pattern: Decorator
 */
public abstract class RateLimiterDecorator implements RateLimiter {

    protected final RateLimiter delegate;

    protected RateLimiterDecorator(RateLimiter delegate) {
        this.delegate = delegate;
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        return delegate.tryAcquire(clientId);
    }

    @Override
    public void reset(String clientId) {
        delegate.reset(clientId);
    }
}
