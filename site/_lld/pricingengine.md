---
layout: lld
render_with_liquid: false
title: "Pricing Engine"
system: pricingengine
description: "LLD of Pricing Engine"
files:
  - "AllRulesStrategy.java"
  - "BulkDiscountRule.java"
  - "CategorySurchargeRule.java"
  - "CouponCodeRule.java"
  - "CustomerTier.java"
  - "FinalPriceCalculator.java"
  - "FirstMatchStrategy.java"
  - "LoyaltyDiscountRule.java"
  - "Main.java"
  - "NewCustomerDiscountRule.java"
  - "PeakHourSurchargeRule.java"
  - "PriceAdjustment.java"
  - "PriceCapStrategy.java"
  - "PricingChain.java"
  - "PricingContext.java"
  - "PricingEngine.java"
  - "PricingRule.java"
  - "PricingStrategy.java"
  - "ProductCategory.java"
---

## Pricing Engine

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="AllRulesStrategy.java">AllRulesStrategy.java</button>
<button class="tab-btn" data-tab="BulkDiscountRule.java">BulkDiscountRule.java</button>
<button class="tab-btn" data-tab="CategorySurchargeRule.java">CategorySurchargeRule.java</button>
<button class="tab-btn" data-tab="CouponCodeRule.java">CouponCodeRule.java</button>
<button class="tab-btn" data-tab="CustomerTier.java">CustomerTier.java</button>
<button class="tab-btn" data-tab="FinalPriceCalculator.java">FinalPriceCalculator.java</button>
<button class="tab-btn" data-tab="FirstMatchStrategy.java">FirstMatchStrategy.java</button>
<button class="tab-btn" data-tab="LoyaltyDiscountRule.java">LoyaltyDiscountRule.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="NewCustomerDiscountRule.java">NewCustomerDiscountRule.java</button>
<button class="tab-btn" data-tab="PeakHourSurchargeRule.java">PeakHourSurchargeRule.java</button>
<button class="tab-btn" data-tab="PriceAdjustment.java">PriceAdjustment.java</button>
<button class="tab-btn" data-tab="PriceCapStrategy.java">PriceCapStrategy.java</button>
<button class="tab-btn" data-tab="PricingChain.java">PricingChain.java</button>
<button class="tab-btn" data-tab="PricingContext.java">PricingContext.java</button>
<button class="tab-btn" data-tab="PricingEngine.java">PricingEngine.java</button>
<button class="tab-btn" data-tab="PricingRule.java">PricingRule.java</button>
<button class="tab-btn" data-tab="PricingStrategy.java">PricingStrategy.java</button>
<button class="tab-btn" data-tab="ProductCategory.java">ProductCategory.java</button>
</div>
<div class="tab-content active" id="AllRulesStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

import java.util.Comparator;
import java.util.List;

/**
 * Applies ALL applicable rules sorted by priority (ascending).
 * Each rule&#x27;s delta is applied sequentially to the running price.
 * Pattern: Strategy (concrete)
 */
public class AllRulesStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(PricingContext context, List&lt;PricingRule&gt; rules) {
        double price = context.getBasePrice();

        List&lt;PricingRule&gt; sorted = rules.stream()
                .filter(r -&gt; r.applies(context))
                .sorted(Comparator.comparingInt(PricingRule::getPriority))
                .toList();

        for (PricingRule rule : sorted) {
            PriceAdjustment adj = rule.apply(context);
            double delta = adj.computeDelta(price);
            System.out.printf(&quot;[AllRulesStrategy] %s → delta=%.2f (price: %.2f → %.2f)%n&quot;,
                    adj.getReason(), delta, price, price + delta);
            price += delta;
            if (price &lt; 0) price = 0; // floor at zero
        }

        return price;
    }
}</code></pre>
</div>
<div class="tab-content" id="BulkDiscountRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * Bulk discount rule:
 *   qty &gt;= 50 → 20% discount
 *   qty &gt;= 10 → 10% discount
 * Priority 10 (high).
 */
public class BulkDiscountRule implements PricingRule {

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.getQuantity() &gt;= 10;
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = ctx.getQuantity() &gt;= 50 ? 0.20 : 0.10;
        String reason = String.format(&quot;Bulk discount (%d units → %.0f%%)&quot;, ctx.getQuantity(), rate * 100);
        ctx.recordRule(&quot;BulkDiscountRule&quot;);
        return PriceAdjustment.discount(rate, reason);
    }

    @Override
    public int getPriority() { return 10; }
}</code></pre>
</div>
<div class="tab-content" id="CategorySurchargeRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

import java.util.HashMap;
import java.util.Map;

