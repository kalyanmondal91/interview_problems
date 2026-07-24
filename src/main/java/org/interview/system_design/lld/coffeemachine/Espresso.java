package org.interview.system_design.lld.coffeemachine;

/** Concrete coffee: Espresso with base cost $2.00. */
public class Espresso extends Coffee {

    private static final double BASE_COST = 2.0;

    public Espresso(CoffeeSize size) {
        super(size);
    }

    @Override
    public String getDescription() {
        return "Espresso";
    }

    @Override
    public double getCost() {
        return BASE_COST * size.getSizeMultiplier();
    }
}
