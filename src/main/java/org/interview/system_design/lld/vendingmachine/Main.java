package org.interview.system_design.lld.vendingmachine;

/**
 * Demo runner for the Vending Machine LLD.
 * Patterns: State Pattern, Strategy (payment, change)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        VendingMachine machine = new VendingMachine();

        machine.loadProduct(new Product("P1", "Cola",  0.75, 5));
        machine.loadProduct(new Product("P2", "Water", 0.50, 3));
        machine.loadProduct(new Product("P3", "Chips", 1.25, 2));
        machine.loadProduct(new Product("P4", "Candy", 0.25, 0)); // Out of stock

        banner("Initial Inventory");
        machine.displayStatus();

        banner("Scenario 1: Buy Cola ($0.75) with exact change");
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        System.out.printf("  Inserted: $%.2f%n", machine.getInsertedAmount());
        machine.selectItem("P1");
        machine.dispense();
        System.out.println("  State after: " + machine.getCurrentState());

        banner("Scenario 2: Buy Chips ($1.25) with $2.00 -- expect $0.75 change");
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.DOLLAR);
        System.out.printf("  Inserted: $%.2f%n", machine.getInsertedAmount());
        machine.selectItem("P3");
        machine.dispense();
        System.out.println("  State after: " + machine.getCurrentState());

        banner("Scenario 3: Select item without inserting money");
        machine.selectItem("P2");
        System.out.println("  State: " + machine.getCurrentState() + " (should stay IDLE)");

        banner("Scenario 4: Insert $0.50 then cancel -- expect refund");
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        System.out.printf("  Inserted: $%.2f%n", machine.getInsertedAmount());
        machine.cancel();
        System.out.printf("  After cancel -- inserted: $%.2f | state: %s%n",
                machine.getInsertedAmount(), machine.getCurrentState());

        banner("Scenario 5: Buy Candy (out-of-stock)");
        machine.insertCoin(Coin.QUARTER);
        machine.selectItem("P4");
        System.out.println("  State after selecting out-of-stock: " + machine.getCurrentState());
        machine.cancel();

        banner("Scenario 6: Insufficient funds for Cola ($0.75)");
        machine.insertCoin(Coin.DIME);
        machine.insertCoin(Coin.NICKEL);
        System.out.printf("  Inserted: $%.2f (need $0.75)%n", machine.getInsertedAmount());
        machine.selectItem("P1");
        System.out.println("  State: " + machine.getCurrentState());
        machine.cancel();

        banner("Final Inventory");
        machine.displayStatus();

        System.out.println();
        System.out.println("=== Vending Machine Demo Complete ===");
    }
}
