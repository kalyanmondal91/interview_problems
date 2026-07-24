---
layout: lld
render_with_liquid: false
title: "Vending Machine"
system: vendingmachine
description: "LLD of Vending Machine"
files:
  - "ChangeCalculator.java"
  - "Coin.java"
  - "CoinPaymentStrategy.java"
  - "DispensingState.java"
  - "HasMoneyState.java"
  - "IdleState.java"
  - "Inventory.java"
  - "ItemSelectedState.java"
  - "Main.java"
  - "PaymentStrategy.java"
  - "Product.java"
  - "VendingMachine.java"
  - "VendingMachineState.java"
  - "VendingMachineStateHandler.java"
---

## Vending Machine

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="ChangeCalculator.java">ChangeCalculator.java</button>
<button class="tab-btn" data-tab="Coin.java">Coin.java</button>
<button class="tab-btn" data-tab="CoinPaymentStrategy.java">CoinPaymentStrategy.java</button>
<button class="tab-btn" data-tab="DispensingState.java">DispensingState.java</button>
<button class="tab-btn" data-tab="HasMoneyState.java">HasMoneyState.java</button>
<button class="tab-btn" data-tab="IdleState.java">IdleState.java</button>
<button class="tab-btn" data-tab="Inventory.java">Inventory.java</button>
<button class="tab-btn" data-tab="ItemSelectedState.java">ItemSelectedState.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="PaymentStrategy.java">PaymentStrategy.java</button>
<button class="tab-btn" data-tab="Product.java">Product.java</button>
<button class="tab-btn" data-tab="VendingMachine.java">VendingMachine.java</button>
<button class="tab-btn" data-tab="VendingMachineState.java">VendingMachineState.java</button>
<button class="tab-btn" data-tab="VendingMachineStateHandler.java">VendingMachineStateHandler.java</button>
</div>
<div class="tab-content active" id="ChangeCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.EnumMap;
import java.util.Map;

/**
 * Greedy change calculator that returns the minimum number of coins.
 * Iterates from highest to lowest denomination.
 */
public class ChangeCalculator {

    private static final double EPSILON = 0.001;

    /**
     * Calculates the minimum coin set needed to make the given change amount.
     *
     * @param changeAmount the amount to return as change
     * @return map of coin denominations to counts; may be empty if no change needed
     * @throws IllegalArgumentException if exact change cannot be made
     */
    public Map&lt;Coin, Integer&gt; calculateChange(double changeAmount) {
        Map&lt;Coin, Integer&gt; change = new EnumMap&lt;&gt;(Coin.class);
        // Work in cents to avoid floating-point issues
        long remainingCents = Math.round(changeAmount * 100);

        // Coins in descending order of value
        Coin[] denominations = {Coin.DOLLAR, Coin.QUARTER, Coin.DIME, Coin.NICKEL, Coin.PENNY};

        for (Coin coin : denominations) {
            long coinCents = Math.round(coin.getValue() * 100);
            if (remainingCents &gt;= coinCents) {
                int count = (int) (remainingCents / coinCents);
                change.put(coin, count);
                remainingCents -= (long) count * coinCents;
            }
        }

        if (remainingCents &gt; 0) {
            throw new IllegalArgumentException(
                    &quot;Cannot make exact change for amount: &quot; + changeAmount);
        }
        return change;
    }

    /** Formats a change map as a human-readable string. */
    public String formatChange(Map&lt;Coin, Integer&gt; change) {
        if (change.isEmpty()) return &quot;No change&quot;;
        StringBuilder sb = new StringBuilder(&quot;Change: &quot;);
        change.forEach((coin, count) -&gt;
                sb.append(count).append(&quot;x&quot;).append(coin.name()).append(&quot; &quot;));
        return sb.toString().trim();
    }
}</code></pre>
</div>
<div class="tab-content" id="Coin-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * Enum representing coin denominations accepted by the vending machine.
 * Each coin has a monetary value in dollars.
 */
public enum Coin {
    PENNY(0.01),
    NICKEL(0.05),
    DIME(0.10),
    QUARTER(0.25),
    DOLLAR(1.00);

    private final double value;

