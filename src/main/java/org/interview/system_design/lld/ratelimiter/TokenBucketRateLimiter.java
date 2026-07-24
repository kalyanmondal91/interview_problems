package org.interview.system_design.lld.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Token Bucket algorithm.
 *
 * Each client has a bucket holding up to {@code maxTokens} tokens.
 * Tokens are refilled at a constant rate of {@code maxTokens / windowMs} tokens/ms.
 * Each request consumes one token; requests are rejected when the bucket is empty.
 *
 * Thread-safe: each bucket uses synchronized access.
 * Pattern: Strategy (concrete)
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final int maxTokens;
    private final long windowMs;

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimitConfig config) {
        this.maxTokens = config.getMaxRequests();
        this.windowMs  = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(clientId, k -> new TokenBucket());
        synchronized (bucket) {
            bucket.refill();
            if (bucket.tokens > 0) {
                bucket.tokens--;
                long resetMs = System.currentTimeMillis() + windowMs;
                return RateLimitResult.allow(bucket.tokens, resetMs);
            } else {
                long resetMs = bucket.lastRefillTime + windowMs;
                return RateLimitResult.deny("Token bucket exhausted for client: " + clientId, resetMs);
            }
        }
    }

    @Override
    public void reset(String clientId) {
        buckets.remove(clientId);
    }

    // ---- Inner state ----

    private class TokenBucket {
        int  tokens       = maxTokens;
        long lastRefillTime = System.currentTimeMillis();

        void refill() {
            long now   = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            if (elapsed > 0) {
                // How many tokens to add proportional to elapsed time
                long tokensToAdd = (elapsed * maxTokens) / windowMs;
                if (tokensToAdd > 0) {
                    tokens = (int) Math.min(maxTokens, tokens + tokensToAdd);
                    lastRefillTime = now;
                }
            }
        }
    }
}
