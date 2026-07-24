package org.interview.system_design.lld.shoppingcart;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service layer for cart operations.
 * <p>Design Patterns: Strategy (discount), Observer (cart events), Builder (CartItem)</p>
 */
public class CartService {

    private final Map<String, Cart> carts = new ConcurrentHashMap<>();
    private final TaxCalculator taxCalculator;

    public CartService(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    /** Creates a new active cart for a user. */
    public Cart createCart(String userId, String region) {
        Cart cart = new Cart(userId, taxCalculator, region);
        carts.put(cart.getCartId(), cart);
        return cart;
    }

    public Optional<Cart> getCart(String cartId) {
        return Optional.ofNullable(carts.get(cartId));
    }

    /** Adds a product to the specified cart. */
    public CartItem addToCart(String cartId, Product product, int quantity) {
        Cart cart = getCartOrThrow(cartId);
        CartItem item = new CartItemBuilder()
                .itemId(UUID.randomUUID().toString())
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .build();
        cart.addItem(item);
        return item;
    }

    /** Removes an item from the cart by itemId. */
    public boolean removeFromCart(String cartId, String itemId) {
        Cart cart = getCartOrThrow(cartId);
        return cart.removeItem(itemId);
    }

    /** Updates item quantity; removes if quantity <= 0. */
    public boolean updateQuantity(String cartId, String itemId, int newQuantity) {
        Cart cart = getCartOrThrow(cartId);
        return cart.updateQuantity(itemId, newQuantity);
    }

    /** Sets the discount strategy for the cart. */
    public void applyDiscount(String cartId, DiscountStrategy strategy) {
        getCartOrThrow(cartId).setDiscountStrategy(strategy);
    }

    /** Checks out the cart and returns the final summary. */
    public CartSummary checkout(String cartId) {
        Cart cart = getCartOrThrow(cartId);
        return cart.checkout();
    }

    /**
     * Merges a guest cart into a registered user's cart.
     * Moves all active items from guestCartId into userCartId.
     */
    public void mergeGuestCart(String guestCartId, String userCartId) {
        Cart guestCart = getCartOrThrow(guestCartId);
        Cart userCart = getCartOrThrow(userCartId);
        for (CartItem item : guestCart.getActiveItems()) {
            CartItem merged = new CartItemBuilder()
                    .itemId(UUID.randomUUID().toString())
                    .product(item.getProduct())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .build();
            userCart.addItem(merged);
        }
        guestCart.setStatus(CartStatus.MERGED);
    }

    /** Returns the current cart summary without checking out. */
    public CartSummary getCartSummary(String cartId) {
        return getCartOrThrow(cartId).getSummary();
    }

    private Cart getCartOrThrow(String cartId) {
        Cart cart = carts.get(cartId);
        if (cart == null) throw new IllegalArgumentException("Cart not found: " + cartId);
        return cart;
    }
}
