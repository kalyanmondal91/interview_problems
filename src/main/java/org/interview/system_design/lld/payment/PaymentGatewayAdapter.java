package org.interview.system_design.lld.payment;

/**
 * Adapter interface that normalises heterogeneous gateway APIs to a single contract.
 * Pattern: Adapter (target interface)
 *
 * Concrete adapters (StripeAdapter, PayPalAdapter, RazorpayAdapter) wrap the real
 * gateway objects and translate method calls + data formats.
 */
public interface PaymentGatewayAdapter {

    /** Human-readable name, e.g. "Stripe", "PayPal". */
    String getGatewayName();

    /** Processing fee as a fraction of transaction amount (e.g. 0.029 = 2.9%). */
    double getFeePercent();

    /** Expected processing latency in milliseconds (used by FastestGatewayStrategy). */
    long getEstimatedLatencyMs();

    /**
     * Processes the payment request via the wrapped gateway.
     *
     * @param request normalised payment request
     * @return normalised payment response
     */
    PaymentResponse processPayment(PaymentRequest request);

    /**
     * Issues a refund via the wrapped gateway.
     *
     * @param transactionId gateway-specific transaction identifier
     * @param amount        amount to refund (in original request currency)
     * @return normalised payment response with REFUNDED status on success
     */
    PaymentResponse refund(String transactionId, double amount);
}
