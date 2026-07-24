package org.interview.system_design.lld.coffeemachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for the coffee machine system.
 * Handles placing orders, processing them, and maintaining history.
 */
public class CoffeeMachineService {

    private final List<Order> pendingOrders = new ArrayList<>();
    private final List<Order> orderHistory = new ArrayList<>();
    private final CoffeeMachineTemplate machine;

    public CoffeeMachineService(CoffeeMachineTemplate machine) {
        this.machine = machine;
    }

    /**
     * Places a new order for the given (optionally decorated) coffee.
     *
     * @param coffee   the coffee (may be wrapped in decorators)
     * @param quantity number of cups
     * @return the created Order
     */
    public Order placeOrder(Coffee coffee, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Order order = new Order(coffee, quantity);
        pendingOrders.add(order);
        System.out.println("Order placed: " + order);
        return order;
    }

    /**
     * Processes the next pending order.
     *
     * @return the processed Order, or empty if no pending orders
     */
    public Optional<Order> processOrder() {
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders.");
            return Optional.empty();
        }
        Order order = pendingOrders.remove(0);
        System.out.println("Processing: " + order);
        // Simulate machine work for each cup
        for (int i = 0; i < order.getQuantity(); i++) {
            machine.makeCoffee(order.getCoffee().getSize());
        }
        orderHistory.add(order);
        System.out.println("Order completed: " + order.getOrderId());
        return Optional.of(order);
    }

    /** Returns an immutable view of the order history. */
    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    /** Returns count of pending orders. */
    public int getPendingCount() {
        return pendingOrders.size();
    }
}
