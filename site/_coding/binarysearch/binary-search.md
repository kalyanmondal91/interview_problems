---
layout: problem
title: "Binary Search"
category: binarysearch
category_display: "Binary Search"
difficulty: Easy
time_complexity: "O(log n)"
space_complexity: "O(1)"
leetcode: 704
tags: [binarysearch]
render_with_liquid: false
---

## Problem

Binary Search Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums. If target exists, return its index. Otherwise, return -1.

## Approach

Classic iterative binary search. Maintain lo=0 and hi=n-1. Compute mid=(lo+hi)/2. If nums[mid]==target return mid. If target > nums[mid], move lo to mid+1; otherwise move hi to mid-1. If target is not found, return -1. This runs in O(log n).

## Solution

```java
package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Binary Search
 * Difficulty: Easy
 *
 * Description:
 * Given an array of integers nums which is sorted in ascending order, and an integer target,
 * write a function to search target in nums.
 * If target exists, return its index. Otherwise, return -1.
 *
 * Example:
 *   Input: nums = [-1,0,3,5,9,12], target = 9
 *   Output: 4
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^4
 *   - -10^4 < nums[i], target < 10^4
 *   - All integers in nums are unique and sorted in ascending order
 *
 * Approach:
 *   Classic iterative binary search. Maintain lo=0 and hi=n-1.
 *   Compute mid=(lo+hi)/2. If nums[mid]==target return mid.
 *   If target > nums[mid], move lo to mid+1; otherwise move hi to mid-1.
 *   If target is not found, return -1. This runs in O(log n).
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [-1,0,3,5,9,12], target=9 → Output: 4
 *   2. Input: [-1,0,3,5,9,12], target=2 → Output: -1
 *   3. Edge: [5], target=5 → Output: 0
 */
public class BinarySearch {

    public int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            else if (nums[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        BinarySearch sol = new BinarySearch();

        // Test 1
        System.out.println("Test 1: " + sol.search(new int[]{-1, 0, 3, 5, 9, 12}, 9)); // Expected: 4

        // Test 2
        System.out.println("Test 2: " + sol.search(new int[]{-1, 0, 3, 5, 9, 12}, 2)); // Expected: -1

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.search(new int[]{5}, 5)); // Expected: 0
    }
}
```

## Complexity

- **Time:** O(log n)
- **Space:** O(1)
