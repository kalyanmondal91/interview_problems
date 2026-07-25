package org.interview.design_patterns.behavioral.chain_of_responsibility;

/**
 * ============================================================
 * Design Pattern: Chain of Responsibility (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Avoid coupling the sender of a request to its receiver by giving
 *   more than one object a chance to handle the request. Chain the
 *   receiving objects and pass the request along the chain until an
 *   object handles it.
 *
 * PROBLEM IT SOLVES:
 *   - Multiple handlers could process a request, but you don't want
 *     the sender to know which one will handle it.
 *   - You want to dynamically configure the handling pipeline.
 *   - Approval workflows, middleware, request filters.
 *
 * STRUCTURE:
 *   - Handler (interface/abstract): declares handle(request), setNext(handler)
 *   - ConcreteHandler: handles requests it is responsible for; forwards others
 *   - Client: sends request to the first handler in the chain
 *
 * REAL-WORLD EXAMPLES:
 *   - Servlet Filter chain (Java EE)
 *   - Spring Security filter chain
 *   - Logging level handlers (DEBUG → INFO → WARN → ERROR)
 *   - Purchase approval: employee → manager → director → CEO
 *   - Exception handling up the call stack
 *
 * PROS:
 *   + Reduce coupling: sender doesn't know who handles the request
 *   + Open/Closed: add handlers without changing existing ones
 *   + Control the order of handling
 *
 * CONS:
 *   - Request might reach end of chain unhandled
 *   - Can be hard to debug/trace if chain is long
 *
 * SCENARIO:
 *   Purchase approval workflow:
 *   Employee ($0-$500) → Manager ($501-$2000) → Director ($2001-$10000) → CEO (any)
 */
public class ChainOfResponsibilityPattern {

    // ================================================================
    // Request object
    // ================================================================
    static class PurchaseRequest {
        private final String purpose;
        private final double amount;

        PurchaseRequest(String purpose, double amount) {
            this.purpose = purpose;
            this.amount  = amount;
        }

        public double getAmount() { return amount; }

        @Override
        public String toString() {
            return String.format("Purchase[purpose='%s', amount=$%.0f]", purpose, amount);
        }
    }

    // ================================================================
    // Handler (abstract)
    // Uses a "self-chaining" pattern: setNext returns the next handler
    // so you can chain fluently: handler1.setNext(handler2).setNext(handler3)
    // ================================================================
    static abstract class Approver {
        private Approver next; // next handler in the chain

        /**
         * Set next handler. Returns next handler for fluent chaining.
         * handler1.setNext(handler2).setNext(handler3)
         */
        public Approver setNext(Approver next) {
            this.next = next;
            return next; // return next to allow fluent chaining
        }

        /**
         * Attempt to handle the request.
         * If this handler can't, forward to next.
         */
        public final void handle(PurchaseRequest request) {
            if (canApprove(request)) {
                approve(request);
            } else if (next != null) {
                System.out.printf("  [%s] Cannot approve $%.0f — escalating...%n",
                    getTitle(), request.getAmount());
                next.handle(request);
            } else {
                System.out.printf("  [Chain End] No one could approve: %s%n", request);
            }
        }

        /** Subclass-specific check: can this handler approve? */
        protected abstract boolean canApprove(PurchaseRequest request);

        /** Subclass-specific approval action */
        protected abstract void approve(PurchaseRequest request);

        /** Human-readable role name */
        protected abstract String getTitle();
    }

    // ================================================================
    // ConcreteHandlers
    // ================================================================

    static class Employee extends Approver {
        private static final double LIMIT = 500;

        @Override
        protected boolean canApprove(PurchaseRequest r) { return r.getAmount() <= LIMIT; }

        @Override
        protected void approve(PurchaseRequest r) {
            System.out.printf("  ✅ [Employee] Approved %s (within $%.0f limit)%n", r, LIMIT);
        }

        @Override protected String getTitle() { return "Employee"; }
    }

    static class Manager extends Approver {
        private static final double LIMIT = 2_000;

        @Override
        protected boolean canApprove(PurchaseRequest r) { return r.getAmount() <= LIMIT; }

        @Override
        protected void approve(PurchaseRequest r) {
            System.out.printf("  ✅ [Manager] Approved %s (within $%.0f limit)%n", r, LIMIT);
        }

        @Override protected String getTitle() { return "Manager"; }
    }

    static class Director extends Approver {
        private static final double LIMIT = 10_000;

        @Override
        protected boolean canApprove(PurchaseRequest r) { return r.getAmount() <= LIMIT; }

        @Override
        protected void approve(PurchaseRequest r) {
            System.out.printf("  ✅ [Director] Approved %s (within $%.0f limit)%n", r, LIMIT);
        }

        @Override protected String getTitle() { return "Director"; }
    }

    static class CEO extends Approver {
        @Override
        protected boolean canApprove(PurchaseRequest r) { return true; } // approves everything

        @Override
        protected void approve(PurchaseRequest r) {
            if (r.getAmount() > 50_000) {
                System.out.printf("  ⚠️  [CEO] Approved %s — requires board notification!%n", r);
            } else {
                System.out.printf("  ✅ [CEO] Approved %s%n", r);
            }
        }

        @Override protected String getTitle() { return "CEO"; }
    }

    // ================================================================
    // BONUS: HTTP Middleware Chain (functional approach with Java lambdas)
    // Shows CoR with modern Java without traditional subclassing
    // ================================================================
    @FunctionalInterface
    interface Middleware {
        boolean handle(String request, String nextInChain);
    }

    static class AuthMiddleware implements Middleware {
        @Override
        public boolean handle(String request, String next) {
            if (!request.contains("token=")) {
                System.out.println("  [Auth] REJECTED — no auth token");
                return false;
            }
            System.out.println("  [Auth] Token valid ✓");
            return true; // continue to next
        }
    }

    static class LoggingMiddleware implements Middleware {
        @Override
        public boolean handle(String request, String next) {
            System.out.println("  [Log] Request: " + request);
            return true;
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Purchase Approval Chain ===");

        // Build the chain
        Approver employee = new Employee();
        Approver manager  = new Manager();
        Approver director = new Director();
        Approver ceo      = new CEO();

        // Chain: Employee → Manager → Director → CEO
        employee.setNext(manager).setNext(director).setNext(ceo);

        // Test various amounts
        PurchaseRequest[] requests = {
            new PurchaseRequest("Office supplies",   300),
            new PurchaseRequest("Team lunch",        450),
            new PurchaseRequest("Conference tickets",1500),
            new PurchaseRequest("New server",        8000),
            new PurchaseRequest("Acquisition",      75000)
        };

        for (PurchaseRequest request : requests) {
            System.out.println("\nProcessing: " + request);
            employee.handle(request); // always start from beginning of chain
        }

        System.out.println("\n=== Chain without all handlers (partial chain) ===");
        // Build a chain with only Employee → Manager (Director and CEO not included)
        Approver emp2 = new Employee();
        Approver mgr2 = new Manager();
        emp2.setNext(mgr2);
        // Request that exceeds Manager's limit — no one handles it
        System.out.println("Processing: large purchase ($5000)");
        emp2.handle(new PurchaseRequest("New equipment", 5000));
    }
}
