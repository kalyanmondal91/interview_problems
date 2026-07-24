---
layout: problem
title: "Subarray Sum Equals K"
category: hashing
category_display: "Hashing"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(n)"
tags: [hashing]
render_with_liquid: false
---

## Problem

Subarray Sum Equals K Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals k. A subarray is a contiguous non-empty sequence of elements within an array.

## Approach

Use a running prefix sum and a HashMap tracking how many times each prefix sum has occurred. For each index, if prefixSum - k exists in the map, it means there are that many subarrays ending at the current index that sum to k. Initialize the map with {0: 1} to handle subarrays starting from index 0.

## Solution

```java
package org.interview.coding.hashing;

import java.util.HashMap;

/**
 * Problem: Subarray Sum Equals K
 * Difficulty: Medium
 *
 * Description:
 * Given an array of integers nums and an integer k, return the total number of subarrays
 * whose sum equals k. A subarray is a contiguous non-empty sequence of elements within
 * an array.
 *
 * Example:
 *   Input: nums = [1,1,1], k = 2
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= nums.length <= 2 * 10^4
 *   - -1000 <= nums[i] <= 1000
 *   - -10^7 <= k <= 10^7
 *
 * Approach:
 *   Use a running prefix sum and a HashMap tracking how many times each prefix sum has
 *   occurred. For each index, if prefixSum - k exists in the map, it means there are that
 *   many subarrays ending at the current index that sum to k. Initialize the map with
 *   {0: 1} to handle subarrays starting from index 0.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: nums=[1,1,1], k=2 → Output: 2
 *   2. Input: nums=[1,2,3], k=3 → Output: 2 ([1,2] and [3])
 *   3. Edge case: nums=[-1,-1,1], k=-1 → Output: 2
 */
public class SubarraySumEqualsK {

    public int subarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);
        int sum = 0, count = 0;

        for (int num : nums) {
            sum += num;
            count += prefixCount.getOrDefault(sum - k, 0);
            prefixCount.merge(sum, 1, Integer::sum);
        }
        return count;
    }

    public static void main(String[] args) {
        SubarraySumEqualsK sol = new SubarraySumEqualsK();
        // Test 1
        System.out.println(sol.subarraySum(new int[]{1, 1, 1}, 2));      // 2
        // Test 2
        System.out.println(sol.subarraySum(new int[]{1, 2, 3}, 3));      // 2
        // Test 3 (edge case: negative numbers)
        System.out.println(sol.subarraySum(new int[]{-1, -1, 1}, -1));   // 2
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
