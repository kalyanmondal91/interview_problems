---
layout: problem
title: "Remove Duplicates From Sorted Array"
category: twopointers
category_display: "Two Pointers"
difficulty: Easy
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 26
tags: [twopointers]
render_with_liquid: false
---

## Problem

Remove Duplicates from Sorted Array Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once. Return the number of unique elements k. The first k elements of nums should hold the result; the rest doesn't matter.

## Approach

Use two pointers: a slow pointer (k) that tracks the position to write the next unique element, and a fast pointer (i) that scans the array. Whenever nums[i] differs from nums[k-1] (the last written unique element), write it at position k and increment k. The array is sorted so all duplicates are adjacent.

## Solution

```java
package org.interview.coding.twopointers;

/**
 * Problem: Remove Duplicates from Sorted Array
 * Difficulty: Easy
 *
 * Description:
 * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place
 * such that each unique element appears only once. Return the number of unique elements k.
 * The first k elements of nums should hold the result; the rest doesn't matter.
 *
 * Example:
 *   Input: nums = [1,1,2]
 *   Output: 2, nums = [1,2,_]
 *
 * Constraints:
 *   - 1 <= nums.length <= 3 * 10^4
 *   - -100 <= nums[i] <= 100
 *   - nums is sorted in non-decreasing order
 *
 * Approach:
 *   Use two pointers: a slow pointer (k) that tracks the position to write the next unique
 *   element, and a fast pointer (i) that scans the array. Whenever nums[i] differs from
 *   nums[k-1] (the last written unique element), write it at position k and increment k.
 *   The array is sorted so all duplicates are adjacent.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: nums=[1,1,2] → Output: 2
 *   2. Input: nums=[0,0,1,1,1,2,2,3,3,4] → Output: 5
 *   3. Edge case: nums=[1] → Output: 1
 */
public class RemoveDuplicatesFromSortedArray {

    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int k = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[k - 1]) {
                nums[k++] = nums[i];
            }
        }
        return k;
    }

    public static void main(String[] args) {
        RemoveDuplicatesFromSortedArray sol = new RemoveDuplicatesFromSortedArray();
        // Test 1
        int[] nums1 = {1, 1, 2};
        System.out.println(sol.removeDuplicates(nums1)); // 2
        // Test 2
        int[] nums2 = {0,0,1,1,1,2,2,3,3,4};
        System.out.println(sol.removeDuplicates(nums2)); // 5
        // Test 3 (edge case: single element)
        int[] nums3 = {1};
        System.out.println(sol.removeDuplicates(nums3)); // 1
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
