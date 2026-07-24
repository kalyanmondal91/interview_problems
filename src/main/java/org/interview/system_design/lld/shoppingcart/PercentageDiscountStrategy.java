package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Applies a fixed percentage discount to the cart subtotal.
 * <p>Design Pattern: Strategy</p>
 */
public class PercentageDiscountStrategy implements DiscountStrategy {

    private final double discountPercent;

    public PercentageDiscountStrategy(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be 0-100");
        }
        this.discountPercent = discountPercent;
    }

    @Override
    public double applyDiscount(List<CartItem> items) {
        double subtotal = items.stream()
                .filter(i -> i.getStatus() == CartItemStatus.ACTIVE)
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();
        return Math.round(subtotal * (discountPercent / 100.0) * 100.0) / 100.0;
    }

    @Override
    public String getDescription() {
        return discountPercent + "% off total";
    }
}
