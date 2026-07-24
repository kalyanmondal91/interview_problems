---
layout: problem
title: "Container With Most Water"
category: arrays
category_display: "Arrays"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 11
tags: [arrays]
render_with_liquid: false
---

## Problem

Container With Most Water Given n non-negative integers representing heights of vertical lines at positions 0..n-1, find two lines that together with the x-axis form a container that holds the most water. Return the maximum amount of water the container can store.

## Approach

Use two pointers starting at both ends of the array. The water held is determined by the shorter of the two lines times the distance between them. Move the pointer at the shorter line inward, since moving the taller line can only decrease or maintain the width without necessarily increasing the height. Track the maximum at each step.

## Solution

```java
package org.interview.coding.arrays;

/**
 * Problem: Container With Most Water
 * Difficulty: Medium
 *
 * Description:
 * Given n non-negative integers representing heights of vertical lines at positions 0..n-1,
 * find two lines that together with the x-axis form a container that holds the most water.
 * Return the maximum amount of water the container can store.
 *
 * Example:
 *   Input: height = [1,8,6,2,5,4,8,3,7]
 *   Output: 49
 *
 * Constraints:
 *   - n == height.length
 *   - 2 <= n <= 10^5
 *   - 0 <= height[i] <= 10^4
 *
 * Approach:
 *   Use two pointers starting at both ends of the array. The water held is determined by
 *   the shorter of the two lines times the distance between them. Move the pointer at the
 *   shorter line inward, since moving the taller line can only decrease or maintain the
 *   width without necessarily increasing the height. Track the maximum at each step.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: height=[1,8,6,2,5,4,8,3,7] → Output: 49
 *   2. Input: height=[1,1] → Output: 1
 *   3. Edge case: height=[4,3,2,1,4] → Output: 16
 */
public class ContainerWithMostWater {

    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int water = Math.min(height[left], height[right]) * (right - left);
            maxWater = Math.max(maxWater, water);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;
    }

    public static void main(String[] args) {
        ContainerWithMostWater sol = new ContainerWithMostWater();
        // Test 1
        System.out.println(sol.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})); // 49
        // Test 2
        System.out.println(sol.maxArea(new int[]{1, 1}));                        // 1
        // Test 3 (edge case: symmetric heights)
        System.out.println(sol.maxArea(new int[]{4, 3, 2, 1, 4}));              // 16
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
