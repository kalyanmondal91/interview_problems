package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Find Minimum in Rotated Sorted Array
 * Difficulty: Medium
 *
 * Description:
 * Suppose an array of length n sorted in ascending order is rotated between 1 and n times.
 * Given the sorted rotated array nums of unique elements, return the minimum element.
 * You must write an algorithm that runs in O(log n) time.
 *
 * Example:
 *   Input: nums = [3,4,5,1,2]
 *   Output: 1
 *
 * Constraints:
 *   - n == nums.length
 *   - 1 <= n <= 5000
 *   - -5000 <= nums[i] <= 5000
 *   - All integers in nums are unique
 *
 * Approach:
 *   Binary search. If nums[mid] > nums[hi], the minimum is in the right half (lo=mid+1).
 *   Otherwise, the minimum is in the left half including mid (hi=mid).
 *   When lo == hi, we've found the minimum. This handles all rotation cases.
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [3,4,5,1,2] → Output: 1
 *   2. Input: [4,5,6,7,0,1,2] → Output: 0
 *   3. Edge: [1] → Output: 1
 */
public class FindMinimumInRotatedSortedArray {

    public int findMin(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] > nums[hi]) lo = mid + 1;
            else hi = mid;
        }
        return nums[lo];
    }

    public static void main(String[] args) {
        FindMinimumInRotatedSortedArray sol = new FindMinimumInRotatedSortedArray();

        // Test 1
        System.out.println("Test 1: " + sol.findMin(new int[]{3, 4, 5, 1, 2})); // Expected: 1

        // Test 2
        System.out.println("Test 2: " + sol.findMin(new int[]{4, 5, 6, 7, 0, 1, 2})); // Expected: 0

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.findMin(new int[]{1})); // Expected: 1
    }
}
