package org.interview.system_design.lld.coffeemachine;

/** Adds caramel drizzle to the coffee (+$0.50). */
public class CaramelDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public CaramelDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + ", Caramel";
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}
