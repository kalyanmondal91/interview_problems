---
layout: design_pattern
title: "Strategy"
category: "behavioral"
difficulty: "Medium"
also_known_as: "Policy"
intent: "Define a family of algorithms, encapsulate each one, and make them interchangeable."
tags: [behavioral, strategy]
render_with_liquid: false
---

## Intent

Define a family of algorithms, encapsulate each one, and make them interchangeable.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Strategy** | Declares the algorithm method |
| **ConcreteStrategy** | Implements a specific algorithm variant |
| **Context** | Holds a Strategy reference; delegates the algorithm to it |
| **Client** | Creates context with desired strategy; can swap strategies |

## Pros

- Open/Closed: add strategies without changing context
- Replace inheritance with composition
- Isolate algorithm from client
- Switch algorithms at runtime

## Cons

- Client must be aware of strategies to pick the right one
- Lambdas can often replace Strategy classes for simple cases

## Real-World Examples

- `java.util.Comparator`
- `Spring Security AuthenticationStrategy`
- `Payment methods: CreditCard, PayPal, Bitcoin`
- `Route planning: fastest, shortest, cheapest`

## Variants

- Classic Strategy (interface + classes)
- Lambda Strategy (Java 8+ functional interfaces)

## Full Java Implementation

```java
package org.interview.design_patterns.behavioral.strategy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * ============================================================
 * Design Pattern: Strategy (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Define a family of algorithms, encapsulate each one, and make
 *   them interchangeable. Strategy lets the algorithm vary
 *   independently from clients that use it.
 *
 * ALSO KNOWN AS: Policy
 *
 * PROBLEM IT SOLVES:
 *   - You have a class that uses different algorithms for the same
 *     operation (sorting, payment, routing, compression).
 *   - Conditional logic (if/switch on algorithm type) clutters the class.
 *   - Algorithms must be swappable at runtime.
 *
 * STRUCTURE:
 *   - Strategy (interface): declares the algorithm method
 *   - ConcreteStrategy: implements a specific algorithm variant
 *   - Context: holds a Strategy reference; delegates the algorithm to it
 *   - Client: creates context with desired strategy; can swap strategies
 *
 * DIFFERENCE FROM TEMPLATE METHOD:
 *   - Template Method: uses INHERITANCE; parent defines skeleton; subclasses fill steps
 *   - Strategy: uses COMPOSITION; algorithm is swapped at runtime
 *
 * REAL-WORLD EXAMPLES:
 *   - java.util.Comparator (sort strategy)
 *   - Spring Security AuthenticationStrategy
 *   - Payment methods: CreditCard, PayPal, Bitcoin
 *   - Route planning: fastest, shortest, cheapest
 *   - Compression: ZIP, GZIP, LZ77
 *
 * PROS:
 *   + Open/Closed: add strategies without changing context
 *   + Replace inheritance with composition
 *   + Isolate algorithm from client; easy to test each strategy
 *   + Switch algorithms at runtime
 *
 * CONS:
 *   - Client must be aware of strategies to pick the right one
 *   - Function references (lambdas) can often replace Strategy classes
 *
 * SCENARIO:
 *   E-commerce checkout with pluggable payment strategies.
 *   + Also shows: data sorting with pluggable sort strategies.
 */
public class StrategyPattern {

    // ================================================================
    // EXAMPLE 1: Payment Strategy
    // ================================================================

    /** Strategy interface: all payment methods implement this */
    interface PaymentStrategy {
        boolean pay(double amount);
        String getMethodName();
    }

    /** Concrete Strategy: Credit Card */
    static class CreditCardStrategy implements PaymentStrategy {
        private final String cardNumber;
        private final String cvv;
        private double balance;

        CreditCardStrategy(String cardNumber, String cvv, double balance) {
            this.cardNumber = cardNumber;
            this.cvv        = cvv;
            this.balance    = balance;
        }

        @Override
        public boolean pay(double amount) {
            if (balance < amount) {
                System.out.printf("  [CreditCard] DECLINED — insufficient funds ($%.2f < $%.2f)%n",
                    balance, amount);
                return false;
            }
            balance -= amount;
            System.out.printf("  [CreditCard] Charged $%.2f to card ending %s | Remaining: $%.2f%n",
                amount, cardNumber.substring(cardNumber.length() - 4), balance);
            return true;
        }

        @Override public String getMethodName() { return "Credit Card"; }
    }

    /** Concrete Strategy: PayPal */
    static class PayPalStrategy implements PaymentStrategy {
        private final String email;
        private double balance;

        PayPalStrategy(String email, double balance) {
            this.email   = email;
            this.balance = balance;
        }

        @Override
        public boolean pay(double amount) {
            if (balance < amount) {
                System.out.printf("  [PayPal] FAILED — balance too low ($%.2f)%n", balance);
                return false;
            }
            balance -= amount;
            System.out.printf("  [PayPal] $%.2f paid from %s | Remaining: $%.2f%n",
                amount, email, balance);
            return true;
        }

        @Override public String getMethodName() { return "PayPal"; }
    }

    /** Concrete Strategy: Bitcoin */
    static class BitcoinStrategy implements PaymentStrategy {
        private final String walletAddress;

        BitcoinStrategy(String walletAddress) {
            this.walletAddress = walletAddress;
        }

        @Override
        public boolean pay(double amount) {
            double btcRate = 65_000.0; // USD per BTC (simplified)
            double btcAmount = amount / btcRate;
            System.out.printf("  [Bitcoin] Sending %.8f BTC to wallet %s... (=$%.2f)%n",
                btcAmount, walletAddress.substring(0, 10) + "...", amount);
            // Simulate blockchain confirmation
            System.out.println("  [Bitcoin] Transaction broadcast to mempool. Awaiting confirmation.");
            return true;
        }

        @Override public String getMethodName() { return "Bitcoin"; }
    }

    /**
     * Context: Shopping Cart
     * Holds a payment strategy; delegates payment to it.
     * Strategy can be changed at any time (even mid-session).
     */
    static class ShoppingCart {
        private PaymentStrategy paymentStrategy;
        private double          total;

        void setPaymentStrategy(PaymentStrategy strategy) {
            this.paymentStrategy = strategy;
            System.out.println("[Cart] Payment method set to: " + strategy.getMethodName());
        }

        void addItem(String name, double price) {
            System.out.printf("[Cart] Added: %s ($%.2f)%n", name, price);
            total += price;
        }

        void checkout() {
            System.out.printf("[Cart] Checkout — Total: $%.2f%n", total);
            if (paymentStrategy == null) {
                throw new IllegalStateException("No payment method selected!");
            }
            boolean success = paymentStrategy.pay(total);
            System.out.println("[Cart] Payment " + (success ? "SUCCESSFUL ✓" : "FAILED ✗"));
            if (success) total = 0;
        }
    }

    // ================================================================
    // EXAMPLE 2: Sorting Strategy (classic textbook example)
    // Demonstrates runtime algorithm switching
    // ================================================================

    /** Strategy interface: sort algorithm */
    interface SortStrategy {
        void sort(int[] arr);
        String name();
    }

    static class BubbleSortStrategy implements SortStrategy {
        @Override
        public void sort(int[] arr) {
            // O(n^2) — simple but slow; good for nearly-sorted or tiny arrays
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                    }
                }
            }
        }
        @Override public String name() { return "BubbleSort O(n²)"; }
    }

    static class QuickSortStrategy implements SortStrategy {
        @Override
        public void sort(int[] arr) {
            // O(n log n) avg — fast; good for general use
            quickSort(arr, 0, arr.length - 1);
        }

        private void quickSort(int[] arr, int lo, int hi) {
            if (lo < hi) {
                int pivot = partition(arr, lo, hi);
                quickSort(arr, lo, pivot - 1);
                quickSort(arr, pivot + 1, hi);
            }
        }

        private int partition(int[] arr, int lo, int hi) {
            int pivot = arr[hi];
            int i = lo - 1;
            for (int j = lo; j < hi; j++) {
                if (arr[j] <= pivot) { i++; int t = arr[i]; arr[i] = arr[j]; arr[j] = t; }
            }
            int t = arr[i + 1]; arr[i + 1] = arr[hi]; arr[hi] = t;
            return i + 1;
        }

        @Override public String name() { return "QuickSort O(n log n)"; }
    }

    /** Context: Sorter — holds a SortStrategy; can swap it at runtime */
    static class Sorter {
        private SortStrategy strategy;

        void setStrategy(SortStrategy strategy) {
            this.strategy = strategy;
        }

        void sort(int[] arr) {
            System.out.println("Sorting with " + strategy.name() + ": " + Arrays.toString(arr));
            strategy.sort(arr);
            System.out.println("Result: " + Arrays.toString(arr));
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Payment Strategy ===");
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Laptop",       1299.99);
        cart.addItem("USB Hub",        39.99);
        cart.addItem("Mechanical KB", 129.99);

        // Pay with credit card
        cart.setPaymentStrategy(new CreditCardStrategy("4111111111111234", "123", 2000.00));
        cart.checkout();

        // Rebuild cart and pay with PayPal
        cart.addItem("Monitor", 449.99);
        cart.setPaymentStrategy(new PayPalStrategy("alice@example.com", 500.00));
        cart.checkout();

        // Build cart and pay with Bitcoin
        cart.addItem("SSD Drive", 89.99);
        cart.setPaymentStrategy(new BitcoinStrategy("1A1zP1eP5QGefi2DMPTfTL5SLmv7Divf"));
        cart.checkout();

        System.out.println("\n=== Sorting Strategy ===");
        Sorter sorter = new Sorter();

        // Small array → use BubbleSort (simpler, no overhead)
        sorter.setStrategy(new BubbleSortStrategy());
        sorter.sort(new int[]{5, 3, 8, 1, 9, 2});

        // Large data → switch to QuickSort at runtime
        sorter.setStrategy(new QuickSortStrategy());
        sorter.sort(new int[]{64, 34, 25, 12, 22, 11, 90});

        // Java 8+ alternative: Strategy as lambda (Comparator)
        System.out.println("\n=== Strategy as Lambda (Java 8+) ===");
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob", "Diana");
        names.sort(Comparator.naturalOrder());
        System.out.println("Natural order: " + names);
        names.sort(Comparator.comparingInt(String::length).reversed());
        System.out.println("By length desc: " + names);
    }
}

```
