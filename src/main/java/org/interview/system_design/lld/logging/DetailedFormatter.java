package org.interview.system_design.lld.logging;

/**
 * Produces detailed log lines: "[LEVEL] timestamp [thread] className - message"
 * Appends stack trace when present.
 */
public class DetailedFormatter implements LogFormatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s [%s] %s - %s",
                record.getLevel(),
                record.getTimestamp(),
                record.getThreadName(),
                record.getClassName(),
                record.getMessage()));
        if (record.hasStackTrace()) {
            sb.append(System.lineSeparator()).append(record.getStackTrace());
        }
        return sb.toString();
    }
}