    Coin(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + &quot;($&quot; + String.format(&quot;%.2f&quot;, value) + &quot;)&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="CoinPaymentStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.Map;

/**
 * Coin-based payment strategy.
 * Validates that the total inserted coin value meets or exceeds the required amount.
 */
public class CoinPaymentStrategy implements PaymentStrategy {

    /** Floating-point tolerance for coin comparisons. */
    private static final double EPSILON = 0.001;

    @Override
    public boolean pay(double amount, Map&lt;Coin, Integer&gt; coinSlot) {
        double totalInserted = coinSlot.entrySet().stream()
                .mapToDouble(e -&gt; e.getKey().getValue() * e.getValue())
                .sum();
        return totalInserted &gt;= amount - EPSILON;
    }

    /**
     * Computes the total inserted value from the coin slot.
     *
     * @param coinSlot map of coins and their counts
     * @return total monetary value
     */
    public double getTotalInserted(Map&lt;Coin, Integer&gt; coinSlot) {
        return coinSlot.entrySet().stream()
                .mapToDouble(e -&gt; e.getKey().getValue() * e.getValue())
                .sum();
    }
}</code></pre>
</div>
<div class="tab-content" id="DispensingState-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.Map;

/**
 * Handles the dispensing event: deducts product, computes and returns change,
 * then transitions back to IDLE.
 */
public class DispensingState implements VendingMachineStateHandler {
    private final ChangeCalculator changeCalculator = new ChangeCalculator();

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        System.out.println(&quot;Dispensing in progress. Cannot insert coin now.&quot;);
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        System.out.println(&quot;Dispensing in progress. Cannot select item now.&quot;);
    }

    @Override
    public void dispense(VendingMachine machine) {
        Product product = machine.getSelectedProduct();
        if (product == null) {
            System.out.println(&quot;Error: no product selected.&quot;);
            machine.setState(VendingMachineState.IDLE);
            return;
        }

        // Dispense the product
        product.dispense();
        System.out.println(&quot;Dispensing: &quot; + product.getName());

        // Calculate and return change
        double change = machine.getInsertedAmount() - product.getPrice();
        if (change &gt; 0.001) {
            try {
                Map&lt;Coin, Integer&gt; changeCoins = changeCalculator.calculateChange(change);
                System.out.println(changeCalculator.formatChange(changeCoins));
            } catch (IllegalArgumentException e) {
                System.out.printf(&quot;Returning change: $%.2f%n&quot;, change);
            }
        }

        // Reset machine
        machine.clearCoins();
        machine.setSelectedProduct(null);
        machine.setState(VendingMachineState.IDLE);
        System.out.println(&quot;Thank you for your purchase!&quot;);
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println(&quot;Dispensing in progress. Cannot cancel now.&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="HasMoneyState-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.Optional;

/**
 * Handles events when money has been inserted but no item is selected yet.
 * Accepts more coins and item selection; cancel returns inserted coins.
 */
public class HasMoneyState implements VendingMachineStateHandler {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.printf(&quot;Inserted %s. Total: $%.2f%n&quot;, coin, machine.getInsertedAmount());
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        Optional&lt;Product&gt; productOpt = machine.getInventory().getProduct(productId);
        if (productOpt.isEmpty()) {
            System.out.println(&quot;Product not found: &quot; + productId);
            return;
        }
        Product product = productOpt.get();
        if (!product.isAvailable()) {
            System.out.println(&quot;Product is out of stock: &quot; + product.getName());
            return;
        }
        if (machine.getInsertedAmount() &lt; product.getPrice() - 0.001) {
            System.out.printf(&quot;Insufficient funds. Required: $%.2f, Inserted: $%.2f%n&quot;,
                    product.getPrice(), machine.getInsertedAmount());
            return;
        }
        machine.setSelectedProduct(product);
        System.out.printf(&quot;Selected: %s ($%.2f)%n&quot;, product.getName(), product.getPrice());
        machine.setState(VendingMachineState.ITEM_SELECTED);
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println(&quot;Please select an item first.&quot;);
    }

    @Override
    public void cancel(VendingMachine machine) {
        double refund = machine.getInsertedAmount();
        machine.clearCoins();
        machine.setState(VendingMachineState.IDLE);
        System.out.printf(&quot;Transaction cancelled. Returning $%.2f%n&quot;, refund);
    }
}</code></pre>
</div>
<div class="tab-content" id="IdleState-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * Handles events when the vending machine is IDLE (no money inserted).
 * Only coin insertion is valid; all other operations are rejected.
 */
public class IdleState implements VendingMachineStateHandler {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.printf(&quot;Inserted %s. Total: $%.2f%n&quot;, coin, machine.getInsertedAmount());
        machine.setState(VendingMachineState.HAS_MONEY);
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        System.out.println(&quot;Please insert money first.&quot;);
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println(&quot;Please insert money and select an item first.&quot;);
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println(&quot;Nothing to cancel. Machine is idle.&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="Inventory-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the inventory of products in the vending machine.
 * Uses a Map keyed by product ID for O(1) lookups.
 */
public class Inventory {
    private final Map&lt;String, Product&gt; products;

    public Inventory() {
        this.products = new HashMap&lt;&gt;();
    }

    /** Adds or replaces a product in inventory. */
    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    /** Removes a product from inventory entirely. */
    public void removeProduct(String productId) {
        products.remove(productId);
    }

    /** Retrieves a product by ID, or empty if not found. */
    public Optional&lt;Product&gt; getProduct(String productId) {
        return Optional.ofNullable(products.get(productId));
    }

    /** Returns true if the product exists and has quantity &gt; 0. */
    public boolean isAvailable(String productId) {
        return products.containsKey(productId) &amp;&amp; products.get(productId).isAvailable();
    }

    /** Restocks a product by adding quantity. Creates product if it doesn&#x27;t exist. */
    public void restock(String productId, int quantity) {
        if (products.containsKey(productId)) {
            products.get(productId).restock(quantity);
        } else {
            throw new IllegalArgumentException(&quot;Product not found: &quot; + productId);
        }
    }

    public Map&lt;String, Product&gt; getAllProducts() {
        return Collections.unmodifiableMap(products);
    }

    /** Displays the current inventory to stdout. */
    public void display() {
        System.out.println(&quot;=== Inventory ===&quot;);
        if (products.isEmpty()) {
            System.out.println(&quot;  (empty)&quot;);
        } else {
            products.values().forEach(p -&gt; System.out.println(&quot;  &quot; + p));
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="ItemSelectedState-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * Handles events when an item has been selected and payment is sufficient.
 * Allows dispensing or cancellation; additional coins/re-selection also handled.
 */
public class ItemSelectedState implements VendingMachineStateHandler {

    @Override
    public void insertCoin(VendingMachine machine, Coin coin) {
        machine.addCoin(coin);
        System.out.printf(&quot;Extra coin inserted: %s. Total: $%.2f%n&quot;, coin, machine.getInsertedAmount());
    }

    @Override
    public void selectItem(VendingMachine machine, String productId) {
        System.out.println(&quot;Item already selected. Press dispense or cancel.&quot;);
    }

    @Override
    public void dispense(VendingMachine machine) {
        machine.setState(VendingMachineState.DISPENSING);
        machine.getStateHandler().dispense(machine);
    }

    @Override
    public void cancel(VendingMachine machine) {
        double refund = machine.getInsertedAmount();
        machine.clearCoins();
        machine.setSelectedProduct(null);
        machine.setState(VendingMachineState.IDLE);
        System.out.printf(&quot;Transaction cancelled. Returning $%.2f%n&quot;, refund);
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * Demo runner for the Vending Machine LLD.
 * Patterns: State Pattern, Strategy (payment, change)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) {

        VendingMachine machine = new VendingMachine();

        machine.loadProduct(new Product(&quot;P1&quot;, &quot;Cola&quot;,  0.75, 5));
        machine.loadProduct(new Product(&quot;P2&quot;, &quot;Water&quot;, 0.50, 3));
        machine.loadProduct(new Product(&quot;P3&quot;, &quot;Chips&quot;, 1.25, 2));
        machine.loadProduct(new Product(&quot;P4&quot;, &quot;Candy&quot;, 0.25, 0)); // Out of stock

        banner(&quot;Initial Inventory&quot;);
        machine.displayStatus();

        banner(&quot;Scenario 1: Buy Cola ($0.75) with exact change&quot;);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        System.out.printf(&quot;  Inserted: $%.2f%n&quot;, machine.getInsertedAmount());
        machine.selectItem(&quot;P1&quot;);
        machine.dispense();
        System.out.println(&quot;  State after: &quot; + machine.getCurrentState());

        banner(&quot;Scenario 2: Buy Chips ($1.25) with $2.00 -- expect $0.75 change&quot;);
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.DOLLAR);
        System.out.printf(&quot;  Inserted: $%.2f%n&quot;, machine.getInsertedAmount());
        machine.selectItem(&quot;P3&quot;);
        machine.dispense();
        System.out.println(&quot;  State after: &quot; + machine.getCurrentState());

        banner(&quot;Scenario 3: Select item without inserting money&quot;);
        machine.selectItem(&quot;P2&quot;);
        System.out.println(&quot;  State: &quot; + machine.getCurrentState() + &quot; (should stay IDLE)&quot;);

        banner(&quot;Scenario 4: Insert $0.50 then cancel -- expect refund&quot;);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        System.out.printf(&quot;  Inserted: $%.2f%n&quot;, machine.getInsertedAmount());
        machine.cancel();
        System.out.printf(&quot;  After cancel -- inserted: $%.2f | state: %s%n&quot;,
                machine.getInsertedAmount(), machine.getCurrentState());

        banner(&quot;Scenario 5: Buy Candy (out-of-stock)&quot;);
        machine.insertCoin(Coin.QUARTER);
        machine.selectItem(&quot;P4&quot;);
        System.out.println(&quot;  State after selecting out-of-stock: &quot; + machine.getCurrentState());
        machine.cancel();

        banner(&quot;Scenario 6: Insufficient funds for Cola ($0.75)&quot;);
        machine.insertCoin(Coin.DIME);
        machine.insertCoin(Coin.NICKEL);
        System.out.printf(&quot;  Inserted: $%.2f (need $0.75)%n&quot;, machine.getInsertedAmount());
        machine.selectItem(&quot;P1&quot;);
        System.out.println(&quot;  State: &quot; + machine.getCurrentState());
        machine.cancel();

        banner(&quot;Final Inventory&quot;);
        machine.displayStatus();

        System.out.println();
        System.out.println(&quot;=== Vending Machine Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="PaymentStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.Map;

/**
 * Strategy interface for payment processing in the vending machine.
 * Allows different payment methods (coin, card, etc.) to be supported.
 */
public interface PaymentStrategy {
    /**
     * Processes a payment attempt.
     *
     * @param amount   the total amount required
     * @param coinSlot the coins currently inserted, keyed by coin type
     * @return true if payment is successful (sufficient amount inserted)
     */
    boolean pay(double amount, Map&lt;Coin, Integer&gt; coinSlot);
}</code></pre>
</div>
<div class="tab-content" id="Product-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * Represents a product available in the vending machine.
 */
public class Product {
    private final String productId;
    private final String name;
    private final double price;
    private int quantity;

    public Product(String productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isAvailable() { return quantity &gt; 0; }

    /** Decrements quantity by 1 after dispense. */
    public void dispense() {
        if (quantity &lt;= 0) throw new IllegalStateException(&quot;Product out of stock: &quot; + name);
        quantity--;
    }

    /** Restocks by adding the given amount. */
    public void restock(int amount) {
        if (amount &lt; 0) throw new IllegalArgumentException(&quot;Restock amount must be non-negative&quot;);
        quantity += amount;
    }

    @Override
    public String toString() {
        return String.format(&quot;Product{id=&#x27;%s&#x27;, name=&#x27;%s&#x27;, price=$%.2f, qty=%d}&quot;,
                productId, name, price, quantity);
    }
}</code></pre>
</div>
<div class="tab-content" id="VendingMachine-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Context class for the Vending Machine State pattern.
 *
 * Maintains current state, inserted coins, selected product, and inventory.
 * Delegates all user interactions to the active state handler.
 *
 * State transitions:
 *   IDLE → HAS_MONEY (on insertCoin)
 *   HAS_MONEY → ITEM_SELECTED (on selectItem with enough money)
 *   HAS_MONEY → IDLE (on cancel)
 *   ITEM_SELECTED → DISPENSING → IDLE (on dispense)
 *   ITEM_SELECTED → IDLE (on cancel)
 */
public class VendingMachine {
    private VendingMachineState currentState;
    private final Map&lt;VendingMachineState, VendingMachineStateHandler&gt; stateHandlers;
    private final Map&lt;Coin, Integer&gt; coinSlot;
    private Product selectedProduct;
    private final Inventory inventory;

    public VendingMachine() {
        this.inventory = new Inventory();
        this.coinSlot = new EnumMap&lt;&gt;(Coin.class);
        this.stateHandlers = new HashMap&lt;&gt;();

        // Register all state handlers
        stateHandlers.put(VendingMachineState.IDLE,          new IdleState());
        stateHandlers.put(VendingMachineState.HAS_MONEY,     new HasMoneyState());
        stateHandlers.put(VendingMachineState.ITEM_SELECTED, new ItemSelectedState());
        stateHandlers.put(VendingMachineState.DISPENSING,    new DispensingState());

        this.currentState = VendingMachineState.IDLE;
    }

    // ---- Public API (delegates to current state handler) ----

    public void insertCoin(Coin coin) {
        getStateHandler().insertCoin(this, coin);
    }

    public void selectItem(String productId) {
        getStateHandler().selectItem(this, productId);
    }

    public void dispense() {
        getStateHandler().dispense(this);
    }

    public void cancel() {
        getStateHandler().cancel(this);
    }

    // ---- State management ----

    public VendingMachineState getCurrentState() { return currentState; }

    public void setState(VendingMachineState state) {
        this.currentState = state;
        System.out.println(&quot;[State → &quot; + state + &quot;]&quot;);
    }

    public VendingMachineStateHandler getStateHandler() {
        return stateHandlers.get(currentState);
    }

    // ---- Coin slot management ----

    public void addCoin(Coin coin) {
        coinSlot.merge(coin, 1, Integer::sum);
    }

    public double getInsertedAmount() {
        return coinSlot.entrySet().stream()
                .mapToDouble(e -&gt; e.getKey().getValue() * e.getValue())
                .sum();
    }

    public void clearCoins() {
        coinSlot.clear();
    }

    public Map&lt;Coin, Integer&gt; getCoinSlot() { return coinSlot; }

    // ---- Product selection ----

    public Product getSelectedProduct() { return selectedProduct; }

    public void setSelectedProduct(Product product) { this.selectedProduct = product; }

    // ---- Inventory ----

    public Inventory getInventory() { return inventory; }

    /** Convenience: load a product directly from the service layer. */
    public void loadProduct(Product product) {
        inventory.addProduct(product);
    }

    public void displayStatus() {
        System.out.println(&quot;State: &quot; + currentState
                + &quot; | Inserted: $&quot; + String.format(&quot;%.2f&quot;, getInsertedAmount())
                + &quot; | Selected: &quot; + (selectedProduct != null ? selectedProduct.getName() : &quot;none&quot;));
        inventory.display();
    }

    /**
     * Demo main method.
     */
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        vm.loadProduct(new Product(&quot;P1&quot;, &quot;Cola&quot;,   1.25, 5));
        vm.loadProduct(new Product(&quot;P2&quot;, &quot;Chips&quot;,  0.75, 3));
        vm.loadProduct(new Product(&quot;P3&quot;, &quot;Water&quot;,  1.00, 10));

        System.out.println(&quot;--- Buying Cola ---&quot;);
        vm.insertCoin(Coin.DOLLAR);
        vm.insertCoin(Coin.QUARTER);
        vm.selectItem(&quot;P1&quot;);
        vm.dispense();

        System.out.println(&quot;\n--- Buying Chips with overpay ---&quot;);
        vm.insertCoin(Coin.DOLLAR);
        vm.selectItem(&quot;P2&quot;);
        vm.dispense();

        System.out.println(&quot;\n--- Cancel test ---&quot;);
        vm.insertCoin(Coin.QUARTER);
        vm.cancel();
    }
}</code></pre>
</div>
<div class="tab-content" id="VendingMachineState-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * Enum representing the states of the vending machine.
 */
public enum VendingMachineState {
    IDLE,
    HAS_MONEY,
    ITEM_SELECTED,
    DISPENSING,
    MAINTENANCE
}</code></pre>
</div>
<div class="tab-content" id="VendingMachineStateHandler-java">
<pre><code class="language-java">package org.interview.system_design.lld.vendingmachine;

/**
 * State handler interface for the vending machine State pattern.
 * Each concrete state implements the allowed operations for that state.
 */
public interface VendingMachineStateHandler {
    /** Insert a coin into the machine. */
    void insertCoin(VendingMachine machine, Coin coin);

    /** Select an item by product ID. */
    void selectItem(VendingMachine machine, String productId);

    /** Dispense the selected item. */
    void dispense(VendingMachine machine);

    /** Cancel the transaction and return inserted coins. */
    void cancel(VendingMachine machine);
}</code></pre>
</div>
</div>
