package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Counting Bits
 * Difficulty: Easy
 *
 * Description: Given an integer n, return an array ans of length n+1 such that for each i
 * in [0,n], ans[i] is the number of 1s in the binary representation of i.
 *
 * Example:
 *   Input: n=5 → Output: [0,1,1,2,1,2]
 *
 * Approach: Use DP with the observation that for any number i, dp[i] = dp[i >> 1] + (i & 1).
 * Right-shifting by 1 removes the last bit (already computed); adding (i&1) counts the LSB.
 * This builds each answer in O(1) from a previously computed smaller value.
 * No need to count bits individually for each number.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n) for output array
 *
 * Test Cases:
 *   1. n=2 → [0,1,1]
 *   2. n=5 → [0,1,1,2,1,2]
 *   3. Edge: n=0 → [0]
 */
public class CountingBits {

    public static int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i >> 1] + (i & 1);
        }
        return dp;
    }

    public static void main(String[] args) {
        // Test Case 1: n=2
        System.out.println("countBits(2)=" + Arrays.toString(countBits(2))); // [0,1,1]

        // Test Case 2: n=5
        System.out.println("countBits(5)=" + Arrays.toString(countBits(5))); // [0,1,1,2,1,2]

        // Test Case 3: Edge - n=0
        System.out.println("countBits(0)=" + Arrays.toString(countBits(0))); // [0]

        // Bonus: n=8
        System.out.println("countBits(8)=" + Arrays.toString(countBits(8))); // [0,1,1,2,1,2,2,3,1]
    }
}
