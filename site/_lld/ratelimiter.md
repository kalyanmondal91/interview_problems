---
layout: lld
render_with_liquid: false
title: "Rate Limiter"
system: ratelimiter
description: "LLD of Rate Limiter"
files:
  - "CompositeRateLimiter.java"
  - "FixedWindowRateLimiter.java"
  - "LeakyBucketRateLimiter.java"
  - "LoggingRateLimiterDecorator.java"
  - "Main.java"
  - "MetricsRateLimiterDecorator.java"
  - "RateLimitConfig.java"
  - "RateLimitResult.java"
  - "RateLimiter.java"
  - "RateLimiterDecorator.java"
  - "RateLimiterFactory.java"
  - "RateLimitingService.java"
  - "SlidingWindowRateLimiter.java"
  - "TimeWindow.java"
  - "TokenBucketRateLimiter.java"
---

## Rate Limiter

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="CompositeRateLimiter.java">CompositeRateLimiter.java</button>
<button class="tab-btn" data-tab="FixedWindowRateLimiter.java">FixedWindowRateLimiter.java</button>
<button class="tab-btn" data-tab="LeakyBucketRateLimiter.java">LeakyBucketRateLimiter.java</button>
<button class="tab-btn" data-tab="LoggingRateLimiterDecorator.java">LoggingRateLimiterDecorator.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="MetricsRateLimiterDecorator.java">MetricsRateLimiterDecorator.java</button>
<button class="tab-btn" data-tab="RateLimitConfig.java">RateLimitConfig.java</button>
<button class="tab-btn" data-tab="RateLimitResult.java">RateLimitResult.java</button>
<button class="tab-btn" data-tab="RateLimiter.java">RateLimiter.java</button>
<button class="tab-btn" data-tab="RateLimiterDecorator.java">RateLimiterDecorator.java</button>
<button class="tab-btn" data-tab="RateLimiterFactory.java">RateLimiterFactory.java</button>
<button class="tab-btn" data-tab="RateLimitingService.java">RateLimitingService.java</button>
<button class="tab-btn" data-tab="SlidingWindowRateLimiter.java">SlidingWindowRateLimiter.java</button>
<button class="tab-btn" data-tab="TimeWindow.java">TimeWindow.java</button>
<button class="tab-btn" data-tab="TokenBucketRateLimiter.java">TokenBucketRateLimiter.java</button>
</div>
<div class="tab-content active" id="CompositeRateLimiter-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite Rate Limiter — applies ALL child rate limiters.
 * A request is allowed only if every child permits it.
 * This enables layered policies, e.g. &quot;100 req/min AND 1000 req/hour&quot;.
 * Pattern: Composite
 */
public class CompositeRateLimiter implements RateLimiter {

    private final List&lt;RateLimiter&gt; limiters;

    public CompositeRateLimiter(List&lt;RateLimiter&gt; limiters) {
        if (limiters == null || limiters.isEmpty()) {
            throw new IllegalArgumentException(&quot;CompositeRateLimiter requires at least one child&quot;);
        }
        this.limiters = new ArrayList&lt;&gt;(limiters);
    }

    public CompositeRateLimiter(RateLimiter... limiters) {
        this(Arrays.asList(limiters));
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        RateLimitResult lastResult = null;
        for (RateLimiter limiter : limiters) {
            lastResult = limiter.tryAcquire(clientId);
            if (!lastResult.isAllowed()) {
                // Short-circuit: first denial wins
                return RateLimitResult.deny(
                        &quot;[Composite] Denied by &quot; + limiter.getClass().getSimpleName()
                        + &quot;: &quot; + lastResult.getReason(),
                        lastResult.getResetTimeMs());
            }
        }
        // All passed — return the most conservative remaining count
        return lastResult;
    }

    @Override
    public void reset(String clientId) {
        limiters.forEach(l -&gt; l.reset(clientId));
    }

