---
layout: design_pattern
title: "Visitor"
category: "behavioral"
difficulty: "Hard"
intent: "Represent an operation to be performed on elements of an object structure."
tags: [behavioral, visitor]
render_with_liquid: false
---

## Intent

Represent an operation to be performed on elements of an object structure.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Visitor** | One visit() method per ConcreteElement type |
| **ConcreteVisitor** | Implements the specific operation for each element |
| **Element** | Declares accept(Visitor) method |
| **ConcreteElement** | Implements accept() by calling visitor.visit(this) |
| **ObjectStructure** | Iterates elements and passes visitor to each |

## Pros

- Open/Closed for Visitors: add new operations without touching elements
- Single Responsibility: each visitor has one job
- Accumulate state across element visits

## Cons

- Violates Open/Closed for Elements: adding a new element requires updating ALL visitors
- Breaks encapsulation: elements may expose internal state to visitors

## Real-World Examples

- `Java compiler AST passes`
- `XML/DOM traversal`
- `Tax calculation on different financial instruments`
- `Serialization frameworks (Jackson traverses object graphs)`

## Variants

- Single dispatch
- Double dispatch (the classic GoF Visitor technique)

## Full Java Implementation

```java
package org.interview.design_patterns.behavioral.visitor;

import java.util.List;

/**
 * ============================================================
 * Design Pattern: Visitor (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Represent an operation to be performed on elements of an object
 *   structure. Visitor lets you define a new operation WITHOUT changing
 *   the classes of the elements on which it operates.
 *
 * PROBLEM IT SOLVES:
 *   - You have a stable class hierarchy (doesn't change often).
 *   - You need to add many DIFFERENT operations on those classes.
 *   - Adding each operation directly to the classes would pollute them.
 *
 * STRUCTURE:
 *   - Visitor (interface): one visit() method per ConcreteElement type
 *   - ConcreteVisitor: implements the specific operation for each element
 *   - Element (interface): declares accept(Visitor) method
 *   - ConcreteElement: implements accept() by calling visitor.visit(this)
 *   - ObjectStructure: iterates elements and passes visitor to each
 *
 * KEY TECHNIQUE: "Double Dispatch"
 *   Calling element.accept(visitor) dispatches first on element type,
 *   then visitor.visit(element) dispatches on visitor type.
 *   This achieves runtime polymorphism on both types simultaneously.
 *
 * REAL-WORLD EXAMPLES:
 *   - Java compiler AST passes (parsing → type checking → code gen)
 *   - XML/DOM traversal
 *   - Tax calculation on different financial instruments
 *   - Serialization frameworks (Jackson traverses object graphs)
 *
 * PROS:
 *   + Open/Closed for VISITORS: add new operations without touching elements
 *   + Single Responsibility: each visitor has one clearly-defined job
 *   + Accumulate state across element visits
 *
 * CONS:
 *   - Violates Open/Closed for ELEMENTS: adding a new element type requires
 *     updating ALL visitor implementations
 *   - Breaks encapsulation: elements may expose internal state to visitors
 *
 * SCENARIO:
 *   An e-commerce shopping cart with different item types.
 *   Visitors calculate: total price, taxes, and generate receipts.
 *   None of these operations are baked into the item classes.
 */
public class VisitorPattern {

    // ================================================================
    // Element Interface — all cart items implement accept(Visitor)
    // ================================================================
    interface CartItem {
        /**
         * "accept" is the key Visitor method — double dispatch hook.
         * Each element calls the appropriate visit() on the visitor.
         */
        void accept(CartVisitor visitor);
        String getName();
        double getPrice();
    }

    // ================================================================
    // Visitor Interface — one visit() method per ConcreteElement
    // ================================================================
    interface CartVisitor {
        void visit(PhysicalItem item);
        void visit(DigitalItem item);
        void visit(SubscriptionItem item);
    }

    // ================================================================
    // ConcreteElements — different cart item types
    // ================================================================

    /** Physical product (e.g., a book, laptop) — has shipping weight */
    static class PhysicalItem implements CartItem {
        private final String name;
        private final double price;
        private final double weightKg; // for shipping calculation

        PhysicalItem(String name, double price, double weightKg) {
            this.name      = name;
            this.price     = price;
            this.weightKg  = weightKg;
        }

        @Override
        public void accept(CartVisitor visitor) {
            visitor.visit(this); // dispatch: visits THIS specific type
        }

        public double getWeightKg() { return weightKg; }
        @Override public String getName()  { return name; }
        @Override public double getPrice() { return price; }
    }

    /** Digital product (e.g., e-book, software) — no shipping, no import tax */
    static class DigitalItem implements CartItem {
        private final String name;
        private final double price;
        private final String downloadUrl;

        DigitalItem(String name, double price, String downloadUrl) {
            this.name        = name;
            this.price       = price;
            this.downloadUrl = downloadUrl;
        }

        @Override
        public void accept(CartVisitor visitor) {
            visitor.visit(this);
        }

        public String getDownloadUrl() { return downloadUrl; }
        @Override public String getName()  { return name; }
        @Override public double getPrice() { return price; }
    }

    /** Subscription item (e.g., streaming service) — recurring charge */
    static class SubscriptionItem implements CartItem {
        private final String name;
        private final double monthlyPrice;
        private final int    months;

        SubscriptionItem(String name, double monthlyPrice, int months) {
            this.name         = name;
            this.monthlyPrice = monthlyPrice;
            this.months       = months;
        }

        @Override
        public void accept(CartVisitor visitor) {
            visitor.visit(this);
        }

        public double getMonthlyPrice() { return monthlyPrice; }
        public int    getMonths()       { return months; }
        @Override public String getName()  { return name; }
        @Override public double getPrice() { return monthlyPrice * months; } // total commitment
    }

    // ================================================================
    // ConcreteVisitor 1: Total Price Calculator
    // Accumulates the total cart value
    // ================================================================
    static class PriceCalculatorVisitor implements CartVisitor {
        private double total = 0;

        @Override
        public void visit(PhysicalItem item) {
            total += item.getPrice();
            System.out.printf("  [Price] Physical '%s': $%.2f%n", item.getName(), item.getPrice());
        }

        @Override
        public void visit(DigitalItem item) {
            total += item.getPrice();
            System.out.printf("  [Price] Digital  '%s': $%.2f%n", item.getName(), item.getPrice());
        }

        @Override
        public void visit(SubscriptionItem item) {
            double subtotal = item.getPrice();
            total += subtotal;
            System.out.printf("  [Price] Subscription '%s': $%.2f/mo × %d = $%.2f%n",
                item.getName(), item.getMonthlyPrice(), item.getMonths(), subtotal);
        }

        public double getTotal() { return total; }
    }

    // ================================================================
    // ConcreteVisitor 2: Tax Calculator
    // Different tax rules for different item types
    // ================================================================
    static class TaxCalculatorVisitor implements CartVisitor {
        private double totalTax = 0;
        // Tax rates
        private static final double PHYSICAL_TAX     = 0.08;  // 8% sales tax
        private static final double DIGITAL_TAX      = 0.05;  // 5% digital goods tax
        private static final double SUBSCRIPTION_TAX = 0.03;  // 3% service tax

        @Override
        public void visit(PhysicalItem item) {
            double tax = item.getPrice() * PHYSICAL_TAX;
            totalTax += tax;
            System.out.printf("  [Tax] Physical '%s': $%.2f (%.0f%% = $%.2f)%n",
                item.getName(), item.getPrice(), PHYSICAL_TAX * 100, tax);
        }

        @Override
        public void visit(DigitalItem item) {
            double tax = item.getPrice() * DIGITAL_TAX;
            totalTax += tax;
            System.out.printf("  [Tax] Digital '%s': $%.2f (%.0f%% = $%.2f)%n",
                item.getName(), item.getPrice(), DIGITAL_TAX * 100, tax);
        }

        @Override
        public void visit(SubscriptionItem item) {
            double tax = item.getPrice() * SUBSCRIPTION_TAX;
            totalTax += tax;
            System.out.printf("  [Tax] Subscription '%s': $%.2f (%.0f%% = $%.2f)%n",
                item.getName(), item.getPrice(), SUBSCRIPTION_TAX * 100, tax);
        }

        public double getTotalTax() { return totalTax; }
    }

    // ================================================================
    // ConcreteVisitor 3: Receipt Generator
    // Prints formatted receipt lines for each item
    // ================================================================
    static class ReceiptVisitor implements CartVisitor {
        private final StringBuilder receipt = new StringBuilder();

        ReceiptVisitor() {
            receipt.append("========================================\n");
            receipt.append("              ORDER RECEIPT             \n");
            receipt.append("========================================\n");
        }

        @Override
        public void visit(PhysicalItem item) {
            receipt.append(String.format("📦 %-25s $%7.2f%n", item.getName(), item.getPrice()));
            receipt.append(String.format("   Shipping: %.1f kg%n", item.getWeightKg()));
        }

        @Override
        public void visit(DigitalItem item) {
            receipt.append(String.format("💻 %-25s $%7.2f%n", item.getName(), item.getPrice()));
            receipt.append(String.format("   Download: %s%n", item.getDownloadUrl()));
        }

        @Override
        public void visit(SubscriptionItem item) {
            receipt.append(String.format("🔄 %-25s $%7.2f%n", item.getName(), item.getPrice()));
            receipt.append(String.format("   $%.2f/month × %d months%n",
                item.getMonthlyPrice(), item.getMonths()));
        }

        public String getReceipt() { return receipt.toString(); }
    }

    // ================================================================
    // Object Structure: ShoppingCart
    // Holds elements; applies visitors to all elements
    // ================================================================
    static class ShoppingCart {
        private final List<CartItem> items;

        ShoppingCart(List<CartItem> items) {
            this.items = items;
        }

        /** Apply a visitor to all items in the cart */
        public void accept(CartVisitor visitor) {
            for (CartItem item : items) {
                item.accept(visitor); // visitor visits each item
            }
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        // Build a mixed shopping cart
        List<CartItem> cartItems = List.of(
            new PhysicalItem("MacBook Pro",          1_999.99, 2.1),
            new PhysicalItem("Mechanical Keyboard",    149.99, 0.9),
            new DigitalItem("JetBrains IDE License",   249.00, "https://dl.jetbrains.com/..."),
            new DigitalItem("Photoshop Brushes Pack",   29.99, "https://adobe.com/download/..."),
            new SubscriptionItem("Netflix Premium",      22.99, 12),
            new SubscriptionItem("GitHub Copilot",       10.00, 6)
        );

        ShoppingCart cart = new ShoppingCart(cartItems);

        System.out.println("=== Price Calculation ===");
        PriceCalculatorVisitor priceVisitor = new PriceCalculatorVisitor();
        cart.accept(priceVisitor);
        System.out.printf("TOTAL: $%.2f%n", priceVisitor.getTotal());

        System.out.println("\n=== Tax Calculation ===");
        TaxCalculatorVisitor taxVisitor = new TaxCalculatorVisitor();
        cart.accept(taxVisitor);
        System.out.printf("TOTAL TAX: $%.2f%n", taxVisitor.getTotalTax());
        System.out.printf("GRAND TOTAL: $%.2f%n", priceVisitor.getTotal() + taxVisitor.getTotalTax());

        System.out.println("\n=== Receipt Generation ===");
        ReceiptVisitor receiptVisitor = new ReceiptVisitor();
        cart.accept(receiptVisitor);
        System.out.print(receiptVisitor.getReceipt());
    }
}

```
