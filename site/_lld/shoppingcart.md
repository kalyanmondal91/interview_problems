---
layout: lld
render_with_liquid: false
title: "Shopping Cart"
system: shoppingcart
description: "LLD of Shopping Cart"
files:
  - "BuyXGetYStrategy.java"
  - "Cart.java"
  - "CartItem.java"
  - "CartItemBuilder.java"
  - "CartItemStatus.java"
  - "CartObserver.java"
  - "CartService.java"
  - "CartStatus.java"
  - "CartSummary.java"
  - "DiscountStrategy.java"
  - "Main.java"
  - "MembershipDiscountStrategy.java"
  - "PercentageDiscountStrategy.java"
  - "Product.java"
  - "ProductStatus.java"
  - "StandardTaxCalculator.java"
  - "TaxCalculator.java"
---

## Shopping Cart

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="BuyXGetYStrategy.java">BuyXGetYStrategy.java</button>
<button class="tab-btn" data-tab="Cart.java">Cart.java</button>
<button class="tab-btn" data-tab="CartItem.java">CartItem.java</button>
<button class="tab-btn" data-tab="CartItemBuilder.java">CartItemBuilder.java</button>
<button class="tab-btn" data-tab="CartItemStatus.java">CartItemStatus.java</button>
<button class="tab-btn" data-tab="CartObserver.java">CartObserver.java</button>
<button class="tab-btn" data-tab="CartService.java">CartService.java</button>
<button class="tab-btn" data-tab="CartStatus.java">CartStatus.java</button>
<button class="tab-btn" data-tab="CartSummary.java">CartSummary.java</button>
<button class="tab-btn" data-tab="DiscountStrategy.java">DiscountStrategy.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="MembershipDiscountStrategy.java">MembershipDiscountStrategy.java</button>
<button class="tab-btn" data-tab="PercentageDiscountStrategy.java">PercentageDiscountStrategy.java</button>
<button class="tab-btn" data-tab="Product.java">Product.java</button>
<button class="tab-btn" data-tab="ProductStatus.java">ProductStatus.java</button>
<button class="tab-btn" data-tab="StandardTaxCalculator.java">StandardTaxCalculator.java</button>
<button class="tab-btn" data-tab="TaxCalculator.java">TaxCalculator.java</button>
</div>
<div class="tab-content active" id="BuyXGetYStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Buy X items, get Y items free (e.g., Buy 2 Get 1 Free).
 * Applied per product line.
 * &lt;p&gt;Design Pattern: Strategy&lt;/p&gt;
 */
public class BuyXGetYStrategy implements DiscountStrategy {

    private final int buyX;
    private final int getY;

    public BuyXGetYStrategy(int buyX, int getY) {
        this.buyX = buyX;
        this.getY = getY;
    }

