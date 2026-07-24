---
layout: problem
title: "Sqrt X"
category: math
category_display: "Math"
difficulty: Easy
time_complexity: "O(log x)"
space_complexity: "O(1)"
tags: [math]
render_with_liquid: false
---

## Problem

Sqrt(x) Given a non-negative integer x, return the floor of the square root of x. Do not use any built-in exponent functions or operators. The returned integer should be the largest integer r such that r*r <= x.

## Approach

Binary search on the range [0, x]. For each midpoint mid, check if mid*mid <= x. Use long to prevent integer overflow (mid*mid can exceed Integer.MAX_VALUE). When mid*mid <= x, record mid as a candidate answer and search higher; otherwise search lower.

## Solution

```java
package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Sqrt(x)
 * Difficulty: Easy
 *
 * Description: Given a non-negative integer x, return the floor of the square root of x.
 * Do not use any built-in exponent functions or operators. The returned integer should be
 * the largest integer r such that r*r <= x.
 *
 * Example:
 *   Input: x=4 → Output: 2
 *   Input: x=8 → Output: 2 (floor(sqrt(8)) = 2 since 2*2=4 <= 8 < 9=3*3)
 *
 * Approach: Binary search on the range [0, x]. For each midpoint mid, check if mid*mid <= x.
 * Use long to prevent integer overflow (mid*mid can exceed Integer.MAX_VALUE). When mid*mid <= x,
 * record mid as a candidate answer and search higher; otherwise search lower.
 *
 * Time Complexity: O(log x)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. x=4 → 2 (exact square root)
 *   2. x=8 → 2 (floor, since sqrt(8)≈2.83)
 *   3. Edge: x=0 → 0, x=1 → 1
 */
public class SqrtX {

    public static int mySqrt(int x) {
        if (x == 0) return 0;
        int lo = 1, hi = x, ans = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if ((long) mid * mid <= x) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        // Test Case 1: Exact square root
        System.out.println("sqrt(4)=" + mySqrt(4));   // 2

        // Test Case 2: Non-perfect square, floor result
        System.out.println("sqrt(8)=" + mySqrt(8));   // 2

        // Test Case 3: Edge cases
        System.out.println("sqrt(0)=" + mySqrt(0));   // 0
        System.out.println("sqrt(1)=" + mySqrt(1));   // 1

        // Bonus: large value
        System.out.println("sqrt(2147395600)=" + mySqrt(2147395600)); // 46340
    }
}
```

## Complexity

- **Time:** O(log x)
- **Space:** O(1)
