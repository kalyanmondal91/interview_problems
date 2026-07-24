package org.interview.system_design.lld.pricingengine;

import java.util.HashMap;
import java.util.Map;

/**
 * Applies hardcoded coupon code discounts.
 * Supported codes: SAVE10 (10%), SAVE20 (20%).
 * Priority 50.
 */
public class CouponCodeRule implements PricingRule {

    private static final Map<String, Double> COUPONS = new HashMap<>();

    static {
        COUPONS.put("SAVE10", 0.10);
        COUPONS.put("SAVE20", 0.20);
    }

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.getCouponCode() != null
                && COUPONS.containsKey(ctx.getCouponCode().toUpperCase());
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = COUPONS.get(ctx.getCouponCode().toUpperCase());
        ctx.recordRule("CouponCodeRule");
        return PriceAdjustment.discount(rate,
                "Coupon " + ctx.getCouponCode().toUpperCase() + " (-" + (int)(rate * 100) + "%)");
    }

    @Override
    public int getPriority() { return 50; }
}
