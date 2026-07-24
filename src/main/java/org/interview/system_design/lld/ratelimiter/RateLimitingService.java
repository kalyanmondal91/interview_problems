package org.interview.system_design.lld.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-client rate limiting service.
 * Manages named rule configurations and creates / caches the appropriate limiter per client.
 *
 * Usage:
 * <pre>
 *   RateLimitingService svc = new RateLimitingService(RateLimiterFactory.Algorithm.SLIDING_WINDOW);
 *   svc.registerRule("api_v1", new RateLimitConfig(100, TimeWindow.MINUTE));
 *   RateLimitResult r = svc.checkLimit("client_123", "api_v1");
 * </pre>
 */
public class RateLimitingService {

    private final RateLimiterFactory.Algorithm algorithm;

    /** rule name → config */
    private final Map<String, RateLimitConfig> rules = new ConcurrentHashMap<>();

    /** (clientId + ruleId) → limiter instance */
    private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    public RateLimitingService(RateLimiterFactory.Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /** Registers a named rate-limit rule configuration. */
    public void registerRule(String ruleName, RateLimitConfig config) {
        rules.put(ruleName, config);
        System.out.printf("[RateLimitingService] Registered rule '%s': %s%n", ruleName, config);
    }

    /**
     * Checks whether a client is allowed to proceed under a named rule.
     *
     * @param clientId identifier for the caller
     * @param ruleName the rule to apply
     * @return rate limit result
     */
    public RateLimitResult checkLimit(String clientId, String ruleName) {
        RateLimitConfig config = rules.get(ruleName);
        if (config == null) {
            throw new IllegalArgumentException("Unknown rule: " + ruleName);
        }
        String key = clientId + ":" + ruleName;
        RateLimiter limiter = limiters.computeIfAbsent(key,
                k -> RateLimiterFactory.createWithDecorators(algorithm, config));
        return limiter.tryAcquire(clientId);
    }

    /** Resets a specific client's state for a given rule. */
    public void reset(String clientId, String ruleName) {
        String key = clientId + ":" + ruleName;
        RateLimiter limiter = limiters.get(key);
        if (limiter != null) limiter.reset(clientId);
    }

    /** Removes all state (useful for testing / admin). */
    public void resetAll() {
        limiters.clear();
    }
}
