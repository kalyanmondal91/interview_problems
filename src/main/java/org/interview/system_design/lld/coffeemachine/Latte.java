package org.interview.system_design.lld.coffeemachine;

/** Concrete coffee: Latte with base cost $3.00. */
public class Latte extends Coffee {

    private static final double BASE_COST = 3.0;

    public Latte(CoffeeSize size) {
        super(size);
    }

    @Override
    public String getDescription() {
        return "Latte";
    }

    @Override
    public double getCost() {
        return BASE_COST * size.getSizeMultiplier();
    }
}
