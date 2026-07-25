---
layout: design_pattern
title: "Flyweight"
category: "structural"
difficulty: "Hard"
intent: "Use sharing to support large numbers of fine-grained objects efficiently."
tags: [structural, flyweight, memory]
render_with_liquid: false
---

## Intent

Use sharing to support large numbers of fine-grained objects efficiently.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Flyweight** | Stores intrinsic (shared, immutable) state |
| **FlyweightFactory** | Creates and caches flyweights |
| **Context** | Stores extrinsic (unique) state |

## Pros

- Massive memory savings when many similar objects exist
- Reduces object creation overhead

## Cons

- Code becomes more complex (must separate intrinsic/extrinsic state)
- CPU may increase slightly
- Harder to debug

## Real-World Examples

- `Java String pool (interning)`
- `Character glyphs in a word processor`
- `Map tiles in a game engine`
- `Integer cache (-128 to 127)`

## Full Java Implementation

```java
package org.interview.design_patterns.structural.flyweight;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ============================================================
 * Design Pattern: Flyweight (Structural)
 * ============================================================
 *
 * INTENT:
 *   Use sharing to support large numbers of fine-grained objects
 *   efficiently by separating intrinsic (shared) state from
 *   extrinsic (unique) state.
 *
 * PROBLEM IT SOLVES:
 *   Creating millions of objects (e.g., particles in a game, characters
 *   in a document, map tiles) is memory-prohibitive if each stores
 *   all its data independently.
 *
 * KEY CONCEPTS:
 *   - Intrinsic state: shared, immutable, stored inside the flyweight
 *     (e.g., sprite image, texture, font data)
 *   - Extrinsic state: unique per instance, passed in at runtime
 *     (e.g., position, color, health — differs per particle)
 *
 * STRUCTURE:
 *   - Flyweight: stores intrinsic state; accepts extrinsic state via methods
 *   - FlyweightFactory: creates and caches flyweights; returns existing ones
 *   - Client: stores extrinsic state; calls flyweight methods passing extrinsic data
 *
 * REAL-WORLD EXAMPLES:
 *   - Java String pool (interning)
 *   - Character glyphs in a word processor
 *   - Map tiles in a game engine
 *   - Integer cache in Java (-128 to 127): Integer.valueOf(127) == Integer.valueOf(127)
 *
 * PROS:
 *   + Massive memory savings when many similar objects exist
 *   + Reduces object creation overhead
 *
 * CONS:
 *   - Code becomes more complex (must separate intrinsic/extrinsic state)
 *   - CPU may increase slightly (extrinsic state passed every call)
 *   - Harder to debug (shared objects)
 *
 * SCENARIO:
 *   A forest game: millions of trees on screen. Each tree type (Oak, Pine, Birch)
 *   has a heavy sprite/mesh (intrinsic). Each tree instance has position/health (extrinsic).
 *   Without flyweight: 1M trees × 10MB mesh = 10TB RAM. With flyweight: 3 meshes × 10MB = 30MB.
 */
public class FlyweightPattern {

    // ================================================================
    // Flyweight — holds INTRINSIC (shared, immutable) state
    // This is the heavy object that gets reused by many instances
    // ================================================================
    static class TreeType {
        // INTRINSIC STATE: shared among all trees of this type
        private final String name;      // "Oak", "Pine", "Birch"
        private final String color;     // hex color of leaves
        private final String texture;   // path to 3D mesh/sprite (simulated as string)

        TreeType(String name, String color, String texture) {
            this.name    = name;
            this.color   = color;
            this.texture = texture;
            // Simulate heavy object load (model data, GPU textures, etc.)
            System.out.println("  [TreeType] Loading heavy assets for: " + name);
        }

        /**
         * Draw the tree using the flyweight's intrinsic data PLUS
         * the extrinsic data (x, y, health) passed by the caller.
         */
        public void draw(int x, int y, int health) {
            // In a real game: upload texture to GPU, set transform matrix, etc.
            System.out.printf("  Drawing %s [color=%s] at (%d,%d) health=%d%n",
                name, color, x, y, health);
        }

        public String getName() { return name; }
    }

    // ================================================================
    // Flyweight Factory
    // Creates TreeType objects only once; caches and reuses them.
    // ================================================================
    static class TreeTypeFactory {
        // Cache: key = tree name; value = shared flyweight object
        private static final Map<String, TreeType> cache = new HashMap<>();

        /**
         * Returns an existing TreeType or creates a new one if not found.
         * This is the SHARING mechanism.
         */
        public static TreeType getTreeType(String name, String color, String texture) {
            // Use name as the key (intrinsic identifier)
            return cache.computeIfAbsent(name, k -> new TreeType(name, color, texture));
        }

        public static int getCachedCount() { return cache.size(); }
    }

    // ================================================================
    // Context (Client-side) — holds EXTRINSIC (unique) state
    // This is the lightweight object we create millions of
    // ================================================================
    static class Tree {
        // EXTRINSIC STATE: unique per tree instance
        private final int x;
        private final int y;
        private       int health;

        // Reference to shared flyweight — NOT a copy, just a pointer
        private final TreeType type;

        Tree(int x, int y, int health, TreeType type) {
            this.x      = x;
            this.y      = y;
            this.health = health;
            this.type   = type;
        }

        /** Delegates rendering to the flyweight, passing own extrinsic state */
        public void draw() {
            type.draw(x, y, health);
        }
    }

    // ================================================================
    // Forest — client that manages a large number of Tree objects
    // ================================================================
    static class Forest {
        // Stores only lightweight Tree contexts (not full TreeType per tree)
        private final java.util.List<Tree> trees = new java.util.ArrayList<>();
        private final Random rnd = new Random(42);

        /**
         * Plant a tree: gets (or creates) the shared TreeType flyweight,
         * then creates a lightweight Tree context.
         */
        public void plantTree(int x, int y, String name, String color, String texture) {
            // Flyweight factory returns shared object — no new heavy object per tree!
            TreeType type = TreeTypeFactory.getTreeType(name, color, texture);
            Tree     tree = new Tree(x, y, 100, type);
            trees.add(tree);
        }

        /** Plant many random trees of a given type */
        public void plantMany(int count, String name, String color, String texture) {
            for (int i = 0; i < count; i++) {
                plantTree(rnd.nextInt(1000), rnd.nextInt(1000), name, color, texture);
            }
        }

        public void draw() {
            trees.forEach(Tree::draw);
        }

        public int size() { return trees.size(); }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Planting 1,000,000 trees ===");
        System.out.println("(Only 3 TreeType flyweight objects will be created)\n");

        Forest forest = new Forest();

        System.out.println("[Loading Phase] Creating flyweight objects:");
        // Plant 1M trees — only 3 unique TreeType objects are ever created!
        forest.plantMany(400_000, "Oak",   "#2D5A27", "models/oak.obj");
        forest.plantMany(350_000, "Pine",  "#1A4A1A", "models/pine.obj");
        forest.plantMany(250_000, "Birch", "#7AB648", "models/birch.obj");

        System.out.printf("%n=== Forest summary ===%n");
        System.out.printf("Total trees in forest: %,d%n", forest.size());
        System.out.printf("Unique TreeType flyweights (heavy objects): %d%n",
            TreeTypeFactory.getCachedCount());
        System.out.printf("Memory saved: instead of %,d heavy objects, only %d exist!%n",
            forest.size(), TreeTypeFactory.getCachedCount());

        System.out.println("\n=== Drawing first 3 trees (sample) ===");
        // Draw just a few to demonstrate the drawing mechanism
        Forest smallForest = new Forest();
        smallForest.plantTree(10,  20, "Oak",   "#2D5A27", "models/oak.obj");
        smallForest.plantTree(50, 100, "Pine",  "#1A4A1A", "models/pine.obj");
        smallForest.plantTree(75,  30, "Oak",   "#2D5A27", "models/oak.obj"); // REUSES Oak flyweight
        smallForest.draw();
        System.out.printf("TreeTypes in second forest: %d (Oak reused!)%n",
            TreeTypeFactory.getCachedCount());
    }
}

```
