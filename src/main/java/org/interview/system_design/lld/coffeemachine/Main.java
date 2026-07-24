package org.interview.system_design.lld.coffeemachine;

import java.util.Optional;

/**
 * Demo runner for the Coffee Machine LLD.
 * Patterns: Decorator (add-ons), Template Method (machine brew steps)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        banner("Scenario 1: Plain coffees -- base cost and description");
        Coffee espresso  = new Espresso(CoffeeSize.MEDIUM);
        Coffee americano = new Americano(CoffeeSize.MEDIUM);
        Coffee latte     = new Latte(CoffeeSize.MEDIUM);
        printCoffee(espresso);
        printCoffee(americano);
        printCoffee(latte);

        banner("Scenario 2: Decorated coffees -- stack add-ons");
        Coffee fancyLatte = new WhipCreamDecorator(
                               new CaramelDecorator(
                               new VanillaDecorator(
                               new SugarDecorator(
                               new MilkDecorator(new Latte(CoffeeSize.LARGE))))));
        printCoffee(fancyLatte);

        Coffee milkEspresso = new MilkDecorator(new SugarDecorator(new Espresso(CoffeeSize.SMALL)));
        printCoffee(milkEspresso);

        banner("Scenario 3: Cost comparison -- decorators add up correctly");
        Coffee base    = new Americano(CoffeeSize.MEDIUM);
        Coffee withOne = new MilkDecorator(new Americano(CoffeeSize.MEDIUM));
        Coffee withTwo = new MilkDecorator(new SugarDecorator(new Americano(CoffeeSize.MEDIUM)));
        System.out.printf("  Americano alone:   $%.2f%n", base.getCost());
        System.out.printf("  + Milk:            $%.2f (diff: +$%.2f)%n",
                withOne.getCost(), withOne.getCost() - base.getCost());
        System.out.printf("  + Milk + Sugar:    $%.2f (diff: +$%.2f)%n",
                withTwo.getCost(), withTwo.getCost() - base.getCost());

        banner("Scenario 4: EspressoMachine template steps");
        CoffeeMachineTemplate espressoMachine = new EspressoMachine();
        espressoMachine.makeCoffee(CoffeeSize.MEDIUM);

        banner("Scenario 5: DripCoffeeMachine template steps");
        CoffeeMachineTemplate dripMachine = new DripCoffeeMachine();
        dripMachine.makeCoffee(CoffeeSize.LARGE);

        banner("Scenario 6: CoffeeMachineService -- order history");
        CoffeeMachineService service = new CoffeeMachineService(new EspressoMachine());
        Coffee order1Coffee = new MilkDecorator(new Latte(CoffeeSize.MEDIUM));
        Coffee order2Coffee = new SugarDecorator(new Espresso(CoffeeSize.SMALL));
        Order o1 = service.placeOrder(order1Coffee, 2);
        Order o2 = service.placeOrder(order2Coffee, 1);
        Optional<Order> processed1 = service.processOrder();
        Optional<Order> processed2 = service.processOrder();

        System.out.println("  Order history:");
        service.getOrderHistory().forEach(o ->
                System.out.printf("    Order %s: %s x%d = $%.2f%n",
                        o.getOrderId(), o.getCoffee().getDescription(),
                        o.getQuantity(), o.getTotalCost()));

        System.out.println();
        System.out.println("=== Coffee Machine Demo Complete ===");
    }

    private static void printCoffee(Coffee coffee) {
        System.out.printf("  %-60s $%.2f%n", coffee.getDescription(), coffee.getCost());
    }
}
