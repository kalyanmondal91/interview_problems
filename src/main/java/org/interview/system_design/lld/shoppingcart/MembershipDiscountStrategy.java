package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Applies a discount based on membership tier.
 * <p>Design Pattern: Strategy</p>
 */
public class MembershipDiscountStrategy implements DiscountStrategy {

    public enum MembershipTier {
        BASIC(0.0),
        SILVER(0.05),
        GOLD(0.10),
        PLATINUM(0.15);

        private final double discountRate;

        MembershipTier(double discountRate) {
            this.discountRate = discountRate;
        }

        public double getDiscountRate() {
            return discountRate;
        }
    }

    private final MembershipTier tier;

    public MembershipDiscountStrategy(MembershipTier tier) {
        this.tier = tier;
    }

    @Override
    public double applyDiscount(List<CartItem> items) {
        double subtotal = items.stream()
                .filter(i -> i.getStatus() == CartItemStatus.ACTIVE)
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();
        return Math.round(subtotal * tier.getDiscountRate() * 100.0) / 100.0;
    }

    @Override
    public String getDescription() {
        return tier.name() + " membership " + (tier.getDiscountRate() * 100) + "% discount";
    }
}