/**
 * Category-specific surcharge:
 *   LUXURY     → +10%
 *   ELECTRONICS → +5%
 * Priority 60 (applied after most discounts).
 */
public class CategorySurchargeRule implements PricingRule {

    private static final Map&lt;ProductCategory, Double&gt; SURCHARGES = new HashMap&lt;&gt;();

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
        ctx.recordRule(&quot;CategorySurchargeRule&quot;);
        return PriceAdjustment.surcharge(rate,
                ctx.getCategory() + &quot; category surcharge (+&quot; + (int)(rate * 100) + &quot;%)&quot;);
    }

    @Override
    public int getPriority() { return 60; }
}</code></pre>
</div>
<div class="tab-content" id="CouponCodeRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

import java.util.HashMap;
import java.util.Map;

/**
 * Applies hardcoded coupon code discounts.
 * Supported codes: SAVE10 (10%), SAVE20 (20%).
 * Priority 50.
 */
public class CouponCodeRule implements PricingRule {

    private static final Map&lt;String, Double&gt; COUPONS = new HashMap&lt;&gt;();

    static {
        COUPONS.put(&quot;SAVE10&quot;, 0.10);
        COUPONS.put(&quot;SAVE20&quot;, 0.20);
    }

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.getCouponCode() != null
                &amp;&amp; COUPONS.containsKey(ctx.getCouponCode().toUpperCase());
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = COUPONS.get(ctx.getCouponCode().toUpperCase());
        ctx.recordRule(&quot;CouponCodeRule&quot;);
        return PriceAdjustment.discount(rate,
                &quot;Coupon &quot; + ctx.getCouponCode().toUpperCase() + &quot; (-&quot; + (int)(rate * 100) + &quot;%)&quot;);
    }

    @Override
    public int getPriority() { return 50; }
}</code></pre>
</div>
<div class="tab-content" id="CustomerTier-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * Customer loyalty tiers, each with a base discount rate for loyalty pricing.
 */
public enum CustomerTier {
    BRONZE(0.02),    //  2% loyalty discount
    SILVER(0.05),    //  5% loyalty discount
    GOLD(0.10),      // 10% loyalty discount
    PLATINUM(0.15);  // 15% loyalty discount

    private final double discountRate;

