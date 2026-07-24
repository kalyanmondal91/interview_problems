package org.interview.system_design.lld.logging;

/**
 * Appender that writes formatted log records to System.out.
 */
public class ConsoleAppender extends LogAppender {

    public ConsoleAppender(LogLevel threshold, LogFormatter formatter) {
        super(threshold, formatter);
    }

    @Override
    protected void append(LogRecord record) {
        System.out.println(formatter.format(record));
    }
}
