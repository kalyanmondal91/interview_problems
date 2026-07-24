---
layout: problem
title: "Asteroid Collision"
category: stacks
category_display: "Stacks"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(n)"
tags: [stacks]
render_with_liquid: false
---

## Problem

Asteroid Collision Given an array asteroids of integers representing asteroids in a row, find out the state of the asteroids after all collisions. Positive values move right, negative move left. When two asteroids meet, the smaller one explodes. If equal, both explode. Asteroids moving in the same direction never meet.

## Approach

Use a stack. For each asteroid: if it's positive or the stack is empty or has a negative top, push it (no collision). Otherwise (current is negative, top is positive), simulate collision: if |current| > top, pop the top and retry; if equal, pop and discard current; if |current| < top, discard current. Convert final stack to array.

## Solution

```java
package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Problem: Asteroid Collision
 * Difficulty: Medium
 *
 * Description:
 * Given an array asteroids of integers representing asteroids in a row, find out the state
 * of the asteroids after all collisions. Positive values move right, negative move left.
 * When two asteroids meet, the smaller one explodes. If equal, both explode. Asteroids
 * moving in the same direction never meet.
 *
 * Example:
 *   Input: asteroids = [5,10,-5]
 *   Output: [5,10]
 *
 * Constraints:
 *   - 2 <= asteroids.length <= 10^4
 *   - -1000 <= asteroids[i] <= 1000
 *   - asteroids[i] != 0
 *
 * Approach:
 *   Use a stack. For each asteroid: if it's positive or the stack is empty or has a
 *   negative top, push it (no collision). Otherwise (current is negative, top is positive),
 *   simulate collision: if |current| > top, pop the top and retry; if equal, pop and
 *   discard current; if |current| < top, discard current. Convert final stack to array.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [5,10,-5] → Output: [5,10]
 *   2. Input: [8,-8] → Output: []
 *   3. Edge case: [10,2,-5] → Output: [10]
 */
public class AsteroidCollision {

    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (int ast : asteroids) {
            boolean survived = true;
            while (survived && ast < 0 && !stack.isEmpty() && stack.peek() > 0) {
                if (stack.peek() < -ast) {
                    stack.pop();
                } else if (stack.peek() == -ast) {
                    stack.pop();
                    survived = false;
                } else {
                    survived = false;
                }
            }
            if (survived) stack.push(ast);
        }

        int[] result = new int[stack.size()];
        for (int i = result.length - 1; i >= 0; i--) result[i] = stack.pop();
        return result;
    }

    public static void main(String[] args) {
        AsteroidCollision sol = new AsteroidCollision();
        // Test 1
        System.out.println(Arrays.toString(sol.asteroidCollision(new int[]{5,10,-5})));  // [5,10]
        // Test 2
        System.out.println(Arrays.toString(sol.asteroidCollision(new int[]{8,-8})));     // []
        // Test 3 (edge case: larger right asteroid survives)
        System.out.println(Arrays.toString(sol.asteroidCollision(new int[]{10,2,-5}))); // [10]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