    CustomerTier(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}</code></pre>
</div>
<div class="tab-content" id="FinalPriceCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

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
    public PriceBreakdown calculate(PricingContext context, List&lt;PricingRule&gt; rules) {
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
                new ArrayList&lt;&gt;(context.getAppliedRules())
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
        private final List&lt;String&gt; appliedRules;

        PriceBreakdown(String productId, double baseUnitPrice, double finalUnitPrice,
                       int quantity, double totalPrice, List&lt;String&gt; appliedRules) {
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
        public List&lt;String&gt; getAppliedRules()  { return appliedRules; }

        @Override
        public String toString() {
            return String.format(
                    &quot;PriceBreakdown[product=%s, base=%.2f, unit=%.2f, qty=%d, total=%.2f, rules=%s]&quot;,
                    productId, baseUnitPrice, finalUnitPrice, quantity, totalPrice, appliedRules);
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="FirstMatchStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

import java.util.Comparator;
import java.util.List;

/**
 * Applies only the single highest-priority matching rule.
 * Useful for exclusive promotions where only one discount is honoured.
 * Pattern: Strategy (concrete)
 */
public class FirstMatchStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(PricingContext context, List&lt;PricingRule&gt; rules) {
        double price = context.getBasePrice();

        PricingRule best = rules.stream()
                .filter(r -&gt; r.applies(context))
                .min(Comparator.comparingInt(PricingRule::getPriority))
                .orElse(null);

        if (best != null) {
            PriceAdjustment adj = best.apply(context);
            double delta = adj.computeDelta(price);
            System.out.printf(&quot;[FirstMatchStrategy] %s → delta=%.2f%n&quot;, adj.getReason(), delta);
            price += delta;
            if (price &lt; 0) price = 0;
        }

        return price;
    }
}</code></pre>
</div>
<div class="tab-content" id="LoyaltyDiscountRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * Loyalty discount based on CustomerTier discount rate.
 * Priority 20.
 */
public class LoyaltyDiscountRule implements PricingRule {

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.getCustomerTier() != null;
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        double rate = ctx.getCustomerTier().getDiscountRate();
        String reason = String.format(&quot;Loyalty discount (%s tier → %.0f%%)&quot;,
                ctx.getCustomerTier(), rate * 100);
        ctx.recordRule(&quot;LoyaltyDiscountRule&quot;);
        return PriceAdjustment.discount(rate, reason);
    }

    @Override
    public int getPriority() { return 20; }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

import java.util.List;

/**
 * Demo runner for the Pricing Engine LLD.
 * Patterns: Rules Engine, Strategy (AllRules vs FirstMatch), Chain
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
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
        banner(&quot;Scenario 1: GOLD customer, qty=15, ELECTRONICS -- multiple rules apply&quot;);
        engine.setStrategy(new AllRulesStrategy());
        PricingContext ctx1 = new PricingContext(
                &quot;SKU-001&quot;, 100.0, ProductCategory.ELECTRONICS, CustomerTier.GOLD,
                15, null, false, false);

        FinalPriceCalculator.PriceBreakdown bd1 = engine.calculateFinalPrice(ctx1);
        System.out.printf(&quot;  Base price (per unit): $%.2f%n&quot;, bd1.getBaseUnitPrice());
        List&lt;String&gt; rules1 = bd1.getAppliedRules();
        if (rules1.isEmpty()) {
            System.out.println(&quot;  No rules applied.&quot;);
        } else {
            rules1.forEach(r -&gt; System.out.printf(&quot;  Applied rule: %s%n&quot;, r));
        }
        System.out.printf(&quot;  Final unit price: $%.2f | Qty: %d | Total: $%.2f%n&quot;,
                bd1.getFinalUnitPrice(), bd1.getQuantity(), bd1.getTotalPrice());

        // -- Scenario 2: New customer with coupon SAVE20 --
        banner(&quot;Scenario 2: New customer + coupon SAVE20&quot;);
        PricingContext ctx2 = new PricingContext(
                &quot;SKU-002&quot;, 80.0, ProductCategory.CLOTHING, CustomerTier.BRONZE,
                1, &quot;SAVE20&quot;, true, false);
        FinalPriceCalculator.PriceBreakdown bd2 = engine.calculateFinalPrice(ctx2);
        System.out.printf(&quot;  Base: $%.2f | Final unit: $%.2f | Total: $%.2f%n&quot;,
                bd2.getBaseUnitPrice(), bd2.getFinalUnitPrice(), bd2.getTotalPrice());
        bd2.getAppliedRules().forEach(r -&gt; System.out.printf(&quot;  Applied: %s%n&quot;, r));

        // -- Scenario 3: Peak hour LUXURY item --
        banner(&quot;Scenario 3: Peak hour LUXURY item -- surcharges stack&quot;);
        PricingContext ctx3 = new PricingContext(
                &quot;SKU-003&quot;, 500.0, ProductCategory.LUXURY, CustomerTier.SILVER,
                1, null, false, true);
        FinalPriceCalculator.PriceBreakdown bd3 = engine.calculateFinalPrice(ctx3);
        System.out.printf(&quot;  Base: $%.2f -&gt; Final: $%.2f%n&quot;, bd3.getBaseUnitPrice(), bd3.getFinalUnitPrice());
        bd3.getAppliedRules().forEach(r -&gt; System.out.printf(&quot;  Applied: %s%n&quot;, r));

        // -- Scenario 4: AllRules vs FirstMatch on same context --
        banner(&quot;Scenario 4: AllRulesStrategy vs FirstMatchStrategy -- same context&quot;);
        PricingContext ctx4 = new PricingContext(
                &quot;SKU-004&quot;, 200.0, ProductCategory.BOOKS, CustomerTier.GOLD,
                25, &quot;SAVE10&quot;, true, false);

        engine.setStrategy(new AllRulesStrategy());
        FinalPriceCalculator.PriceBreakdown allRules = engine.calculateFinalPrice(ctx4);

        engine.setStrategy(new FirstMatchStrategy());
        FinalPriceCalculator.PriceBreakdown firstMatch = engine.calculateFinalPrice(ctx4);

        System.out.printf(&quot;  AllRules  final: $%.2f (applied %d rules)%n&quot;,
                allRules.getFinalUnitPrice(), allRules.getAppliedRules().size());
        System.out.printf(&quot;  FirstMatch final: $%.2f (applied %d rule)%n&quot;,
                firstMatch.getFinalUnitPrice(), firstMatch.getAppliedRules().size());

        // -- Scenario 5: PLATINUM customer, bulk 100 items --
        banner(&quot;Scenario 5: PLATINUM customer, qty=100 -- maximum discounts&quot;);
        engine.setStrategy(new AllRulesStrategy());
        PricingContext ctx5 = new PricingContext(
                &quot;SKU-005&quot;, 50.0, ProductCategory.FOOD, CustomerTier.PLATINUM,
                100, null, false, false);
        FinalPriceCalculator.PriceBreakdown bd5 = engine.calculateFinalPrice(ctx5);
        System.out.printf(&quot;  Base unit: $%.2f | Final unit: $%.2f | Qty: %d | TOTAL: $%.2f%n&quot;,
                bd5.getBaseUnitPrice(), bd5.getFinalUnitPrice(), bd5.getQuantity(), bd5.getTotalPrice());
        bd5.getAppliedRules().forEach(r -&gt; System.out.printf(&quot;  Applied: %s%n&quot;, r));

        // -- Scenario 6: Invalid coupon --
        banner(&quot;Scenario 6: Invalid coupon code -- no discount applied&quot;);
        PricingContext ctx6 = new PricingContext(
                &quot;SKU-006&quot;, 60.0, ProductCategory.ELECTRONICS, CustomerTier.BRONZE,
                1, &quot;INVALID_CODE&quot;, false, false);
        engine.setStrategy(new AllRulesStrategy());
        FinalPriceCalculator.PriceBreakdown bd6 = engine.calculateFinalPrice(ctx6);
        System.out.printf(&quot;  Base: $%.2f -&gt; Final: $%.2f (coupon not applied)%n&quot;,
                bd6.getBaseUnitPrice(), bd6.getFinalUnitPrice());

        banner(&quot;Pricing Engine Demo Complete&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="NewCustomerDiscountRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * New-customer discount: −5% on first purchase.
 * Priority 40.
 */
public class NewCustomerDiscountRule implements PricingRule {

    private static final double DISCOUNT_RATE = 0.05;

    @Override
    public boolean applies(PricingContext ctx) {
        return ctx.isNewCustomer();
    }

    @Override
    public PriceAdjustment apply(PricingContext ctx) {
        ctx.recordRule(&quot;NewCustomerDiscountRule&quot;);
        return PriceAdjustment.discount(DISCOUNT_RATE, &quot;New customer welcome discount (-5%)&quot;);
    }

    @Override
    public int getPriority() { return 40; }
}</code></pre>
</div>
<div class="tab-content" id="PeakHourSurchargeRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

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
        ctx.recordRule(&quot;PeakHourSurchargeRule&quot;);
        return PriceAdjustment.surcharge(SURCHARGE_RATE, &quot;Peak hour surcharge (+15%)&quot;);
    }

    @Override
    public int getPriority() { return 30; }
}</code></pre>
</div>
<div class="tab-content" id="PriceAdjustment-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * Represents a single price modification produced by a pricing rule.
 * Immutable value object.
 */
public class PriceAdjustment {

