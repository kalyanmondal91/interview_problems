---
layout: lld
render_with_liquid: false
title: "Coffee Machine"
system: coffeemachine
description: "LLD of Coffee Machine"
files:
  - "Americano.java"
  - "CaramelDecorator.java"
  - "Coffee.java"
  - "CoffeeDecorator.java"
  - "CoffeeMachineService.java"
  - "CoffeeMachineTemplate.java"
  - "CoffeeSize.java"
  - "DripCoffeeMachine.java"
  - "Espresso.java"
  - "EspressoMachine.java"
  - "Latte.java"
  - "Main.java"
  - "MilkDecorator.java"
  - "Order.java"
  - "SugarDecorator.java"
  - "VanillaDecorator.java"
  - "WhipCreamDecorator.java"
---

## Coffee Machine

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="Americano.java">Americano.java</button>
<button class="tab-btn" data-tab="CaramelDecorator.java">CaramelDecorator.java</button>
<button class="tab-btn" data-tab="Coffee.java">Coffee.java</button>
<button class="tab-btn" data-tab="CoffeeDecorator.java">CoffeeDecorator.java</button>
<button class="tab-btn" data-tab="CoffeeMachineService.java">CoffeeMachineService.java</button>
<button class="tab-btn" data-tab="CoffeeMachineTemplate.java">CoffeeMachineTemplate.java</button>
<button class="tab-btn" data-tab="CoffeeSize.java">CoffeeSize.java</button>
<button class="tab-btn" data-tab="DripCoffeeMachine.java">DripCoffeeMachine.java</button>
<button class="tab-btn" data-tab="Espresso.java">Espresso.java</button>
<button class="tab-btn" data-tab="EspressoMachine.java">EspressoMachine.java</button>
<button class="tab-btn" data-tab="Latte.java">Latte.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="MilkDecorator.java">MilkDecorator.java</button>
<button class="tab-btn" data-tab="Order.java">Order.java</button>
<button class="tab-btn" data-tab="SugarDecorator.java">SugarDecorator.java</button>
<button class="tab-btn" data-tab="VanillaDecorator.java">VanillaDecorator.java</button>
<button class="tab-btn" data-tab="WhipCreamDecorator.java">WhipCreamDecorator.java</button>
</div>
<div class="tab-content active" id="Americano-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Concrete coffee: Americano with base cost $2.50. */
public class Americano extends Coffee {

    private static final double BASE_COST = 2.5;

    public Americano(CoffeeSize size) {
        super(size);
    }

    @Override
    public String getDescription() {
        return &quot;Americano&quot;;
    }

    @Override
    public double getCost() {
        return BASE_COST * size.getSizeMultiplier();
    }
}</code></pre>
</div>
<div class="tab-content" id="CaramelDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Adds caramel drizzle to the coffee (+$0.50). */
public class CaramelDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public CaramelDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + &quot;, Caramel&quot;;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}</code></pre>
</div>
<div class="tab-content" id="Coffee-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/**
 * Abstract base class for all coffee types.
 * Defines the interface for description, cost, and size.
 */
public abstract class Coffee {

    protected CoffeeSize size;

    public Coffee(CoffeeSize size) {
        this.size = size;
    }

    /** Returns human-readable description of the coffee. */
    public abstract String getDescription();

    /** Returns base cost adjusted by size multiplier. */
    public abstract double getCost();

    public CoffeeSize getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format(&quot;%s [%s] - $%.2f&quot;, getDescription(), size, getCost());
    }
}</code></pre>
</div>
<div class="tab-content" id="CoffeeDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

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
}</code></pre>
</div>
<div class="tab-content" id="CoffeeMachineService-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for the coffee machine system.
 * Handles placing orders, processing them, and maintaining history.
 */
public class CoffeeMachineService {

    private final List&lt;Order&gt; pendingOrders = new ArrayList&lt;&gt;();
    private final List&lt;Order&gt; orderHistory = new ArrayList&lt;&gt;();
    private final CoffeeMachineTemplate machine;

    public CoffeeMachineService(CoffeeMachineTemplate machine) {
        this.machine = machine;
    }

    /**
     * Places a new order for the given (optionally decorated) coffee.
     *
     * @param coffee   the coffee (may be wrapped in decorators)
     * @param quantity number of cups
     * @return the created Order
     */
    public Order placeOrder(Coffee coffee, int quantity) {
        if (quantity &lt;= 0) {
            throw new IllegalArgumentException(&quot;Quantity must be positive&quot;);
        }
        Order order = new Order(coffee, quantity);
        pendingOrders.add(order);
        System.out.println(&quot;Order placed: &quot; + order);
        return order;
    }

    /**
     * Processes the next pending order.
     *
     * @return the processed Order, or empty if no pending orders
     */
    public Optional&lt;Order&gt; processOrder() {
        if (pendingOrders.isEmpty()) {
            System.out.println(&quot;No pending orders.&quot;);
            return Optional.empty();
        }
        Order order = pendingOrders.remove(0);
        System.out.println(&quot;Processing: &quot; + order);
        // Simulate machine work for each cup
        for (int i = 0; i &lt; order.getQuantity(); i++) {
            machine.makeCoffee(order.getCoffee().getSize());
        }
        orderHistory.add(order);
        System.out.println(&quot;Order completed: &quot; + order.getOrderId());
        return Optional.of(order);
    }

