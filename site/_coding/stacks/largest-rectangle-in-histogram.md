---
layout: problem
title: "Largest Rectangle In Histogram"
category: stacks
category_display: "Stacks"
difficulty: Hard
time_complexity: "O(n)"
space_complexity: "O(n)"
leetcode: 84
tags: [stacks]
render_with_liquid: false
---

## Problem

Largest Rectangle in Histogram Given an array of integers heights representing the histogram bar heights (each bar has width 1), return the area of the largest rectangle that can be formed in the histogram.

## Approach

Use a monotonic increasing stack of indices. For each bar, if it's shorter than the bar at the stack's top, pop and calculate the rectangle area with the popped bar as height. The width extends from the current index to the new stack top (+1). Push bars after processing. After the main loop, process remaining bars with right boundary at n.

## Solution

```java
package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Largest Rectangle in Histogram
 * Difficulty: Hard
 *
 * Description:
 * Given an array of integers heights representing the histogram bar heights (each bar has
 * width 1), return the area of the largest rectangle that can be formed in the histogram.
 *
 * Example:
 *   Input: heights = [2,1,5,6,2,3]
 *   Output: 10
 *
 * Constraints:
 *   - 1 <= heights.length <= 10^5
 *   - 0 <= heights[i] <= 10^4
 *
 * Approach:
 *   Use a monotonic increasing stack of indices. For each bar, if it's shorter than the
 *   bar at the stack's top, pop and calculate the rectangle area with the popped bar as
 *   height. The width extends from the current index to the new stack top (+1). Push bars
 *   after processing. After the main loop, process remaining bars with right boundary at n.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: heights=[2,1,5,6,2,3] → Output: 10
 *   2. Input: heights=[2,4] → Output: 4
 *   3. Edge case: heights=[1] → Output: 1
 */
public class LargestRectangleInHistogram {

    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            int currHeight = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && currHeight < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }

    public static void main(String[] args) {
        LargestRectangleInHistogram sol = new LargestRectangleInHistogram();
        // Test 1
        System.out.println(sol.largestRectangleArea(new int[]{2,1,5,6,2,3})); // 10
        // Test 2
        System.out.println(sol.largestRectangleArea(new int[]{2,4}));         // 4
        // Test 3 (edge case: single bar)
        System.out.println(sol.largestRectangleArea(new int[]{1}));           // 1
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
