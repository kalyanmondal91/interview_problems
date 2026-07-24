package org.interview.system_design.lld.pricingengine;

import java.util.List;

/**
 * Demo runner for the Pricing Engine LLD.
 * Patterns: Rules Engine, Strategy (AllRules vs FirstMatch), Chain
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // -- Build engine with all rules --
        PricingEngine engine = new PricingEngine();
        engine.registerRule(new BulkDiscountRule());
        engine.registerRule(new LoyaltyDiscountRule());
        engine.registerRule(new PeakHourSurchargeRule());
        engine.registerRule(new NewCustomerDiscountRule());
        engine.registerRule(new CouponCodeRule());
        engine.registerRule(new CategorySurchargeRule());

        // -- Scenario 1: GOLD customer, qty=15, ELECTRONICS --
        banner("Scenario 1: GOLD customer, qty=15, ELECTRONICS -- multiple rules apply");
        engine.setStrategy(new AllRulesStrategy());
        PricingContext ctx1 = new PricingContext(
                "SKU-001", 100.0, ProductCategory.ELECTRONICS, CustomerTier.GOLD,
                15, null, false, false);

        FinalPriceCalculator.PriceBreakdown bd1 = engine.calculateFinalPrice(ctx1);
        System.out.printf("  Base price (per unit): $%.2f%n", bd1.getBaseUnitPrice());
        List<String> rules1 = bd1.getAppliedRules();
        if (rules1.isEmpty()) {
            System.out.println("  No rules applied.");
        } else {
            rules1.forEach(r -> System.out.printf("  Applied rule: %s%n", r));
        }
        System.out.printf("  Final unit price: $%.2f | Qty: %d | Total: $%.2f%n",
                bd1.getFinalUnitPrice(), bd1.getQuantity(), bd1.getTotalPrice());

        // -- Scenario 2: New customer with coupon SAVE20 --
        banner("Scenario 2: New customer + coupon SAVE20");
        PricingContext ctx2 = new PricingContext(
                "SKU-002", 80.0, ProductCategory.CLOTHING, CustomerTier.BRONZE,
                1, "SAVE20", true, false);
        FinalPriceCalculator.PriceBreakdown bd2 = engine.calculateFinalPrice(ctx2);
        System.out.printf("  Base: $%.2f | Final unit: $%.2f | Total: $%.2f%n",
                bd2.getBaseUnitPrice(), bd2.getFinalUnitPrice(), bd2.getTotalPrice());
        bd2.getAppliedRules().forEach(r -> System.out.printf("  Applied: %s%n", r));

        // -- Scenario 3: Peak hour LUXURY item --
        banner("Scenario 3: Peak hour LUXURY item -- surcharges stack");
        PricingContext ctx3 = new PricingContext(
                "SKU-003", 500.0, ProductCategory.LUXURY, CustomerTier.SILVER,
                1, null, false, true);
        FinalPriceCalculator.PriceBreakdown bd3 = engine.calculateFinalPrice(ctx3);
        System.out.printf("  Base: $%.2f -> Final: $%.2f%n", bd3.getBaseUnitPrice(), bd3.getFinalUnitPrice());
        bd3.getAppliedRules().forEach(r -> System.out.printf("  Applied: %s%n", r));

        // -- Scenario 4: AllRules vs FirstMatch on same context --
        banner("Scenario 4: AllRulesStrategy vs FirstMatchStrategy -- same context");
        PricingContext ctx4 = new PricingContext(
                "SKU-004", 200.0, ProductCategory.BOOKS, CustomerTier.GOLD,
                25, "SAVE10", true, false);

        engine.setStrategy(new AllRulesStrategy());
        FinalPriceCalculator.PriceBreakdown allRules = engine.calculateFinalPrice(ctx4);

        engine.setStrategy(new FirstMatchStrategy());
        FinalPriceCalculator.PriceBreakdown firstMatch = engine.calculateFinalPrice(ctx4);

        System.out.printf("  AllRules  final: $%.2f (applied %d rules)%n",
                allRules.getFinalUnitPrice(), allRules.getAppliedRules().size());
        System.out.printf("  FirstMatch final: $%.2f (applied %d rule)%n",
                firstMatch.getFinalUnitPrice(), firstMatch.getAppliedRules().size());

        // -- Scenario 5: PLATINUM customer, bulk 100 items --
        banner("Scenario 5: PLATINUM customer, qty=100 -- maximum discounts");
        engine.setStrategy(new AllRulesStrategy());
        PricingContext ctx5 = new PricingContext(
                "SKU-005", 50.0, ProductCategory.FOOD, CustomerTier.PLATINUM,
                100, null, false, false);
        FinalPriceCalculator.PriceBreakdown bd5 = engine.calculateFinalPrice(ctx5);
        System.out.printf("  Base unit: $%.2f | Final unit: $%.2f | Qty: %d | TOTAL: $%.2f%n",
                bd5.getBaseUnitPrice(), bd5.getFinalUnitPrice(), bd5.getQuantity(), bd5.getTotalPrice());
        bd5.getAppliedRules().forEach(r -> System.out.printf("  Applied: %s%n", r));

        // -- Scenario 6: Invalid coupon --
        banner("Scenario 6: Invalid coupon code -- no discount applied");
        PricingContext ctx6 = new PricingContext(
                "SKU-006", 60.0, ProductCategory.ELECTRONICS, CustomerTier.BRONZE,
                1, "INVALID_CODE", false, false);
        engine.setStrategy(new AllRulesStrategy());
        FinalPriceCalculator.PriceBreakdown bd6 = engine.calculateFinalPrice(ctx6);
        System.out.printf("  Base: $%.2f -> Final: $%.2f (coupon not applied)%n",
                bd6.getBaseUnitPrice(), bd6.getFinalUnitPrice());

        banner("Pricing Engine Demo Complete");
    }
}
