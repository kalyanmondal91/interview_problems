package org.interview.system_design.lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fixed Window Counter algorithm.
 *
 * Maintains a counter per client per fixed time window.
 * The counter resets at the start of each new window.
 *
 * Known limitation: burst traffic is possible at window boundaries.
 * Thread-safe: uses AtomicInteger and AtomicLong.
 * Pattern: Strategy (concrete)
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final int  maxRequests;
    private final long windowMs;

    private final ConcurrentHashMap<String, WindowState> windows = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimitConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowMs    = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        WindowState state = windows.computeIfAbsent(clientId, k -> new WindowState());
        synchronized (state) {
            long now = System.currentTimeMillis();
            // Reset window if it has expired
            if (now - state.windowStart >= windowMs) {
                state.count      = 0;
                state.windowStart = now;
            }
            if (state.count < maxRequests) {
                state.count++;
                int remaining = maxRequests - state.count;
                long resetMs  = state.windowStart + windowMs;
                return RateLimitResult.allow(remaining, resetMs);
            } else {
                long resetMs = state.windowStart + windowMs;
                return RateLimitResult.deny(
                        "Rate limit exceeded in fixed window for client: " + clientId, resetMs);
            }
        }
    }

    @Override
    public void reset(String clientId) {
        windows.remove(clientId);
    }

    // ---- Inner state ----

    private static class WindowState {
        int  count       = 0;
        long windowStart = System.currentTimeMillis();
    }
}