    public enum Type {
        DISCOUNT,   // reduces price
        SURCHARGE   // increases price
    }

    private final Type   adjustmentType;
    private final double amount;      // absolute amount (0 if percentage-only)
    private final double percentage;  // 0.10 = 10%; 0 if amount-only
    private final String reason;

    public PriceAdjustment(Type adjustmentType, double amount, double percentage, String reason) {
        this.adjustmentType = adjustmentType;
        this.amount         = amount;
        this.percentage     = percentage;
        this.reason         = reason;
    }

    /** Convenience: percentage-only adjustment. */
    public static PriceAdjustment discount(double percentage, String reason) {
        return new PriceAdjustment(Type.DISCOUNT, 0, percentage, reason);
    }

    /** Convenience: percentage-only surcharge. */
    public static PriceAdjustment surcharge(double percentage, String reason) {
        return new PriceAdjustment(Type.SURCHARGE, 0, percentage, reason);
    }

    /**
     * Computes the net delta to apply to the current price.
     * Negative delta = discount; positive delta = surcharge.
     *
     * @param currentPrice price before this adjustment
     */
    public double computeDelta(double currentPrice) {
        double delta = amount + (currentPrice * percentage);
        return adjustmentType == Type.DISCOUNT ? -delta : delta;
    }

    public Type   getAdjustmentType() { return adjustmentType; }
    public double getAmount()         { return amount; }
    public double getPercentage()     { return percentage; }
    public String getReason()         { return reason; }

