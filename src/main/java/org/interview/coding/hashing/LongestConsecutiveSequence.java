package org.interview.coding.hashing;

import java.util.HashSet;

/**
 * Problem: Longest Consecutive Sequence
 * Difficulty: Medium
 *
 * Description:
 * Given an unsorted array of integers nums, return the length of the longest consecutive
 * elements sequence. The algorithm must run in O(n) time.
 *
 * Example:
 *   Input: nums = [100,4,200,1,3,2]
 *   Output: 4
 *
 * Constraints:
 *   - 0 <= nums.length <= 10^5
 *   - -10^9 <= nums[i] <= 10^9
 *
 * Approach:
 *   Add all numbers to a HashSet for O(1) lookup. Then iterate through the array. For each
 *   number, only start counting a sequence if it is the beginning of one (i.e., num-1 is not
 *   in the set). From that start, keep incrementing and checking if the next number exists.
 *   This ensures each number is visited at most twice overall, keeping it O(n).
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: nums=[100,4,200,1,3,2] → Output: 4 (sequence 1,2,3,4)
 *   2. Input: nums=[0,3,7,2,5,8,4,6,0,1] → Output: 9
 *   3. Edge case: nums=[] → Output: 0
 */
public class LongestConsecutiveSequence {

    public int longestConsecutive(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int maxLen = 0;
        for (int n : set) {
            if (!set.contains(n - 1)) { // start of a sequence
                int curr = n, len = 1;
                while (set.contains(curr + 1)) {
                    curr++;
                    len++;
                }
                maxLen = Math.max(maxLen, len);
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        LongestConsecutiveSequence sol = new LongestConsecutiveSequence();
        // Test 1
        System.out.println(sol.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));      // 4
        // Test 2
        System.out.println(sol.longestConsecutive(new int[]{0,3,7,2,5,8,4,6,0,1}));       // 9
        // Test 3 (edge case: empty array)
        System.out.println(sol.longestConsecutive(new int[]{}));                           // 0
    }
}
