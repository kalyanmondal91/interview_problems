---
layout: problem
title: "Kth Largest Element In Array"
category: heaps
category_display: "Heaps"
difficulty: Medium
time_complexity: "O(n log k)"
space_complexity: "O(k)"
leetcode: 215
tags: [heaps]
render_with_liquid: false
---

## Problem

Kth Largest Element in an Array Given an integer array nums and an integer k, return the kth largest element in the array. Note that it is the kth largest element in sorted order, not the kth distinct element. You must solve it in O(n log k) time complexity.

## Approach

Use a min-heap of size k. For each element in the array, add it to the heap. If the heap size exceeds k, poll the minimum element (the smallest of the k largest seen so far). After processing all elements, the top of the heap is the kth largest element. This avoids sorting the entire array and runs in O(n log k) time. Alternative approach: quickselect gives O(n) average time but O(n^2) worst case.

## Solution

```java
package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Kth Largest Element in an Array
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums and an integer k, return the kth largest element in the array.
 * Note that it is the kth largest element in sorted order, not the kth distinct element.
 * You must solve it in O(n log k) time complexity.
 *
 * Example:
 *   Input: nums = [3,2,1,5,6,4], k = 2
 *   Output: 5
 *
 * Constraints:
 *   - 1 <= k <= nums.length <= 10^5
 *   - -10^4 <= nums[i] <= 10^4
 *
 * Approach:
 *   Use a min-heap of size k. For each element in the array, add it to the heap.
 *   If the heap size exceeds k, poll the minimum element (the smallest of the k largest seen so far).
 *   After processing all elements, the top of the heap is the kth largest element.
 *   This avoids sorting the entire array and runs in O(n log k) time.
 *   Alternative approach: quickselect gives O(n) average time but O(n^2) worst case.
 *
 * Time Complexity: O(n log k)
 * Space Complexity: O(k)
 *
 * Test Cases:
 *   1. Input: [3,2,1,5,6,4], k=2 → Output: 5
 *   2. Input: [3,2,3,1,2,4,5,5,6], k=4 → Output: 4
 *   3. Edge: [1], k=1 → Output: 1
 */
public class KthLargestElementInArray {

    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    // Alternative: Quickselect O(avg n)
    public int findKthLargestQuickSelect(int[] nums, int k) {
        int targetIndex = nums.length - k;
        return quickSelect(nums, 0, nums.length - 1, targetIndex);
    }

    private int quickSelect(int[] nums, int left, int right, int k) {
        int pivot = nums[right];
        int p = left;
        for (int i = left; i < right; i++) {
            if (nums[i] <= pivot) {
                int tmp = nums[p]; nums[p] = nums[i]; nums[i] = tmp;
                p++;
            }
        }
        int tmp = nums[p]; nums[p] = nums[right]; nums[right] = tmp;
        if (p == k) return nums[p];
        else if (p < k) return quickSelect(nums, p + 1, right, k);
        else return quickSelect(nums, left, p - 1, k);
    }

    public static void main(String[] args) {
        KthLargestElementInArray sol = new KthLargestElementInArray();

        // Test 1
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        System.out.println("Test 1: " + sol.findKthLargest(nums1, 2)); // Expected: 5

        // Test 2
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        System.out.println("Test 2: " + sol.findKthLargest(nums2, 4)); // Expected: 4

        // Test 3 (edge case)
        int[] nums3 = {1};
        System.out.println("Test 3: " + sol.findKthLargest(nums3, 1)); // Expected: 1
    }
}
```

## Complexity

- **Time:** O(n log k)
- **Space:** O(k)
