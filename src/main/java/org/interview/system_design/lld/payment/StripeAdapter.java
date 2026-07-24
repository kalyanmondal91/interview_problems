package org.interview.system_design.lld.payment;

/**
 * Adapter that wraps StripeGateway and exposes the unified PaymentGatewayAdapter API.
 * Translates: amount (double) → cents (long), handles exception → FAILED response.
 * Pattern: Adapter (Adaptee = StripeGateway)
 */
public class StripeAdapter implements PaymentGatewayAdapter {

    private final StripeGateway stripeGateway;

    public StripeAdapter(StripeGateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    @Override
    public String getGatewayName() { return "Stripe"; }

    @Override
    public double getFeePercent() { return stripeGateway.getFeePercent(); }

    @Override
    public long getEstimatedLatencyMs() { return stripeGateway.getLatencyMs(); }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            // Stripe works in smallest currency unit (cents for USD)
            long amountCents = Math.round(request.getAmount() * 100);
            String txnId = stripeGateway.charge(
                    request.getUserId(),
                    amountCents,
                    request.getCurrency().name()
            );
            return new PaymentResponse(
                    request.getRequestId(), PaymentStatus.SUCCESS, txnId, "Payment successful via Stripe");
        } catch (RuntimeException e) {
            return new PaymentResponse(
                    request.getRequestId(), PaymentStatus.FAILED, null, e.getMessage());
        }
    }

    @Override
    public PaymentResponse refund(String transactionId, double amount) {
        long amountCents = Math.round(amount * 100);
        boolean success = stripeGateway.refundCharge(transactionId, amountCents);
        PaymentStatus status = success ? PaymentStatus.REFUNDED : PaymentStatus.FAILED;
        String msg = success ? "Refund issued via Stripe" : "Stripe refund failed";
        return new PaymentResponse(null, status, transactionId, msg);
    }
}
