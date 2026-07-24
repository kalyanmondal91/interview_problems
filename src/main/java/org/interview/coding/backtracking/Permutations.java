package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Permutations
 * Difficulty: Medium
 *
 * Description:
 * Given an array nums of distinct integers, return all the possible permutations in any order.
 *
 * Example:
 *   Input: nums = [1,2,3]
 *   Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 * Constraints:
 *   - 1 <= nums.length <= 6
 *   - -10 <= nums[i] <= 10
 *   - All integers in nums are unique.
 *
 * Approach:
 *   Use backtracking with a boolean used[] array to track which elements have been placed. At each
 *   step, try each unused element, mark it as used, add to current permutation, recurse, then
 *   unmark and remove. When the current permutation length equals nums.length, add a copy to the
 *   result. This generates all n! permutations systematically.
 *
 * Time Complexity: O(n * n!)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [1,2,3] → Output: 6 permutations
 *   2. Input: [0,1] → Output: [[0,1],[1,0]]
 *   3. Edge: [1] → Output: [[1]]
 */
public class Permutations {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            current.add(nums[i]);
            backtrack(nums, used, current, result);
            current.remove(current.size() - 1);
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        Permutations solution = new Permutations();

        // Test 1: three elements - 6 permutations
        System.out.println(solution.permute(new int[]{1, 2, 3}));

        // Test 2: two elements
        System.out.println(solution.permute(new int[]{0, 1}));
        // Expected: [[0,1],[1,0]]

        // Test 3: edge case single element
        System.out.println(solution.permute(new int[]{1}));
        // Expected: [[1]]
    }
}
