package org.interview.system_design.lld.ratelimiter;

import java.util.List;

/**
 * Demo runner for the Rate Limiter LLD.
 * Patterns: Strategy (algorithm), Composite, Decorator
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // ── Scenario 1: Token Bucket -- 5 req/min, fire 7 ─────────────────────
        System.out.println("\n=== Scenario 1: TOKEN_BUCKET -- 5 req/min, fire 7 requests ===");
        RateLimitConfig config = new RateLimitConfig(5, TimeWindow.MINUTE);
        RateLimiter tokenBucket = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.TOKEN_BUCKET, config);

        for (int i = 1; i <= 7; i++) {
            RateLimitResult result = tokenBucket.tryAcquire("client-A");
            System.out.printf("  Request %d: %s | Remaining: %d%s%n",
                    i, result.isAllowed() ? "ALLOWED" : "DENIED",
                    result.getRemainingRequests(),
                    result.isAllowed() ? "" : " | Reason: " + result.getReason());
        }

        // ── Scenario 2: Fixed Window ───────────────────────────────────────────
        System.out.println("\n=== Scenario 2: FIXED_WINDOW -- 5 req/min, fire 7 requests ===");
        RateLimiter fixedWindow = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.FIXED_WINDOW, config);

        for (int i = 1; i <= 7; i++) {
            RateLimitResult result = fixedWindow.tryAcquire("client-B");
            System.out.printf("  Request %d: %s | Remaining: %d%n",
                    i, result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingRequests());
        }

        // ── Scenario 3: Sliding Window ────────────────────────────────────────
        System.out.println("\n=== Scenario 3: SLIDING_WINDOW -- 3 req/10s window ===");
        RateLimitConfig slideConfig = new RateLimitConfig(3, TimeWindow.SECOND);
        RateLimiter sliding = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.SLIDING_WINDOW, slideConfig);

        for (int i = 1; i <= 5; i++) {
            RateLimitResult r = sliding.tryAcquire("client-C");
            System.out.printf("  Request %d: %s%n", i, r.isAllowed() ? "ALLOWED" : "DENIED");
        }

        // ── Scenario 4: CompositeRateLimiter ─────────────────────────────────
        System.out.println("\n=== Scenario 4: CompositeRateLimiter -- user (10/min) AND ip (3/min) ===");
        RateLimiter userLimiter = new RateLimiterFactory().create(
                RateLimiterFactory.Algorithm.TOKEN_BUCKET, new RateLimitConfig(10, TimeWindow.MINUTE));
        RateLimiter ipLimiter   = new RateLimiterFactory().create(
                RateLimiterFactory.Algorithm.TOKEN_BUCKET, new RateLimitConfig(3,  TimeWindow.MINUTE));

        CompositeRateLimiter composite = new CompositeRateLimiter(List.of(userLimiter, ipLimiter));

        for (int i = 1; i <= 5; i++) {
            RateLimitResult r = composite.tryAcquire("user-1");
            System.out.printf("  Request %d: %s%s%n", i,
                    r.isAllowed() ? "ALLOWED" : "DENIED (IP limit hit)",
                    r.isAllowed() ? "" : " -- " + r.getReason());
        }

        // ── Scenario 5: LoggingDecorator ─────────────────────────────────────
        System.out.println("\n=== Scenario 5: LoggingRateLimiterDecorator ===");
        RateLimiter base    = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.TOKEN_BUCKET,
                new RateLimitConfig(2, TimeWindow.MINUTE));
        RateLimiter logging = new LoggingRateLimiterDecorator(base);

        logging.tryAcquire("demo-user"); // allowed
        logging.tryAcquire("demo-user"); // allowed
        logging.tryAcquire("demo-user"); // denied -- logging shows both

        // ── Scenario 6: MetricsDecorator ─────────────────────────────────────
        System.out.println("\n=== Scenario 6: MetricsRateLimiterDecorator -- rejection rate ===");
        RateLimiter base2   = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.FIXED_WINDOW,
                new RateLimitConfig(3, TimeWindow.MINUTE));
        MetricsRateLimiterDecorator metrics = new MetricsRateLimiterDecorator(base2);

        for (int i = 0; i < 6; i++) metrics.tryAcquire("metric-client");

        System.out.printf("  Total calls: 6 | Allowed: %d | Rejected: %d | Rejection rate: %.0f%%%n",
                metrics.getAllowedCount(), metrics.getRejectedCount(), metrics.getRejectionRate() * 100);

        // ── Scenario 7: RateLimitingService ───────────────────────────────────
        System.out.println("\n=== Scenario 7: RateLimitingService with named rules ===");
        RateLimitingService service = new RateLimitingService(RateLimiterFactory.Algorithm.TOKEN_BUCKET);
        service.registerRule("api-default",  new RateLimitConfig(5, TimeWindow.MINUTE));
        service.registerRule("premium-tier", new RateLimitConfig(20, TimeWindow.MINUTE));

        System.out.println("  Free tier (5/min):");
        for (int i = 1; i <= 7; i++) {
            RateLimitResult r = service.checkLimit("free-user", "api-default");
            System.out.printf("    Call %d: %s%n", i, r.isAllowed() ? "OK" : "RATE LIMITED");
        }
        System.out.println("  Premium tier (20/min): first 3 calls all ALLOWED");
        for (int i = 1; i <= 3; i++) {
            RateLimitResult r = service.checkLimit("premium-user", "premium-tier");
            System.out.printf("    Call %d: %s%n", i, r.isAllowed() ? "OK" : "RATE LIMITED");
        }

        System.out.println("\n=== Rate Limiter Demo Complete ===");
    }
}