    @Override
    public double applyDiscount(List&lt;CartItem&gt; items) {
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
        return &quot;Buy &quot; + buyX + &quot; Get &quot; + getY + &quot; Free&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="Cart-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Shopping cart with observer support and pluggable discount strategy.
 * &lt;p&gt;Design Patterns: Observer (CartObserver), Strategy (DiscountStrategy)&lt;/p&gt;
 */
public class Cart {

    private final String cartId;
    private final String userId;
    private final List&lt;CartItem&gt; items;
    private CartStatus status;
    private final List&lt;CartObserver&gt; observers;
    private DiscountStrategy discountStrategy;
    private final TaxCalculator taxCalculator;
    private final String region;
    private final Instant createdAt;
    private Instant lastModifiedAt;

    public Cart(String userId, TaxCalculator taxCalculator, String region) {
        this.cartId = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = new ArrayList&lt;&gt;();
        this.status = CartStatus.ACTIVE;
        this.observers = new CopyOnWriteArrayList&lt;&gt;();
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
        Optional&lt;CartItem&gt; existing = items.stream()
                .filter(i -&gt; i.getStatus() == CartItemStatus.ACTIVE
                        &amp;&amp; i.getProduct().getProductId().equals(item.getProduct().getProductId()))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        lastModifiedAt = Instant.now();
        observers.forEach(o -&gt; o.onItemAdded(item));
    }

    public boolean removeItem(String itemId) {
        ensureActive();
        Optional&lt;CartItem&gt; found = items.stream()
                .filter(i -&gt; i.getItemId().equals(itemId))
                .findFirst();
        if (found.isPresent()) {
            found.get().setStatus(CartItemStatus.REMOVED);
            lastModifiedAt = Instant.now();
            observers.forEach(o -&gt; o.onItemRemoved(found.get()));
            return true;
        }
        return false;
    }

    public boolean updateQuantity(String itemId, int newQuantity) {
        ensureActive();
        if (newQuantity &lt;= 0) return removeItem(itemId);
        return items.stream()
                .filter(i -&gt; i.getItemId().equals(itemId) &amp;&amp; i.getStatus() == CartItemStatus.ACTIVE)
                .findFirst()
                .map(i -&gt; { i.setQuantity(newQuantity); lastModifiedAt = Instant.now(); return true; })
                .orElse(false);
    }

    public void saveForLater(String itemId) {
        items.stream()
                .filter(i -&gt; i.getItemId().equals(itemId))
                .findFirst()
                .ifPresent(i -&gt; i.setStatus(CartItemStatus.SAVED_FOR_LATER));
    }

    // ─── Checkout ─────────────────────────────────────────────────────────────

    public CartSummary checkout() {
        ensureActive();
        CartSummary summary = getSummary();
        this.status = CartStatus.CHECKED_OUT;
        this.lastModifiedAt = Instant.now();
        observers.forEach(o -&gt; o.onCartCheckedOut(this));
        return summary;
    }

    // ─── Summary ──────────────────────────────────────────────────────────────

    public CartSummary getSummary() {
        List&lt;CartItem&gt; activeItems = getActiveItems();
        double subtotal = activeItems.stream()
                .mapToDouble(i -&gt; i.getUnitPrice() * i.getQuantity())
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
            throw new IllegalStateException(&quot;Cart is not active: &quot; + status);
        }
    }

    public List&lt;CartItem&gt; getActiveItems() {
        return items.stream()
                .filter(i -&gt; i.getStatus() == CartItemStatus.ACTIVE)
                .collect(java.util.stream.Collectors.toList());
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public String getCartId() { return cartId; }
    public String getUserId() { return userId; }
    public CartStatus getStatus() { return status; }
    public List&lt;CartItem&gt; getAllItems() { return Collections.unmodifiableList(items); }
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
        return &quot;Cart{cartId=&#x27;&quot; + cartId + &quot;&#x27;, userId=&#x27;&quot; + userId + &quot;&#x27;, status=&quot; + status + &quot;}&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="CartItem-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

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
        return String.format(&quot;CartItem[%s] %s qty=%d unit=%.2f disc=%.0f%% line=%.2f status=%s&quot;,
                itemId, product.getName(), quantity, unitPrice, discountPercent, getLineTotal(), status);
    }
}</code></pre>
</div>
<div class="tab-content" id="CartItemBuilder-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

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
        if (product == null) throw new IllegalStateException(&quot;product is required&quot;);
        if (quantity &lt;= 0)   throw new IllegalStateException(&quot;quantity must be &gt; 0&quot;);
        return new CartItem(itemId, product, quantity, unitPrice, discountPercent, status);
    }
}</code></pre>
</div>
<div class="tab-content" id="CartItemStatus-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/**
 * Status of a single item within the cart.
 */
public enum CartItemStatus {
    ACTIVE,
    SAVED_FOR_LATER,
    REMOVED
}</code></pre>
</div>
<div class="tab-content" id="CartObserver-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/**
 * Observer interface for cart lifecycle events.
 * &lt;p&gt;Design Pattern: Observer&lt;/p&gt;
 */
public interface CartObserver {
    void onItemAdded(CartItem item);
    void onItemRemoved(CartItem item);
    void onCartCheckedOut(Cart cart);
}</code></pre>
</div>
<div class="tab-content" id="CartService-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service layer for cart operations.
 * &lt;p&gt;Design Patterns: Strategy (discount), Observer (cart events), Builder (CartItem)&lt;/p&gt;
 */
public class CartService {

    private final Map&lt;String, Cart&gt; carts = new ConcurrentHashMap&lt;&gt;();
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

