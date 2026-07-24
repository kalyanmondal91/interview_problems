package org.interview.system_design.lld.coffeemachine;

/** Adds sugar to the coffee (+$0.50). */
public class SugarDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}
