package org.interview.system_design.lld.pricingengine;

/**
 * Peak-hour surcharge: +15% during peak demand hours.
 * Priority 30.
 */
public class PeakHourSurchargeRule implements PricingRule {

    private static final double SURCHARGE_RATE = 0.15;

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.isPeakHour();
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        ctx.recordRule("PeakHourSurchargeRule");
        return PriceAdjustment.surcharge(SURCHARGE_RATE, "Peak hour surcharge (+15%)");
    }

    @Override
    public int getPriority() { return 30; }
}
