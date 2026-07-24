package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Buy X items, get Y items free (e.g., Buy 2 Get 1 Free).
 * Applied per product line.
 * <p>Design Pattern: Strategy</p>
 */
public class BuyXGetYStrategy implements DiscountStrategy {

    private final int buyX;
    private final int getY;

    public BuyXGetYStrategy(int buyX, int getY) {
        this.buyX = buyX;
        this.getY = getY;
    }

    @Override
    public double applyDiscount(List<CartItem> items) {
        double totalDiscount = 0.0;
        for (CartItem item : items) {
            if (item.getStatus() != CartItemStatus.ACTIVE) continue;
            int qty = item.getQuantity();
            int groups = qty / (buyX + getY);
            int freeItems = groups * getY;
            totalDiscount += freeItems * item.getUnitPrice();
        }
        return Math.round(totalDiscount * 100.0) / 100.0;
    }

    @Override
    public String getDescription() {
        return "Buy " + buyX + " Get " + getY + " Free";
    }
}
