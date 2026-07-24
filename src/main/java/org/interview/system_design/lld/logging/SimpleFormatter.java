package org.interview.system_design.lld.logging;

/**
 * Produces concise log lines: "[LEVEL] timestamp - message"
 */
public class SimpleFormatter implements LogFormatter {

    @Override
    public String format(LogRecord record) {
        return String.format("[%s] %s - %s",
                record.getLevel(),
                record.getTimestamp(),
                record.getMessage());
    }
}
