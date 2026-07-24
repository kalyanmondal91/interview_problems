---
layout: problem
title: "Knapsack01"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Medium
time_complexity: "O(n * W)"
space_complexity: "O(W)"
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

0/1 Knapsack Given a knapsack with weight capacity W and n items each with a weight and value, determine the maximum value that can be put into the knapsack. Each item can only be used once (0/1 — either take it or leave it).

## Approach

1D DP where dp[w] represents the max value achievable with weight capacity w. Iterate items in outer loop, and for each item traverse weights in reverse (from capacity down to weights[i]). The reverse traversal ensures each item is used at most once. dp[w] = max(dp[w], dp[w-weights[i]] + values[i]).

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: 0/1 Knapsack
 * Difficulty: Medium
 *
 * Description:
 * Given a knapsack with weight capacity W and n items each with a weight and value,
 * determine the maximum value that can be put into the knapsack. Each item can only
 * be used once (0/1 — either take it or leave it).
 *
 * Example:
 *   Input: weights=[1,3,4,5], values=[1,4,5,7], capacity=7
 *   Output: 9 (items with weights 3 and 4, values 4+5)
 *
 * Constraints:
 *   - 1 <= n <= 1000
 *   - 1 <= weights[i] <= 1000
 *   - 0 <= values[i] <= 1000
 *   - 1 <= capacity <= 1000
 *
 * Approach:
 *   1D DP where dp[w] represents the max value achievable with weight capacity w.
 *   Iterate items in outer loop, and for each item traverse weights in reverse (from
 *   capacity down to weights[i]). The reverse traversal ensures each item is used at
 *   most once. dp[w] = max(dp[w], dp[w-weights[i]] + values[i]).
 *
 * Time Complexity: O(n * W)
 * Space Complexity: O(W)
 *
 * Test Cases:
 *   1. Input: weights=[1,3,4,5], values=[1,4,5,7], W=7 → Output: 9
 *   2. Input: weights=[2,3,4], values=[3,4,5], W=5 → Output: 7
 *   3. Edge: capacity=0 → Output: 0
 */
public class Knapsack01 {

    public int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < n; i++) {
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        return dp[capacity];
    }

    public static void main(String[] args) {
        Knapsack01 sol = new Knapsack01();

        System.out.println("Test 1 (expect 9): "
                + sol.knapsack(new int[]{1,3,4,5}, new int[]{1,4,5,7}, 7));
        System.out.println("Test 2 (expect 7): "
                + sol.knapsack(new int[]{2,3,4}, new int[]{3,4,5}, 5));
        System.out.println("Test 3 capacity=0 (expect 0): "
                + sol.knapsack(new int[]{1,2}, new int[]{10,20}, 0));
    }
}
```

## Complexity

- **Time:** O(n * W)
- **Space:** O(W)
