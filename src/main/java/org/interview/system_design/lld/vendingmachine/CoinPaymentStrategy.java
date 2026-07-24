package org.interview.system_design.lld.vendingmachine;

import java.util.Map;

/**
 * Coin-based payment strategy.
 * Validates that the total inserted coin value meets or exceeds the required amount.
 */
public class CoinPaymentStrategy implements PaymentStrategy {

    /** Floating-point tolerance for coin comparisons. */
    private static final double EPSILON = 0.001;

    @Override
    public boolean pay(double amount, Map<Coin, Integer> coinSlot) {
        double totalInserted = coinSlot.entrySet().stream()
                .mapToDouble(e -> e.getKey().getValue() * e.getValue())
                .sum();
        return totalInserted >= amount - EPSILON;
    }

    /**
     * Computes the total inserted value from the coin slot.
     *
     * @param coinSlot map of coins and their counts
     * @return total monetary value
     */
    public double getTotalInserted(Map<Coin, Integer> coinSlot) {
        return coinSlot.entrySet().stream()
                .mapToDouble(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }
}
