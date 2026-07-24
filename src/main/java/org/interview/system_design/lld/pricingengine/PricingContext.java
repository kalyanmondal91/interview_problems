package org.interview.system_design.lld.pricingengine;

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
    private final List<String> appliedRules = new ArrayList<>();

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
    public List<String>    getAppliedRules() { return appliedRules; }

    /** Called by a rule when it successfully applies itself. */
    public void recordRule(String ruleName) {
        appliedRules.add(ruleName);
    }

    @Override
    public String toString() {
        return String.format(
                "PricingContext[product=%s, base=%.2f, cat=%s, tier=%s, qty=%d, peak=%s, new=%s]",
                productId, basePrice, category, customerTier, quantity, isPeakHour, isNewCustomer);
    }
}
