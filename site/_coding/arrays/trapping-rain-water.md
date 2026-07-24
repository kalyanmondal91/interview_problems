---
layout: problem
title: "Trapping Rain Water"
category: arrays
category_display: "Arrays"
difficulty: Hard
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 42
tags: [arrays]
render_with_liquid: false
---

## Problem

Trapping Rain Water Given n non-negative integers representing an elevation map where each bar has width 1, compute how much water it can trap after raining. The water level at any position is determined by the minimum of the maximum height to its left and right.

## Approach

Use two pointers from both ends. Maintain maxLeft and maxRight as the highest bar seen from each side. At each step, process the side with the smaller maximum: water at that position is maxSide - height[pos]. Move the respective pointer inward. This works because the water level is bounded by the smaller of the two maximum heights.

## Solution

```java
package org.interview.coding.arrays;

/**
 * Problem: Trapping Rain Water
 * Difficulty: Hard
 *
 * Description:
 * Given n non-negative integers representing an elevation map where each bar has width 1,
 * compute how much water it can trap after raining. The water level at any position is
 * determined by the minimum of the maximum height to its left and right.
 *
 * Example:
 *   Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 *   Output: 6
 *
 * Constraints:
 *   - n == height.length
 *   - 1 <= n <= 2 * 10^4
 *   - 0 <= height[i] <= 10^5
 *
 * Approach:
 *   Use two pointers from both ends. Maintain maxLeft and maxRight as the highest bar seen
 *   from each side. At each step, process the side with the smaller maximum: water at that
 *   position is maxSide - height[pos]. Move the respective pointer inward. This works because
 *   the water level is bounded by the smaller of the two maximum heights.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: height=[0,1,0,2,1,0,1,3,2,1,2,1] → Output: 6
 *   2. Input: height=[4,2,0,3,2,5] → Output: 9
 *   3. Edge case: height=[1,2,3,4,5] → Output: 0 (monotonically increasing, no water)
 */
public class TrappingRainWater {

    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int maxLeft = 0, maxRight = 0;
        int water = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= maxLeft) {
                    maxLeft = height[left];
                } else {
                    water += maxLeft - height[left];
                }
                left++;
            } else {
                if (height[right] >= maxRight) {
                    maxRight = height[right];
                } else {
                    water += maxRight - height[right];
                }
                right--;
            }
        }
        return water;
    }

    public static void main(String[] args) {
        TrappingRainWater sol = new TrappingRainWater();
        // Test 1
        System.out.println(sol.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})); // 6
        // Test 2
        System.out.println(sol.trap(new int[]{4, 2, 0, 3, 2, 5}));                    // 9
        // Test 3 (edge case: no trapping possible)
        System.out.println(sol.trap(new int[]{1, 2, 3, 4, 5}));                       // 0
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
