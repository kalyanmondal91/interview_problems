---
layout: lld
render_with_liquid: false
title: "Logging Framework"
system: logging
description: "LLD of Logging Framework"
files:
  - "ConsoleAppender.java"
  - "DatabaseAppender.java"
  - "DetailedFormatter.java"
  - "FileAppender.java"
  - "LogAppender.java"
  - "LogFormatter.java"
  - "LogLevel.java"
  - "LogRecord.java"
  - "Logger.java"
  - "LoggerFactory.java"
  - "LoggingConfig.java"
  - "Main.java"
  - "SimpleFormatter.java"
---

## Logging Framework

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="ConsoleAppender.java">ConsoleAppender.java</button>
<button class="tab-btn" data-tab="DatabaseAppender.java">DatabaseAppender.java</button>
<button class="tab-btn" data-tab="DetailedFormatter.java">DetailedFormatter.java</button>
<button class="tab-btn" data-tab="FileAppender.java">FileAppender.java</button>
<button class="tab-btn" data-tab="LogAppender.java">LogAppender.java</button>
<button class="tab-btn" data-tab="LogFormatter.java">LogFormatter.java</button>
<button class="tab-btn" data-tab="LogLevel.java">LogLevel.java</button>
<button class="tab-btn" data-tab="LogRecord.java">LogRecord.java</button>
<button class="tab-btn" data-tab="Logger.java">Logger.java</button>
<button class="tab-btn" data-tab="LoggerFactory.java">LoggerFactory.java</button>
<button class="tab-btn" data-tab="LoggingConfig.java">LoggingConfig.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="SimpleFormatter.java">SimpleFormatter.java</button>
</div>
<div class="tab-content active" id="ConsoleAppender-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

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
}</code></pre>
</div>
<div class="tab-content" id="DatabaseAppender-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Appender that simulates persisting log records to a database.
 * Stores records in-memory; in production would use a JDBC/ORM layer.
 */
public class DatabaseAppender extends LogAppender {

    private final List&lt;LogRecord&gt; records = new ArrayList&lt;&gt;();

    public DatabaseAppender(LogLevel threshold, LogFormatter formatter) {
        super(threshold, formatter);
    }

    @Override
    protected void append(LogRecord record) {
        records.add(record);
        // In production: INSERT INTO logs (level, timestamp, message, ...) VALUES (...)
    }

    /** Returns an immutable view of all persisted log records. */
    public List&lt;LogRecord&gt; getRecords() {
        return Collections.unmodifiableList(records);
    }
}</code></pre>
</div>
<div class="tab-content" id="DetailedFormatter-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

/**
 * Produces detailed log lines: &quot;[LEVEL] timestamp [thread] className - message&quot;
 * Appends stack trace when present.
 */
public class DetailedFormatter implements LogFormatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(&quot;[%s] %s [%s] %s - %s&quot;,
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
}</code></pre>
</div>
<div class="tab-content" id="FileAppender-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Appender that simulates writing log records to a file.
 * Internally stores lines in-memory; in production this would write to disk.
 */
public class FileAppender extends LogAppender {

    private final String filePath;
    private final List&lt;String&gt; logLines = new ArrayList&lt;&gt;();

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

    /** Returns an immutable snapshot of lines written to this &quot;file&quot;. */
    public List&lt;String&gt; getLogLines() {
        return Collections.unmodifiableList(logLines);
    }
}</code></pre>
</div>
<div class="tab-content" id="LogAppender-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

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
}</code></pre>
</div>
<div class="tab-content" id="LogFormatter-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

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
}</code></pre>
</div>
<div class="tab-content" id="LogLevel-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

/**
 * Severity levels for log records, ordered by increasing severity.
 */
public enum LogLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    FATAL(4);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    /** Returns true if this level is at least as severe as the given threshold. */
    public boolean isAtLeast(LogLevel threshold) {
        return this.level &gt;= threshold.level;
    }
}</code></pre>
</div>
<div class="tab-content" id="LogRecord-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

import java.time.LocalDateTime;