    /** Returns an immutable view of the order history. */
    public List&lt;Order&gt; getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    /** Returns count of pending orders. */
    public int getPendingCount() {
        return pendingOrders.size();
    }
}</code></pre>
</div>
<div class="tab-content" id="CoffeeMachineTemplate-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

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
        System.out.println(&quot;=== Starting coffee preparation ===&quot;);
        grindBeans();
        heatWater();
        brew();
        addIngredients();
        Coffee coffee = createCoffee(size);
        serveCoffee();
        System.out.println(&quot;=== Coffee ready: &quot; + coffee + &quot; ===&quot;);
        return coffee;
    }

    /** Factory method to create the concrete Coffee object. */
    protected abstract Coffee createCoffee(CoffeeSize size);
}</code></pre>
</div>
<div class="tab-content" id="CoffeeSize-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/**
 * Enum representing coffee sizes with a multiplier applied to base cost.
 */
public enum CoffeeSize {
    SMALL(0.8),
    MEDIUM(1.0),
    LARGE(1.3);

    private final double sizeMultiplier;

    CoffeeSize(double sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;
    }

    public double getSizeMultiplier() {
        return sizeMultiplier;
    }
}</code></pre>
</div>
<div class="tab-content" id="DripCoffeeMachine-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/**
 * Concrete drip coffee machine — brews Americano using filter drip method.
 */
public class DripCoffeeMachine extends CoffeeMachineTemplate {

    @Override
    protected void grindBeans() {
        System.out.println(&quot;[DripCoffeeMachine] Grinding beans to medium coarseness...&quot;);
    }

    @Override
    protected void heatWater() {
        System.out.println(&quot;[DripCoffeeMachine] Heating water to 90°C...&quot;);
    }

    @Override
    protected void brew() {
        System.out.println(&quot;[DripCoffeeMachine] Dripping hot water through filter basket...&quot;);
    }

    @Override
    protected void addIngredients() {
        System.out.println(&quot;[DripCoffeeMachine] Topping with hot water for Americano style.&quot;);
    }

    @Override
    protected void serveCoffee() {
        System.out.println(&quot;[DripCoffeeMachine] Pouring into a large mug.&quot;);
    }

    @Override
    protected Coffee createCoffee(CoffeeSize size) {
        return new Americano(size);
    }
}</code></pre>
</div>
<div class="tab-content" id="Espresso-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Concrete coffee: Espresso with base cost $2.00. */
public class Espresso extends Coffee {

    private static final double BASE_COST = 2.0;

    public Espresso(CoffeeSize size) {
        super(size);
    }

    @Override
    public String getDescription() {
        return &quot;Espresso&quot;;
    }

    @Override
    public double getCost() {
        return BASE_COST * size.getSizeMultiplier();
    }
}</code></pre>
</div>
<div class="tab-content" id="EspressoMachine-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/**
 * Concrete espresso machine — brews espresso using high-pressure extraction.
 */
public class EspressoMachine extends CoffeeMachineTemplate {

    @Override
    protected void grindBeans() {
        System.out.println(&quot;[EspressoMachine] Grinding beans to fine espresso grind...&quot;);
    }

    @Override
    protected void heatWater() {
        System.out.println(&quot;[EspressoMachine] Heating water to 93°C...&quot;);
    }

    @Override
    protected void brew() {
        System.out.println(&quot;[EspressoMachine] Extracting espresso shot under 9 bar pressure...&quot;);
    }

    @Override
    protected void addIngredients() {
        System.out.println(&quot;[EspressoMachine] No extra ingredients for base espresso.&quot;);
    }

    @Override
    protected void serveCoffee() {
        System.out.println(&quot;[EspressoMachine] Serving espresso in a pre-warmed cup.&quot;);
    }

    @Override
    protected Coffee createCoffee(CoffeeSize size) {
        return new Espresso(size);
    }
}</code></pre>
</div>
<div class="tab-content" id="Latte-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Concrete coffee: Latte with base cost $3.00. */
public class Latte extends Coffee {

    private static final double BASE_COST = 3.0;

    public Latte(CoffeeSize size) {
        super(size);
    }

    @Override
    public String getDescription() {
        return &quot;Latte&quot;;
    }

