package org.interview.system_design.lld.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Orchestrates the full payment lifecycle:
 *   1. Validate via Chain of Responsibility (AmountValidator → CurrencyValidator → FraudDetectionValidator)
 *   2. Select gateway via Strategy pattern
 *   3. Process payment through selected adapter
 *   4. Notify observers of the result
 *
 * Pattern: Adapter + Chain of Responsibility + Strategy + Observer (notification)
 */
public class PaymentService {

    private final PaymentValidator validationChain;
    private final List<PaymentGatewayAdapter> gateways;
    private final GatewaySelectionStrategy selectionStrategy;

    /** In-memory transaction store: transactionId → response */
    private final Map<String, PaymentResponse> transactionStore = new ConcurrentHashMap<>();

    /** Simple observer list for payment events */
    private final List<PaymentEventListener> listeners = new ArrayList<>();

    public PaymentService(List<PaymentGatewayAdapter> gateways,
                          GatewaySelectionStrategy selectionStrategy) {
        this.gateways = Collections.unmodifiableList(new ArrayList<>(gateways));
        this.selectionStrategy = selectionStrategy;

        // Build default validation chain
        AmountValidator amountValidator = new AmountValidator();
        CurrencyValidator currencyValidator = new CurrencyValidator();
        FraudDetectionValidator fraudValidator = new FraudDetectionValidator();
        amountValidator.setNext(currencyValidator).setNext(fraudValidator);
        this.validationChain = amountValidator;
    }

    /** Register an observer that receives payment lifecycle events. */
    public void addListener(PaymentEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Processes a payment end-to-end.
     *
     * @param request the payment to process
     * @return PaymentResponse with final status
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        System.out.println("[PaymentService] Processing: " + request);

        // Step 1 — validate
        String validationError = validationChain.validate(request);
        if (validationError != null) {
            PaymentResponse failed = new PaymentResponse(
                    request.getRequestId(), PaymentStatus.FAILED, null,
                    "Validation failed: " + validationError);
            notifyListeners(failed, false);
            return failed;
        }

        // Step 2 — select gateway
        PaymentGatewayAdapter gateway = selectionStrategy.selectGateway(request, gateways);

        // Step 3 — process
        PaymentResponse response = gateway.processPayment(request);

        // Step 4 — store and notify
        if (response.getTransactionId() != null) {
            transactionStore.put(response.getTransactionId(), response);
        }
        notifyListeners(response, response.isSuccess());
        System.out.println("[PaymentService] Result: " + response);
        return response;
    }

    /**
     * Issues a refund for a completed transaction.
     *
     * @param transactionId the transaction to refund
     * @param amount        amount to refund
     * @return PaymentResponse with REFUNDED or FAILED status
     */
    public PaymentResponse refund(String transactionId, double amount) {
        // Find the original gateway by prefix heuristic
        PaymentGatewayAdapter gateway = resolveGatewayByTransactionId(transactionId);
        if (gateway == null) {
            return new PaymentResponse(null, PaymentStatus.FAILED, transactionId,
                    "Cannot determine gateway for transaction: " + transactionId);
        }
        PaymentResponse response = gateway.refund(transactionId, amount);
        notifyListeners(response, response.getStatus() == PaymentStatus.REFUNDED);
        return response;
    }

    /** Returns a read-only view of all processed transactions. */
    public Map<String, PaymentResponse> getTransactionStore() {
        return Collections.unmodifiableMap(transactionStore);
    }

    // ---- Private helpers ----

    private PaymentGatewayAdapter resolveGatewayByTransactionId(String txnId) {
        if (txnId.startsWith("stripe_"))   return findGateway("Stripe");
        if (txnId.startsWith("paypal_"))   return findGateway("PayPal");
        if (txnId.startsWith("razorpay_")) return findGateway("Razorpay");
        return gateways.isEmpty() ? null : gateways.get(0); // fallback
    }

    private PaymentGatewayAdapter findGateway(String name) {
        return gateways.stream()
                .filter(g -> g.getGatewayName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private void notifyListeners(PaymentResponse response, boolean success) {
        listeners.forEach(l -> {
            if (success) l.onPaymentSuccess(response);
            else         l.onPaymentFailed(response);
        });
    }

    // ---- Nested observer interface ----

    /** Listener interface for payment lifecycle events. */
    public interface PaymentEventListener {
        void onPaymentSuccess(PaymentResponse response);
        void onPaymentFailed(PaymentResponse response);
    }
}
