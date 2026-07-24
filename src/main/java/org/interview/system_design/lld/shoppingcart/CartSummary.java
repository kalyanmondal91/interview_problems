package org.interview.system_design.lld.shoppingcart;

/**
 * Immutable snapshot of cart totals.
 */
public class CartSummary {

    private final int itemCount;
    private final double subtotal;
    private final double discountAmount;
    private final double tax;
    private final double total;

    public CartSummary(int itemCount, double subtotal, double discountAmount, double tax, double total) {
        this.itemCount = itemCount;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.tax = tax;
        this.total = total;
    }

    public int getItemCount() { return itemCount; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTax() { return tax; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return String.format(
                "CartSummary{items=%d, subtotal=%.2f, discount=%.2f, tax=%.2f, total=%.2f}",
                itemCount, subtotal, discountAmount, tax, total);
    }
}
