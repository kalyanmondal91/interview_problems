---
layout: problem
title: "Max Consecutive Ones I I I"
category: twopointers
category_display: "Two Pointers"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
tags: [twopointers]
render_with_liquid: false
---

## Problem

Max Consecutive Ones III Given a binary array nums and an integer k, return the maximum number of consecutive 1s in the array if you can flip at most k 0s to 1s.

## Approach

Use a sliding window. Expand the right pointer. When a 0 is encountered, decrement k. If k drops below 0, shrink from the left: if the element removed was 0, restore k. This maintains a window with at most k zeros at all times. Track the maximum window size.

## Solution

```java
package org.interview.coding.twopointers;

/**
 * Problem: Max Consecutive Ones III
 * Difficulty: Medium
 *
 * Description:
 * Given a binary array nums and an integer k, return the maximum number of consecutive 1s
 * in the array if you can flip at most k 0s to 1s.
 *
 * Example:
 *   Input: nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
 *   Output: 6
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - nums[i] is either 0 or 1
 *   - 0 <= k <= nums.length
 *
 * Approach:
 *   Use a sliding window. Expand the right pointer. When a 0 is encountered, decrement k.
 *   If k drops below 0, shrink from the left: if the element removed was 0, restore k.
 *   This maintains a window with at most k zeros at all times. Track the maximum window size.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: nums=[1,1,1,0,0,0,1,1,1,1,0], k=2 → Output: 6
 *   2. Input: nums=[0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], k=3 → Output: 10
 *   3. Edge case: nums=[0,0,0], k=0 → Output: 0
 */
public class MaxConsecutiveOnesIII {

    public int longestOnes(int[] nums, int k) {
        int left = 0, maxLen = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) k--;
            while (k < 0) {
                if (nums[left] == 0) k++;
                left++;
            }
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        MaxConsecutiveOnesIII sol = new MaxConsecutiveOnesIII();
        // Test 1
        System.out.println(sol.longestOnes(new int[]{1,1,1,0,0,0,1,1,1,1,0}, 2));                      // 6
        // Test 2
        System.out.println(sol.longestOnes(new int[]{0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1}, 3));     // 10
        // Test 3 (edge case: all zeros, no flips)
        System.out.println(sol.longestOnes(new int[]{0, 0, 0}, 0));                                    // 0
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
