---
layout: problem
title: "House Robber"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 198
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

House Robber (I & II) House Robber I: Given an array of non-negative integers representing money in each house, return the maximum amount you can rob without robbing two adjacent houses. House Robber II: Same problem but houses are arranged in a circle (first and last are adjacent). Solve by running linear robber on [0..n-2] and [1..n-1], taking max.

## Approach

Maintain two variables: prev2 (max profit two houses back) and prev1 (max profit one house back). At each house i, current = max(prev1, prev2 + nums[i]). Update prev2 = prev1, prev1 = current. For circular variant, run this twice: once excluding last house, once excluding first house, and return the maximum.

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: House Robber (I & II)
 * Difficulty: Medium
 *
 * Description:
 * House Robber I: Given an array of non-negative integers representing money in each
 * house, return the maximum amount you can rob without robbing two adjacent houses.
 * House Robber II: Same problem but houses are arranged in a circle (first and last
 * are adjacent). Solve by running linear robber on [0..n-2] and [1..n-1], taking max.
 *
 * Example:
 *   Input: nums=[2,7,9,3,1]
 *   Output: 12 (rob houses 0,2,4)
 *
 * Constraints:
 *   - 1 <= nums.length <= 100
 *   - 0 <= nums[i] <= 400
 *
 * Approach:
 *   Maintain two variables: prev2 (max profit two houses back) and prev1 (max profit
 *   one house back). At each house i, current = max(prev1, prev2 + nums[i]).
 *   Update prev2 = prev1, prev1 = current. For circular variant, run this twice:
 *   once excluding last house, once excluding first house, and return the maximum.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,3,1] → Output: 4 (rob houses 0 and 2)
 *   2. Input: [2,7,9,3,1] → Output: 12
 *   3. Edge (circular): [2,3,2] → Output: 3 (cannot rob both ends)
 */
public class HouseRobber {

    public int rob(int[] nums) {
        if (nums.length == 1) return nums[0];
        int prev2 = 0, prev1 = 0;
        for (int num : nums) {
            int curr = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }

    private int robLinear(int[] nums, int start, int end) {
        int prev2 = 0, prev1 = 0;
        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }

    public int robCircular(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        return Math.max(robLinear(nums, 0, n - 2), robLinear(nums, 1, n - 1));
    }

    public static void main(String[] args) {
        HouseRobber sol = new HouseRobber();

        System.out.println("Test 1 [1,2,3,1] (expect 4): " + sol.rob(new int[]{1, 2, 3, 1}));
        System.out.println("Test 2 [2,7,9,3,1] (expect 12): " + sol.rob(new int[]{2, 7, 9, 3, 1}));
        System.out.println("Test 3 circular [2,3,2] (expect 3): " + sol.robCircular(new int[]{2, 3, 2}));
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
