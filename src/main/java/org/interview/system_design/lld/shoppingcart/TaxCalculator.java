package org.interview.system_design.lld.shoppingcart;

/** Strategy interface for tax calculation. */
public interface TaxCalculator {
    /**
     * Calculates tax on the given subtotal for the specified region.
     *
     * @param subtotal the pre-tax amount
     * @param region   the region/state code for tax rules
     * @return the tax amount
     */
    double calculateTax(double subtotal, String region);
}
