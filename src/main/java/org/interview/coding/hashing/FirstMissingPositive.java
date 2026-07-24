package org.interview.coding.hashing;

/**
 * Problem: First Missing Positive
 * Difficulty: Hard
 *
 * Description:
 * Given an unsorted integer array nums, return the smallest missing positive integer.
 * The algorithm must run in O(n) time and use O(1) auxiliary space.
 *
 * Example:
 *   Input: nums = [1,2,0]
 *   Output: 3
 *
 * Constraints:
 *   - 1 <= nums.length <= 5 * 10^5
 *   - -2^31 <= nums[i] <= 2^31 - 1
 *
 * Approach:
 *   Use the array itself as a hash map. The answer must be in [1, n+1]. First, mark invalid
 *   values (<=0 or >n) as n+1. Then for each value v in [1, n], mark nums[v-1] as negative
 *   (if not already). Finally, scan for the first index with a positive value — that index+1
 *   is the missing positive. If all indices are negative, return n+1.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: nums=[1,2,0] → Output: 3
 *   2. Input: nums=[3,4,-1,1] → Output: 2
 *   3. Edge case: nums=[7,8,9,11,12] → Output: 1
 */
public class FirstMissingPositive {

    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // Step 1: Replace invalid values with n+1
        for (int i = 0; i < n; i++) {
            if (nums[i] <= 0 || nums[i] > n) nums[i] = n + 1;
        }

        // Step 2: Mark presence using sign
        for (int i = 0; i < n; i++) {
            int val = Math.abs(nums[i]);
            if (val <= n) {
                nums[val - 1] = -Math.abs(nums[val - 1]);
            }
        }

        // Step 3: Find first positive index
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) return i + 1;
        }
        return n + 1;
    }

    public static void main(String[] args) {
        FirstMissingPositive sol = new FirstMissingPositive();
        // Test 1
        System.out.println(sol.firstMissingPositive(new int[]{1, 2, 0}));         // 3
        // Test 2
        System.out.println(sol.firstMissingPositive(new int[]{3, 4, -1, 1}));     // 2
        // Test 3 (edge case: all large numbers)
        System.out.println(sol.firstMissingPositive(new int[]{7, 8, 9, 11, 12})); // 1
    }
}
