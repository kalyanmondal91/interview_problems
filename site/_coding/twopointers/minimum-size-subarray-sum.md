---
layout: problem
title: "Minimum Size Subarray Sum"
category: twopointers
category_display: "Two Pointers"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 209
tags: [twopointers]
render_with_liquid: false
---

## Problem

Minimum Size Subarray Sum Given an array of positive integers nums and a positive integer target, return the minimal length of a contiguous subarray whose sum is greater than or equal to target. If there is no such subarray, return 0.

## Approach

Use a sliding window with two pointers. Expand the right pointer to add elements to the running sum. Once the sum reaches or exceeds target, record the window size and shrink from the left to try finding a smaller valid window. Repeat until the right pointer has traversed the entire array.

## Solution

```java
package org.interview.coding.twopointers;

/**
 * Problem: Minimum Size Subarray Sum
 * Difficulty: Medium
 *
 * Description:
 * Given an array of positive integers nums and a positive integer target, return the minimal
 * length of a contiguous subarray whose sum is greater than or equal to target. If there is
 * no such subarray, return 0.
 *
 * Example:
 *   Input: target = 7, nums = [2,3,1,2,4,3]
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= target <= 10^9
 *   - 1 <= nums.length <= 10^5
 *   - 1 <= nums[i] <= 10^4
 *
 * Approach:
 *   Use a sliding window with two pointers. Expand the right pointer to add elements to
 *   the running sum. Once the sum reaches or exceeds target, record the window size and
 *   shrink from the left to try finding a smaller valid window. Repeat until the right
 *   pointer has traversed the entire array.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: target=7, nums=[2,3,1,2,4,3] → Output: 2 ([4,3])
 *   2. Input: target=4, nums=[1,4,4] → Output: 1 ([4])
 *   3. Edge case: target=11, nums=[1,1,1,1,1,1,1,1] → Output: 0 (impossible)
 */
public class MinimumSizeSubarraySum {

    public int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left++];
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    public static void main(String[] args) {
        MinimumSizeSubarraySum sol = new MinimumSizeSubarraySum();
        // Test 1
        System.out.println(sol.minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3})); // 2
        // Test 2
        System.out.println(sol.minSubArrayLen(4, new int[]{1, 4, 4}));          // 1
        // Test 3 (edge case: no valid subarray)
        System.out.println(sol.minSubArrayLen(11, new int[]{1,1,1,1,1,1,1,1})); // 0
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
