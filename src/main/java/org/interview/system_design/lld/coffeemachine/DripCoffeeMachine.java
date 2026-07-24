package org.interview.system_design.lld.coffeemachine;

/**
 * Concrete drip coffee machine — brews Americano using filter drip method.
 */
public class DripCoffeeMachine extends CoffeeMachineTemplate {

    @Override
    protected void grindBeans() {
        System.out.println("[DripCoffeeMachine] Grinding beans to medium coarseness...");
    }

    @Override
    protected void heatWater() {
        System.out.println("[DripCoffeeMachine] Heating water to 90°C...");
    }

    @Override
    protected void brew() {
        System.out.println("[DripCoffeeMachine] Dripping hot water through filter basket...");
    }

    @Override
    protected void addIngredients() {
        System.out.println("[DripCoffeeMachine] Topping with hot water for Americano style.");
    }

    @Override
    protected void serveCoffee() {
        System.out.println("[DripCoffeeMachine] Pouring into a large mug.");
    }

    @Override
    protected Coffee createCoffee(CoffeeSize size) {
        return new Americano(size);
    }
}
