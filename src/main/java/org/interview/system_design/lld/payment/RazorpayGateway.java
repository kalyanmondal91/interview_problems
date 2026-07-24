package org.interview.system_design.lld.payment;

import java.util.UUID;

/**
 * Simulated Razorpay payment gateway (popular in India).
 * Uses createOrder / triggerRefund method names.
 */
public class RazorpayGateway {

    private static final double FEE_PERCENT = 0.02; // 2.0%
    private static final long LATENCY_MS   = 80;

    public String createOrder(String contactId, int amountPaise, String currency) {
        simulateLatency();
        System.out.printf("[Razorpay] Creating order contact=%s amount=%d paise %s%n",
                contactId, amountPaise, currency);
        if (Math.random() < 0.04) {
            throw new RuntimeException("Razorpay: gateway timeout");
        }
        return "razorpay_txn_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean triggerRefund(String razorpayOrderId, int amountPaise) {
        simulateLatency();
        System.out.printf("[Razorpay] Triggering refund order=%s amount=%d paise%n",
                razorpayOrderId, amountPaise);
        return true;
    }

    public double getFeePercent() { return FEE_PERCENT; }
    public long getLatencyMs()   { return LATENCY_MS; }

    private void simulateLatency() {
        try { Thread.sleep(LATENCY_MS / 10); } catch (InterruptedException ignored) {}
    }
}
