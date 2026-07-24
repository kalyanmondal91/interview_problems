---
layout: problem
title: "Partition Equal Subset Sum"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Medium
time_complexity: "O(n * target)"
space_complexity: "O(target)"
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

Partition Equal Subset Sum Given a non-empty array of positive integers, determine if the array can be partitioned into two subsets such that the sum of elements in both subsets is equal.

## Approach

If total sum is odd, impossible. Otherwise target = totalSum / 2. Boolean subset sum DP: dp[j] = true if subset summing to j exists. For each number, iterate j from target down to nums[i]: dp[j] |= dp[j-nums[i]]. Reverse traversal ensures each number is used at most once (0/1 knapsack style). Return dp[target].

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Partition Equal Subset Sum
 * Difficulty: Medium
 *
 * Description:
 * Given a non-empty array of positive integers, determine if the array can be
 * partitioned into two subsets such that the sum of elements in both subsets is equal.
 *
 * Example:
 *   Input: nums=[1,5,11,5]
 *   Output: true ([1,5,5] and [11])
 *
 * Constraints:
 *   - 1 <= nums.length <= 200
 *   - 1 <= nums[i] <= 100
 *
 * Approach:
 *   If total sum is odd, impossible. Otherwise target = totalSum / 2.
 *   Boolean subset sum DP: dp[j] = true if subset summing to j exists.
 *   For each number, iterate j from target down to nums[i]: dp[j] |= dp[j-nums[i]].
 *   Reverse traversal ensures each number is used at most once (0/1 knapsack style).
 *   Return dp[target].
 *
 * Time Complexity: O(n * target)
 * Space Complexity: O(target)
 *
 * Test Cases:
 *   1. Input: [1,5,11,5] → Output: true
 *   2. Input: [1,2,3,5] → Output: false
 *   3. Edge: [1,1] → Output: true
 */
public class PartitionEqualSubsetSum {

    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int n : nums) total += n;
        if (total % 2 != 0) return false;
        int target = total / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        return dp[target];
    }

    public static void main(String[] args) {
        PartitionEqualSubsetSum sol = new PartitionEqualSubsetSum();

        System.out.println("Test 1 [1,5,11,5] (expect true): "
                + sol.canPartition(new int[]{1, 5, 11, 5}));
        System.out.println("Test 2 [1,2,3,5] (expect false): "
                + sol.canPartition(new int[]{1, 2, 3, 5}));
        System.out.println("Test 3 [1,1] (expect true): "
                + sol.canPartition(new int[]{1, 1}));
    }
}
```

## Complexity

- **Time:** O(n * target)
- **Space:** O(target)
