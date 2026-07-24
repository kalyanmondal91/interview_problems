package org.interview.system_design.lld.logging;

/**
 * Abstract handler in the Chain of Responsibility.
 * Each appender has a minimum level threshold and an optional next handler.
 * Pattern: Chain of Responsibility
 */
public abstract class LogAppender {

    protected LogLevel threshold;
    protected LogAppender next;
    protected LogFormatter formatter;

    public LogAppender(LogLevel threshold, LogFormatter formatter) {
        this.threshold = threshold;
        this.formatter = formatter;
    }

    /** Sets the next handler in the chain. Returns this for fluent chaining. */
    public LogAppender setNext(LogAppender next) {
        this.next = next;
        return this;
    }

    /**
     * Handles the record if it meets the threshold, then passes it to the next handler.
     */
    public void handle(LogRecord record) {
        if (record.getLevel().isAtLeast(threshold)) {
            append(record);
        }
        if (next != null) {
            next.handle(record);
        }
    }

    /** Concrete appenders implement the actual write logic here. */
    protected abstract void append(LogRecord record);
}
