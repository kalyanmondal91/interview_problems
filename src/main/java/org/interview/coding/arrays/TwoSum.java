package org.interview.coding.arrays;

import java.util.HashMap;
import java.util.Arrays;

/**
 * Problem: Two Sum
 * Difficulty: Easy
 *
 * Description:
 * Given an array of integers nums and an integer target, return the indices of the two numbers
 * such that they add up to target. You may assume that each input would have exactly one solution,
 * and you may not use the same element twice.
 *
 * Example:
 *   Input: nums = [2,7,11,15], target = 9
 *   Output: [0,1]
 *
 * Constraints:
 *   - 2 <= nums.length <= 10^4
 *   - -10^9 <= nums[i] <= 10^9
 *   - Only one valid answer exists
 *
 * Approach:
 *   Use a HashMap to store each number and its index as we iterate through the array.
 *   For each element, compute the complement (target - current) and check if it already
 *   exists in the map. If it does, we found our pair. This avoids the O(n^2) brute force
 *   by reducing lookups to O(1) amortized with hashing.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: nums=[2,7,11,15], target=9 → Output: [0,1]
 *   2. Input: nums=[3,2,4], target=6 → Output: [1,2]
 *   3. Edge case: nums=[3,3], target=6 → Output: [0,1]
 */
public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        TwoSum sol = new TwoSum();
        // Test 1
        System.out.println(Arrays.toString(sol.twoSum(new int[]{2, 7, 11, 15}, 9))); // [0,1]
        // Test 2
        System.out.println(Arrays.toString(sol.twoSum(new int[]{3, 2, 4}, 6)));      // [1,2]
        // Test 3 (edge case: duplicate values)
        System.out.println(Arrays.toString(sol.twoSum(new int[]{3, 3}, 6)));          // [0,1]
    }
}
