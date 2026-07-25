---
layout: design_pattern
title: "Prototype"
category: "creational"
difficulty: "Medium"
intent: "Specify the kinds of objects to create using a prototypical instance, and create new objects by copying that prototype."
tags: [creational, prototype]
render_with_liquid: false
---

## Intent

Specify the kinds of objects to create using a prototypical instance, and create new objects by copying that prototype.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Prototype** | Declares the clone() method |
| **ConcretePrototype** | Implements clone() |
| **PrototypeRegistry (optional)** | Manages a catalog of pre-built prototypes |
| **Client** | Calls prototype.clone() instead of new ConcreteClass() |

## Pros

- Avoids expensive re-initialization
- Add/remove products at runtime
- Alternative to subclassing

## Cons

- Deep-cloning objects with circular references is tricky
- Requires implementing clone() on all subclasses

## Real-World Examples

- `Object.clone() with Cloneable`
- `JavaScript Object.assign()`
- `Spring prototype-scope beans`
- `Game character templates`

## Variants

- Shallow copy (fast, shared references)
- Deep copy (safe, fully independent)
- Registry-based prototype

## Full Java Implementation

```java
package org.interview.design_patterns.creational.prototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * Design Pattern: Prototype (Creational)
 * ============================================================
 *
 * INTENT:
 *   Specify the kinds of objects to create using a prototypical
 *   instance, and create new objects by COPYING (cloning) that prototype.
 *
 * PROBLEM IT SOLVES:
 *   - Object creation is expensive (DB call, complex computation).
 *   - You need many slightly-varied copies of the same complex object.
 *   - You want to avoid subclass explosion just to vary parameters.
 *
 * STRUCTURE:
 *   - Prototype (interface): declares the clone() method
 *   - ConcretePrototype: implements clone() — performs shallow or deep copy
 *   - Client: calls prototype.clone() instead of new ConcreteClass()
 *   - PrototypeRegistry (optional): manages a catalog of pre-built prototypes
 *
 * SHALLOW vs DEEP COPY:
 *   - Shallow: copies field values; nested objects share references (fast, risky)
 *   - Deep:    recursively copies all nested objects (safe, slower)
 *
 * REAL-WORLD EXAMPLES:
 *   - java.lang.Object#clone() with Cloneable
 *   - JavaScript's Object.assign() / spread operator
 *   - Spring BeanDefinition prototype scope
 *   - Game character templates (copy a base character, tweak stats)
 *
 * PROS:
 *   + Avoids expensive re-initialization
 *   + Add/remove products at runtime
 *   + Alternative to subclassing for object configuration
 *
 * CONS:
 *   - Deep-cloning complex objects with circular references is tricky
 *   - Requires implementing clone() on all subclasses
 *
 * SCENARIO:
 *   Game development: enemies share a base template; copies are tweaked
 *   for each level without re-running expensive setup logic.
 */
public class PrototypePattern {

    // ================================================================
    // Prototype Interface
    // Note: We use our own Cloneable marker rather than java.lang.Cloneable
    // to avoid the pitfalls of Object.clone() (checked exception, etc.)
    // ================================================================
    interface Prototype<T> {
        /** Returns a deep copy of this object */
        T clone();
    }

    // ================================================================
    // Concrete Prototype: GameCharacter
    // Simulates an expensive-to-create game character (e.g., loaded from DB)
    // ================================================================
    static class GameCharacter implements Prototype<GameCharacter> {
        private String name;
        private String type;           // "warrior", "mage", "archer"
        private int    health;
        private int    damage;
        private double speed;
        private List<String> inventory; // mutable list — needs deep copy

        GameCharacter(String name, String type, int health, int damage, double speed) {
            this.name      = name;
            this.type      = type;
            this.health    = health;
            this.damage    = damage;
            this.speed     = speed;
            this.inventory = new ArrayList<>();

            // Simulate expensive initialization (e.g., loading assets)
            System.out.println("  [Expensive Init] Creating " + name + " from scratch...");
        }

        /** Deep copy constructor — used by clone() */
        private GameCharacter(GameCharacter source) {
            this.name      = source.name;
            this.type      = source.type;
            this.health    = source.health;
            this.damage    = source.damage;
            this.speed     = source.speed;
            // Deep copy: new list with same elements (Strings are immutable)
            this.inventory = new ArrayList<>(source.inventory);
            // Note: no "expensive init" logged — this is the whole point!
        }

        @Override
        public GameCharacter clone() {
            return new GameCharacter(this); // uses deep copy constructor
        }

        public void addItem(String item)   { inventory.add(item); }
        public void setName(String name)   { this.name = name; }
        public void setHealth(int health)  { this.health = health; }
        public void setDamage(int damage)  { this.damage = damage; }

        @Override
        public String toString() {
            return String.format("GameCharacter{name='%s', type='%s', hp=%d, dmg=%d, spd=%.1f, inv=%s}",
                name, type, health, damage, speed, inventory);
        }
    }

    // ================================================================
    // Prototype Registry
    // A catalog of pre-built prototypes; clients clone from it.
    // Avoids re-running expensive initialization.
    // ================================================================
    static class CharacterRegistry {
        private final Map<String, GameCharacter> registry = new HashMap<>();

        public void register(String key, GameCharacter prototype) {
            registry.put(key, prototype);
        }

        /**
         * Returns a CLONE of the stored prototype.
         * Client gets a fresh copy — changes don't affect the prototype.
         */
        public GameCharacter getClone(String key) {
            GameCharacter proto = registry.get(key);
            if (proto == null) throw new IllegalArgumentException("No prototype for key: " + key);
            return proto.clone();
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Building Prototypes (expensive, done once) ===");

        // Create base prototypes — expensive initialization happens here
        GameCharacter warriorProto = new GameCharacter("Warrior", "warrior", 200, 50, 1.0);
        warriorProto.addItem("Iron Sword");
        warriorProto.addItem("Shield");

        GameCharacter mageProto = new GameCharacter("Mage", "mage", 100, 120, 0.8);
        mageProto.addItem("Magic Staff");
        mageProto.addItem("Spellbook");

        // Register in the prototype registry
        CharacterRegistry registry = new CharacterRegistry();
        registry.register("warrior", warriorProto);
        registry.register("mage", mageProto);

        System.out.println("\n=== Cloning Prototypes (cheap, no expensive init) ===");

        // Spawn 3 warriors for level 1 — cloned, NOT re-initialized
        GameCharacter w1 = registry.getClone("warrior");
        w1.setName("Warrior-1");
        w1.addItem("Health Potion");   // only w1's inventory is affected

        GameCharacter w2 = registry.getClone("warrior");
        w2.setName("Warrior-2");
        w2.setHealth(150);            // boss fight variant with less HP

        GameCharacter w3 = registry.getClone("warrior");
        w3.setName("Warrior-3");
        w3.setDamage(80);             // elite variant with more damage

        System.out.println(w1);
        System.out.println(w2);
        System.out.println(w3);

        // Verify deep copy: w1's extra item didn't affect prototype
        System.out.println("\nOriginal prototype still clean: " + warriorProto);

        // Clone a mage
        GameCharacter m1 = registry.getClone("mage");
        m1.setName("Mage-1");
        System.out.println(m1);
    }
}

```
