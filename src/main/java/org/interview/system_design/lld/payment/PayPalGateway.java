package org.interview.system_design.lld.payment;

import java.util.UUID;

/**
 * Simulated PayPal payment gateway.
 * PayPal uses executePayment / issueRefund method names.
 */
public class PayPalGateway {

    private static final double FEE_PERCENT = 0.034; // 3.4%
    private static final long LATENCY_MS   = 200;

    public String executePayment(String payerId, double amount, String currencyCode) {
        simulateLatency();
        System.out.printf("[PayPal] Executing payment payer=%s amount=%.2f %s%n",
                payerId, amount, currencyCode);
        if (Math.random() < 0.03) {
            throw new RuntimeException("PayPal: insufficient funds");
        }
        return "paypal_txn_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean issueRefund(String paypalTxnId, double amount) {
        simulateLatency();
        System.out.printf("[PayPal] Issuing refund txn=%s amount=%.2f%n", paypalTxnId, amount);
        return true;
    }

    public double getFeePercent() { return FEE_PERCENT; }
    public long getLatencyMs()   { return LATENCY_MS; }

    private void simulateLatency() {
        try { Thread.sleep(LATENCY_MS / 10); } catch (InterruptedException ignored) {}
    }
}
