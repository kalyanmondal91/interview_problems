package org.interview.system_design.lld.pricingengine;

import java.util.HashMap;
import java.util.Map;

/**
 * Category-specific surcharge:
 *   LUXURY     → +10%
 *   ELECTRONICS → +5%
 * Priority 60 (applied after most discounts).
 */
public class CategorySurchargeRule implements PricingRule {

    private static final Map<ProductCategory, Double> SURCHARGES = new HashMap<>();

    static {
        SURCHARGES.put(ProductCategory.LUXURY, 0.10);
        SURCHARGES.put(ProductCategory.ELECTRONICS, 0.05);
    }

    @Override
    public boolean applies(PricingContext ctx) {
        return SURCHARGES.containsKey(ctx.getCategory());
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = SURCHARGES.get(ctx.getCategory());
        ctx.recordRule("CategorySurchargeRule");
        return PriceAdjustment.surcharge(rate,
                ctx.getCategory() + " category surcharge (+" + (int)(rate * 100) + "%)");
    }

    @Override
    public int getPriority() { return 60; }
}
