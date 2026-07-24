package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Search in Rotated Sorted Array
 * Difficulty: Medium
 *
 * Description:
 * There is an integer array nums sorted in ascending order (with distinct values) that has been
 * rotated at an unknown pivot. Given the array nums and an integer target, return the index of
 * target if it is in nums, or -1 if it is not.
 *
 * Example:
 *   Input: nums = [4,5,6,7,0,1,2], target = 0
 *   Output: 4
 *
 * Constraints:
 *   - 1 <= nums.length <= 5000
 *   - -10^4 <= nums[i] <= 10^4
 *   - All values in nums are unique
 *
 * Approach:
 *   Modified binary search. Determine which half is sorted.
 *   If nums[lo] <= nums[mid], the left half is sorted: check if target is in [nums[lo], nums[mid]).
 *   Otherwise, the right half is sorted: check if target is in (nums[mid], nums[hi]].
 *   Narrow the search window accordingly. This achieves O(log n).
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [4,5,6,7,0,1,2], target=0 → Output: 4
 *   2. Input: [4,5,6,7,0,1,2], target=3 → Output: -1
 *   3. Edge: [1], target=0 → Output: -1
 */
public class SearchInRotatedSortedArray {

    public int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;

            // Left half is sorted
            if (nums[lo] <= nums[mid]) {
                if (nums[lo] <= target && target < nums[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else {
                // Right half is sorted
                if (nums[mid] < target && target <= nums[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SearchInRotatedSortedArray sol = new SearchInRotatedSortedArray();

        // Test 1
        System.out.println("Test 1: " + sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0)); // Expected: 4

        // Test 2
        System.out.println("Test 2: " + sol.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3)); // Expected: -1

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.search(new int[]{1}, 0)); // Expected: -1
    }
}
