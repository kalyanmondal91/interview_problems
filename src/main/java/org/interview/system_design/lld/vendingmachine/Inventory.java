package org.interview.system_design.lld.vendingmachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the inventory of products in the vending machine.
 * Uses a Map keyed by product ID for O(1) lookups.
 */
public class Inventory {
    private final Map<String, Product> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    /** Adds or replaces a product in inventory. */
    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    /** Removes a product from inventory entirely. */
    public void removeProduct(String productId) {
        products.remove(productId);
    }

    /** Retrieves a product by ID, or empty if not found. */
    public Optional<Product> getProduct(String productId) {
        return Optional.ofNullable(products.get(productId));
    }

    /** Returns true if the product exists and has quantity > 0. */
    public boolean isAvailable(String productId) {
        return products.containsKey(productId) && products.get(productId).isAvailable();
    }

    /** Restocks a product by adding quantity. Creates product if it doesn't exist. */
    public void restock(String productId, int quantity) {
        if (products.containsKey(productId)) {
            products.get(productId).restock(quantity);
        } else {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
    }

    public Map<String, Product> getAllProducts() {
        return Collections.unmodifiableMap(products);
    }

    /** Displays the current inventory to stdout. */
    public void display() {
        System.out.println("=== Inventory ===");
        if (products.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            products.values().forEach(p -> System.out.println("  " + p));
        }
    }
}
