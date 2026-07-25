---
layout: design_pattern
title: "Abstract Factory"
category: "creational"
difficulty: "Medium"
also_known_as: "Kit"
intent: "Provide an interface for creating families of related or dependent objects without specifying concrete classes."
tags: [creational, factory]
render_with_liquid: false
---

## Intent

Provide an interface for creating families of related or dependent objects without specifying concrete classes.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **AbstractFactory** | Declares creation methods for each product type |
| **ConcreteFactory** | Implements creation for a product family |
| **AbstractProduct** | Interface for each product type |
| **ConcreteProduct** | Implements a product for a specific family |
| **Client** | Uses only abstract interfaces |

## Pros

- Ensures product family compatibility
- Promotes consistency
- Supports Open/Closed Principle

## Cons

- Adding new product types requires changing AbstractFactory
- Can lead to many classes

## Real-World Examples

- `DocumentBuilderFactory`
- `Cross-platform UI toolkits`
- `Database driver families`

## Full Java Implementation

```java
package org.interview.design_patterns.creational.abstract_factory;

/**
 * ============================================================
 * Design Pattern: Abstract Factory (Creational)
 * ============================================================
 *
 * INTENT:
 *   Provide an interface for creating FAMILIES of related or
 *   dependent objects without specifying their concrete classes.
 *
 * DIFFERENCE FROM FACTORY METHOD:
 *   - Factory Method creates ONE product; Abstract Factory creates
 *     a FAMILY of related products.
 *   - Abstract Factory uses composition (holds factory references);
 *     Factory Method uses inheritance (subclasses override).
 *
 * STRUCTURE:
 *   - AbstractFactory: interface with creation methods for each product type
 *   - ConcreteFactory: implements AbstractFactory for a specific product family
 *   - AbstractProduct: interface for each product type
 *   - ConcreteProduct: implements the product for a specific family
 *   - Client: uses only abstract interfaces; decoupled from concrete classes
 *
 * REAL-WORLD EXAMPLES:
 *   - UI toolkit families: WindowsFactory vs MacFactory (buttons, checkboxes)
 *   - javax.xml.parsers.DocumentBuilderFactory
 *   - Database drivers: MySQLFactory vs PostgreSQLFactory (Connection, Statement)
 *
 * PROS:
 *   + Ensures product family compatibility
 *   + Promotes consistency among products
 *   + Supports Open/Closed Principle
 *
 * CONS:
 *   - Adding new product types requires changing the AbstractFactory interface
 *     (and all its implementations)
 *
 * SCENARIO:
 *   Cross-platform UI components. A Windows factory creates Windows-style
 *   buttons and checkboxes; a Mac factory creates Mac-style ones.
 *   The Application class uses only abstract interfaces.
 */
public class AbstractFactoryPattern {

    // ================================================================
    // Abstract Products
    // ================================================================

    /** Abstract product: Button */
    interface Button {
        void render();
        void onClick();
    }

    /** Abstract product: Checkbox */
    interface Checkbox {
        void render();
        void toggle();
    }

    // ================================================================
    // Concrete Products — Windows Family
    // ================================================================

    static class WindowsButton implements Button {
        @Override
        public void render() {
            System.out.println("[Windows] Rendering a rectangular button with Win11 style");
        }
        @Override
        public void onClick() {
            System.out.println("[Windows] Button clicked — Win32 event dispatched");
        }
    }

    static class WindowsCheckbox implements Checkbox {
        @Override
        public void render() {
            System.out.println("[Windows] Rendering a square checkbox (Win11 style)");
        }
        @Override
        public void toggle() {
            System.out.println("[Windows] Checkbox toggled — Win32 state updated");
        }
    }

    // ================================================================
    // Concrete Products — Mac Family
    // ================================================================

    static class MacButton implements Button {
        @Override
        public void render() {
            System.out.println("[Mac] Rendering a rounded button with macOS style");
        }
        @Override
        public void onClick() {
            System.out.println("[Mac] Button clicked — Cocoa event dispatched");
        }
    }

    static class MacCheckbox implements Checkbox {
        @Override
        public void render() {
            System.out.println("[Mac] Rendering a rounded checkbox (macOS style)");
        }
        @Override
        public void toggle() {
            System.out.println("[Mac] Checkbox toggled — Cocoa state updated");
        }
    }

    // ================================================================
    // Abstract Factory
    // Declares creation methods for EACH product type in the family.
    // ================================================================
    interface UIFactory {
        Button createButton();
        Checkbox createCheckbox();
    }

    // ================================================================
    // Concrete Factories — one per product family
    // ================================================================

    /** Creates Windows-style UI components */
    static class WindowsUIFactory implements UIFactory {
        @Override
        public Button createButton() {
            return new WindowsButton();
        }
        @Override
        public Checkbox createCheckbox() {
            return new WindowsCheckbox();
        }
    }

    /** Creates Mac-style UI components */
    static class MacUIFactory implements UIFactory {
        @Override
        public Button createButton() {
            return new MacButton();
        }
        @Override
        public Checkbox createCheckbox() {
            return new MacCheckbox();
        }
    }

    // ================================================================
    // Client
    // Works ONLY with abstract interfaces — completely decoupled from
    // any specific product family. Swap the factory to change the OS.
    // ================================================================
    static class Application {
        private final Button button;
        private final Checkbox checkbox;

        // Factory injected at construction time (dependency injection)
        Application(UIFactory factory) {
            this.button   = factory.createButton();
            this.checkbox = factory.createCheckbox();
        }

        public void render() {
            button.render();
            checkbox.render();
        }

        public void interact() {
            button.onClick();
            checkbox.toggle();
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        // Determine OS at runtime (normally read from config/env)
        String os = System.getProperty("os.name", "Windows");
        UIFactory factory;

        if (os.toLowerCase().contains("mac")) {
            factory = new MacUIFactory();
        } else {
            factory = new WindowsUIFactory();
        }

        Application app = new Application(factory);
        app.render();
        app.interact();

        System.out.println("\n--- Forcing Mac factory ---");
        Application macApp = new Application(new MacUIFactory());
        macApp.render();

        System.out.println("\n--- Forcing Windows factory ---");
        Application winApp = new Application(new WindowsUIFactory());
        winApp.render();
    }
}

```
