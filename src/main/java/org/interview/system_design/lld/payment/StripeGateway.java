package org.interview.system_design.lld.payment;

import java.util.UUID;

/**
 * Simulated Stripe payment gateway.
 * Stripe uses its own SDK method names (charge / refundCharge).
 */
public class StripeGateway {

    private static final double FEE_PERCENT = 0.029; // 2.9%
    private static final long LATENCY_MS   = 120;

    public String charge(String userId, long amountCents, String currency) {
        simulateLatency();
        System.out.printf("[Stripe] Charging user=%s amount=%d cents %s%n",
                userId, amountCents, currency);
        // Simulate ~5% failure
        if (Math.random() < 0.05) {
            throw new RuntimeException("Stripe: card declined");
        }
        return "stripe_txn_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean refundCharge(String stripeChargeId, long amountCents) {
        simulateLatency();
        System.out.printf("[Stripe] Refunding charge=%s amount=%d cents%n",
                stripeChargeId, amountCents);
        return true;
    }

    public double getFeePercent() { return FEE_PERCENT; }
    public long getLatencyMs()   { return LATENCY_MS; }

    private void simulateLatency() {
        try { Thread.sleep(LATENCY_MS / 10); } catch (InterruptedException ignored) {}
    }
}
