package org.interview.system_design.lld.pricingengine;

import java.util.List;

/**
 * Decorator strategy that wraps another PricingStrategy and enforces min/max price bounds.
 * Pattern: Strategy + Decorator
 */
public class PriceCapStrategy implements PricingStrategy {

    private final PricingStrategy inner;
    private final double          minPrice;
    private final double          maxPrice;

    public PriceCapStrategy(PricingStrategy inner, double minPrice, double maxPrice) {
        if (minPrice > maxPrice) throw new IllegalArgumentException("minPrice must be <= maxPrice");
        this.inner    = inner;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public double calculatePrice(PricingContext context, List<PricingRule> rules) {
        double price = inner.calculatePrice(context, rules);
        if (price < minPrice) {
            System.out.printf("[PriceCapStrategy] Price %.2f below min %.2f — capped%n", price, minPrice);
            return minPrice;
        }
        if (price > maxPrice) {
            System.out.printf("[PriceCapStrategy] Price %.2f above max %.2f — capped%n", price, maxPrice);
            return maxPrice;
        }
        return price;
    }
}
