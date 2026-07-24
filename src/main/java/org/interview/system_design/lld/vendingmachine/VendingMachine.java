package org.interview.system_design.lld.vendingmachine;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Context class for the Vending Machine State pattern.
 *
 * Maintains current state, inserted coins, selected product, and inventory.
 * Delegates all user interactions to the active state handler.
 *
 * State transitions:
 *   IDLE → HAS_MONEY (on insertCoin)
 *   HAS_MONEY → ITEM_SELECTED (on selectItem with enough money)
 *   HAS_MONEY → IDLE (on cancel)
 *   ITEM_SELECTED → DISPENSING → IDLE (on dispense)
 *   ITEM_SELECTED → IDLE (on cancel)
 */
public class VendingMachine {
    private VendingMachineState currentState;
    private final Map<VendingMachineState, VendingMachineStateHandler> stateHandlers;
    private final Map<Coin, Integer> coinSlot;
    private Product selectedProduct;
    private final Inventory inventory;

    public VendingMachine() {
        this.inventory = new Inventory();
        this.coinSlot = new EnumMap<>(Coin.class);
        this.stateHandlers = new HashMap<>();

        // Register all state handlers
        stateHandlers.put(VendingMachineState.IDLE,          new IdleState());
        stateHandlers.put(VendingMachineState.HAS_MONEY,     new HasMoneyState());
        stateHandlers.put(VendingMachineState.ITEM_SELECTED, new ItemSelectedState());
        stateHandlers.put(VendingMachineState.DISPENSING,    new DispensingState());

        this.currentState = VendingMachineState.IDLE;
    }

    // ---- Public API (delegates to current state handler) ----

    public void insertCoin(Coin coin) {
        getStateHandler().insertCoin(this, coin);
    }

    public void selectItem(String productId) {
        getStateHandler().selectItem(this, productId);
    }

    public void dispense() {
        getStateHandler().dispense(this);
    }

    public void cancel() {
        getStateHandler().cancel(this);
    }

    // ---- State management ----

    public VendingMachineState getCurrentState() { return currentState; }

    public void setState(VendingMachineState state) {
        this.currentState = state;
        System.out.println("[State → " + state + "]");
    }

    public VendingMachineStateHandler getStateHandler() {
        return stateHandlers.get(currentState);
    }

    // ---- Coin slot management ----

    public void addCoin(Coin coin) {
        coinSlot.merge(coin, 1, Integer::sum);
    }

    public double getInsertedAmount() {
        return coinSlot.entrySet().stream()
                .mapToDouble(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }

    public void clearCoins() {
        coinSlot.clear();
    }

    public Map<Coin, Integer> getCoinSlot() { return coinSlot; }

    // ---- Product selection ----

    public Product getSelectedProduct() { return selectedProduct; }

    public void setSelectedProduct(Product product) { this.selectedProduct = product; }

    // ---- Inventory ----

    public Inventory getInventory() { return inventory; }

    /** Convenience: load a product directly from the service layer. */
    public void loadProduct(Product product) {
        inventory.addProduct(product);
    }

    public void displayStatus() {
        System.out.println("State: " + currentState
                + " | Inserted: $" + String.format("%.2f", getInsertedAmount())
                + " | Selected: " + (selectedProduct != null ? selectedProduct.getName() : "none"));
        inventory.display();
    }

    /**
     * Demo main method.
     */
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        vm.loadProduct(new Product("P1", "Cola",   1.25, 5));
        vm.loadProduct(new Product("P2", "Chips",  0.75, 3));
        vm.loadProduct(new Product("P3", "Water",  1.00, 10));

        System.out.println("--- Buying Cola ---");
        vm.insertCoin(Coin.DOLLAR);
        vm.insertCoin(Coin.QUARTER);
        vm.selectItem("P1");
        vm.dispense();

        System.out.println("\n--- Buying Chips with overpay ---");
        vm.insertCoin(Coin.DOLLAR);
        vm.selectItem("P2");
        vm.dispense();

        System.out.println("\n--- Cancel test ---");
        vm.insertCoin(Coin.QUARTER);
        vm.cancel();
    }
}
