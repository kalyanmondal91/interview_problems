---
layout: problem
title: "Pow X N"
category: math
category_display: "Math"
difficulty: Medium
time_complexity: "O(log n)"
space_complexity: "O(1)"
tags: [math]
render_with_liquid: false
---

## Problem

Pow(x, n) Implement pow(x, n) which calculates x raised to the power n (x^n). Handle negative exponents and the edge case where n = Integer.MIN_VALUE.

## Approach

Fast exponentiation (binary exponentiation / exponentiation by squaring). Use n as a long to handle n=Integer.MIN_VALUE (whose abs overflows int). If n < 0, convert x to 1/x and negate n. While n > 0: if n is odd (n&1==1), multiply result by x; then square x and halve n. This reduces O(n) multiplications to O(log n).

## Solution

```java
package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Pow(x, n)
 * Difficulty: Medium
 *
 * Description: Implement pow(x, n) which calculates x raised to the power n (x^n).
 * Handle negative exponents and the edge case where n = Integer.MIN_VALUE.
 *
 * Example:
 *   Input: x=2.0, n=10 → Output: 1024.0
 *   Input: x=2.1, n=3 → Output: 9.261000...
 *   Input: x=2.0, n=-2 → Output: 0.25
 *
 * Approach: Fast exponentiation (binary exponentiation / exponentiation by squaring).
 * Use n as a long to handle n=Integer.MIN_VALUE (whose abs overflows int). If n < 0, convert
 * x to 1/x and negate n. While n > 0: if n is odd (n&1==1), multiply result by x; then
 * square x and halve n. This reduces O(n) multiplications to O(log n).
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. pow(2.0, 10) → 1024.0
 *   2. pow(2.0, -2) → 0.25
 *   3. Edge: pow(2.0, Integer.MIN_VALUE) → 0.0 (very small number near 0)
 */
public class PowXN {

    public static double myPow(double x, int n) {
        long exp = n; // Use long to handle Integer.MIN_VALUE
        if (exp < 0) {
            x = 1.0 / x;
            exp = -exp;
        }
        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) result *= x;
            x *= x;
            exp >>= 1;
        }
        return result;
    }

    public static void main(String[] args) {
        // Test Case 1: Positive exponent
        System.out.printf("pow(2.0, 10)=%.1f%n", myPow(2.0, 10));  // 1024.0

        // Test Case 2: Negative exponent
        System.out.printf("pow(2.0, -2)=%.2f%n", myPow(2.0, -2));  // 0.25

        // Test Case 3: Edge - minimum integer exponent
        System.out.printf("pow(2.0, MIN)=%.20f%n", myPow(2.0, Integer.MIN_VALUE)); // ~0.0

        // Bonus: fractional base
        System.out.printf("pow(2.1, 3)=%.6f%n", myPow(2.1, 3));    // 9.261000
        System.out.printf("pow(1.0, MAX)=%.1f%n", myPow(1.0, Integer.MAX_VALUE)); // 1.0
    }
}
```

## Complexity

- **Time:** O(log n)
- **Space:** O(1)
