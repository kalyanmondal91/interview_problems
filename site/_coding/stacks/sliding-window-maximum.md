---
layout: problem
title: "Sliding Window Maximum"
category: stacks
category_display: "Stacks"
difficulty: Hard
time_complexity: "O(n)"
space_complexity: "O(k)"
tags: [stacks]
render_with_liquid: false
---

## Problem

Sliding Window Maximum Given an integer array nums and an integer k, there is a sliding window of size k that moves from the leftmost to the rightmost position. At each step, return the maximum value in the current window. Return all maximums as an array.

## Approach

Use a monotonic decreasing deque that stores indices. For each new element: remove indices from the back of the deque if their values are smaller (they can never be window max). Remove from the front if the index is outside the current window. The front always holds the index of the current window's maximum.

## Solution

```java
package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Problem: Sliding Window Maximum
 * Difficulty: Hard
 *
 * Description:
 * Given an integer array nums and an integer k, there is a sliding window of size k that
 * moves from the leftmost to the rightmost position. At each step, return the maximum
 * value in the current window. Return all maximums as an array.
 *
 * Example:
 *   Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 *   Output: [3,3,5,5,6,7]
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - -10^4 <= nums[i] <= 10^4
 *   - 1 <= k <= nums.length
 *
 * Approach:
 *   Use a monotonic decreasing deque that stores indices. For each new element: remove
 *   indices from the back of the deque if their values are smaller (they can never be
 *   window max). Remove from the front if the index is outside the current window.
 *   The front always holds the index of the current window's maximum.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(k)
 *
 * Test Cases:
 *   1. Input: nums=[1,3,-1,-3,5,3,6,7], k=3 → Output: [3,3,5,5,6,7]
 *   2. Input: nums=[1], k=1 → Output: [1]
 *   3. Edge case: nums=[1,-1], k=1 → Output: [1,-1]
 */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // stores indices, decreasing by value

        for (int i = 0; i < n; i++) {
            // Remove out-of-window elements
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) deque.pollFirst();
            // Remove smaller elements from back
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) deque.pollLast();
            deque.offerLast(i);
            if (i >= k - 1) result[i - k + 1] = nums[deque.peekFirst()];
        }
        return result;
    }

    public static void main(String[] args) {
        SlidingWindowMaximum sol = new SlidingWindowMaximum();
        // Test 1
        System.out.println(Arrays.toString(sol.maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3))); // [3,3,5,5,6,7]
        // Test 2
        System.out.println(Arrays.toString(sol.maxSlidingWindow(new int[]{1}, 1)));                  // [1]
        // Test 3 (edge case: k=1)
        System.out.println(Arrays.toString(sol.maxSlidingWindow(new int[]{1,-1}, 1)));               // [1,-1]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(k)
