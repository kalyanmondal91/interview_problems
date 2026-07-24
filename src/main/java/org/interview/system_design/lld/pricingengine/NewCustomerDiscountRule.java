package org.interview.system_design.lld.pricingengine;

/**
 * New-customer discount: −5% on first purchase.
 * Priority 40.
 */
public class NewCustomerDiscountRule implements PricingRule {

    private static final double DISCOUNT_RATE = 0.05;

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.isNewCustomer();
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        ctx.recordRule("NewCustomerDiscountRule");
        return PriceAdjustment.discount(DISCOUNT_RATE, "New customer welcome discount (-5%)");
    }

    @Override
    public int getPriority() { return 40; }
}
