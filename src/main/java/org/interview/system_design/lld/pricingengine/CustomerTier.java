package org.interview.system_design.lld.pricingengine;

/**
 * Customer loyalty tiers, each with a base discount rate for loyalty pricing.
 */
public enum CustomerTier {
    BRONZE(0.02),    //  2% loyalty discount
    SILVER(0.05),    //  5% loyalty discount
    GOLD(0.10),      // 10% loyalty discount
    PLATINUM(0.15);  // 15% loyalty discount

    private final double discountRate;

    CustomerTier(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
