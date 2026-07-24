package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Jump Game
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums where nums[i] represents the maximum jump length from
 * index i, return true if you can reach the last index starting from index 0.
 *
 * Example:
 *   Input: nums=[2,3,1,1,4]
 *   Output: true
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^4
 *   - 0 <= nums[i] <= 10^5
 *
 * Approach:
 *   Greedy: track maxReach, the farthest index reachable so far. At each position i,
 *   update maxReach = max(maxReach, i + nums[i]). If i > maxReach, we're stuck and
 *   cannot proceed. If maxReach >= n-1 at any point, we can reach the end.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [2,3,1,1,4] → Output: true
 *   2. Input: [3,2,1,0,4] → Output: false
 *   3. Edge: [0] → Output: true (already at last index)
 */
public class JumpGame {

    public boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
            if (maxReach >= nums.length - 1) return true;
        }
        return true;
    }

    public static void main(String[] args) {
        JumpGame sol = new JumpGame();

        System.out.println("Test 1 [2,3,1,1,4] (expect true): "
                + sol.canJump(new int[]{2, 3, 1, 1, 4}));
        System.out.println("Test 2 [3,2,1,0,4] (expect false): "
                + sol.canJump(new int[]{3, 2, 1, 0, 4}));
        System.out.println("Test 3 [0] (expect true): "
                + sol.canJump(new int[]{0}));
    }
}
