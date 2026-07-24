package org.interview.system_design.lld.coffeemachine;

/**
 * Abstract base class for all coffee types.
 * Defines the interface for description, cost, and size.
 */
public abstract class Coffee {

    protected CoffeeSize size;

    public Coffee(CoffeeSize size) {
        this.size = size;
    }

    /** Returns human-readable description of the coffee. */
    public abstract String getDescription();

    /** Returns base cost adjusted by size multiplier. */
    public abstract double getCost();

    public CoffeeSize getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - $%.2f", getDescription(), size, getCost());
    }
}
