package org.interview.system_design.lld.ratelimiter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding Window Log algorithm.
 *
 * Records the timestamp of every accepted request in a deque.
 * Before each new request, evicts timestamps outside the rolling window.
 * Accepts if the remaining log size is below maxRequests.
 *
 * Memory: O(maxRequests) per client.
 * Thread-safe: per-client synchronized access.
 * Pattern: Strategy (concrete)
 */
public class SlidingWindowRateLimiter implements RateLimiter {

    private final int  maxRequests;
    private final long windowMs;

    private final ConcurrentHashMap<String, Deque<Long>> logs = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimitConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowMs    = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        Deque<Long> log = logs.computeIfAbsent(clientId, k -> new ArrayDeque<>());
        synchronized (log) {
            long now      = System.currentTimeMillis();
            long cutoff   = now - windowMs;

            // Evict expired timestamps
            while (!log.isEmpty() && log.peekFirst() <= cutoff) {
                log.pollFirst();
            }

            if (log.size() < maxRequests) {
                log.addLast(now);
                int remaining = maxRequests - log.size();
                long resetMs  = log.isEmpty() ? now + windowMs : log.peekFirst() + windowMs;
                return RateLimitResult.allow(remaining, resetMs);
            } else {
                long oldestRequest = log.peekFirst();
                long resetMs = oldestRequest + windowMs;
                return RateLimitResult.deny(
                        "Sliding window limit reached for client: " + clientId, resetMs);
            }
        }
    }

    @Override
    public void reset(String clientId) {
        logs.remove(clientId);
    }
}
