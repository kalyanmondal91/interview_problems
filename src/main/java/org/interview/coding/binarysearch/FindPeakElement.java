package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Find Peak Element
 * Difficulty: Medium
 *
 * Description:
 * A peak element is an element that is strictly greater than its neighbors.
 * Given a 0-indexed integer array nums, find a peak element and return its index.
 * If the array contains multiple peaks, return the index to any of the peaks.
 * You must write an algorithm that runs in O(log n) time.
 *
 * Example:
 *   Input: nums = [1,2,3,1]
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= nums.length <= 1000
 *   - -2^31 <= nums[i] <= 2^31 - 1
 *   - nums[i] != nums[i+1] for all valid i
 *
 * Approach:
 *   Binary search. If nums[mid] < nums[mid+1], the peak must be in the right half (lo=mid+1)
 *   because the sequence is still ascending. Otherwise the peak is in the left half including
 *   mid (hi=mid). When lo==hi, we've found a peak. This works due to the boundary conditions.
 *
 * Time Complexity: O(log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,3,1] → Output: 2
 *   2. Input: [1,2,1,3,5,6,4] → Output: 5 (or 1)
 *   3. Edge: [1] → Output: 0
 */
public class FindPeakElement {

    public int findPeakElement(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] < nums[mid + 1]) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    public static void main(String[] args) {
        FindPeakElement sol = new FindPeakElement();

        // Test 1
        System.out.println("Test 1: " + sol.findPeakElement(new int[]{1, 2, 3, 1})); // Expected: 2

        // Test 2
        System.out.println("Test 2: " + sol.findPeakElement(new int[]{1, 2, 1, 3, 5, 6, 4})); // Expected: 1 or 5

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.findPeakElement(new int[]{1})); // Expected: 0
    }
}
