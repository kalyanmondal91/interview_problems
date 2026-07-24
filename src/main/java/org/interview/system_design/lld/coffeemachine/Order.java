package org.interview.system_design.lld.coffeemachine;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a customer order containing a (possibly decorated) coffee.
 */
public class Order {

    private final String orderId;
    private final Coffee coffee;
    private final int quantity;
    private final double totalCost;
    private final LocalDateTime timestamp;

    public Order(Coffee coffee, int quantity) {
        this.orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.coffee = coffee;
        this.quantity = quantity;
        this.totalCost = coffee.getCost() * quantity;
        this.timestamp = LocalDateTime.now();
    }

    public String getOrderId() { return orderId; }
    public Coffee getCoffee() { return coffee; }
    public int getQuantity() { return quantity; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Order[%s] %dx %s = $%.2f @ %s",
                orderId, quantity, coffee.getDescription(), totalCost, timestamp);
    }
}
