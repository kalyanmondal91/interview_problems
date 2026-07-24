---
layout: problem
title: "Product Of Array Except Self"
category: arrays
category_display: "Arrays"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1) extra (output array not counted)"
leetcode: 238
tags: [arrays]
render_with_liquid: false
---

## Problem

Product of Array Except Self Given an integer array nums, return an array answer such that answer[i] is equal to the product of all elements of nums except nums[i]. The algorithm must run in O(n) time and without using the division operation.

## Approach

Build the result array in two passes. In the first pass (left to right), store the running prefix product at each index (product of all elements to the left). In the second pass (right to left), multiply by the running suffix product (product of all elements to the right). This achieves O(n) time and O(1) extra space (excluding output).

## Solution

```java
package org.interview.coding.arrays;

import java.util.Arrays;

/**
 * Problem: Product of Array Except Self
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums, return an array answer such that answer[i] is equal to the
 * product of all elements of nums except nums[i]. The algorithm must run in O(n) time and
 * without using the division operation.
 *
 * Example:
 *   Input: nums = [1,2,3,4]
 *   Output: [24,12,8,6]
 *
 * Constraints:
 *   - 2 <= nums.length <= 10^5
 *   - -30 <= nums[i] <= 30
 *   - The product of any prefix or suffix fits in a 32-bit integer
 *
 * Approach:
 *   Build the result array in two passes. In the first pass (left to right), store the
 *   running prefix product at each index (product of all elements to the left). In the
 *   second pass (right to left), multiply by the running suffix product (product of all
 *   elements to the right). This achieves O(n) time and O(1) extra space (excluding output).
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1) extra (output array not counted)
 *
 * Test Cases:
 *   1. Input: nums=[1,2,3,4] → Output: [24,12,8,6]
 *   2. Input: nums=[-1,1,0,-3,3] → Output: [0,0,9,0,0]
 *   3. Edge case: nums=[0,0] → Output: [0,0]
 */
public class ProductOfArrayExceptSelf {

    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // First pass: prefix products
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        // Second pass: multiply by suffix products
        int suffixProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= suffixProduct;
            suffixProduct *= nums[i];
        }

        return result;
    }

    public static void main(String[] args) {
        ProductOfArrayExceptSelf sol = new ProductOfArrayExceptSelf();
        // Test 1
        System.out.println(Arrays.toString(sol.productExceptSelf(new int[]{1, 2, 3, 4})));        // [24,12,8,6]
        // Test 2
        System.out.println(Arrays.toString(sol.productExceptSelf(new int[]{-1, 1, 0, -3, 3})));   // [0,0,9,0,0]
        // Test 3 (edge case: two zeros)
        System.out.println(Arrays.toString(sol.productExceptSelf(new int[]{0, 0})));               // [0,0]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1) extra (output array not counted)
