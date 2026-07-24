package org.interview.system_design.lld.logging;

/**
 * Demo runner for the Logging Framework LLD.
 * Patterns: Chain of Responsibility, Singleton
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // Scenario 1: INFO threshold -- DEBUG filtered out
        banner("Scenario 1: Chain with INFO threshold");
        DatabaseAppender dbAppender = new DatabaseAppender(LogLevel.WARN, new DetailedFormatter());
        FileAppender     fileApp    = new FileAppender(LogLevel.INFO, new SimpleFormatter(), "app.log");
        ConsoleAppender  console    = new ConsoleAppender(LogLevel.INFO, new SimpleFormatter());
        // Chain: console -> file -> db (each has own threshold)
        console.setNext(fileApp);
        fileApp.setNext(dbAppender);

        Logger logger = LoggerFactory.getInstance().getLogger("com.example.Service");
        logger.setRootAppender(console);

        logger.debug("This DEBUG message should be filtered");
        logger.info("Application started");
        logger.warn("Low memory warning");
        logger.error("Connection timeout");
        System.out.println("  File lines captured: " + fileApp.getLogLines().size());
        System.out.println("  DB records captured (WARN+): " + dbAppender.getRecords().size());

        // Scenario 2: Two loggers, different class names
        banner("Scenario 2: Two loggers -- singleton registry");
        Logger loggerA  = LoggerFactory.getInstance().getLogger("com.example.ServiceA");
        Logger loggerB  = LoggerFactory.getInstance().getLogger("com.example.ServiceB");
        Logger loggerA2 = LoggerFactory.getInstance().getLogger("com.example.ServiceA"); // same name

        System.out.println("  loggerA == loggerA2 (same instance): " + (loggerA == loggerA2));
        System.out.println("  loggerA != loggerB (different names): " + (loggerA != loggerB));

        // Scenario 3: FATAL propagates through all appenders
        banner("Scenario 3: FATAL log -- propagates to all appenders");
        FileAppender     fatalFile    = new FileAppender(LogLevel.DEBUG, new DetailedFormatter(), "fatal.log");
        DatabaseAppender fatalDb      = new DatabaseAppender(LogLevel.DEBUG, new DetailedFormatter());
        ConsoleAppender  fatalConsole = new ConsoleAppender(LogLevel.DEBUG, new SimpleFormatter());
        fatalConsole.setNext(fatalFile);
        fatalFile.setNext(fatalDb);

        Logger sysLogger = LoggerFactory.getInstance().getLogger("com.example.System");
        sysLogger.setRootAppender(fatalConsole);
        sysLogger.fatal("Critical system failure -- shutting down!");
        System.out.println("  File lines: "  + fatalFile.getLogLines().size());
        System.out.println("  DB records: "  + fatalDb.getRecords().size());

        // Scenario 4: Change threshold at runtime
        banner("Scenario 4: Raise threshold to WARN -- INFO now filtered");
        FileAppender warnFile = new FileAppender(LogLevel.WARN, new SimpleFormatter(), "warn.log");
        Logger appLogger = LoggerFactory.getInstance().getLogger("com.example.App");
        appLogger.setRootAppender(warnFile);

        appLogger.info("This INFO should be filtered");
        appLogger.warn("This WARN should appear");
        appLogger.error("This ERROR should appear");
        System.out.println("  Lines in warn log (expect 2): " + warnFile.getLogLines().size());
        warnFile.getLogLines().forEach(l -> System.out.println("    " + l));

        // Scenario 5: Multiple messages and record count
        banner("Scenario 5: Bulk logging -- count DB records");
        DatabaseAppender bulkDb = new DatabaseAppender(LogLevel.DEBUG, new DetailedFormatter());
        Logger bulkLogger = LoggerFactory.getInstance().getLogger("com.example.Bulk");
        bulkLogger.setRootAppender(bulkDb);

        for (int i = 1; i <= 5; i++) {
            bulkLogger.info("Batch item " + i + " processed");
        }
        bulkLogger.warn("Batch completed with warnings");
        System.out.println("  Total DB records: " + bulkDb.getRecords().size());
        bulkDb.getRecords().forEach(r ->
                System.out.println("    [" + r.getLevel() + "] " + r.getMessage()));

        System.out.println();
        System.out.println("=== Logging Framework Demo Complete ===");
    }
}
