package org.interview.system_design.lld.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration object that assembles the appender chain and sets the minimum log level.
 * Uses a builder-style API for setting up the pipeline.
 */
public class LoggingConfig {

    private LogLevel minimumLevel;
    private final List<LogAppender> appenders = new ArrayList<>();

    public LoggingConfig() {
        // Default: INFO level, single console appender with simple formatting
        this.minimumLevel = LogLevel.INFO;
        appenders.add(new ConsoleAppender(LogLevel.INFO, new SimpleFormatter()));
    }

    /** Sets the global minimum level below which no record is processed. */
    public LoggingConfig setMinimumLevel(LogLevel level) {
        this.minimumLevel = level;
        return this;
    }

    /** Adds an appender to the chain (order matters — first added = first in chain). */
    public LoggingConfig addAppender(LogAppender appender) {
        appenders.add(appender);
        return this;
    }

    /** Replaces all appenders with the supplied list. */
    public LoggingConfig setAppenders(List<LogAppender> appenders) {
        this.appenders.clear();
        this.appenders.addAll(appenders);
        return this;
    }

    public LogLevel getMinimumLevel() { return minimumLevel; }

    /**
     * Builds the chain by linking all configured appenders in order.
     *
     * @return the head of the appender chain, or null if no appenders configured
     */
    public LogAppender buildChain() {
        if (appenders.isEmpty()) return null;
        for (int i = 0; i < appenders.size() - 1; i++) {
            appenders.get(i).setNext(appenders.get(i + 1));
        }
        // Reset last appender's next to null to avoid stale references on rebuild
        appenders.get(appenders.size() - 1).setNext(null);
        return appenders.get(0);
    }
}
