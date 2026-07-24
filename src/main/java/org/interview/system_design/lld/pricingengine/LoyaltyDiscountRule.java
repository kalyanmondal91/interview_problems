package org.interview.system_design.lld.pricingengine;

/**
 * Loyalty discount based on CustomerTier discount rate.
 * Priority 20.
 */
public class LoyaltyDiscountRule implements PricingRule {

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.getCustomerTier() != null;
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = ctx.getCustomerTier().getDiscountRate();
        String reason = String.format("Loyalty discount (%s tier → %.0f%%)",
                ctx.getCustomerTier(), rate * 100);
        ctx.recordRule("LoyaltyDiscountRule");
        return PriceAdjustment.discount(rate, reason);
    }

    @Override
    public int getPriority() { return 20; }
}