/**
 * Immutable record representing a single log event.
 */
public class LogRecord {

    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;
    private final String className;
    private final String stackTrace; // optional, may be null

    public LogRecord(LogLevel level, String message, String className) {
        this(level, message, className, null);
    }

    public LogRecord(LogLevel level, String message, String className, String stackTrace) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
        this.threadName = Thread.currentThread().getName();
        this.className = className;
        this.stackTrace = stackTrace;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public LogLevel getLevel() { return level; }
    public String getMessage() { return message; }
    public String getThreadName() { return threadName; }
    public String getClassName() { return className; }
    public String getStackTrace() { return stackTrace; }
    public boolean hasStackTrace() { return stackTrace != null &amp;&amp; !stackTrace.isEmpty(); }
}</code></pre>
</div>
<div class="tab-content" id="Logger-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

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
            sb.append(&quot;	at &quot;).append(el).append(System.lineSeparator());
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
}</code></pre>
</div>
<div class="tab-content" id="LoggerFactory-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Singleton registry that vends Logger instances per class name.
 * All loggers share the same default configuration from LoggingConfig.
 * Pattern: Singleton
 */
public class LoggerFactory {

    private static volatile LoggerFactory instance;
    private final Map&lt;String, Logger&gt; loggers = new ConcurrentHashMap&lt;&gt;();
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
                name -&gt; new Logger(name, config.buildChain(), config.getMinimumLevel()));
    }

    /** Convenience overload accepting a Class reference. */
    public Logger getLogger(Class&lt;?&gt; clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Applies a new config and reconfigures all existing loggers.
     */
    public void applyConfig(LoggingConfig config) {
        this.config = config;
        loggers.values().forEach(logger -&gt; {
            logger.setRootAppender(config.buildChain());
            logger.setMinimumLevel(config.getMinimumLevel());
        });
    }
}</code></pre>
</div>
<div class="tab-content" id="LoggingConfig-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration object that assembles the appender chain and sets the minimum log level.
 * Uses a builder-style API for setting up the pipeline.
 */
public class LoggingConfig {

