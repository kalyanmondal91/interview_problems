package org.interview.system_design.lld.payment;

/**
 * Adapter that wraps RazorpayGateway and exposes the unified PaymentGatewayAdapter API.
 * Razorpay works in smallest unit (paise = 1/100 INR); converts from the request currency.
 * Pattern: Adapter (Adaptee = RazorpayGateway)
 */
public class RazorpayAdapter implements PaymentGatewayAdapter {

    private final RazorpayGateway razorpayGateway;

    public RazorpayAdapter(RazorpayGateway razorpayGateway) {
        this.razorpayGateway = razorpayGateway;
    }

    @Override
    public String getGatewayName() { return "Razorpay"; }

    @Override
    public double getFeePercent() { return razorpayGateway.getFeePercent(); }

    @Override
    public long getEstimatedLatencyMs() { return razorpayGateway.getLatencyMs(); }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            // Convert amount to INR paise (smallest Razorpay unit)
            double amountInr = request.getCurrency().toUsd(request.getAmount())
                               / Currency.INR.getExchangeRateToUsd();
            int amountPaise = (int) Math.round(amountInr * 100);
            String txnId = razorpayGateway.createOrder(
                    request.getUserId(),
                    amountPaise,
                    "INR"
            );
            return new PaymentResponse(
                    request.getRequestId(), PaymentStatus.SUCCESS, txnId, "Payment successful via Razorpay");
        } catch (RuntimeException e) {
            return new PaymentResponse(
                    request.getRequestId(), PaymentStatus.FAILED, null, e.getMessage());
        }
    }

    @Override
    public PaymentResponse refund(String transactionId, double amount) {
        double amountInr = Currency.USD.toUsd(amount) / Currency.INR.getExchangeRateToUsd();
        int amountPaise = (int) Math.round(amountInr * 100);
        boolean success = razorpayGateway.triggerRefund(transactionId, amountPaise);
        PaymentStatus status = success ? PaymentStatus.REFUNDED : PaymentStatus.FAILED;
        String msg = success ? "Refund issued via Razorpay" : "Razorpay refund failed";
        return new PaymentResponse(null, status, transactionId, msg);
    }
}
