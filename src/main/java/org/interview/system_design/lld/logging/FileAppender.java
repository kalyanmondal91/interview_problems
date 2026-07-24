package org.interview.system_design.lld.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Appender that simulates writing log records to a file.
 * Internally stores lines in-memory; in production this would write to disk.
 */
public class FileAppender extends LogAppender {

    private final String filePath;
    private final List<String> logLines = new ArrayList<>();

    public FileAppender(LogLevel threshold, LogFormatter formatter, String filePath) {
        super(threshold, formatter);
        this.filePath = filePath;
    }

    @Override
    protected void append(LogRecord record) {
        String line = formatter.format(record);
        logLines.add(line);
        // In a real impl: Files.write(Paths.get(filePath), line, StandardOpenOption.APPEND)
    }

    public String getFilePath() { return filePath; }

    /** Returns an immutable snapshot of lines written to this "file". */
    public List<String> getLogLines() {
        return Collections.unmodifiableList(logLines);
    }
}
