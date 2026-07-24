package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Longest Increasing Subsequence
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array, return the length of the longest strictly increasing
 * subsequence. A subsequence is derived by deleting some elements without changing
 * the relative order of the remaining elements.
 *
 * Example:
 *   Input: nums=[10,9,2,5,3,7,101,18]
 *   Output: 4 ([2,3,7,101])
 *
 * Constraints:
 *   - 1 <= nums.length <= 2500
 *   - -10^4 <= nums[i] <= 10^4
 *
 * Approach:
 *   O(n log n) patience sorting approach: maintain a tails array where tails[i] is the
 *   smallest tail element of all increasing subsequences of length i+1. For each number,
 *   binary search tails to find the first element >= current number. If found, replace
 *   it (smaller tail allows more future extensions); if not found, append (extend LIS).
 *   The length of tails is the LIS length.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [10,9,2,5,3,7,101,18] → Output: 4
 *   2. Input: [0,1,0,3,2,3] → Output: 4
 *   3. Edge: [7,7,7,7] → Output: 1 (no strictly increasing)
 */
public class LongestIncreasingSubsequence {

    public int lengthOfLIS(int[] nums) {
        List<Integer> tails = new ArrayList<>();
        for (int num : nums) {
            int lo = 0, hi = tails.size();
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails.get(mid) < num) lo = mid + 1;
                else hi = mid;
            }
            if (lo == tails.size()) {
                tails.add(num);
            } else {
                tails.set(lo, num);
            }
        }
        return tails.size();
    }

    public static void main(String[] args) {
        LongestIncreasingSubsequence sol = new LongestIncreasingSubsequence();

        System.out.println("Test 1 [10,9,2,5,3,7,101,18] (expect 4): "
                + sol.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        System.out.println("Test 2 [0,1,0,3,2,3] (expect 4): "
                + sol.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
        System.out.println("Test 3 [7,7,7,7] (expect 1): "
                + sol.lengthOfLIS(new int[]{7, 7, 7, 7}));
    }
}
