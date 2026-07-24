package org.interview.system_design.lld.shoppingcart;

/**
 * Represents one line item in a shopping cart.
 * Constructed exclusively via {@link CartItemBuilder}.
 * Pattern: Builder
 */
public class CartItem {

    private final String         itemId;
    private final Product        product;
    private       int            quantity;
    private final double         unitPrice;
    private       double         discountPercent; // 0–100
    private       CartItemStatus status;

    // Package-private: only CartItemBuilder should instantiate
    CartItem(String itemId, Product product, int quantity,
             double unitPrice, double discountPercent, CartItemStatus status) {
        this.itemId          = itemId;
        this.product         = product;
        this.quantity        = quantity;
        this.unitPrice       = unitPrice;
        this.discountPercent = discountPercent;
        this.status          = status;
    }

    public String         getItemId()          { return itemId; }
    public Product        getProduct()         { return product; }
    public int            getQuantity()        { return quantity; }
    public double         getUnitPrice()       { return unitPrice; }
    public double         getDiscountPercent() { return discountPercent; }
    public CartItemStatus getStatus()          { return status; }

    public void setQuantity(int quantity)             { this.quantity = quantity; }
    public void setDiscountPercent(double pct)        { this.discountPercent = pct; }
    public void setStatus(CartItemStatus status)      { this.status = status; }

    /** Effective price per unit after discount. */
    public double getEffectiveUnitPrice() {
        return unitPrice * (1 - discountPercent / 100.0);
    }

    /** Total line price: effective unit price × quantity. */
    public double getLineTotal() {
        return getEffectiveUnitPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("CartItem[%s] %s qty=%d unit=%.2f disc=%.0f%% line=%.2f status=%s",
                itemId, product.getName(), quantity, unitPrice, discountPercent, getLineTotal(), status);
    }
}