    private LogLevel minimumLevel;
    private final List&lt;LogAppender&gt; appenders = new ArrayList&lt;&gt;();

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
    public LoggingConfig setAppenders(List&lt;LogAppender&gt; appenders) {
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
        for (int i = 0; i &lt; appenders.size() - 1; i++) {
            appenders.get(i).setNext(appenders.get(i + 1));
        }
        // Reset last appender&#x27;s next to null to avoid stale references on rebuild
        appenders.get(appenders.size() - 1).setNext(null);
        return appenders.get(0);
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

/**
 * Demo runner for the Logging Framework LLD.
 * Patterns: Chain of Responsibility, Singleton
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) {

        // Scenario 1: INFO threshold -- DEBUG filtered out
        banner(&quot;Scenario 1: Chain with INFO threshold&quot;);
        DatabaseAppender dbAppender = new DatabaseAppender(LogLevel.WARN, new DetailedFormatter());
        FileAppender     fileApp    = new FileAppender(LogLevel.INFO, new SimpleFormatter(), &quot;app.log&quot;);
        ConsoleAppender  console    = new ConsoleAppender(LogLevel.INFO, new SimpleFormatter());
        // Chain: console -&gt; file -&gt; db (each has own threshold)
        console.setNext(fileApp);
        fileApp.setNext(dbAppender);

        Logger logger = LoggerFactory.getInstance().getLogger(&quot;com.example.Service&quot;);
        logger.setRootAppender(console);

        logger.debug(&quot;This DEBUG message should be filtered&quot;);
        logger.info(&quot;Application started&quot;);
        logger.warn(&quot;Low memory warning&quot;);
        logger.error(&quot;Connection timeout&quot;);
        System.out.println(&quot;  File lines captured: &quot; + fileApp.getLogLines().size());
        System.out.println(&quot;  DB records captured (WARN+): &quot; + dbAppender.getRecords().size());

        // Scenario 2: Two loggers, different class names
        banner(&quot;Scenario 2: Two loggers -- singleton registry&quot;);
        Logger loggerA  = LoggerFactory.getInstance().getLogger(&quot;com.example.ServiceA&quot;);
        Logger loggerB  = LoggerFactory.getInstance().getLogger(&quot;com.example.ServiceB&quot;);
        Logger loggerA2 = LoggerFactory.getInstance().getLogger(&quot;com.example.ServiceA&quot;); // same name

        System.out.println(&quot;  loggerA == loggerA2 (same instance): &quot; + (loggerA == loggerA2));
        System.out.println(&quot;  loggerA != loggerB (different names): &quot; + (loggerA != loggerB));

        // Scenario 3: FATAL propagates through all appenders
        banner(&quot;Scenario 3: FATAL log -- propagates to all appenders&quot;);
        FileAppender     fatalFile    = new FileAppender(LogLevel.DEBUG, new DetailedFormatter(), &quot;fatal.log&quot;);
        DatabaseAppender fatalDb      = new DatabaseAppender(LogLevel.DEBUG, new DetailedFormatter());
        ConsoleAppender  fatalConsole = new ConsoleAppender(LogLevel.DEBUG, new SimpleFormatter());
        fatalConsole.setNext(fatalFile);
        fatalFile.setNext(fatalDb);

        Logger sysLogger = LoggerFactory.getInstance().getLogger(&quot;com.example.System&quot;);
        sysLogger.setRootAppender(fatalConsole);
        sysLogger.fatal(&quot;Critical system failure -- shutting down!&quot;);
        System.out.println(&quot;  File lines: &quot;  + fatalFile.getLogLines().size());
        System.out.println(&quot;  DB records: &quot;  + fatalDb.getRecords().size());

        // Scenario 4: Change threshold at runtime
        banner(&quot;Scenario 4: Raise threshold to WARN -- INFO now filtered&quot;);
        FileAppender warnFile = new FileAppender(LogLevel.WARN, new SimpleFormatter(), &quot;warn.log&quot;);
        Logger appLogger = LoggerFactory.getInstance().getLogger(&quot;com.example.App&quot;);
        appLogger.setRootAppender(warnFile);

        appLogger.info(&quot;This INFO should be filtered&quot;);
        appLogger.warn(&quot;This WARN should appear&quot;);
        appLogger.error(&quot;This ERROR should appear&quot;);
        System.out.println(&quot;  Lines in warn log (expect 2): &quot; + warnFile.getLogLines().size());
        warnFile.getLogLines().forEach(l -&gt; System.out.println(&quot;    &quot; + l));

        // Scenario 5: Multiple messages and record count
        banner(&quot;Scenario 5: Bulk logging -- count DB records&quot;);
        DatabaseAppender bulkDb = new DatabaseAppender(LogLevel.DEBUG, new DetailedFormatter());
        Logger bulkLogger = LoggerFactory.getInstance().getLogger(&quot;com.example.Bulk&quot;);
        bulkLogger.setRootAppender(bulkDb);

        for (int i = 1; i &lt;= 5; i++) {
            bulkLogger.info(&quot;Batch item &quot; + i + &quot; processed&quot;);
        }
        bulkLogger.warn(&quot;Batch completed with warnings&quot;);
        System.out.println(&quot;  Total DB records: &quot; + bulkDb.getRecords().size());
        bulkDb.getRecords().forEach(r -&gt;
                System.out.println(&quot;    [&quot; + r.getLevel() + &quot;] &quot; + r.getMessage()));

        System.out.println();
        System.out.println(&quot;=== Logging Framework Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="SimpleFormatter-java">
<pre><code class="language-java">package org.interview.system_design.lld.logging;

/**
 * Produces concise log lines: &quot;[LEVEL] timestamp - message&quot;
 */
public class SimpleFormatter implements LogFormatter {

    @Override
    public String format(LogRecord record) {
        return String.format(&quot;[%s] %s - %s&quot;,
                record.getLevel(),
                record.getTimestamp(),
                record.getMessage());
    }
}</code></pre>
</div>
</div>
