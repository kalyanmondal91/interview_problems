package org.interview.system_design.lld.shoppingcart;

/**
 * Standard flat-rate tax calculator.
 * Applies 8% tax regardless of region (simplified).
 */
public class StandardTaxCalculator implements TaxCalculator {

    private static final double TAX_RATE = 0.08;

    @Override
    public double calculateTax(double subtotal, String region) {
        return Math.round(subtotal * TAX_RATE * 100.0) / 100.0;
    }
}
