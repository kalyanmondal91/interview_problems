---
layout: design_pattern
title: "Proxy"
category: "structural"
difficulty: "Medium"
also_known_as: "Surrogate"
intent: "Provide a surrogate or placeholder for another object to control access to it."
tags: [structural, proxy]
render_with_liquid: false
---

## Intent

Provide a surrogate or placeholder for another object to control access to it.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Subject** | Common interface for RealSubject and Proxy |
| **RealSubject** | The actual object the proxy controls access to |
| **Proxy** | Holds reference to RealSubject; intercepts client calls |
| **Client** | Works with Subject interface |

## Pros

- Open/Closed: control object without modifying it
- Lifecycle management
- Cross-cutting concerns without polluting business logic

## Cons

- Adds indirection; can slow down requests
- Code may become complex

## Real-World Examples

- `Java RMI`
- `Spring AOP proxies`
- `java.lang.reflect.Proxy`
- `Hibernate lazy-loading proxies`

## Variants

- Virtual Proxy (lazy init)
- Protection Proxy (access control)
- Caching Proxy
- Remote Proxy
- Logging Proxy

## Full Java Implementation

```java
package org.interview.design_patterns.structural.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 * Design Pattern: Proxy (Structural)
 * ============================================================
 *
 * INTENT:
 *   Provide a surrogate or placeholder for another object to
 *   control access to it.
 *
 * PROBLEM IT SOLVES:
 *   You need to add behavior (lazy loading, access control, logging,
 *   caching) when accessing an object WITHOUT changing the object's code
 *   and without the client knowing there's an intermediary.
 *
 * STRUCTURE:
 *   - Subject (interface): common interface for RealSubject and Proxy
 *   - RealSubject: the real object that the proxy controls access to
 *   - Proxy: holds reference to RealSubject; intercepts client calls
 *   - Client: works with Subject interface; unaware of proxy vs real
 *
 * PROXY TYPES:
 *   1. Virtual Proxy: lazy initialization of expensive objects
 *   2. Protection Proxy: access control / authorization
 *   3. Caching Proxy: caches results of expensive operations
 *   4. Remote Proxy: represents an object in a different address space
 *   5. Logging Proxy: logs all requests before/after delegation
 *
 * DIFFERENCE FROM DECORATOR:
 *   - Proxy: manages lifecycle; usually single layer; controls ACCESS
 *   - Decorator: adds features; typically stacked; enhances BEHAVIOR
 *
 * REAL-WORLD EXAMPLES:
 *   - Java RMI (Remote Proxy)
 *   - Spring AOP proxies (Logging/Transaction Proxy)
 *   - java.lang.reflect.Proxy (Dynamic Proxy)
 *   - Hibernate lazy-loading proxies (Virtual Proxy)
 *   - Security frameworks: authorization before method calls
 *
 * PROS:
 *   + Open/Closed: control object without modifying it
 *   + Lifecycle management (create/destroy real subject on demand)
 *   + Cross-cutting concerns (logging, security) without polluting business logic
 *
 * CONS:
 *   - Adds indirection; can slow down requests
 *   - Code may become complex
 */
public class ProxyPattern {

    // ================================================================
    // Subject Interface — shared by RealSubject and all Proxy types
    // ================================================================
    interface DatabaseService {
        String query(String sql);
        void execute(String sql);
    }

    // ================================================================
    // RealSubject — the actual expensive-to-create database connection
    // ================================================================
    static class RealDatabaseService implements DatabaseService {
        private final String connectionUrl;

        RealDatabaseService(String connectionUrl) {
            this.connectionUrl = connectionUrl;
            // Simulate expensive connection setup
            System.out.println("  [RealDB] Connecting to: " + connectionUrl + " (expensive!)");
            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.println("  [RealDB] Connected.");
        }

        @Override
        public String query(String sql) {
            System.out.println("  [RealDB] Executing query: " + sql);
            return "ResultSet{rows: [row1, row2, row3]}"; // simulated result
        }

        @Override
        public void execute(String sql) {
            System.out.println("  [RealDB] Executing statement: " + sql);
        }
    }

    // ================================================================
    // Proxy 1: Virtual Proxy (Lazy Initialization)
    // Creates the real connection only when first needed, not at startup
    // ================================================================
    static class LazyDatabaseProxy implements DatabaseService {
        private final String connectionUrl;
        private RealDatabaseService realService; // null until first use

        LazyDatabaseProxy(String connectionUrl) {
            this.connectionUrl = connectionUrl;
            System.out.println("[LazyProxy] Created proxy (no connection yet)");
        }

        // Lazily initializes the real service
        private RealDatabaseService getRealService() {
            if (realService == null) {
                System.out.println("[LazyProxy] First use — creating real connection...");
                realService = new RealDatabaseService(connectionUrl);
            }
            return realService;
        }

        @Override
        public String query(String sql) {
            return getRealService().query(sql);
        }

        @Override
        public void execute(String sql) {
            getRealService().execute(sql);
        }
    }

    // ================================================================
    // Proxy 2: Protection Proxy (Access Control)
    // Checks user role before allowing certain operations
    // ================================================================
    static class ProtectedDatabaseProxy implements DatabaseService {
        private final DatabaseService realService;
        private final String          userRole;  // "admin", "reader", "unknown"

        ProtectedDatabaseProxy(DatabaseService realService, String userRole) {
            this.realService = realService;
            this.userRole    = userRole;
        }

        @Override
        public String query(String sql) {
            // Readers and admins can query
            if (userRole.equals("admin") || userRole.equals("reader")) {
                System.out.println("[ProtProxy] Access granted for QUERY (role=" + userRole + ")");
                return realService.query(sql);
            }
            throw new SecurityException("User role '" + userRole + "' cannot execute queries");
        }

        @Override
        public void execute(String sql) {
            // Only admins can execute DDL/DML
            if (userRole.equals("admin")) {
                System.out.println("[ProtProxy] Access granted for EXECUTE (role=" + userRole + ")");
                realService.execute(sql);
            } else {
                throw new SecurityException("User role '" + userRole + "' cannot execute statements");
            }
        }
    }

    // ================================================================
    // Proxy 3: Caching Proxy
    // Caches SELECT query results; bypasses real DB on cache hit
    // ================================================================
    static class CachingDatabaseProxy implements DatabaseService {
        private final DatabaseService       realService;
        private final Map<String, String>   cache = new HashMap<>();

        CachingDatabaseProxy(DatabaseService realService) {
            this.realService = realService;
        }

        @Override
        public String query(String sql) {
            if (cache.containsKey(sql)) {
                System.out.println("[CacheProxy] Cache HIT for: " + sql);
                return cache.get(sql);
            }
            System.out.println("[CacheProxy] Cache MISS — delegating to real DB");
            String result = realService.query(sql);
            cache.put(sql, result);
            return result;
        }

        @Override
        public void execute(String sql) {
            // Invalidate cache on writes
            System.out.println("[CacheProxy] Write operation — clearing cache");
            cache.clear();
            realService.execute(sql);
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Virtual Proxy (Lazy Initialization) ===");
        DatabaseService lazy = new LazyDatabaseProxy("jdbc:postgresql://localhost:5432/mydb");
        System.out.println("Proxy created. No DB connection yet.");
        System.out.println("First query (triggers connection):");
        lazy.query("SELECT * FROM users");
        System.out.println("Second query (connection reused):");
        lazy.query("SELECT * FROM orders");

        System.out.println("\n=== Protection Proxy ===");
        DatabaseService real = new RealDatabaseService("jdbc:mysql://localhost/prod");

        DatabaseService adminProxy  = new ProtectedDatabaseProxy(real, "admin");
        DatabaseService readerProxy = new ProtectedDatabaseProxy(real, "reader");

        System.out.println("Admin can query and execute:");
        adminProxy.query("SELECT * FROM sensitive_table");
        adminProxy.execute("DELETE FROM logs WHERE age > 30");

        System.out.println("Reader can query but not execute:");
        readerProxy.query("SELECT * FROM public_table");
        try {
            readerProxy.execute("DROP TABLE users"); // should throw
        } catch (SecurityException e) {
            System.out.println("[BLOCKED] " + e.getMessage());
        }

        System.out.println("\n=== Caching Proxy ===");
        DatabaseService cached = new CachingDatabaseProxy(real);
        cached.query("SELECT * FROM products");  // miss
        cached.query("SELECT * FROM products");  // hit
        cached.query("SELECT * FROM products");  // hit again
        cached.execute("INSERT INTO products VALUES (42, 'Widget')"); // clears cache
        cached.query("SELECT * FROM products");  // miss (cache was cleared)
    }
}

```
