package org.interview.system_design.lld.vendingmachine;

/**
 * Represents a product available in the vending machine.
 */
public class Product {
    private final String productId;
    private final String name;
    private final double price;
    private int quantity;

    public Product(String productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isAvailable() { return quantity > 0; }

    /** Decrements quantity by 1 after dispense. */
    public void dispense() {
        if (quantity <= 0) throw new IllegalStateException("Product out of stock: " + name);
        quantity--;
    }

    /** Restocks by adding the given amount. */
    public void restock(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Restock amount must be non-negative");
        quantity += amount;
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=$%.2f, qty=%d}",
                productId, name, price, quantity);
    }
}
