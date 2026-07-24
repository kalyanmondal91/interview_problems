package org.interview.system_design.lld.ratelimiter;

/**
 * Factory that creates a RateLimiter by algorithm type from a RateLimitConfig.
 * Pattern: Factory Method
 */
public class RateLimiterFactory {

    public enum Algorithm {
        TOKEN_BUCKET,
        LEAKY_BUCKET,
        FIXED_WINDOW,
        SLIDING_WINDOW
    }

    /** Creates a plain (undecorated) rate limiter for the given algorithm. */
    public static RateLimiter create(Algorithm algorithm, RateLimitConfig config) {
        switch (algorithm) {
            case TOKEN_BUCKET:   return new TokenBucketRateLimiter(config);
            case LEAKY_BUCKET:   return new LeakyBucketRateLimiter(config);
            case FIXED_WINDOW:   return new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW: return new SlidingWindowRateLimiter(config);
            default: throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }

    /** Creates a rate limiter wrapped with both logging and metrics decorators. */
    public static RateLimiter createWithDecorators(Algorithm algorithm, RateLimitConfig config) {
        RateLimiter base = create(algorithm, config);
        RateLimiter logged = new LoggingRateLimiterDecorator(base);
        return new MetricsRateLimiterDecorator(logged);
    }
}
