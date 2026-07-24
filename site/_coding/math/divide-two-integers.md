---
layout: problem
title: "Divide Two Integers"
category: math
category_display: "Math"
difficulty: Medium
time_complexity: "O(log²n)"
space_complexity: "O(1)"
tags: [math]
render_with_liquid: false
---

## Problem

Divide Two Integers Divide two integers without using multiplication, division, or mod operators. Return the quotient truncated toward zero. Overflow case: Integer.MIN_VALUE / -1 returns Integer.MAX_VALUE (since the true result 2^31 exceeds int range).

## Approach

Work with longs to avoid overflow. Determine sign. Use bit-shifting to find the largest multiple of divisor that fits in dividend: double the divisor via left shift until it exceeds dividend. Subtract and accumulate the corresponding power-of-2 multiplier. Repeat with remaining dividend. Handle overflow for MIN_VALUE / -1 explicitly.

## Solution

```java
package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Divide Two Integers
 * Difficulty: Medium
 *
 * Description: Divide two integers without using multiplication, division, or mod operators.
 * Return the quotient truncated toward zero. Overflow case: Integer.MIN_VALUE / -1 returns
 * Integer.MAX_VALUE (since the true result 2^31 exceeds int range).
 *
 * Example:
 *   Input: dividend=10, divisor=3 → Output: 3
 *   Input: dividend=7, divisor=-3 → Output: -2
 *
 * Approach: Work with longs to avoid overflow. Determine sign. Use bit-shifting to find the
 * largest multiple of divisor that fits in dividend: double the divisor via left shift until
 * it exceeds dividend. Subtract and accumulate the corresponding power-of-2 multiplier.
 * Repeat with remaining dividend. Handle overflow for MIN_VALUE / -1 explicitly.
 *
 * Time Complexity: O(log²n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. 10 / 3 → 3
 *   2. 7 / -3 → -2
 *   3. Edge: Integer.MIN_VALUE / -1 → Integer.MAX_VALUE (overflow capped)
 */
public class DivideTwoIntegers {

    public static int divide(int dividend, int divisor) {
        // Handle overflow
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;

        long dvd = Math.abs((long) dividend);
        long dvs = Math.abs((long) divisor);
        boolean negative = (dividend < 0) != (divisor < 0);

        long result = 0;
        while (dvd >= dvs) {
            long temp = dvs;
            long multiple = 1;
            while (dvd >= (temp << 1)) {
                temp <<= 1;
                multiple <<= 1;
            }
            dvd -= temp;
            result += multiple;
        }

        return negative ? (int) -result : (int) result;
    }

    public static void main(String[] args) {
        // Test Case 1: Basic division
        System.out.println("divide(10,3)=" + divide(10, 3));   // 3

        // Test Case 2: Negative result
        System.out.println("divide(7,-3)=" + divide(7, -3));   // -2

        // Test Case 3: Edge - overflow case
        System.out.println("divide(MIN,-1)=" + divide(Integer.MIN_VALUE, -1)); // 2147483647

        // Bonus: exact division and divide by 1
        System.out.println("divide(-1,1)=" + divide(-1, 1));   // -1
        System.out.println("divide(0,1)=" + divide(0, 1));     // 0
    }
}
```

## Complexity

- **Time:** O(log²n)
- **Space:** O(1)
