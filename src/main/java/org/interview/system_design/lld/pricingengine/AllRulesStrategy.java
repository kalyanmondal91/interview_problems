package org.interview.system_design.lld.pricingengine;

import java.util.Comparator;
import java.util.List;

/**
 * Applies ALL applicable rules sorted by priority (ascending).
 * Each rule's delta is applied sequentially to the running price.
 * Pattern: Strategy (concrete)
 */
public class AllRulesStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(PricingContext context, List<PricingRule> rules) {
        double price = context.getBasePrice();

        List<PricingRule> sorted = rules.stream()
                .filter(r -> r.applies(context))
                .sorted(Comparator.comparingInt(PricingRule::getPriority))
                .toList();

        for (PricingRule rule : sorted) {
            PriceAdjustment adj = rule.apply(context);
            double delta = adj.computeDelta(price);
            System.out.printf("[AllRulesStrategy] %s → delta=%.2f (price: %.2f → %.2f)%n",
                    adj.getReason(), delta, price, price + delta);
            price += delta;
            if (price < 0) price = 0; // floor at zero
        }

        return price;
    }
}
