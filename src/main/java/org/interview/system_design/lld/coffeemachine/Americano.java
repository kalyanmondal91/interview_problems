package org.interview.system_design.lld.coffeemachine;

/** Concrete coffee: Americano with base cost $2.50. */
public class Americano extends Coffee {

    private static final double BASE_COST = 2.5;

    public Americano(CoffeeSize size) {
        super(size);
    }

    @Override
    public String getDescription() {
        return "Americano";
    }

    @Override
    public double getCost() {
        return BASE_COST * size.getSizeMultiplier();
    }
}
