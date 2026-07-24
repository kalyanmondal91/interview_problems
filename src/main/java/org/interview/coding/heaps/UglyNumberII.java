package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Ugly Number II
 * Difficulty: Medium
 *
 * Description:
 * An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5.
 * Given an integer n, return the nth ugly number.
 * The first ugly number is 1.
 *
 * Example:
 *   Input: n = 10
 *   Output: 12
 *
 * Constraints:
 *   - 1 <= n <= 1690
 *
 * Approach:
 *   Use dynamic programming with three pointers i2, i3, i5.
 *   Maintain an array of ugly numbers. Each next ugly number is the minimum of
 *   ugly[i2]*2, ugly[i3]*3, ugly[i5]*5. Advance the pointer(s) whose product equals
 *   the chosen minimum (advance multiple if there are ties to avoid duplicates).
 *   This runs in O(n) time without any heap.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: n=10 → Output: 12
 *   2. Input: n=1 → Output: 1
 *   3. Edge: n=15 → Output: 24
 */
public class UglyNumberII {

    public int nthUglyNumber(int n) {
        int[] ugly = new int[n];
        ugly[0] = 1;
        int i2 = 0, i3 = 0, i5 = 0;

        for (int i = 1; i < n; i++) {
            int next2 = ugly[i2] * 2;
            int next3 = ugly[i3] * 3;
            int next5 = ugly[i5] * 5;
            int nextUgly = Math.min(next2, Math.min(next3, next5));
            ugly[i] = nextUgly;
            if (nextUgly == next2) i2++;
            if (nextUgly == next3) i3++;
            if (nextUgly == next5) i5++;
        }
        return ugly[n - 1];
    }

    public static void main(String[] args) {
        UglyNumberII sol = new UglyNumberII();

        // Test 1
        System.out.println("Test 1: " + sol.nthUglyNumber(10)); // Expected: 12

        // Test 2
        System.out.println("Test 2: " + sol.nthUglyNumber(1)); // Expected: 1

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.nthUglyNumber(15)); // Expected: 24
    }
}
