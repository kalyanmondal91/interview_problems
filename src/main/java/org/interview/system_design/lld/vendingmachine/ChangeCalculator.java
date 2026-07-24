package org.interview.system_design.lld.vendingmachine;

import java.util.EnumMap;
import java.util.Map;

/**
 * Greedy change calculator that returns the minimum number of coins.
 * Iterates from highest to lowest denomination.
 */
public class ChangeCalculator {

    private static final double EPSILON = 0.001;

    /**
     * Calculates the minimum coin set needed to make the given change amount.
     *
     * @param changeAmount the amount to return as change
     * @return map of coin denominations to counts; may be empty if no change needed
     * @throws IllegalArgumentException if exact change cannot be made
     */
    public Map<Coin, Integer> calculateChange(double changeAmount) {
        Map<Coin, Integer> change = new EnumMap<>(Coin.class);
        // Work in cents to avoid floating-point issues
        long remainingCents = Math.round(changeAmount * 100);

        // Coins in descending order of value
        Coin[] denominations = {Coin.DOLLAR, Coin.QUARTER, Coin.DIME, Coin.NICKEL, Coin.PENNY};

        for (Coin coin : denominations) {
            long coinCents = Math.round(coin.getValue() * 100);
            if (remainingCents >= coinCents) {
                int count = (int) (remainingCents / coinCents);
                change.put(coin, count);
                remainingCents -= (long) count * coinCents;
            }
        }

        if (remainingCents > 0) {
            throw new IllegalArgumentException(
                    "Cannot make exact change for amount: " + changeAmount);
        }
        return change;
    }

    /** Formats a change map as a human-readable string. */
    public String formatChange(Map<Coin, Integer> change) {
        if (change.isEmpty()) return "No change";
        StringBuilder sb = new StringBuilder("Change: ");
        change.forEach((coin, count) ->
                sb.append(count).append("x").append(coin.name()).append(" "));
        return sb.toString().trim();
    }
}
