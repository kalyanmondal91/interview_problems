package org.interview.system_design.lld.coffeemachine;

/** Adds whipped cream to the coffee (+$0.50). */
public class WhipCreamDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public WhipCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Whip Cream";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}
