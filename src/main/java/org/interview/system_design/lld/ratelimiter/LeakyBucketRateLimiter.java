package org.interview.system_design.lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Leaky Bucket algorithm.
 *
 * Requests fill a fixed-capacity queue. The queue drains (leaks) at a constant
 * rate of {@code maxRequests / windowMs} requests/ms.
 * Incoming requests are accepted only when the queue is not full.
 *
 * Thread-safe: each bucket uses synchronized access.
 * Pattern: Strategy (concrete)
 */
public class LeakyBucketRateLimiter implements RateLimiter {

    private final int  capacity;
    private final long windowMs;

    private final ConcurrentHashMap<String, LeakyBucket> buckets = new ConcurrentHashMap<>();

    public LeakyBucketRateLimiter(RateLimitConfig config) {
        this.capacity = config.getMaxRequests();
        this.windowMs = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        LeakyBucket bucket = buckets.computeIfAbsent(clientId, k -> new LeakyBucket());
        synchronized (bucket) {
            bucket.leak();
            if (bucket.water < capacity) {
                bucket.water++;
                int remaining = capacity - bucket.water;
                long resetMs = System.currentTimeMillis() + windowMs;
                return RateLimitResult.allow(remaining, resetMs);
            } else {
                long resetMs = bucket.lastLeakTime + windowMs;
                return RateLimitResult.deny("Leaky bucket full for client: " + clientId, resetMs);
            }
        }
    }

    @Override
    public void reset(String clientId) {
        buckets.remove(clientId);
    }

    // ---- Inner state ----

    private class LeakyBucket {
        int  water       = 0;
        long lastLeakTime = System.currentTimeMillis();

        void leak() {
            long now     = System.currentTimeMillis();
            long elapsed = now - lastLeakTime;
            if (elapsed > 0) {
                long leaked = (elapsed * capacity) / windowMs;
                if (leaked > 0) {
                    water = (int) Math.max(0, water - leaked);
                    lastLeakTime = now;
                }
            }
        }
    }
}
