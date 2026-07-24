package org.interview.system_design.lld.vendingmachine;

/**
 * State handler interface for the vending machine State pattern.
 * Each concrete state implements the allowed operations for that state.
 */
public interface VendingMachineStateHandler {
    /** Insert a coin into the machine. */
    void insertCoin(VendingMachine machine, Coin coin);

    /** Select an item by product ID. */
    void selectItem(VendingMachine machine, String productId);

    /** Dispense the selected item. */
    void dispense(VendingMachine machine);

    /** Cancel the transaction and return inserted coins. */
    void cancel(VendingMachine machine);
}
