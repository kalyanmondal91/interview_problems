package org.interview.system_design.lld.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Decorator that tracks hit/miss counts and rejection rate.
 * Pattern: Decorator (concrete)
 */
public class MetricsRateLimiterDecorator extends RateLimiterDecorator {

    private final AtomicLong totalRequests  = new AtomicLong(0);
    private final AtomicLong allowedCount   = new AtomicLong(0);
    private final AtomicLong rejectedCount  = new AtomicLong(0);

    public MetricsRateLimiterDecorator(RateLimiter delegate) {
        super(delegate);
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        RateLimitResult result = delegate.tryAcquire(clientId);
        totalRequests.incrementAndGet();
        if (result.isAllowed()) {
            allowedCount.incrementAndGet();
        } else {
            rejectedCount.incrementAndGet();
        }
        return result;
    }

    public long getTotalRequests()  { return totalRequests.get(); }
    public long getAllowedCount()   { return allowedCount.get(); }
    public long getRejectedCount()  { return rejectedCount.get(); }

    /** Returns rejection rate as a percentage (0–100). */
    public double getRejectionRate() {
        long total = totalRequests.get();
        return total == 0 ? 0.0 : (rejectedCount.get() * 100.0) / total;
    }

    public void printStats() {
        System.out.printf("[MetricsDecorator] total=%d allowed=%d rejected=%d rejectionRate=%.1f%%%n",
                getTotalRequests(), getAllowedCount(), getRejectedCount(), getRejectionRate());
    }
}
