package org.interview.design_patterns.structural.adapter;

/**
 * ============================================================
 * Design Pattern: Adapter (Structural)
 * ============================================================
 *
 * INTENT:
 *   Convert the interface of a class into another interface that
 *   clients expect. Adapter lets classes work together that couldn't
 *   otherwise because of incompatible interfaces.
 *
 * ALSO KNOWN AS: Wrapper
 *
 * PROBLEM IT SOLVES:
 *   - You want to use an existing class but its interface doesn't match.
 *   - You're integrating a third-party library with a different API.
 *   - You need to make incompatible classes collaborate.
 *
 * STRUCTURE:
 *   - Target: the interface the client expects
 *   - Adaptee: the existing class with incompatible interface
 *   - Adapter: wraps Adaptee and implements Target (Object Adapter)
 *             OR extends Adaptee and implements Target (Class Adapter)
 *   - Client: works only with Target interface
 *
 * TWO VARIANTS:
 *   1. Object Adapter (preferred in Java): uses composition — holds an Adaptee
 *   2. Class Adapter: uses multiple inheritance (impossible in Java without tricks)
 *
 * REAL-WORLD EXAMPLES:
 *   - java.io.InputStreamReader (adapts InputStream to Reader)
 *   - java.util.Arrays#asList() (adapts array to List)
 *   - Spring's HandlerAdapter (adapts different handler types to DispatcherServlet)
 *   - Integrating legacy payment gateway with new payment API
 *
 * PROS:
 *   + Single Responsibility: separate interface conversion from business logic
 *   + Open/Closed: add new adapters without changing existing code
 *
 * CONS:
 *   - Overall complexity increases (extra classes)
 *   - Sometimes simpler to change the service class if you own it
 *
 * SCENARIO:
 *   New payment system expects a PaymentProcessor interface.
 *   Legacy system uses OldPaymentGateway with a completely different API.
 *   The Adapter bridges them without changing either.
 */
public class AdapterPattern {

    // ================================================================
    // Target Interface — what the client code expects
    // ================================================================
    interface PaymentProcessor {
        boolean processPayment(String cardNumber, double amount, String currency);
        boolean refundPayment(String transactionId, double amount);
    }

    // ================================================================
    // Adaptee — legacy/third-party class with incompatible interface
    // We cannot change this class (third-party or legacy code)
    // ================================================================
    static class LegacyPaymentGateway {
        /**
         * Legacy method: accepts amount in cents, no currency param
         * Returns a transaction reference or null on failure
         */
        public String charge(String card, long amountInCents) {
            System.out.printf("[Legacy Gateway] Charging card %s for %d cents%n",
                maskCard(card), amountInCents);
            // Simulate: return transaction ID on success
            return "TXN-" + System.currentTimeMillis();
        }

        /**
         * Legacy refund: uses a different parameter name ("ref" not "transactionId")
         * Returns true/false
         */
        public boolean reverseCharge(String txnRef) {
            System.out.println("[Legacy Gateway] Reversing transaction: " + txnRef);
            return true;
        }

        private String maskCard(String card) {
            return "****-****-****-" + card.substring(card.length() - 4);
        }
    }

    // ================================================================
    // Object Adapter
    // Wraps LegacyPaymentGateway and implements PaymentProcessor.
    // Translates the new API calls into legacy API calls.
    // ================================================================
    static class LegacyPaymentAdapter implements PaymentProcessor {
        private final LegacyPaymentGateway gateway; // composed, not inherited

        LegacyPaymentAdapter(LegacyPaymentGateway gateway) {
            this.gateway = gateway;
        }

        @Override
        public boolean processPayment(String cardNumber, double amount, String currency) {
            // Adaptation 1: convert double dollars to long cents
            long amountInCents = Math.round(amount * 100);
            // Adaptation 2: currency ignored by legacy gateway (log it)
            System.out.println("[Adapter] Currency: " + currency + " (legacy ignores currency)");
            String txnId = gateway.charge(cardNumber, amountInCents);
            return txnId != null; // adaptation 3: convert String result to boolean
        }

        @Override
        public boolean refundPayment(String transactionId, double amount) {
            // Adaptation: map new API (transactionId, amount) to legacy (txnRef only)
            // Legacy doesn't support partial refunds — log a warning
            if (amount > 0) {
                System.out.println("[Adapter] Warning: partial refund not supported by legacy gateway");
            }
            return gateway.reverseCharge(transactionId);
        }
    }

    // ================================================================
    // A modern payment processor (no adaptation needed — implements directly)
    // ================================================================
    static class StripePaymentProcessor implements PaymentProcessor {
        @Override
        public boolean processPayment(String cardNumber, double amount, String currency) {
            System.out.printf("[Stripe] Charging %.2f %s to card ending %s%n",
                amount, currency, cardNumber.substring(cardNumber.length() - 4));
            return true;
        }

        @Override
        public boolean refundPayment(String transactionId, double amount) {
            System.out.printf("[Stripe] Refunding %.2f for txn %s%n", amount, transactionId);
            return true;
        }
    }

    // ================================================================
    // Client — works only with PaymentProcessor interface
    // Doesn't know whether it's talking to legacy or modern gateway
    // ================================================================
    static class PaymentService {
        private final PaymentProcessor processor;

        PaymentService(PaymentProcessor processor) {
            this.processor = processor;
        }

        public void checkout(String card, double amount, String currency) {
            System.out.println("--- Checkout ---");
            boolean success = processor.processPayment(card, amount, currency);
            System.out.println("Payment " + (success ? "SUCCESSFUL" : "FAILED"));
        }

        public void refund(String txnId, double amount) {
            System.out.println("--- Refund ---");
            boolean success = processor.refundPayment(txnId, amount);
            System.out.println("Refund " + (success ? "SUCCESSFUL" : "FAILED"));
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Using Modern Stripe Processor ===");
        PaymentService stripeService = new PaymentService(new StripePaymentProcessor());
        stripeService.checkout("4111111111111234", 99.99, "USD");
        stripeService.refund("pi_abc123", 99.99);

        System.out.println("\n=== Using Legacy Gateway via Adapter ===");
        // Legacy gateway adapted to work with the same PaymentService client
        LegacyPaymentGateway legacy = new LegacyPaymentGateway();
        PaymentProcessor adapted = new LegacyPaymentAdapter(legacy);
        PaymentService legacyService = new PaymentService(adapted);

        legacyService.checkout("4111111111115678", 49.99, "EUR");
        legacyService.refund("TXN-12345678", 49.99);
    }
}
