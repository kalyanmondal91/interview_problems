package org.interview.coding.design;

import java.util.*;
import java.util.concurrent.*;

/**
 * Problem: Design Rate Limiter
 * Difficulty: Medium
 *
 * Description: Implement a token bucket rate limiter where each user has a separate token bucket.
 * allow(userId) returns true if the user is allowed to proceed, false if rate-limited.
 * The bucket refills at 1 token/second up to a maximum of 10 tokens.
 *
 * Example:
 *   Input: allow("user1") called 11 times rapidly
 *   Output: true x10, false (11th attempt rate-limited)
 *
 * Approach: Per user, track current tokens (double) and lastRefillTimestamp. On each allow() call,
 * calculate elapsed time since last refill, add elapsed*refillRate tokens (capped at maxTokens).
 * If tokens >= 1, consume one token and return true; otherwise return false. Use synchronized
 * methods for thread safety across concurrent callers.
 *
 * Time Complexity: O(1) per allow() call
 * Space Complexity: O(U) where U = number of unique users
 *
 * Test Cases:
 *   1. Single user calls allow() 10 times rapidly → all true (bucket starts full)
 *   2. 11th rapid call → false (bucket exhausted)
 *   3. Edge: after 1 second sleep, bucket refills → allow() returns true again
 */
public class DesignRateLimiter {

    private static final int MAX_TOKENS = 10;
    private static final double REFILL_RATE_PER_SECOND = 1.0;

    static class UserBucket {
        double tokens;
        long lastRefillTimestamp;

        UserBucket() {
            this.tokens = MAX_TOKENS;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }
    }

    private final Map<String, UserBucket> buckets = new ConcurrentHashMap<>();

    public synchronized boolean allow(String userId) {
        UserBucket bucket = buckets.computeIfAbsent(userId, k -> new UserBucket());
        long now = System.currentTimeMillis();
        double elapsed = (now - bucket.lastRefillTimestamp) / 1000.0;
        bucket.tokens = Math.min(MAX_TOKENS, bucket.tokens + elapsed * REFILL_RATE_PER_SECOND);
        bucket.lastRefillTimestamp = now;

        if (bucket.tokens >= 1.0) {
            bucket.tokens -= 1.0;
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        DesignRateLimiter limiter = new DesignRateLimiter();

        // Test Case 1: 10 rapid calls should all succeed (bucket starts at 10)
        System.out.println("=== Test 1: 10 rapid requests ===");
        for (int i = 1; i <= 10; i++) {
            System.out.println("Request " + i + ": " + limiter.allow("user1"));
        }

        // Test Case 2: 11th call immediately after - should be false
        System.out.println("=== Test 2: 11th request immediately ===");
        System.out.println("Request 11: " + limiter.allow("user1")); // false

        // Test Case 3: Edge - wait 2 seconds, then 2 requests should succeed
        System.out.println("=== Test 3: After 2 second sleep ===");
        Thread.sleep(2000);
        System.out.println("After wait, request: " + limiter.allow("user1")); // true
        System.out.println("After wait, request: " + limiter.allow("user1")); // true
    }
}
