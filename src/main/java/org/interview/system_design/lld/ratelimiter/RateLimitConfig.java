package org.interview.system_design.lld.ratelimiter;

/**
 * Configuration for a rate limiter instance.
 * maxRequests — allowed calls per timeWindow.
 * clientId   — optional; when set, the limiter applies only to that client.
 */
public class RateLimitConfig {

    private final int maxRequests;
    private final TimeWindow timeWindow;
    private final String clientId; // nullable — means "global"

    public RateLimitConfig(int maxRequests, TimeWindow timeWindow) {
        this(maxRequests, timeWindow, null);
    }

    public RateLimitConfig(int maxRequests, TimeWindow timeWindow, String clientId) {
        if (maxRequests <= 0) throw new IllegalArgumentException("maxRequests must be > 0");
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindow;
        this.clientId = clientId;
    }

    public int getMaxRequests()     { return maxRequests; }
    public TimeWindow getTimeWindow() { return timeWindow; }
    public String getClientId()     { return clientId; }

    @Override
    public String toString() {
        return String.format("RateLimitConfig[max=%d, window=%s, client=%s]",
                maxRequests, timeWindow, clientId);
    }
}
