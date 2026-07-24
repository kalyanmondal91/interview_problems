package org.interview.coding.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: 3Sum
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums, return all the unique triplets [nums[i], nums[j], nums[k]]
 * such that i != j != k and nums[i] + nums[j] + nums[k] == 0. The solution set must not
 * contain duplicate triplets.
 *
 * Example:
 *   Input: nums = [-1,0,1,2,-1,-4]
 *   Output: [[-1,-1,2],[-1,0,1]]
 *
 * Constraints:
 *   - 3 <= nums.length <= 3000
 *   - -10^5 <= nums[i] <= 10^5
 *
 * Approach:
 *   Sort the array first to enable duplicate skipping and two-pointer technique. For each
 *   element (fixed as the first of the triplet), use two pointers from both ends of the
 *   remaining subarray. Skip duplicate values for the fixed element and also skip duplicates
 *   found by the two pointers after recording a valid triplet.
 *
 * Time Complexity: O(n^2)
 * Space Complexity: O(1) extra (excluding output)
 *
 * Test Cases:
 *   1. Input: nums=[-1,0,1,2,-1,-4] → Output: [[-1,-1,2],[-1,0,1]]
 *   2. Input: nums=[0,1,1] → Output: []
 *   3. Edge case: nums=[0,0,0] → Output: [[0,0,0]]
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip duplicates for first element

            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ThreeSum sol = new ThreeSum();
        // Test 1
        System.out.println(sol.threeSum(new int[]{-1, 0, 1, 2, -1, -4})); // [[-1,-1,2],[-1,0,1]]
        // Test 2
        System.out.println(sol.threeSum(new int[]{0, 1, 1}));              // []
        // Test 3 (edge case: all zeros)
        System.out.println(sol.threeSum(new int[]{0, 0, 0}));              // [[0,0,0]]
    }
}
