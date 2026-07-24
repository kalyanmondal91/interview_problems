package org.interview.system_design.lld.ratelimiter;

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
}
