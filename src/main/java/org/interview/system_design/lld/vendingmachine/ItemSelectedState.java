package org.interview.system_design.lld.vendingmachine;

/**
 * Handles events when an item has been selected and payment is sufficient.
 * Allows dispensing or cancellation; additional coins/re-selection also handled.
 */
public class ItemSelectedState implements VendingMachineStateHandler {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.printf("Extra coin inserted: %s. Total: $%.2f%n", coin, machine.getInsertedAmount());
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        System.out.println("Item already selected. Press dispense or cancel.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        machine.setState(VendingMachineState.DISPENSING);
        machine.getStateHandler().dispense(machine);
    }

    @Override
    public void cancel(VendingMachine machine) {
        double refund = machine.getInsertedAmount();
        machine.clearCoins();
        machine.setSelectedProduct(null);
        machine.setState(VendingMachineState.IDLE);
        System.out.printf("Transaction cancelled. Returning $%.2f%n", refund);
    }
}
