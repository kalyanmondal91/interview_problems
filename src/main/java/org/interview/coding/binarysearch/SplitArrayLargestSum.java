package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Split Array Largest Sum
 * Difficulty: Hard
 *
 * Description:
 * Given an integer array nums and an integer k, split nums into k non-empty subarrays such that
 * the largest sum of any subarray is minimized. Return the minimized largest sum.
 *
 * Example:
 *   Input: nums = [7,2,5,10,8], k = 2
 *   Output: 18
 *
 * Constraints:
 *   - 1 <= nums.length <= 1000
 *   - 0 <= nums[i] <= 10^6
 *   - 1 <= k <= min(50, nums.length)
 *
 * Approach:
 *   Binary search on the answer in range [max(nums), sum(nums)].
 *   For each candidate max sum, greedily count the minimum number of splits needed.
 *   If splits needed <= k, the candidate is feasible (hi=mid); otherwise increase (lo=mid+1).
 *   This approach avoids the O(n^2 * k) DP solution.
 *
 * Time Complexity: O(n log(sum(nums)))
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [7,2,5,10,8], k=2 → Output: 18
 *   2. Input: [1,2,3,4,5], k=2 → Output: 9
 *   3. Edge: [1,4,4], k=3 → Output: 4
 */
public class SplitArrayLargestSum {

    public int splitArray(int[] nums, int k) {
        int lo = 0, hi = 0;
        for (int n : nums) {
            lo = Math.max(lo, n);
            hi += n;
        }

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canSplit(nums, k, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    private boolean canSplit(int[] nums, int k, int maxSum) {
        int count = 1, current = 0;
        for (int n : nums) {
            if (current + n > maxSum) {
                count++;
                current = 0;
            }
            current += n;
        }
        return count <= k;
    }

    public static void main(String[] args) {
        SplitArrayLargestSum sol = new SplitArrayLargestSum();

        // Test 1
        System.out.println("Test 1: " + sol.splitArray(new int[]{7, 2, 5, 10, 8}, 2)); // Expected: 18

        // Test 2
        System.out.println("Test 2: " + sol.splitArray(new int[]{1, 2, 3, 4, 5}, 2)); // Expected: 9

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.splitArray(new int[]{1, 4, 4}, 3)); // Expected: 4
    }
}
