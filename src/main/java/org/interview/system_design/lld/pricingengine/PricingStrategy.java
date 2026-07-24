package org.interview.system_design.lld.pricingengine;

import java.util.List;

/**
 * Strategy interface: given a context and a set of rules, compute the final unit price.
 * Pattern: Strategy
 */
public interface PricingStrategy {

    /**
     * Calculates the final unit price for the given context.
     *
     * @param context pricing context (reads inputs, records applied rules)
     * @param rules   all registered pricing rules
     * @return final calculated unit price (>= 0)
     */
    double calculatePrice(PricingContext context, List<PricingRule> rules);
}
