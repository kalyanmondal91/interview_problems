package org.interview.system_design.lld.pricingengine;

/**
 * Represents a single price modification produced by a pricing rule.
 * Immutable value object.
 */
public class PriceAdjustment {

    public enum Type {
        DISCOUNT,   // reduces price
        SURCHARGE   // increases price
    }

    private final Type   adjustmentType;
    private final double amount;      // absolute amount (0 if percentage-only)
    private final double percentage;  // 0.10 = 10%; 0 if amount-only
    private final String reason;

    public PriceAdjustment(Type adjustmentType, double amount, double percentage, String reason) {
        this.adjustmentType = adjustmentType;
        this.amount         = amount;
        this.percentage     = percentage;
        this.reason         = reason;
    }

    /** Convenience: percentage-only adjustment. */
    public static PriceAdjustment discount(double percentage, String reason) {
        return new PriceAdjustment(Type.DISCOUNT, 0, percentage, reason);
    }

    /** Convenience: percentage-only surcharge. */
    public static PriceAdjustment surcharge(double percentage, String reason) {
        return new PriceAdjustment(Type.SURCHARGE, 0, percentage, reason);
    }

    /**
     * Computes the net delta to apply to the current price.
     * Negative delta = discount; positive delta = surcharge.
     *
     * @param currentPrice price before this adjustment
     */
    public double computeDelta(double currentPrice) {
        double delta = amount + (currentPrice * percentage);
        return adjustmentType == Type.DISCOUNT ? -delta : delta;
    }

    public Type   getAdjustmentType() { return adjustmentType; }
    public double getAmount()         { return amount; }
    public double getPercentage()     { return percentage; }
    public String getReason()         { return reason; }

    @Override
    public String toString() {
        return String.format("PriceAdjustment[%s %.0f%% – %s]",
                adjustmentType, percentage * 100, reason);
    }
}