    public Optional&lt;Cart&gt; getCart(String cartId) {
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

    /** Updates item quantity; removes if quantity &lt;= 0. */
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
     * Merges a guest cart into a registered user&#x27;s cart.
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
        if (cart == null) throw new IllegalArgumentException(&quot;Cart not found: &quot; + cartId);
        return cart;
    }
}</code></pre>
</div>
<div class="tab-content" id="CartStatus-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/** Represents the lifecycle status of a shopping cart. */
public enum CartStatus {
    ACTIVE,
    CHECKED_OUT,
    ABANDONED,
    MERGED
}</code></pre>
</div>
<div class="tab-content" id="CartSummary-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

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
                &quot;CartSummary{items=%d, subtotal=%.2f, discount=%.2f, tax=%.2f, total=%.2f}&quot;,
                itemCount, subtotal, discountAmount, tax, total);
    }
}</code></pre>
</div>
<div class="tab-content" id="DiscountStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Strategy interface for applying discounts to cart items.
 * &lt;p&gt;Design Pattern: Strategy — allows swapping discount algorithms at runtime.&lt;/p&gt;
 */
public interface DiscountStrategy {
    /**
     * Calculates the total discount amount for the given cart items.
     *
     * @param items list of active cart items
     * @return the total discount amount (positive value)
     */
    double applyDiscount(List&lt;CartItem&gt; items);

    /** Human-readable description of this strategy. */
    String getDescription();
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/**
 * Demo runner for the Shopping Cart LLD.
 * Patterns: Strategy (discount), Observer, Builder (CartItem)
 */
public class Main {

