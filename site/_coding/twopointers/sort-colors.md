---
layout: problem
title: "Sort Colors"
category: twopointers
category_display: "Two Pointers"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 75
tags: [twopointers]
render_with_liquid: false
---

## Problem

Sort Colors Given an array nums with n objects colored red (0), white (1), or blue (2), sort them in-place so that objects of the same color are adjacent, in the order red, white, blue. You must solve this without using the library's sort function.

## Approach

Use the Dutch National Flag algorithm with three pointers: low, mid, and high. All elements before 'low' are 0, elements between 'low' and 'mid' are 1, and elements after 'high' are 2. Advance mid through the array, swapping 0s to the front and 2s to the back, until mid > high.

## Solution

```java
package org.interview.coding.twopointers;

import java.util.Arrays;

/**
 * Problem: Sort Colors
 * Difficulty: Medium
 *
 * Description:
 * Given an array nums with n objects colored red (0), white (1), or blue (2), sort them
 * in-place so that objects of the same color are adjacent, in the order red, white, blue.
 * You must solve this without using the library's sort function.
 *
 * Example:
 *   Input: nums = [2,0,2,1,1,0]
 *   Output: [0,0,1,1,2,2]
 *
 * Constraints:
 *   - n == nums.length
 *   - 1 <= n <= 300
 *   - nums[i] is either 0, 1, or 2
 *
 * Approach:
 *   Use the Dutch National Flag algorithm with three pointers: low, mid, and high. All
 *   elements before 'low' are 0, elements between 'low' and 'mid' are 1, and elements
 *   after 'high' are 2. Advance mid through the array, swapping 0s to the front and 2s
 *   to the back, until mid > high.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: nums=[2,0,2,1,1,0] → Output: [0,0,1,1,2,2]
 *   2. Input: nums=[2,0,1] → Output: [0,1,2]
 *   3. Edge case: nums=[0] → Output: [0]
 */
public class SortColors {

    public void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                int temp = nums[low]; nums[low] = nums[mid]; nums[mid] = temp;
                low++; mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                int temp = nums[mid]; nums[mid] = nums[high]; nums[high] = temp;
                high--;
            }
        }
    }

    public static void main(String[] args) {
        SortColors sol = new SortColors();
        // Test 1
        int[] nums1 = {2, 0, 2, 1, 1, 0};
        sol.sortColors(nums1);
        System.out.println(Arrays.toString(nums1)); // [0,0,1,1,2,2]
        // Test 2
        int[] nums2 = {2, 0, 1};
        sol.sortColors(nums2);
        System.out.println(Arrays.toString(nums2)); // [0,1,2]
        // Test 3 (edge case: single element)
        int[] nums3 = {0};
        sol.sortColors(nums3);
        System.out.println(Arrays.toString(nums3)); // [0]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