    @Override
    public String toString() {
        return String.format(&quot;PriceAdjustment[%s %.0f%% – %s]&quot;,
                adjustmentType, percentage * 100, reason);
    }
}</code></pre>
</div>
<div class="tab-content" id="PriceCapStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

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
        if (minPrice &gt; maxPrice) throw new IllegalArgumentException(&quot;minPrice must be &lt;= maxPrice&quot;);
        this.inner    = inner;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public double calculatePrice(PricingContext context, List&lt;PricingRule&gt; rules) {
        double price = inner.calculatePrice(context, rules);
        if (price &lt; minPrice) {
            System.out.printf(&quot;[PriceCapStrategy] Price %.2f below min %.2f — capped%n&quot;, price, minPrice);
            return minPrice;
        }
        if (price &gt; maxPrice) {
            System.out.printf(&quot;[PriceCapStrategy] Price %.2f above max %.2f — capped%n&quot;, price, maxPrice);
            return maxPrice;
        }
        return price;
    }
}</code></pre>
</div>
<div class="tab-content" id="PricingChain-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * Chain-of-Responsibility handler for pre/post-processing pricing context.
 * Handlers can enrich the context (e.g., set flags) before rules are evaluated,
 * or perform post-processing (e.g., rounding, currency conversion).
 * Pattern: Chain of Responsibility
 */
public abstract class PricingChain {

    private PricingChain next;

    /** Links the next handler; returns next for fluent chaining. */
    public PricingChain setNext(PricingChain next) {
        this.next = next;
        return next;
    }

    /**
     * Processes the context, optionally modifying it, then delegates to the next handler.
     *
     * @param context the mutable pricing context
     */
    public final void process(PricingContext context) {
        doProcess(context);
        if (next != null) next.process(context);
    }

    /** Subclasses implement their specific processing step here. */
    protected abstract void doProcess(PricingContext context);
}</code></pre>
</div>
<div class="tab-content" id="PricingContext-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable context object passed through the pricing rules chain.
 * Rules read inputs (basePrice, quantity, etc.) and record their decisions
 * into the appliedRules list for auditability.
 */
public class PricingContext {

    private final String          productId;
    private final double          basePrice;
    private final ProductCategory category;
    private final CustomerTier    customerTier;
    private final int             quantity;
    private final String          couponCode;     // nullable
    private final boolean         isNewCustomer;
    private final boolean         isPeakHour;

    /** Audit trail: names of all rules that were applied. */
    private final List&lt;String&gt; appliedRules = new ArrayList&lt;&gt;();

    public PricingContext(String productId, double basePrice,
                          ProductCategory category, CustomerTier customerTier,
                          int quantity, String couponCode,
                          boolean isNewCustomer, boolean isPeakHour) {
        this.productId    = productId;
        this.basePrice    = basePrice;
        this.category     = category;
        this.customerTier = customerTier;
        this.quantity     = quantity;
        this.couponCode   = couponCode;
        this.isNewCustomer = isNewCustomer;
        this.isPeakHour   = isPeakHour;
    }

    public String          getProductId()    { return productId; }
    public double          getBasePrice()    { return basePrice; }
    public ProductCategory getCategory()     { return category; }
    public CustomerTier    getCustomerTier() { return customerTier; }
    public int             getQuantity()     { return quantity; }
    public String          getCouponCode()   { return couponCode; }
    public boolean         isNewCustomer()  { return isNewCustomer; }
    public boolean         isPeakHour()     { return isPeakHour; }
    public List&lt;String&gt;    getAppliedRules() { return appliedRules; }

    /** Called by a rule when it successfully applies itself. */
    public void recordRule(String ruleName) {
        appliedRules.add(ruleName);
    }

    @Override
    public String toString() {
        return String.format(
                &quot;PricingContext[product=%s, base=%.2f, cat=%s, tier=%s, qty=%d, peak=%s, new=%s]&quot;,
                productId, basePrice, category, customerTier, quantity, isPeakHour, isNewCustomer);
    }
}</code></pre>
</div>
<div class="tab-content" id="PricingEngine-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

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

    private final List&lt;PricingRule&gt; rules = new ArrayList&lt;&gt;();
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
        System.out.printf(&quot;[PricingEngine] Registered rule: %s (priority=%d)%n&quot;,
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
        System.out.println(&quot;[PricingEngine] &quot; + breakdown);
        return breakdown;
    }

    /** Returns an unmodifiable view of the registered rules. */
    public List&lt;PricingRule&gt; getRules() {
        return Collections.unmodifiableList(rules);
    }
}</code></pre>
</div>
<div class="tab-content" id="PricingRule-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

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
}</code></pre>
</div>
<div class="tab-content" id="PricingStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

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
     * @return final calculated unit price (&gt;= 0)
     */
    double calculatePrice(PricingContext context, List&lt;PricingRule&gt; rules);
}</code></pre>
</div>
<div class="tab-content" id="ProductCategory-java">
<pre><code class="language-java">package org.interview.system_design.lld.pricingengine;

/**
 * Product categories used in category-specific surcharge rules.
 */
public enum ProductCategory {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    BOOKS,
    FURNITURE,
    LUXURY
}</code></pre>
</div>
</div>
