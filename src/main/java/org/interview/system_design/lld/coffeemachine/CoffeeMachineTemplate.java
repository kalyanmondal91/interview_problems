package org.interview.system_design.lld.coffeemachine;

/**
 * Abstract template defining the invariant steps for making coffee.
 * Subclasses implement the variable steps.
 * Pattern: Template Method
 */
public abstract class CoffeeMachineTemplate {

    /** Grinds coffee beans appropriate for this machine type. */
    protected abstract void grindBeans();

    /** Heats water to the correct temperature. */
    protected abstract void heatWater();

    /** Brews the coffee using the specific technique. */
    protected abstract void brew();

    /** Adds any extra ingredients (milk, sugar, etc.). */
    protected abstract void addIngredients();

    /** Serves the finished coffee to the customer. */
    protected abstract void serveCoffee();

    /**
     * Template method — defines the invariant sequence for making coffee.
     * Cannot be overridden by subclasses.
     */
    public final Coffee makeCoffee(CoffeeSize size) {
        System.out.println("=== Starting coffee preparation ===");
        grindBeans();
        heatWater();
        brew();
        addIngredients();
        Coffee coffee = createCoffee(size);
        serveCoffee();
        System.out.println("=== Coffee ready: " + coffee + " ===");
        return coffee;
    }

    /** Factory method to create the concrete Coffee object. */
    protected abstract Coffee createCoffee(CoffeeSize size);
}
