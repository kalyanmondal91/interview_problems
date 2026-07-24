package org.interview.system_design.lld.logging;

/**
 * Strategy interface for formatting log records into strings.
 */
public interface LogFormatter {
    /**
     * Formats the given log record into a human-readable string.
     *
     * @param record the log record to format
     * @return formatted log string
     */
    String format(LogRecord record);
}
