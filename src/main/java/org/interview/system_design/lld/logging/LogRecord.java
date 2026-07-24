package org.interview.system_design.lld.logging;

import java.time.LocalDateTime;

/**
 * Immutable record representing a single log event.
 */
public class LogRecord {

    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;
    private final String className;
    private final String stackTrace; // optional, may be null

    public LogRecord(LogLevel level, String message, String className) {
        this(level, message, className, null);
    }

    public LogRecord(LogLevel level, String message, String className, String stackTrace) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
        this.threadName = Thread.currentThread().getName();
        this.className = className;
        this.stackTrace = stackTrace;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public String getThreadName() { return threadName; }
    public String getClassName() { return className; }
    public String getStackTrace() { return stackTrace; }
    public boolean hasStackTrace() { return stackTrace != null && !stackTrace.isEmpty(); }
}
