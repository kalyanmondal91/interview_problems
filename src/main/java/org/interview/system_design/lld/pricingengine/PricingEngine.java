package org.interview.system_design.lld.pricingengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entry point for the pricing engine.
 * Manages a registry of PricingRules and the active PricingStrategy.
 * Calculates final prices on demand via FinalPriceCalculator.
 *
 * Patterns: Rules Engine + Strategy + Chain of Responsibility
 */
public class PricingEngine {

    private final List<PricingRule> rules = new ArrayList<>();
    private PricingStrategy strategy;
    private PricingChain    preProcessingChain; // optional

    /** Creates the engine with the AllRulesStrategy as default. */
    public PricingEngine() {
        this.strategy = new AllRulesStrategy();
    }

    // ---- Configuration ----

    /** Registers a pricing rule into the engine. */
    public void registerRule(PricingRule rule) {
        rules.add(rule);
        System.out.printf("[PricingEngine] Registered rule: %s (priority=%d)%n",
                rule.getClass().getSimpleName(), rule.getPriority());
    }

    /** Replaces the active pricing strategy. */
    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    /** Sets an optional chain of pre-processors to run before rules evaluation. */
    public void setPreProcessingChain(PricingChain chain) {
        this.preProcessingChain = chain;
    }

    // ---- Calculation ----

    /**
     * Calculates the final price for the given context using the registered rules and strategy.
     *
     * @param context pricing context (product, customer, quantity, etc.)
     * @return itemized price breakdown
     */
    public FinalPriceCalculator.PriceBreakdown calculateFinalPrice(PricingContext context) {
        FinalPriceCalculator calculator =
                new FinalPriceCalculator(strategy, preProcessingChain);
        FinalPriceCalculator.PriceBreakdown breakdown = calculator.calculate(context, rules);
        System.out.println("[PricingEngine] " + breakdown);
        return breakdown;
    }

    /** Returns an unmodifiable view of the registered rules. */
    public List<PricingRule> getRules() {
        return Collections.unmodifiableList(rules);
    }
}
