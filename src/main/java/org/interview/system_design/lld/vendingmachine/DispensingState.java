package org.interview.system_design.lld.vendingmachine;

import java.util.Map;

/**
 * Handles the dispensing event: deducts product, computes and returns change,
 * then transitions back to IDLE.
 */
public class DispensingState implements VendingMachineStateHandler {
    private final ChangeCalculator changeCalculator = new ChangeCalculator();

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println("Dispensing in progress. Cannot insert coin now.");
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        System.out.println("Dispensing in progress. Cannot select item now.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        Product product = machine.getSelectedProduct();
        if (product == null) {
            System.out.println("Error: no product selected.");
            machine.setState(VendingMachineState.IDLE);
            return;
        }

        // Dispense the product
        product.dispense();
        System.out.println("Dispensing: " + product.getName());

        // Calculate and return change
        double change = machine.getInsertedAmount() - product.getPrice();
        if (change > 0.001) {
            try {
                Map<Coin, Integer> changeCoins = changeCalculator.calculateChange(change);
                System.out.println(changeCalculator.formatChange(changeCoins));
            } catch (IllegalArgumentException e) {
                System.out.printf("Returning change: $%.2f%n", change);
            }
        }

        // Reset machine
        machine.clearCoins();
        machine.setSelectedProduct(null);
        machine.setState(VendingMachineState.IDLE);
        System.out.println("Thank you for your purchase!");
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Dispensing in progress. Cannot cancel now.");
    }
}
