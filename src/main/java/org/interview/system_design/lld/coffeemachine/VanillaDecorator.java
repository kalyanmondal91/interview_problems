package org.interview.system_design.lld.coffeemachine;

/** Adds vanilla syrup to the coffee (+$0.50). */
public class VanillaDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public VanillaDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Vanilla";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}
