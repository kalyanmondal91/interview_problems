package org.interview.design_patterns.creational.singleton;

/**
 * ============================================================
 * Design Pattern: Singleton (Creational)
 * ============================================================
 *
 * INTENT:
 *   Ensure a class has only ONE instance and provide a global
 *   access point to that instance.
 *
 * PROBLEM IT SOLVES:
 *   - Need exactly one object to coordinate actions across the system
 *     (e.g., configuration manager, connection pool, logger).
 *   - Naive approach: global variable — breaks OOP encapsulation.
 *
 * STRUCTURE:
 *   - Private constructor (prevents direct instantiation)
 *   - Private static field holding the single instance
 *   - Public static factory method (getInstance) returning the instance
 *
 * VARIANTS SHOWN HERE:
 *   1. Eager initialization       — instance created at class-load time
 *   2. Lazy (double-checked lock) — thread-safe, created on first use
 *   3. Enum Singleton             — safest; handles serialization & reflection
 *
 * REAL-WORLD EXAMPLES:
 *   - java.lang.Runtime#getRuntime()
 *   - Spring ApplicationContext (singleton scope beans)
 *   - Database connection pool
 *   - Logger instances
 *
 * PROS:
 *   + Controlled access to sole instance
 *   + Reduced namespace pollution vs. global variables
 *   + Permits refinement of operations and representation
 *
 * CONS:
 *   - Violates Single Responsibility Principle (manages own lifecycle)
 *   - Hard to unit-test (global state)
 *   - Can mask bad design (overuse as global variables)
 *
 * Time Complexity:  O(1) for getInstance()
 * Space Complexity: O(1) — one instance ever
 */
public class SingletonPattern {

    // ----------------------------------------------------------------
    // Variant 1: Eager Initialization
    // Instance is created when the class is loaded by the JVM.
    // Thread-safe because class loading is inherently thread-safe.
    // Downside: created even if never used (wastes resources if heavy).
    // ----------------------------------------------------------------
    static class EagerSingleton {
        // 'final' ensures it's assigned exactly once
        private static final EagerSingleton INSTANCE = new EagerSingleton();

        private EagerSingleton() {
            // Private constructor blocks external instantiation
        }

        public static EagerSingleton getInstance() {
            return INSTANCE;
        }

        public void doWork() {
            System.out.println("[EagerSingleton] Doing work. hashCode=" + hashCode());
        }
    }

    // ----------------------------------------------------------------
    // Variant 2: Lazy Double-Checked Locking
    // Instance created only when first requested (lazy).
    // 'volatile' prevents instruction reordering; the synchronized block
    // is entered only if instance is null (fast path after first init).
    // ----------------------------------------------------------------
    static class LazySingleton {
        // volatile: all threads see the latest write; prevents half-init
        private static volatile LazySingleton instance;

        private LazySingleton() {}

        public static LazySingleton getInstance() {
            if (instance == null) {                  // 1st check (no lock needed)
                synchronized (LazySingleton.class) { // lock only for initialization
                    if (instance == null) {          // 2nd check (inside lock)
                        instance = new LazySingleton();
                    }
                }
            }
            return instance;
        }

        public void doWork() {
            System.out.println("[LazySingleton] Doing work. hashCode=" + hashCode());
        }
    }

    // ----------------------------------------------------------------
    // Variant 3: Enum Singleton (RECOMMENDED)
    // - Serialization-safe: JVM guarantees a single instance
    // - Reflection-proof: cannot call constructor via reflection
    // - Thread-safe: enum initialization is handled by JVM
    // ----------------------------------------------------------------
    enum EnumSingleton {
        INSTANCE;

        public void doWork() {
            System.out.println("[EnumSingleton] Doing work. hashCode=" + hashCode());
        }
    }

    // ----------------------------------------------------------------
    // Demo
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        // Eager: multiple calls return the same object
        EagerSingleton e1 = EagerSingleton.getInstance();
        EagerSingleton e2 = EagerSingleton.getInstance();
        System.out.println("EagerSingleton same instance: " + (e1 == e2)); // true
        e1.doWork();

        // Lazy: same behavior, but instance created on first call
        LazySingleton l1 = LazySingleton.getInstance();
        LazySingleton l2 = LazySingleton.getInstance();
        System.out.println("LazySingleton same instance: " + (l1 == l2)); // true
        l1.doWork();

        // Enum: cleanest, safest form
        EnumSingleton.INSTANCE.doWork();
        System.out.println("EnumSingleton same instance: " +
            (EnumSingleton.INSTANCE == EnumSingleton.INSTANCE)); // true
    }
}
