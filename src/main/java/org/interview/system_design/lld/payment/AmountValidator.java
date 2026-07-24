package org.interview.system_design.lld.payment;

/**
 * Validates that the payment amount is within acceptable bounds:
 *   - amount > 0
 *   - amount <= configured maximum (default USD 100,000)
 * Pattern: Chain of Responsibility (concrete handler)
 */
public class AmountValidator extends PaymentValidator {

    private static final double DEFAULT_MAX_AMOUNT = 100_000.0;

    private final double maxAmount;

    public AmountValidator() {
        this(DEFAULT_MAX_AMOUNT);
    }

    public AmountValidator(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    protected String doValidate(PaymentRequest request) {
        double amount = request.getAmount();
        if (amount <= 0) {
            return "Invalid amount: must be greater than zero, got " + amount;
        }
        // Convert to USD for a common cap comparison
        double usdAmount = request.getCurrency().toUsd(amount);
        if (usdAmount > maxAmount) {
            return String.format("Amount %.2f %s (≈ %.2f USD) exceeds maximum limit of %.2f USD",
                    amount, request.getCurrency(), usdAmount, maxAmount);
        }
        return null;
    }
}
