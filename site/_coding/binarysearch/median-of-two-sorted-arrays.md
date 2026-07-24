---
layout: problem
title: "Median Of Two Sorted Arrays"
category: binarysearch
category_display: "Binary Search"
difficulty: Hard
time_complexity: "O(log(min(m,n)))"
space_complexity: "O(1)"
tags: [binarysearch]
render_with_liquid: false
---

## Problem

Median of Two Sorted Arrays Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays. The overall run time complexity should be O(log(min(m,n))).

## Approach

Binary search on the smaller array to find the correct partition point. Partition both arrays such that all elements on the left are <= all on the right. The partition is valid when maxLeft1 <= minRight2 and maxLeft2 <= minRight1. Compute median from the four boundary values depending on total length parity.

## Solution

```java
package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Median of Two Sorted Arrays
 * Difficulty: Hard
 *
 * Description:
 * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of
 * the two sorted arrays. The overall run time complexity should be O(log(min(m,n))).
 *
 * Example:
 *   Input: nums1 = [1,3], nums2 = [2]
 *   Output: 2.00000
 *
 * Constraints:
 *   - nums1.length == m, nums2.length == n
 *   - 0 <= m <= 1000, 0 <= n <= 1000
 *   - 1 <= m + n <= 2000
 *   - -10^6 <= nums1[i], nums2[i] <= 10^6
 *
 * Approach:
 *   Binary search on the smaller array to find the correct partition point.
 *   Partition both arrays such that all elements on the left are <= all on the right.
 *   The partition is valid when maxLeft1 <= minRight2 and maxLeft2 <= minRight1.
 *   Compute median from the four boundary values depending on total length parity.
 *
 * Time Complexity: O(log(min(m,n)))
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: nums1=[1,3], nums2=[2] → Output: 2.0
 *   2. Input: nums1=[1,2], nums2=[3,4] → Output: 2.5
 *   3. Edge: nums1=[], nums2=[1] → Output: 1.0
 */
public class MedianOfTwoSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) return findMedianSortedArrays(nums2, nums1);
        int m = nums1.length, n = nums2.length;
        int lo = 0, hi = m;

        while (lo <= hi) {
            int i = lo + (hi - lo) / 2;
            int j = (m + n + 1) / 2 - i;

            int maxLeft1 = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            int minRight1 = (i == m) ? Integer.MAX_VALUE : nums1[i];
            int maxLeft2 = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            int minRight2 = (j == n) ? Integer.MAX_VALUE : nums2[j];

            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                if ((m + n) % 2 == 1) return Math.max(maxLeft1, maxLeft2);
                return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
            } else if (maxLeft1 > minRight2) {
                hi = i - 1;
            } else {
                lo = i + 1;
            }
        }
        return 0.0;
    }

    public static void main(String[] args) {
        MedianOfTwoSortedArrays sol = new MedianOfTwoSortedArrays();

        // Test 1
        System.out.println("Test 1: " + sol.findMedianSortedArrays(new int[]{1, 3}, new int[]{2})); // Expected: 2.0

        // Test 2
        System.out.println("Test 2: " + sol.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4})); // Expected: 2.5

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.findMedianSortedArrays(new int[]{}, new int[]{1})); // Expected: 1.0
    }
}
```

## Complexity

- **Time:** O(log(min(m,n)))
- **Space:** O(1)
