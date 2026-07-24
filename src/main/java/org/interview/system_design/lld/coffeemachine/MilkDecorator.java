package org.interview.system_design.lld.coffeemachine;

/** Adds milk to the coffee (+$0.50). */
public class MilkDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}
