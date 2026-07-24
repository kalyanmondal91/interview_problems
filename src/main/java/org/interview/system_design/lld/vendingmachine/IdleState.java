package org.interview.system_design.lld.vendingmachine;

/**
 * Handles events when the vending machine is IDLE (no money inserted).
 * Only coin insertion is valid; all other operations are rejected.
 */
public class IdleState implements VendingMachineStateHandler {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.printf("Inserted %s. Total: $%.2f%n", coin, machine.getInsertedAmount());
        machine.setState(VendingMachineState.HAS_MONEY);
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        System.out.println("Please insert money first.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please insert money and select an item first.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Nothing to cancel. Machine is idle.");
    }
}
