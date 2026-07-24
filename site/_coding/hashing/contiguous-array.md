---
layout: problem
title: "Contiguous Array"
category: hashing
category_display: "Hashing"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(n)"
tags: [hashing]
render_with_liquid: false
---

## Problem

Contiguous Array Given a binary array nums, return the maximum length of a contiguous subarray with an equal number of 0s and 1s.

## Approach

Replace 0s with -1 to transform the problem: finding a subarray with equal 0s and 1s becomes finding a subarray with sum 0. Maintain a running prefix sum and a HashMap storing the first index where each prefix sum was seen. If the same prefix sum appears again at index i, the subarray between the first occurrence and i has sum 0.

## Solution

```java
package org.interview.coding.hashing;

import java.util.HashMap;

/**
 * Problem: Contiguous Array
 * Difficulty: Medium
 *
 * Description:
 * Given a binary array nums, return the maximum length of a contiguous subarray with an
 * equal number of 0s and 1s.
 *
 * Example:
 *   Input: nums = [0,1]
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - nums[i] is either 0 or 1
 *
 * Approach:
 *   Replace 0s with -1 to transform the problem: finding a subarray with equal 0s and 1s
 *   becomes finding a subarray with sum 0. Maintain a running prefix sum and a HashMap
 *   storing the first index where each prefix sum was seen. If the same prefix sum appears
 *   again at index i, the subarray between the first occurrence and i has sum 0.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: nums=[0,1] → Output: 2
 *   2. Input: nums=[0,1,0] → Output: 2
 *   3. Edge case: nums=[0,0,0,0,1,1,1,1] → Output: 8
 */
public class ContiguousArray {

    public int findMaxLength(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int sum = 0, maxLen = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += (nums[i] == 1) ? 1 : -1;
            if (map.containsKey(sum)) {
                maxLen = Math.max(maxLen, i - map.get(sum));
            } else {
                map.put(sum, i);
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        ContiguousArray sol = new ContiguousArray();
        // Test 1
        System.out.println(sol.findMaxLength(new int[]{0, 1}));                     // 2
        // Test 2
        System.out.println(sol.findMaxLength(new int[]{0, 1, 0}));                  // 2
        // Test 3 (edge case: equal halves)
        System.out.println(sol.findMaxLength(new int[]{0,0,0,0,1,1,1,1}));          // 8
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
