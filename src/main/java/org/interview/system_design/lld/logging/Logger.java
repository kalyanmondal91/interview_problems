package org.interview.system_design.lld.logging;

/**
 * Logger bound to a specific class name.
 * Routes log records through the configured appender chain.
 * Instances are managed by LoggerFactory (Singleton per class name).
 */
public class Logger {

    private final String className;
    private LogAppender rootAppender;
    private LogLevel minimumLevel;

    Logger(String className, LogAppender rootAppender, LogLevel minimumLevel) {
        this.className = className;
        this.rootAppender = rootAppender;
        this.minimumLevel = minimumLevel;
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }

    public void info(String message) {
        log(LogLevel.INFO, message, null);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message, null);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message, null);
    }

    public void error(String message, Throwable t) {
        log(LogLevel.ERROR, message, stackTraceOf(t));
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message, null);
    }

    public void fatal(String message, Throwable t) {
        log(LogLevel.FATAL, message, stackTraceOf(t));
    }

    private void log(LogLevel level, String message, String stackTrace) {
        if (!level.isAtLeast(minimumLevel)) return;
        LogRecord record = new LogRecord(level, message, className, stackTrace);
        if (rootAppender != null) {
            rootAppender.handle(record);
        }
    }

    private String stackTraceOf(Throwable t) {
        if (t == null) return null;
        StringBuilder sb = new StringBuilder(t.toString()).append(System.lineSeparator());
        for (StackTraceElement el : t.getStackTrace()) {
            sb.append("	at ").append(el).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /** Allows runtime reconfiguration of the appender chain. */
    public void setRootAppender(LogAppender appender) {
        this.rootAppender = appender;
    }

    /** Allows runtime reconfiguration of minimum level. */
    public void setMinimumLevel(LogLevel minimumLevel) {
        this.minimumLevel = minimumLevel;
    }

    public String getClassName() { return className; }
}
