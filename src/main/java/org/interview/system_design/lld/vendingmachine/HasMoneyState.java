package org.interview.system_design.lld.vendingmachine;

import java.util.Optional;

/**
 * Handles events when money has been inserted but no item is selected yet.
 * Accepts more coins and item selection; cancel returns inserted coins.
 */
public class HasMoneyState implements VendingMachineStateHandler {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.printf("Inserted %s. Total: $%.2f%n", coin, machine.getInsertedAmount());
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        Optional<Product> productOpt = machine.getInventory().getProduct(productId);
        if (productOpt.isEmpty()) {
            System.out.println("Product not found: " + productId);
            return;
        }
        Product product = productOpt.get();
        if (!product.isAvailable()) {
            System.out.println("Product is out of stock: " + product.getName());
            return;
        }
        if (machine.getInsertedAmount() < product.getPrice() - 0.001) {
            System.out.printf("Insufficient funds. Required: $%.2f, Inserted: $%.2f%n",
                    product.getPrice(), machine.getInsertedAmount());
            return;
        }
        machine.setSelectedProduct(product);
        System.out.printf("Selected: %s ($%.2f)%n", product.getName(), product.getPrice());
        machine.setState(VendingMachineState.ITEM_SELECTED);
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please select an item first.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        double refund = machine.getInsertedAmount();
        machine.clearCoins();
        machine.setState(VendingMachineState.IDLE);
        System.out.printf("Transaction cancelled. Returning $%.2f%n", refund);
    }
}
