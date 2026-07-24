package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Climbing Stairs
 * Difficulty: Easy
 *
 * Description:
 * You are climbing a staircase with n steps. Each time you can climb 1 or 2 steps.
 * In how many distinct ways can you climb to the top?
 *
 * Example:
 *   Input: n=4
 *   Output: 5
 *
 * Constraints:
 *   - 1 <= n <= 45
 *
 * Approach:
 *   This is essentially the Fibonacci sequence. The number of ways to reach step i
 *   equals the number of ways to reach step i-1 (take 1 step) plus the number of ways
 *   to reach step i-2 (take 2 steps). Base cases: dp[1]=1, dp[2]=2.
 *   Optimize to O(1) space by maintaining only two previous values (prev1, prev2).
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: n=2 → Output: 2
 *   2. Input: n=4 → Output: 5
 *   3. Edge: n=1 → Output: 1
 */
public class ClimbingStairs {

    public int climbStairs(int n) {
        if (n <= 2) return n;
        int prev2 = 1, prev1 = 2;
        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }

    public static void main(String[] args) {
        ClimbingStairs sol = new ClimbingStairs();

        System.out.println("Test 1 n=2 (expect 2): " + sol.climbStairs(2));
        System.out.println("Test 2 n=4 (expect 5): " + sol.climbStairs(4));
        System.out.println("Test 3 n=1 (expect 1): " + sol.climbStairs(1));
    }
}
