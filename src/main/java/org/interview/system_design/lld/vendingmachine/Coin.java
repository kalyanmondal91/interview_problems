package org.interview.system_design.lld.vendingmachine;

/**
 * Enum representing coin denominations accepted by the vending machine.
 * Each coin has a monetary value in dollars.
 */
public enum Coin {
    PENNY(0.01),
    NICKEL(0.05),
    DIME(0.10),
    QUARTER(0.25),
    DOLLAR(1.00);

    private final double value;

    Coin(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + "($" + String.format("%.2f", value) + ")";
    }
}
