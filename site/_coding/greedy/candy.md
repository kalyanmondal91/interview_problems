---
layout: problem
title: "Candy"
category: greedy
category_display: "Greedy"
difficulty: Hard
time_complexity: "O(n)"
space_complexity: "O(n)"
tags: [greedy]
render_with_liquid: false
---

## Problem

Candy There are n children standing in a line, each with a rating value. You are giving candies to these children such that each child must have at least one candy, and children with a higher rating than their adjacent neighbor must get more candies than that neighbor. Find the minimum number of candies.

## Approach

Use two passes over the ratings array. In the left-to-right pass, if ratings[i] > ratings[i-1], set candies[i] = candies[i-1] + 1, otherwise set candies[i] = 1. In the right-to-left pass, if ratings[i] > ratings[i+1], set candies[i] = max(candies[i], candies[i+1] + 1). This ensures both left and right neighbor constraints are satisfied. Sum all values for the answer.

## Solution

```java
package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Candy
 * Difficulty: Hard
 *
 * Description:
 * There are n children standing in a line, each with a rating value. You are giving candies to these
 * children such that each child must have at least one candy, and children with a higher rating than
 * their adjacent neighbor must get more candies than that neighbor. Find the minimum number of candies.
 *
 * Example:
 *   Input: ratings = [1, 0, 2]
 *   Output: 5
 *
 * Constraints:
 *   - n == ratings.length
 *   - 1 <= n <= 2 * 10^4
 *   - 0 <= ratings[i] <= 2 * 10^4
 *
 * Approach:
 *   Use two passes over the ratings array. In the left-to-right pass, if ratings[i] > ratings[i-1],
 *   set candies[i] = candies[i-1] + 1, otherwise set candies[i] = 1. In the right-to-left pass,
 *   if ratings[i] > ratings[i+1], set candies[i] = max(candies[i], candies[i+1] + 1). This ensures
 *   both left and right neighbor constraints are satisfied. Sum all values for the answer.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [1,0,2] → Output: 5
 *   2. Input: [1,2,2] → Output: 4
 *   3. Edge: [1] → Output: 1
 */
public class Candy {

    public int candy(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);

        // Left-to-right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // Right-to-left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }

        int total = 0;
        for (int c : candies) total += c;
        return total;
    }

    public static void main(String[] args) {
        Candy solution = new Candy();

        // Test 1: standard case
        System.out.println(solution.candy(new int[]{1, 0, 2})); // Expected: 5

        // Test 2: equal ratings at end
        System.out.println(solution.candy(new int[]{1, 2, 2})); // Expected: 4

        // Test 3: edge case single child
        System.out.println(solution.candy(new int[]{1})); // Expected: 1
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
