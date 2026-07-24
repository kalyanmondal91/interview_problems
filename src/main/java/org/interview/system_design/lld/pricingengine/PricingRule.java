package org.interview.system_design.lld.pricingengine;

/**
 * Interface for a single pricing rule in the rules engine.
 * Each rule reports whether it applies to a given context and, if so,
 * what price adjustment it produces.
 * Pattern: Rules Engine (strategy variant)
 */
public interface PricingRule {

    /**
     * Determines whether this rule should be applied to the given context.
     *
     * @param context current pricing context
     * @return true if the rule is applicable
     */
    boolean applies(PricingContext context);

    /**
     * Computes the price adjustment for the given context.
     * Called only if {@link #applies} returned true.
     *
     * @param context current pricing context
     * @return the price adjustment to apply
     */
    PriceAdjustment apply(PricingContext context);

    /**
     * Priority determines evaluation order.
     * Lower value = higher priority (applied first).
     */
    int getPriority();
}
