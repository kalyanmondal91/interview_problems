package org.interview.system_design.lld.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Appender that simulates persisting log records to a database.
 * Stores records in-memory; in production would use a JDBC/ORM layer.
 */
public class DatabaseAppender extends LogAppender {

    private final List<LogRecord> records = new ArrayList<>();

    public DatabaseAppender(LogLevel threshold, LogFormatter formatter) {
        super(threshold, formatter);
    }

    @Override
    protected void append(LogRecord record) {
        records.add(record);
        // In production: INSERT INTO logs (level, timestamp, message, ...) VALUES (...)
    }

    /** Returns an immutable view of all persisted log records. */
    public List<LogRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }
}
