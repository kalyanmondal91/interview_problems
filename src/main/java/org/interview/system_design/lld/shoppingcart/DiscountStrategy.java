package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Strategy interface for applying discounts to cart items.
 * <p>Design Pattern: Strategy — allows swapping discount algorithms at runtime.</p>
 */
public interface DiscountStrategy {
    /**
     * Calculates the total discount amount for the given cart items.
     *
     * @param items list of active cart items
     * @return the total discount amount (positive value)
     */
    double applyDiscount(List<CartItem> items);

    /** Human-readable description of this strategy. */
    String getDescription();
}
