package org.interview.system_design.lld.coffeemachine;

/**
 * Abstract decorator that wraps a Coffee instance.
 * Subclasses add extra ingredients to the coffee.
 * Pattern: Decorator
 */
public abstract class CoffeeDecorator extends Coffee {

    protected final Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        super(coffee.getSize());
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost();
    }
}