    @Override
    public double getCost() {
        return BASE_COST * size.getSizeMultiplier();
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

import java.util.Optional;

/**
 * Demo runner for the Coffee Machine LLD.
 * Patterns: Decorator (add-ons), Template Method (machine brew steps)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) {

        banner(&quot;Scenario 1: Plain coffees -- base cost and description&quot;);
        Coffee espresso  = new Espresso(CoffeeSize.MEDIUM);
        Coffee americano = new Americano(CoffeeSize.MEDIUM);
        Coffee latte     = new Latte(CoffeeSize.MEDIUM);
        printCoffee(espresso);
        printCoffee(americano);
        printCoffee(latte);

        banner(&quot;Scenario 2: Decorated coffees -- stack add-ons&quot;);
        Coffee fancyLatte = new WhipCreamDecorator(
                               new CaramelDecorator(
                               new VanillaDecorator(
                               new SugarDecorator(
                               new MilkDecorator(new Latte(CoffeeSize.LARGE))))));
        printCoffee(fancyLatte);

        Coffee milkEspresso = new MilkDecorator(new SugarDecorator(new Espresso(CoffeeSize.SMALL)));
        printCoffee(milkEspresso);

        banner(&quot;Scenario 3: Cost comparison -- decorators add up correctly&quot;);
        Coffee base    = new Americano(CoffeeSize.MEDIUM);
        Coffee withOne = new MilkDecorator(new Americano(CoffeeSize.MEDIUM));
        Coffee withTwo = new MilkDecorator(new SugarDecorator(new Americano(CoffeeSize.MEDIUM)));
        System.out.printf(&quot;  Americano alone:   $%.2f%n&quot;, base.getCost());
        System.out.printf(&quot;  + Milk:            $%.2f (diff: +$%.2f)%n&quot;,
                withOne.getCost(), withOne.getCost() - base.getCost());
        System.out.printf(&quot;  + Milk + Sugar:    $%.2f (diff: +$%.2f)%n&quot;,
                withTwo.getCost(), withTwo.getCost() - base.getCost());

        banner(&quot;Scenario 4: EspressoMachine template steps&quot;);
        CoffeeMachineTemplate espressoMachine = new EspressoMachine();
        espressoMachine.makeCoffee(CoffeeSize.MEDIUM);

        banner(&quot;Scenario 5: DripCoffeeMachine template steps&quot;);
        CoffeeMachineTemplate dripMachine = new DripCoffeeMachine();
        dripMachine.makeCoffee(CoffeeSize.LARGE);

        banner(&quot;Scenario 6: CoffeeMachineService -- order history&quot;);
        CoffeeMachineService service = new CoffeeMachineService(new EspressoMachine());
        Coffee order1Coffee = new MilkDecorator(new Latte(CoffeeSize.MEDIUM));
        Coffee order2Coffee = new SugarDecorator(new Espresso(CoffeeSize.SMALL));
        Order o1 = service.placeOrder(order1Coffee, 2);
        Order o2 = service.placeOrder(order2Coffee, 1);
        Optional&lt;Order&gt; processed1 = service.processOrder();
        Optional&lt;Order&gt; processed2 = service.processOrder();

        System.out.println(&quot;  Order history:&quot;);
        service.getOrderHistory().forEach(o -&gt;
                System.out.printf(&quot;    Order %s: %s x%d = $%.2f%n&quot;,
                        o.getOrderId(), o.getCoffee().getDescription(),
                        o.getQuantity(), o.getTotalCost()));

        System.out.println();
        System.out.println(&quot;=== Coffee Machine Demo Complete ===&quot;);
    }

    private static void printCoffee(Coffee coffee) {
        System.out.printf(&quot;  %-60s $%.2f%n&quot;, coffee.getDescription(), coffee.getCost());
    }
}</code></pre>
</div>
<div class="tab-content" id="MilkDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Adds milk to the coffee (+$0.50). */
public class MilkDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + &quot;, Milk&quot;;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}</code></pre>
</div>
<div class="tab-content" id="Order-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a customer order containing a (possibly decorated) coffee.
 */
public class Order {

    private final String orderId;
    private final Coffee coffee;
    private final int quantity;
    private final double totalCost;
    private final LocalDateTime timestamp;

    public Order(Coffee coffee, int quantity) {
        this.orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.coffee = coffee;
        this.quantity = quantity;
        this.totalCost = coffee.getCost() * quantity;
        this.timestamp = LocalDateTime.now();
    }

    public String getOrderId() { return orderId; }
    public Coffee getCoffee() { return coffee; }
    public int getQuantity() { return quantity; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format(&quot;Order[%s] %dx %s = $%.2f @ %s&quot;,
                orderId, quantity, coffee.getDescription(), totalCost, timestamp);
    }
}</code></pre>
</div>
<div class="tab-content" id="SugarDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Adds sugar to the coffee (+$0.50). */
public class SugarDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + &quot;, Sugar&quot;;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}</code></pre>
</div>
<div class="tab-content" id="VanillaDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Adds vanilla syrup to the coffee (+$0.50). */
public class VanillaDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public VanillaDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + &quot;, Vanilla&quot;;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}</code></pre>
</div>
<div class="tab-content" id="WhipCreamDecorator-java">
<pre><code class="language-java">package org.interview.system_design.lld.coffeemachine;

/** Adds whipped cream to the coffee (+$0.50). */
public class WhipCreamDecorator extends CoffeeDecorator {

    private static final double ADDON_COST = 0.5;

    public WhipCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + &quot;, Whip Cream&quot;;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + ADDON_COST;
    }
}</code></pre>
</div>
</div>
