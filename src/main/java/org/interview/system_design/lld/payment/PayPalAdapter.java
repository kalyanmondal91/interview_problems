package org.interview.system_design.lld.payment;

/**
 * Adapter that wraps PayPalGateway and exposes the unified PaymentGatewayAdapter API.
 * PayPal accepts fractional amounts directly; translates userId → payerId.
 * Pattern: Adapter (Adaptee = PayPalGateway)
 */
public class PayPalAdapter implements PaymentGatewayAdapter {

    private final PayPalGateway payPalGateway;

    public PayPalAdapter(PayPalGateway payPalGateway) {
        this.payPalGateway = payPalGateway;
    }

    @Override
    public String getGatewayName() { return "PayPal"; }

    @Override
    public double getFeePercent() { return payPalGateway.getFeePercent(); }

    @Override
    public long getEstimatedLatencyMs() { return payPalGateway.getLatencyMs(); }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            String txnId = payPalGateway.executePayment(
                    request.getUserId(),
                    request.getAmount(),
                    request.getCurrency().name()
            );
            return new PaymentResponse(
                    request.getRequestId(), PaymentStatus.SUCCESS, txnId, "Payment successful via PayPal");
        } catch (RuntimeException e) {
            return new PaymentResponse(
                    request.getRequestId(), PaymentStatus.FAILED, null, e.getMessage());
        }
    }

    @Override
    public PaymentResponse refund(String transactionId, double amount) {
        boolean success = payPalGateway.issueRefund(transactionId, amount);
        PaymentStatus status = success ? PaymentStatus.REFUNDED : PaymentStatus.FAILED;
        String msg = success ? "Refund issued via PayPal" : "PayPal refund failed";
        return new PaymentResponse(null, status, transactionId, msg);
    }
}
