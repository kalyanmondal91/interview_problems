package org.interview.system_design.lld.shoppingcart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Shopping cart with observer support and pluggable discount strategy.
 * <p>Design Patterns: Observer (CartObserver), Strategy (DiscountStrategy)</p>
 */
public class Cart {

    private final String cartId;
    private final String userId;
    private final List<CartItem> items;
    private CartStatus status;
    private final List<CartObserver> observers;
    private DiscountStrategy discountStrategy;
    private final TaxCalculator taxCalculator;
    private final String region;
    private final Instant createdAt;
    private Instant lastModifiedAt;

    public Cart(String userId, TaxCalculator taxCalculator, String region) {
        this.cartId = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = new ArrayList<>();
        this.status = CartStatus.ACTIVE;
        this.observers = new CopyOnWriteArrayList<>();
        this.taxCalculator = taxCalculator;
        this.region = region;
        this.createdAt = Instant.now();
        this.lastModifiedAt = Instant.now();
    }

    // ─── Observer management ────────────────────────────────────────────────

    public void addObserver(CartObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CartObserver observer) {
        observers.remove(observer);
    }

    // ─── Item operations ─────────────────────────────────────────────────────

    public void addItem(CartItem item) {
        ensureActive();
        // Merge with existing item for same product
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getStatus() == CartItemStatus.ACTIVE
                        && i.getProduct().getProductId().equals(item.getProduct().getProductId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        lastModifiedAt = Instant.now();
        observers.forEach(o -> o.onItemAdded(item));
    }

    public boolean removeItem(String itemId) {
        ensureActive();
        Optional<CartItem> found = items.stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst();
        if (found.isPresent()) {
            found.get().setStatus(CartItemStatus.REMOVED);
            lastModifiedAt = Instant.now();
            observers.forEach(o -> o.onItemRemoved(found.get()));
            return true;
        }
        return false;
    }

    public boolean updateQuantity(String itemId, int newQuantity) {
        ensureActive();
        if (newQuantity <= 0) return removeItem(itemId);
        return items.stream()
                .filter(i -> i.getItemId().equals(itemId) && i.getStatus() == CartItemStatus.ACTIVE)
                .findFirst()
                .map(i -> { i.setQuantity(newQuantity); lastModifiedAt = Instant.now(); return true; })
                .orElse(false);
    }

    public void saveForLater(String itemId) {
        items.stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst()
                .ifPresent(i -> i.setStatus(CartItemStatus.SAVED_FOR_LATER));
    }

    // ─── Checkout ─────────────────────────────────────────────────────────────

    public CartSummary checkout() {
        ensureActive();
        CartSummary summary = getSummary();
        this.status = CartStatus.CHECKED_OUT;
        this.lastModifiedAt = Instant.now();
        observers.forEach(o -> o.onCartCheckedOut(this));
        return summary;
    }

    // ─── Summary ──────────────────────────────────────────────────────────────

    public CartSummary getSummary() {
        List<CartItem> activeItems = getActiveItems();
        double subtotal = activeItems.stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();
        double discountAmount = (discountStrategy != null)
                ? discountStrategy.applyDiscount(activeItems)
                : 0.0;
        double discountedSubtotal = Math.max(0, subtotal - discountAmount);
        double tax = taxCalculator.calculateTax(discountedSubtotal, region);
        double total = Math.round((discountedSubtotal + tax) * 100.0) / 100.0;
        return new CartSummary(activeItems.size(), subtotal, discountAmount, tax, total);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void ensureActive() {
        if (status != CartStatus.ACTIVE) {
            throw new IllegalStateException("Cart is not active: " + status);
        }
    }

    public List<CartItem> getActiveItems() {
        return items.stream()
                .filter(i -> i.getStatus() == CartItemStatus.ACTIVE)
                .collect(java.util.stream.Collectors.toList());
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public String getCartId() { return cartId; }
    public String getUserId() { return userId; }
    public CartStatus getStatus() { return status; }
    public List<CartItem> getAllItems() { return Collections.unmodifiableList(items); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastModifiedAt() { return lastModifiedAt; }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cart{cartId='" + cartId + "', userId='" + userId + "', status=" + status + "}";
    }
}
