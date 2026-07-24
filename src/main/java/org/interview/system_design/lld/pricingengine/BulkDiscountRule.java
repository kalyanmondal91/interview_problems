package org.interview.system_design.lld.pricingengine;

/**
 * Bulk discount rule:
 *   qty >= 50 → 20% discount
 *   qty >= 10 → 10% discount
 * Priority 10 (high).
 */
public class BulkDiscountRule implements PricingRule {

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.getQuantity() >= 10;
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = ctx.getQuantity() >= 50 ? 0.20 : 0.10;
        String reason = String.format("Bulk discount (%d units → %.0f%%)", ctx.getQuantity(), rate * 100);
        ctx.recordRule("BulkDiscountRule");
        return PriceAdjustment.discount(rate, reason);
    }

    @Override
    public int getPriority() { return 10; }
}
