package org.interview.system_design.lld.logging;

/**
 * Severity levels for log records, ordered by increasing severity.
 */
public enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    FATAL(4);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    /** Returns true if this level is at least as severe as the given threshold. */
    public boolean isAtLeast(LogLevel threshold) {
        return this.level >= threshold.level;
    }
}