    public List&lt;RateLimiter&gt; getLimiters() {
        return List.copyOf(limiters);
    }
}</code></pre>
</div>
<div class="tab-content" id="FixedWindowRateLimiter-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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

    private final ConcurrentHashMap&lt;String, WindowState&gt; windows = new ConcurrentHashMap&lt;&gt;();

    public FixedWindowRateLimiter(RateLimitConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowMs    = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        WindowState state = windows.computeIfAbsent(clientId, k -&gt; new WindowState());
        synchronized (state) {
            long now = System.currentTimeMillis();
            // Reset window if it has expired
            if (now - state.windowStart &gt;= windowMs) {
                state.count      = 0;
                state.windowStart = now;
            }
            if (state.count &lt; maxRequests) {
                state.count++;
                int remaining = maxRequests - state.count;
                long resetMs  = state.windowStart + windowMs;
                return RateLimitResult.allow(remaining, resetMs);
            } else {
                long resetMs = state.windowStart + windowMs;
                return RateLimitResult.deny(
                        &quot;Rate limit exceeded in fixed window for client: &quot; + clientId, resetMs);
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
}</code></pre>
</div>
<div class="tab-content" id="LeakyBucketRateLimiter-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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

    private final ConcurrentHashMap&lt;String, LeakyBucket&gt; buckets = new ConcurrentHashMap&lt;&gt;();

    public LeakyBucketRateLimiter(RateLimitConfig config) {
        this.capacity = config.getMaxRequests();
        this.windowMs = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        LeakyBucket bucket = buckets.computeIfAbsent(clientId, k -&gt; new LeakyBucket());
        synchronized (bucket) {
            bucket.leak();
            if (bucket.water &lt; capacity) {
                bucket.water++;
                int remaining = capacity - bucket.water;
                long resetMs = System.currentTimeMillis() + windowMs;
                return RateLimitResult.allow(remaining, resetMs);
            } else {
                long resetMs = bucket.lastLeakTime + windowMs;
                return RateLimitResult.deny(&quot;Leaky bucket full for client: &quot; + clientId, resetMs);
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
            if (elapsed &gt; 0) {
                long leaked = (elapsed * capacity) / windowMs;
                if (leaked &gt; 0) {
                    water = (int) Math.max(0, water - leaked);
                    lastLeakTime = now;
                }
            }
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="LoggingRateLimiterDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

import java.time.LocalDateTime;

/**
 * Decorator that logs every tryAcquire call with its result.
 * Pattern: Decorator (concrete)
 */
public class LoggingRateLimiterDecorator extends RateLimiterDecorator {

    public LoggingRateLimiterDecorator(RateLimiter delegate) {
        super(delegate);
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        RateLimitResult result = delegate.tryAcquire(clientId);
        System.out.printf(&quot;[RateLimiter][%s] client=%s allowed=%s remaining=%d reason=%s%n&quot;,
                LocalDateTime.now(), clientId,
                result.isAllowed(), result.getRemainingRequests(), result.getReason());
        return result;
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

import java.util.List;

/**
 * Demo runner for the Rate Limiter LLD.
 * Patterns: Strategy (algorithm), Composite, Decorator
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // ── Scenario 1: Token Bucket -- 5 req/min, fire 7 ─────────────────────
        System.out.println(&quot;\n=== Scenario 1: TOKEN_BUCKET -- 5 req/min, fire 7 requests ===&quot;);
        RateLimitConfig config = new RateLimitConfig(5, TimeWindow.MINUTE);
        RateLimiter tokenBucket = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.TOKEN_BUCKET, config);

        for (int i = 1; i &lt;= 7; i++) {
            RateLimitResult result = tokenBucket.tryAcquire(&quot;client-A&quot;);
            System.out.printf(&quot;  Request %d: %s | Remaining: %d%s%n&quot;,
                    i, result.isAllowed() ? &quot;ALLOWED&quot; : &quot;DENIED&quot;,
                    result.getRemainingRequests(),
                    result.isAllowed() ? &quot;&quot; : &quot; | Reason: &quot; + result.getReason());
        }

        // ── Scenario 2: Fixed Window ───────────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 2: FIXED_WINDOW -- 5 req/min, fire 7 requests ===&quot;);
        RateLimiter fixedWindow = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.FIXED_WINDOW, config);

        for (int i = 1; i &lt;= 7; i++) {
            RateLimitResult result = fixedWindow.tryAcquire(&quot;client-B&quot;);
            System.out.printf(&quot;  Request %d: %s | Remaining: %d%n&quot;,
                    i, result.isAllowed() ? &quot;ALLOWED&quot; : &quot;DENIED&quot;, result.getRemainingRequests());
        }

        // ── Scenario 3: Sliding Window ────────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 3: SLIDING_WINDOW -- 3 req/10s window ===&quot;);
        RateLimitConfig slideConfig = new RateLimitConfig(3, TimeWindow.SECOND);
        RateLimiter sliding = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.SLIDING_WINDOW, slideConfig);

        for (int i = 1; i &lt;= 5; i++) {
            RateLimitResult r = sliding.tryAcquire(&quot;client-C&quot;);
            System.out.printf(&quot;  Request %d: %s%n&quot;, i, r.isAllowed() ? &quot;ALLOWED&quot; : &quot;DENIED&quot;);
        }

        // ── Scenario 4: CompositeRateLimiter ─────────────────────────────────
        System.out.println(&quot;\n=== Scenario 4: CompositeRateLimiter -- user (10/min) AND ip (3/min) ===&quot;);
        RateLimiter userLimiter = new RateLimiterFactory().create(
                RateLimiterFactory.Algorithm.TOKEN_BUCKET, new RateLimitConfig(10, TimeWindow.MINUTE));
        RateLimiter ipLimiter   = new RateLimiterFactory().create(
                RateLimiterFactory.Algorithm.TOKEN_BUCKET, new RateLimitConfig(3,  TimeWindow.MINUTE));

        CompositeRateLimiter composite = new CompositeRateLimiter(List.of(userLimiter, ipLimiter));

        for (int i = 1; i &lt;= 5; i++) {
            RateLimitResult r = composite.tryAcquire(&quot;user-1&quot;);
            System.out.printf(&quot;  Request %d: %s%s%n&quot;, i,
                    r.isAllowed() ? &quot;ALLOWED&quot; : &quot;DENIED (IP limit hit)&quot;,
                    r.isAllowed() ? &quot;&quot; : &quot; -- &quot; + r.getReason());
        }

        // ── Scenario 5: LoggingDecorator ─────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 5: LoggingRateLimiterDecorator ===&quot;);
        RateLimiter base    = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.TOKEN_BUCKET,
                new RateLimitConfig(2, TimeWindow.MINUTE));
        RateLimiter logging = new LoggingRateLimiterDecorator(base);

        logging.tryAcquire(&quot;demo-user&quot;); // allowed
        logging.tryAcquire(&quot;demo-user&quot;); // allowed
        logging.tryAcquire(&quot;demo-user&quot;); // denied -- logging shows both

        // ── Scenario 6: MetricsDecorator ─────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 6: MetricsRateLimiterDecorator -- rejection rate ===&quot;);
        RateLimiter base2   = new RateLimiterFactory().create(RateLimiterFactory.Algorithm.FIXED_WINDOW,
                new RateLimitConfig(3, TimeWindow.MINUTE));
        MetricsRateLimiterDecorator metrics = new MetricsRateLimiterDecorator(base2);

        for (int i = 0; i &lt; 6; i++) metrics.tryAcquire(&quot;metric-client&quot;);

        System.out.printf(&quot;  Total calls: 6 | Allowed: %d | Rejected: %d | Rejection rate: %.0f%%%n&quot;,
                metrics.getAllowedCount(), metrics.getRejectedCount(), metrics.getRejectionRate() * 100);

        // ── Scenario 7: RateLimitingService ───────────────────────────────────
        System.out.println(&quot;\n=== Scenario 7: RateLimitingService with named rules ===&quot;);
        RateLimitingService service = new RateLimitingService(RateLimiterFactory.Algorithm.TOKEN_BUCKET);
        service.registerRule(&quot;api-default&quot;,  new RateLimitConfig(5, TimeWindow.MINUTE));
        service.registerRule(&quot;premium-tier&quot;, new RateLimitConfig(20, TimeWindow.MINUTE));

        System.out.println(&quot;  Free tier (5/min):&quot;);
        for (int i = 1; i &lt;= 7; i++) {
            RateLimitResult r = service.checkLimit(&quot;free-user&quot;, &quot;api-default&quot;);
            System.out.printf(&quot;    Call %d: %s%n&quot;, i, r.isAllowed() ? &quot;OK&quot; : &quot;RATE LIMITED&quot;);
        }
        System.out.println(&quot;  Premium tier (20/min): first 3 calls all ALLOWED&quot;);
        for (int i = 1; i &lt;= 3; i++) {
            RateLimitResult r = service.checkLimit(&quot;premium-user&quot;, &quot;premium-tier&quot;);
            System.out.printf(&quot;    Call %d: %s%n&quot;, i, r.isAllowed() ? &quot;OK&quot; : &quot;RATE LIMITED&quot;);
        }

        System.out.println(&quot;\n=== Rate Limiter Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="MetricsRateLimiterDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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
        System.out.printf(&quot;[MetricsDecorator] total=%d allowed=%d rejected=%d rejectionRate=%.1f%%%n&quot;,
                getTotalRequests(), getAllowedCount(), getRejectedCount(), getRejectionRate());
    }
}</code></pre>
</div>
<div class="tab-content" id="RateLimitConfig-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

/**
 * Configuration for a rate limiter instance.
 * maxRequests — allowed calls per timeWindow.
 * clientId   — optional; when set, the limiter applies only to that client.
 */
public class RateLimitConfig {

    private final int maxRequests;
    private final TimeWindow timeWindow;
    private final String clientId; // nullable — means &quot;global&quot;

    public RateLimitConfig(int maxRequests, TimeWindow timeWindow) {
        this(maxRequests, timeWindow, null);
    }

    public RateLimitConfig(int maxRequests, TimeWindow timeWindow, String clientId) {
        if (maxRequests &lt;= 0) throw new IllegalArgumentException(&quot;maxRequests must be &gt; 0&quot;);
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindow;
        this.clientId = clientId;
    }

    public int getMaxRequests()     { return maxRequests; }
    public TimeWindow getTimeWindow() { return timeWindow; }
    public String getClientId()     { return clientId; }

    @Override
    public String toString() {
        return String.format(&quot;RateLimitConfig[max=%d, window=%s, client=%s]&quot;,
                maxRequests, timeWindow, clientId);
    }
}</code></pre>
</div>
<div class="tab-content" id="RateLimitResult-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

/**
 * Result of a rate-limit check.
 * Immutable value object returned by every RateLimiter implementation.
 */
public class RateLimitResult {

    private final boolean allowed;
    private final int remainingRequests;
    private final long resetTimeMs;
    private final String reason;

    public RateLimitResult(boolean allowed, int remainingRequests, long resetTimeMs, String reason) {
        this.allowed = allowed;
        this.remainingRequests = remainingRequests;
        this.resetTimeMs = resetTimeMs;
        this.reason = reason;
    }

    public static RateLimitResult allow(int remaining, long resetTimeMs) {
        return new RateLimitResult(true, remaining, resetTimeMs, &quot;OK&quot;);
    }

    public static RateLimitResult deny(String reason, long resetTimeMs) {
        return new RateLimitResult(false, 0, resetTimeMs, reason);
    }

    public boolean isAllowed()          { return allowed; }
    public int getRemainingRequests()   { return remainingRequests; }
    public long getResetTimeMs()        { return resetTimeMs; }
    public String getReason()           { return reason; }

    @Override
    public String toString() {
        return String.format(&quot;RateLimitResult[allowed=%s, remaining=%d, resetMs=%d, reason=%s]&quot;,
                allowed, remainingRequests, resetTimeMs, reason);
    }
}</code></pre>
</div>
<div class="tab-content" id="RateLimiter-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

/**
 * Core rate-limiter interface.
 * Pattern: Strategy (each algorithm is a concrete implementation)
 */
public interface RateLimiter {

    /**
     * Attempts to acquire a request slot for the given client.
     *
     * @param clientId identifies the caller (user, IP, API key)
     * @return result indicating whether the request is allowed
     */
    RateLimitResult tryAcquire(String clientId);

    /**
     * Resets the rate-limit state for a specific client.
     * Useful for administrative resets or testing.
     *
     * @param clientId the client whose state to clear
     */
    void reset(String clientId);
}</code></pre>
</div>
<div class="tab-content" id="RateLimiterDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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
}</code></pre>
</div>
<div class="tab-content" id="RateLimiterFactory-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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
            default: throw new IllegalArgumentException(&quot;Unknown algorithm: &quot; + algorithm);
        }
    }

    /** Creates a rate limiter wrapped with both logging and metrics decorators. */
    public static RateLimiter createWithDecorators(Algorithm algorithm, RateLimitConfig config) {
        RateLimiter base = create(algorithm, config);
        RateLimiter logged = new LoggingRateLimiterDecorator(base);
        return new MetricsRateLimiterDecorator(logged);
    }
}</code></pre>
</div>
<div class="tab-content" id="RateLimitingService-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-client rate limiting service.
 * Manages named rule configurations and creates / caches the appropriate limiter per client.
 *
 * Usage:
 * &lt;pre&gt;
 *   RateLimitingService svc = new RateLimitingService(RateLimiterFactory.Algorithm.SLIDING_WINDOW);
 *   svc.registerRule(&quot;api_v1&quot;, new RateLimitConfig(100, TimeWindow.MINUTE));
 *   RateLimitResult r = svc.checkLimit(&quot;client_123&quot;, &quot;api_v1&quot;);
 * &lt;/pre&gt;
 */
public class RateLimitingService {

    private final RateLimiterFactory.Algorithm algorithm;

    /** rule name → config */
    private final Map&lt;String, RateLimitConfig&gt; rules = new ConcurrentHashMap&lt;&gt;();

    /** (clientId + ruleId) → limiter instance */
    private final Map&lt;String, RateLimiter&gt; limiters = new ConcurrentHashMap&lt;&gt;();

    public RateLimitingService(RateLimiterFactory.Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /** Registers a named rate-limit rule configuration. */
    public void registerRule(String ruleName, RateLimitConfig config) {
        rules.put(ruleName, config);
        System.out.printf(&quot;[RateLimitingService] Registered rule &#x27;%s&#x27;: %s%n&quot;, ruleName, config);
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
            throw new IllegalArgumentException(&quot;Unknown rule: &quot; + ruleName);
        }
        String key = clientId + &quot;:&quot; + ruleName;
        RateLimiter limiter = limiters.computeIfAbsent(key,
                k -&gt; RateLimiterFactory.createWithDecorators(algorithm, config));
        return limiter.tryAcquire(clientId);
    }

    /** Resets a specific client&#x27;s state for a given rule. */
    public void reset(String clientId, String ruleName) {
        String key = clientId + &quot;:&quot; + ruleName;
        RateLimiter limiter = limiters.get(key);
        if (limiter != null) limiter.reset(clientId);
    }

    /** Removes all state (useful for testing / admin). */
    public void resetAll() {
        limiters.clear();
    }
}</code></pre>
</div>
<div class="tab-content" id="SlidingWindowRateLimiter-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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

    private final ConcurrentHashMap&lt;String, Deque&lt;Long&gt;&gt; logs = new ConcurrentHashMap&lt;&gt;();

    public SlidingWindowRateLimiter(RateLimitConfig config) {
        this.maxRequests = config.getMaxRequests();
        this.windowMs    = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        Deque&lt;Long&gt; log = logs.computeIfAbsent(clientId, k -&gt; new ArrayDeque&lt;&gt;());
        synchronized (log) {
            long now      = System.currentTimeMillis();
            long cutoff   = now - windowMs;

            // Evict expired timestamps
            while (!log.isEmpty() &amp;&amp; log.peekFirst() &lt;= cutoff) {
                log.pollFirst();
            }

            if (log.size() &lt; maxRequests) {
                log.addLast(now);
                int remaining = maxRequests - log.size();
                long resetMs  = log.isEmpty() ? now + windowMs : log.peekFirst() + windowMs;
                return RateLimitResult.allow(remaining, resetMs);
            } else {
                long oldestRequest = log.peekFirst();
                long resetMs = oldestRequest + windowMs;
                return RateLimitResult.deny(
                        &quot;Sliding window limit reached for client: &quot; + clientId, resetMs);
            }
        }
    }

    @Override
    public void reset(String clientId) {
        logs.remove(clientId);
    }
}</code></pre>
</div>
<div class="tab-content" id="TimeWindow-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

/**
 * Time window granularities for rate limiting.
 */
public enum TimeWindow {
    SECOND(1_000L),
    MINUTE(60_000L),
    HOUR(3_600_000L),
    DAY(86_400_000L);

    private final long durationMs;

    TimeWindow(long durationMs) {
        this.durationMs = durationMs;
    }

    public long getDurationMs() {
        return durationMs;
    }
}</code></pre>
</div>
<div class="tab-content" id="TokenBucketRateLimiter-java">
<pre><code class="language-java">package org.interview.system_design.lld.ratelimiter;

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

    private final ConcurrentHashMap&lt;String, TokenBucket&gt; buckets = new ConcurrentHashMap&lt;&gt;();

    public TokenBucketRateLimiter(RateLimitConfig config) {
        this.maxTokens = config.getMaxRequests();
        this.windowMs  = config.getTimeWindow().getDurationMs();
    }

    @Override
    public RateLimitResult tryAcquire(String clientId) {
        TokenBucket bucket = buckets.computeIfAbsent(clientId, k -&gt; new TokenBucket());
        synchronized (bucket) {
            bucket.refill();
            if (bucket.tokens &gt; 0) {
                bucket.tokens--;
                long resetMs = System.currentTimeMillis() + windowMs;
                return RateLimitResult.allow(bucket.tokens, resetMs);
            } else {
                long resetMs = bucket.lastRefillTime + windowMs;
                return RateLimitResult.deny(&quot;Token bucket exhausted for client: &quot; + clientId, resetMs);
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
            if (elapsed &gt; 0) {
                // How many tokens to add proportional to elapsed time
                long tokensToAdd = (elapsed * maxTokens) / windowMs;
                if (tokensToAdd &gt; 0) {
                    tokens = (int) Math.min(maxTokens, tokens + tokensToAdd);
                    lastRefillTime = now;
                }
            }
        }
    }
}</code></pre>
</div>
</div>
