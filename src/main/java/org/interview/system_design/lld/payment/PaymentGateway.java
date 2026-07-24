package org.interview.system_design.lld.payment;

/**
 * Low-level gateway API contract (third-party gateway interface).
 * Each real gateway SDK has a different method signature; adapters normalize them.
 * Pattern: Adapter (target interface)
 */
public interface PaymentGateway {

    /**
     * Processes a payment request through this gateway.
     *
     * @param request the payment to process
     * @return response indicating success/failure
     */
    PaymentResponse processPayment(PaymentRequest request);

    /**
     * Issues a refund for a previously completed transaction.
     *
     * @param transactionId original transaction identifier
     * @param amount        amount to refund
     * @return response indicating refund status
     */
    PaymentResponse refund(String transactionId, double amount);
}
