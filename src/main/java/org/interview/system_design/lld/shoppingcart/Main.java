package org.interview.system_design.lld.shoppingcart;

/**
 * Demo runner for the Shopping Cart LLD.
 * Patterns: Strategy (discount), Observer, Builder (CartItem)
 */
public class Main {

    public static void main(String[] args) {

        CartService service = new CartService(new StandardTaxCalculator());

        // ── Products ───────────────────────────────────────────────────────────
        Product laptop = new Product("p1", "Laptop",    "15-inch laptop",    999.99, "Electronics",
                ProductStatus.IN_STOCK, 10);
        Product mouse  = new Product("p2", "Mouse",     "Wireless mouse",     29.99, "Electronics",
                ProductStatus.IN_STOCK, 50);
        Product book   = new Product("p3", "Clean Code","Programming book",   44.99, "Books",
                ProductStatus.IN_STOCK, 20);
        Product tshirt = new Product("p4", "T-Shirt",   "Cotton t-shirt",     19.99, "Clothing",
                ProductStatus.IN_STOCK, 100);

        // ── Scenario 1: Add items and apply percentage discount ────────────────
        System.out.println("\n=== Scenario 1: Add 3 items + 10% discount ===");
        Cart cart1 = service.createCart("user1", "US-CA");

        cart1.addObserver(new CartObserver() {
            @Override public void onItemAdded(CartItem item) {
                System.out.println("  [Event] Added: " + item.getProduct().getName()
                        + " × " + item.getQuantity());
            }
            @Override public void onItemRemoved(CartItem item) {
                System.out.println("  [Event] Removed: " + item.getProduct().getName());
            }
            @Override public void onCartCheckedOut(Cart cart) {
                System.out.println("  [Event] Cart " + cart.getCartId() + " checked out!");
            }
        });

        CartItem i1 = service.addToCart(cart1.getCartId(), laptop, 1);
        CartItem i2 = service.addToCart(cart1.getCartId(), mouse,  2);
        CartItem i3 = service.addToCart(cart1.getCartId(), book,   1);

        service.applyDiscount(cart1.getCartId(), new PercentageDiscountStrategy(10));
        CartSummary s1 = service.getCartSummary(cart1.getCartId());
        System.out.printf("  Subtotal: $%.2f | Discount: $%.2f | Tax: $%.2f | Total: $%.2f%n",
                s1.getSubtotal(), s1.getDiscountAmount(), s1.getTax(), s1.getTotal());

        // ── Scenario 2: Buy 2 Get 1 Free ──────────────────────────────────────
        System.out.println("\n=== Scenario 2: Buy2Get1Free -- add 3 T-Shirts ===");
        Cart cart2 = service.createCart("user2", "US-NY");
        service.addToCart(cart2.getCartId(), tshirt, 3); // 3 shirts -- 1 free

        service.applyDiscount(cart2.getCartId(), new BuyXGetYStrategy(2, 1));
        CartSummary s2 = service.getCartSummary(cart2.getCartId());
        System.out.printf("  3 × $%.2f | Discount (1 free): -$%.2f | Tax: $%.2f | Total: $%.2f%n",
                tshirt.getPrice() * 3, s2.getDiscountAmount(), s2.getTax(), s2.getTotal());

        // ── Scenario 3: Gold membership discount ─────────────────────────────
        System.out.println("\n=== Scenario 3: Gold membership (10% off) vs no discount ===");
        Cart cart3 = service.createCart("user3", "US-TX");
        service.addToCart(cart3.getCartId(), laptop, 1);
        CartSummary noDiscount = service.getCartSummary(cart3.getCartId());

        service.applyDiscount(cart3.getCartId(),
                new MembershipDiscountStrategy(MembershipDiscountStrategy.MembershipTier.GOLD));
        CartSummary goldDiscount = service.getCartSummary(cart3.getCartId());

        System.out.printf("  Without discount: $%.2f%n", noDiscount.getTotal());
        System.out.printf("  With GOLD (10%%): $%.2f (saved $%.2f)%n",
                goldDiscount.getTotal(), noDiscount.getTotal() - goldDiscount.getTotal());

        // ── Scenario 4: Remove and update quantity ─────────────────────────────
        System.out.println("\n=== Scenario 4: Remove item + update quantity ===");
        // cart1 still has laptop, 2×mouse, book
        System.out.println("  Before: " + cart1.getActiveItems().size() + " item types");
        service.removeFromCart(cart1.getCartId(), i3.getItemId()); // remove book
        System.out.println("  After removing book: " + cart1.getActiveItems().size() + " item types");
        service.updateQuantity(cart1.getCartId(), i2.getItemId(), 5); // 5 mice
        CartSummary s4 = service.getCartSummary(cart1.getCartId());
        System.out.printf("  Updated summary -- items: %d | Total: $%.2f%n", s4.getItemCount(), s4.getTotal());

        // ── Scenario 5: Guest cart merge ──────────────────────────────────────
        System.out.println("\n=== Scenario 5: Merge guest cart into user cart ===");
        Cart guestCart = service.createCart("guest-xyz", "US-WA");
        service.addToCart(guestCart.getCartId(), book,   2);
        service.addToCart(guestCart.getCartId(), mouse,  1);

        Cart userCart = service.createCart("user4", "US-WA");
        service.addToCart(userCart.getCartId(), laptop, 1);

        System.out.println("  Guest items: " + guestCart.getActiveItems().size());
        System.out.println("  User  items: " + userCart.getActiveItems().size());
        service.mergeGuestCart(guestCart.getCartId(), userCart.getCartId());
        System.out.println("  After merge -- user items: " + userCart.getActiveItems().size());
        System.out.println("  Guest cart status: " + guestCart.getStatus());

        // ── Scenario 6: Checkout ──────────────────────────────────────────────
        System.out.println("\n=== Scenario 6: Checkout ===");
        CartSummary finalSummary = service.checkout(cart2.getCartId());
        System.out.printf("  Checked out! Items: %d | Total paid: $%.2f%n",
                finalSummary.getItemCount(), finalSummary.getTotal());
        System.out.println("  Cart status: " + cart2.getStatus());

        // Attempt to add to checked-out cart
        try {
            service.addToCart(cart2.getCartId(), tshirt, 1);
            System.out.println("  Add after checkout: ALLOWED (unexpected)");
        } catch (IllegalStateException e) {
            System.out.println("  Add after checkout: REJECTED -- " + e.getMessage());
        }

        System.out.println("\n=== Shopping Cart Demo Complete ===");
    }
}
