package org.interview.system_design.lld.payment;

/**
 * Simulated fraud detection validator.
 * Rejects requests that exhibit suspicious patterns:
 *   - Round, very large amounts (potential money-laundering signal)
 *   - Amounts that end in .99 above a threshold (carding pattern)
 * Pattern: Chain of Responsibility (concrete handler)
 */
public class FraudDetectionValidator extends PaymentValidator {

    private static final double SUSPICIOUS_LARGE_AMOUNT_USD = 9_999.0;
    private static final double CARDING_THRESHOLD_USD       = 500.0;

    @Override
    protected String doValidate(PaymentRequest request) {
        double usdAmount = request.getCurrency().toUsd(request.getAmount());

        // Flag suspiciously round large amounts
        if (usdAmount >= SUSPICIOUS_LARGE_AMOUNT_USD && usdAmount % 1000 == 0) {
            return String.format(
                    "Fraud check failed: suspiciously round large amount %.2f USD for user %s",
                    usdAmount, request.getUserId());
        }

        // Flag carding pattern (e.g. $999.99, $1999.99)
        double cents = Math.round(usdAmount * 100) % 100;
        if (usdAmount > CARDING_THRESHOLD_USD && cents == 99) {
            return String.format(
                    "Fraud check failed: carding pattern detected (amount %.2f USD) for user %s",
                    usdAmount, request.getUserId());
        }

        System.out.printf("[FraudDetection] Request %s passed fraud check (%.2f USD)%n",
                request.getRequestId(), usdAmount);
        return null;
    }
}
