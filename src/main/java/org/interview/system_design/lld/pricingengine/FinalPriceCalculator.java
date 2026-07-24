package org.interview.system_design.lld.pricingengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Combines a PricingStrategy with an optional PricingChain pre-processor.
 * Returns an itemized breakdown of the final price calculation.
 */
public class FinalPriceCalculator {

    private final PricingStrategy strategy;
    private final PricingChain    preProcessor; // nullable

    public FinalPriceCalculator(PricingStrategy strategy) {
        this(strategy, null);
    }

    public FinalPriceCalculator(PricingStrategy strategy, PricingChain preProcessor) {
        this.strategy     = strategy;
        this.preProcessor = preProcessor;
    }

    /**
     * Calculates the final line-item total and returns a full breakdown.
     *
     * @param context pricing context
     * @param rules   all registered rules
     * @return PriceBreakdown with unit price, applied rules, and total
     */
    public PriceBreakdown calculate(PricingContext context, List<PricingRule> rules) {
        // Step 1 — run pre-processing chain
        if (preProcessor != null) preProcessor.process(context);

        // Step 2 — calculate unit price via strategy
        double unitPrice = strategy.calculatePrice(context, rules);

        // Step 3 — total = unit × quantity
        double total = unitPrice * context.getQuantity();

        return new PriceBreakdown(
                context.getProductId(),
                context.getBasePrice(),
                unitPrice,
                context.getQuantity(),
                total,
                new ArrayList<>(context.getAppliedRules())
        );
    }

    // ---- Breakdown DTO ----

    /** Immutable result of a full price calculation. */
    public static class PriceBreakdown {
        private final String       productId;
        private final double       baseUnitPrice;
        private final double       finalUnitPrice;
        private final int          quantity;
        private final double       totalPrice;
        private final List<String> appliedRules;

        PriceBreakdown(String productId, double baseUnitPrice, double finalUnitPrice,
                       int quantity, double totalPrice, List<String> appliedRules) {
            this.productId      = productId;
            this.baseUnitPrice  = baseUnitPrice;
            this.finalUnitPrice = finalUnitPrice;
            this.quantity       = quantity;
            this.totalPrice     = totalPrice;
            this.appliedRules   = appliedRules;
        }

        public String       getProductId()      { return productId; }
        public double       getBaseUnitPrice()  { return baseUnitPrice; }
        public double       getFinalUnitPrice() { return finalUnitPrice; }
        public int          getQuantity()       { return quantity; }
        public double       getTotalPrice()     { return totalPrice; }
        public List<String> getAppliedRules()  { return appliedRules; }

        @Override
        public String toString() {
            return String.format(
                    "PriceBreakdown[product=%s, base=%.2f, unit=%.2f, qty=%d, total=%.2f, rules=%s]",
                    productId, baseUnitPrice, finalUnitPrice, quantity, totalPrice, appliedRules);
        }
    }
}
