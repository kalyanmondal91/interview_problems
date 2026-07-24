package org.interview.system_design.lld.coffeemachine;

/**
 * Concrete espresso machine — brews espresso using high-pressure extraction.
 */
public class EspressoMachine extends CoffeeMachineTemplate {

    @Override
    protected void grindBeans() {
        System.out.println("[EspressoMachine] Grinding beans to fine espresso grind...");
    }

    @Override
    protected void heatWater() {
        System.out.println("[EspressoMachine] Heating water to 93°C...");
    }

    @Override
    protected void brew() {
        System.out.println("[EspressoMachine] Extracting espresso shot under 9 bar pressure...");
    }

    @Override
    protected void addIngredients() {
        System.out.println("[EspressoMachine] No extra ingredients for base espresso.");
    }

    @Override
    protected void serveCoffee() {
        System.out.println("[EspressoMachine] Serving espresso in a pre-warmed cup.");
    }

    @Override
    protected Coffee createCoffee(CoffeeSize size) {
        return new Espresso(size);
    }
}
