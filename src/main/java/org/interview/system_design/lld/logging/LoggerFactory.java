package org.interview.system_design.lld.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Singleton registry that vends Logger instances per class name.
 * All loggers share the same default configuration from LoggingConfig.
 * Pattern: Singleton
 */
public class LoggerFactory {

    private static volatile LoggerFactory instance;
    private final Map<String, Logger> loggers = new ConcurrentHashMap<>();
    private LoggingConfig config;

    private LoggerFactory() {
        this.config = new LoggingConfig();
    }

    /** Returns the single LoggerFactory instance (double-checked locking). */
    public static LoggerFactory getInstance() {
        if (instance == null) {
            synchronized (LoggerFactory.class) {
                if (instance == null) {
                    instance = new LoggerFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Returns (or creates) the Logger for the given class name.
     * Newly created loggers use the current LoggingConfig.
     */
    public Logger getLogger(String className) {
        return loggers.computeIfAbsent(className,
                name -> new Logger(name, config.buildChain(), config.getMinimumLevel()));
    }

    /** Convenience overload accepting a Class reference. */
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Applies a new config and reconfigures all existing loggers.
     */
    public void applyConfig(LoggingConfig config) {
        this.config = config;
        loggers.values().forEach(logger -> {
            logger.setRootAppender(config.buildChain());
            logger.setMinimumLevel(config.getMinimumLevel());
        });
    }
}
