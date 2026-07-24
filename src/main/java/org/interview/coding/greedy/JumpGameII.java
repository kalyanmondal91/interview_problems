package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Jump Game II
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums where nums[i] represents the maximum jump length from
 * index i, return the minimum number of jumps to reach the last index. It is guaranteed
 * that you can always reach the last index.
 *
 * Example:
 *   Input: nums=[2,3,1,1,4]
 *   Output: 2 (jump 1→3, jump 4)
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^4
 *   - 0 <= nums[i] <= 1000
 *
 * Approach:
 *   Greedy BFS-style: maintain curEnd (end of current jump's range) and farthest
 *   (max reachable index so far). Traverse array; at each step update farthest.
 *   When i reaches curEnd, we must make another jump — increment jumps and set
 *   curEnd = farthest. Stop before the last index since we only need to reach it.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [2,3,1,1,4] → Output: 2
 *   2. Input: [2,3,0,1,4] → Output: 2
 *   3. Edge: [0] → Output: 0
 */
public class JumpGameII {

    public int jump(int[] nums) {
        int jumps = 0, curEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == curEnd) {
                jumps++;
                curEnd = farthest;
                if (curEnd >= nums.length - 1) break;
            }
        }
        return jumps;
    }

    public static void main(String[] args) {
        JumpGameII sol = new JumpGameII();

        System.out.println("Test 1 [2,3,1,1,4] (expect 2): "
                + sol.jump(new int[]{2, 3, 1, 1, 4}));
        System.out.println("Test 2 [2,3,0,1,4] (expect 2): "
                + sol.jump(new int[]{2, 3, 0, 1, 4}));
        System.out.println("Test 3 [0] (expect 0): "
                + sol.jump(new int[]{0}));
    }
}
