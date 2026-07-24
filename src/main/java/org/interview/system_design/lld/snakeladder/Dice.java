package org.interview.system_design.lld.snakeladder;

import java.util.Random;

/**
 * Represents one or more dice.
 * roll() returns the sum of all dice rolls (each die: 1–6).
 */
public class Dice {
    private final int numberOfDice;
    private final Random random;

    public Dice(int numberOfDice) {
        if (numberOfDice < 1) throw new IllegalArgumentException("Must have at least one die.");
        this.numberOfDice = numberOfDice;
        this.random = new Random();
    }

    /**
     * Rolls all dice and returns the combined total.
     *
     * @return sum in range [numberOfDice, 6 * numberOfDice]
     */
    public int roll() {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += random.nextInt(6) + 1;
        }
        return total;
    }

    public int getNumberOfDice() { return numberOfDice; }
}
