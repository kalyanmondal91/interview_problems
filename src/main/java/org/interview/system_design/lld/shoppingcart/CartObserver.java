package org.interview.system_design.lld.shoppingcart;

/**
 * Observer interface for cart lifecycle events.
 * <p>Design Pattern: Observer</p>
 */
public interface CartObserver {
    void onItemAdded(CartItem item);
    void onItemRemoved(CartItem item);
    void onCartCheckedOut(Cart cart);
}
