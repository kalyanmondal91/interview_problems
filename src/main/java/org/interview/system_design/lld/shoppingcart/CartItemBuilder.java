package org.interview.system_design.lld.shoppingcart;

import java.util.UUID;

/**
 * Fluent builder for CartItem.
 * Pattern: Builder
 */
public class CartItemBuilder {

    private String         itemId          = UUID.randomUUID().toString();
    private Product        product;
    private int            quantity        = 1;
    private double         unitPrice;
    private double         discountPercent = 0.0;
    private CartItemStatus status          = CartItemStatus.ACTIVE;

    public CartItemBuilder itemId(String id)               { this.itemId = id;               return this; }
    public CartItemBuilder product(Product p)              { this.product = p;
                                                             this.unitPrice = p.getPrice();   return this; }
    public CartItemBuilder quantity(int qty)               { this.quantity = qty;             return this; }
    public CartItemBuilder unitPrice(double price)         { this.unitPrice = price;          return this; }
    public CartItemBuilder discountPercent(double pct)     { this.discountPercent = pct;      return this; }
    public CartItemBuilder status(CartItemStatus status)   { this.status = status;            return this; }

    public CartItem build() {
        if (product == null) throw new IllegalStateException("product is required");
        if (quantity <= 0)   throw new IllegalStateException("quantity must be > 0");
        return new CartItem(itemId, product, quantity, unitPrice, discountPercent, status);
    }
}
