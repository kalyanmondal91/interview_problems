package org.interview.system_design.lld.pricingengine;

import java.util.Comparator;
import java.util.List;

/**
 * Applies only the single highest-priority matching rule.
 * Useful for exclusive promotions where only one discount is honoured.
 * Pattern: Strategy (concrete)
 */
public class FirstMatchStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(PricingContext context, List<PricingRule> rules) {
        double price = context.getBasePrice();

        PricingRule best = rules.stream()
                .filter(r -> r.applies(context))
                .min(Comparator.comparingInt(PricingRule::getPriority))
                .orElse(null);

        if (best != null) {
            PriceAdjustment adj = best.apply(context);
            double delta = adj.computeDelta(price);
            System.out.printf("[FirstMatchStrategy] %s → delta=%.2f%n", adj.getReason(), delta);
            price += delta;
            if (price < 0) price = 0;
        }

        return price;
    }
}