    public static void main(String[] args) {

        CartService service = new CartService(new StandardTaxCalculator());

        // ── Products ───────────────────────────────────────────────────────────
        Product laptop = new Product(&quot;p1&quot;, &quot;Laptop&quot;,    &quot;15-inch laptop&quot;,    999.99, &quot;Electronics&quot;,
                ProductStatus.IN_STOCK, 10);
        Product mouse  = new Product(&quot;p2&quot;, &quot;Mouse&quot;,     &quot;Wireless mouse&quot;,     29.99, &quot;Electronics&quot;,
                ProductStatus.IN_STOCK, 50);
        Product book   = new Product(&quot;p3&quot;, &quot;Clean Code&quot;,&quot;Programming book&quot;,   44.99, &quot;Books&quot;,
                ProductStatus.IN_STOCK, 20);
        Product tshirt = new Product(&quot;p4&quot;, &quot;T-Shirt&quot;,   &quot;Cotton t-shirt&quot;,     19.99, &quot;Clothing&quot;,
                ProductStatus.IN_STOCK, 100);

        // ── Scenario 1: Add items and apply percentage discount ────────────────
        System.out.println(&quot;\n=== Scenario 1: Add 3 items + 10% discount ===&quot;);
        Cart cart1 = service.createCart(&quot;user1&quot;, &quot;US-CA&quot;);

        cart1.addObserver(new CartObserver() {
            @Override public void onItemAdded(CartItem item) {
                System.out.println(&quot;  [Event] Added: &quot; + item.getProduct().getName()
                        + &quot; × &quot; + item.getQuantity());
            }
            @Override public void onItemRemoved(CartItem item) {
                System.out.println(&quot;  [Event] Removed: &quot; + item.getProduct().getName());
            }
            @Override public void onCartCheckedOut(Cart cart) {
                System.out.println(&quot;  [Event] Cart &quot; + cart.getCartId() + &quot; checked out!&quot;);
            }
        });

        CartItem i1 = service.addToCart(cart1.getCartId(), laptop, 1);
        CartItem i2 = service.addToCart(cart1.getCartId(), mouse,  2);
        CartItem i3 = service.addToCart(cart1.getCartId(), book,   1);

        service.applyDiscount(cart1.getCartId(), new PercentageDiscountStrategy(10));
        CartSummary s1 = service.getCartSummary(cart1.getCartId());
        System.out.printf(&quot;  Subtotal: $%.2f | Discount: $%.2f | Tax: $%.2f | Total: $%.2f%n&quot;,
                s1.getSubtotal(), s1.getDiscountAmount(), s1.getTax(), s1.getTotal());

        // ── Scenario 2: Buy 2 Get 1 Free ──────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 2: Buy2Get1Free -- add 3 T-Shirts ===&quot;);
        Cart cart2 = service.createCart(&quot;user2&quot;, &quot;US-NY&quot;);
        service.addToCart(cart2.getCartId(), tshirt, 3); // 3 shirts -- 1 free

        service.applyDiscount(cart2.getCartId(), new BuyXGetYStrategy(2, 1));
        CartSummary s2 = service.getCartSummary(cart2.getCartId());
        System.out.printf(&quot;  3 × $%.2f | Discount (1 free): -$%.2f | Tax: $%.2f | Total: $%.2f%n&quot;,
                tshirt.getPrice() * 3, s2.getDiscountAmount(), s2.getTax(), s2.getTotal());

        // ── Scenario 3: Gold membership discount ─────────────────────────────
        System.out.println(&quot;\n=== Scenario 3: Gold membership (10% off) vs no discount ===&quot;);
        Cart cart3 = service.createCart(&quot;user3&quot;, &quot;US-TX&quot;);
        service.addToCart(cart3.getCartId(), laptop, 1);
        CartSummary noDiscount = service.getCartSummary(cart3.getCartId());

        service.applyDiscount(cart3.getCartId(),
                new MembershipDiscountStrategy(MembershipDiscountStrategy.MembershipTier.GOLD));
        CartSummary goldDiscount = service.getCartSummary(cart3.getCartId());

        System.out.printf(&quot;  Without discount: $%.2f%n&quot;, noDiscount.getTotal());
        System.out.printf(&quot;  With GOLD (10%%): $%.2f (saved $%.2f)%n&quot;,
                goldDiscount.getTotal(), noDiscount.getTotal() - goldDiscount.getTotal());

        // ── Scenario 4: Remove and update quantity ─────────────────────────────
        System.out.println(&quot;\n=== Scenario 4: Remove item + update quantity ===&quot;);
        // cart1 still has laptop, 2×mouse, book
        System.out.println(&quot;  Before: &quot; + cart1.getActiveItems().size() + &quot; item types&quot;);
        service.removeFromCart(cart1.getCartId(), i3.getItemId()); // remove book
        System.out.println(&quot;  After removing book: &quot; + cart1.getActiveItems().size() + &quot; item types&quot;);
        service.updateQuantity(cart1.getCartId(), i2.getItemId(), 5); // 5 mice
        CartSummary s4 = service.getCartSummary(cart1.getCartId());
        System.out.printf(&quot;  Updated summary -- items: %d | Total: $%.2f%n&quot;, s4.getItemCount(), s4.getTotal());

        // ── Scenario 5: Guest cart merge ──────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 5: Merge guest cart into user cart ===&quot;);
        Cart guestCart = service.createCart(&quot;guest-xyz&quot;, &quot;US-WA&quot;);
        service.addToCart(guestCart.getCartId(), book,   2);
        service.addToCart(guestCart.getCartId(), mouse,  1);

        Cart userCart = service.createCart(&quot;user4&quot;, &quot;US-WA&quot;);
        service.addToCart(userCart.getCartId(), laptop, 1);

        System.out.println(&quot;  Guest items: &quot; + guestCart.getActiveItems().size());
        System.out.println(&quot;  User  items: &quot; + userCart.getActiveItems().size());
        service.mergeGuestCart(guestCart.getCartId(), userCart.getCartId());
        System.out.println(&quot;  After merge -- user items: &quot; + userCart.getActiveItems().size());
        System.out.println(&quot;  Guest cart status: &quot; + guestCart.getStatus());

        // ── Scenario 6: Checkout ──────────────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 6: Checkout ===&quot;);
        CartSummary finalSummary = service.checkout(cart2.getCartId());
        System.out.printf(&quot;  Checked out! Items: %d | Total paid: $%.2f%n&quot;,
                finalSummary.getItemCount(), finalSummary.getTotal());
        System.out.println(&quot;  Cart status: &quot; + cart2.getStatus());

        // Attempt to add to checked-out cart
        try {
            service.addToCart(cart2.getCartId(), tshirt, 1);
            System.out.println(&quot;  Add after checkout: ALLOWED (unexpected)&quot;);
        } catch (IllegalStateException e) {
            System.out.println(&quot;  Add after checkout: REJECTED -- &quot; + e.getMessage());
        }

        System.out.println(&quot;\n=== Shopping Cart Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="MembershipDiscountStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Applies a discount based on membership tier.
 * &lt;p&gt;Design Pattern: Strategy&lt;/p&gt;
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
    public double applyDiscount(List&lt;CartItem&gt; items) {
        double subtotal = items.stream()
                .filter(i -&gt; i.getStatus() == CartItemStatus.ACTIVE)
                .mapToDouble(i -&gt; i.getUnitPrice() * i.getQuantity())
                .sum();
        return Math.round(subtotal * tier.getDiscountRate() * 100.0) / 100.0;
    }

    @Override
    public String getDescription() {
        return tier.name() + &quot; membership &quot; + (tier.getDiscountRate() * 100) + &quot;% discount&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="PercentageDiscountStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

import java.util.List;

/**
 * Applies a fixed percentage discount to the cart subtotal.
 * &lt;p&gt;Design Pattern: Strategy&lt;/p&gt;
 */
public class PercentageDiscountStrategy implements DiscountStrategy {

    private final double discountPercent;

    public PercentageDiscountStrategy(double discountPercent) {
        if (discountPercent &lt; 0 || discountPercent &gt; 100) {
            throw new IllegalArgumentException(&quot;Discount percent must be 0-100&quot;);
        }
        this.discountPercent = discountPercent;
    }

    @Override
    public double applyDiscount(List&lt;CartItem&gt; items) {
        double subtotal = items.stream()
                .filter(i -&gt; i.getStatus() == CartItemStatus.ACTIVE)
                .mapToDouble(i -&gt; i.getUnitPrice() * i.getQuantity())
                .sum();
        return Math.round(subtotal * (discountPercent / 100.0) * 100.0) / 100.0;
    }

    @Override
    public String getDescription() {
        return discountPercent + &quot;% off total&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="Product-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/**
 * Product entity. Immutable except for mutable stock quantity.
 */
public class Product {

    private final String        productId;
    private final String        name;
    private final String        description;
    private final double        price;
    private final String        category;
    private       ProductStatus status;
    private       int           stockQuantity;

    public Product(String productId, String name, String description,
                   double price, String category,
                   ProductStatus status, int stockQuantity) {
        this.productId     = productId;
        this.name          = name;
        this.description   = description;
        this.price         = price;
        this.category      = category;
        this.status        = status;
        this.stockQuantity = stockQuantity;
    }

    public String        getProductId()     { return productId; }
    public String        getName()          { return name; }
    public String        getDescription()   { return description; }
    public double        getPrice()         { return price; }
    public String        getCategory()      { return category; }
    public ProductStatus getStatus()        { return status; }
    public int           getStockQuantity() { return stockQuantity; }

    public void setStatus(ProductStatus status)       { this.status = status; }
    public void setStockQuantity(int stockQuantity)   { this.stockQuantity = stockQuantity; }

    public boolean isAvailable() {
        return status == ProductStatus.IN_STOCK || status == ProductStatus.LOW_STOCK;
    }

    @Override
    public String toString() {
        return String.format(&quot;Product[%s] %s $%.2f (%s)&quot;, productId, name, price, status);
    }
}</code></pre>
</div>
<div class="tab-content" id="ProductStatus-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/**
 * Stock/availability status of a product.
 */
public enum ProductStatus {
    IN_STOCK,
    OUT_OF_STOCK,
    LOW_STOCK,
    DISCONTINUED
}</code></pre>
</div>
<div class="tab-content" id="StandardTaxCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/**
 * Standard flat-rate tax calculator.
 * Applies 8% tax regardless of region (simplified).
 */
public class StandardTaxCalculator implements TaxCalculator {

    private static final double TAX_RATE = 0.08;

    @Override
    public double calculateTax(double subtotal, String region) {
        return Math.round(subtotal * TAX_RATE * 100.0) / 100.0;
    }
}</code></pre>
</div>
<div class="tab-content" id="TaxCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.shoppingcart;

/** Strategy interface for tax calculation. */
public interface TaxCalculator {
    /**
     * Calculates tax on the given subtotal for the specified region.
     *
     * @param subtotal the pre-tax amount
     * @param region   the region/state code for tax rules
     * @return the tax amount
     */
    double calculateTax(double subtotal, String region);
}</code></pre>
</div>
</div>
