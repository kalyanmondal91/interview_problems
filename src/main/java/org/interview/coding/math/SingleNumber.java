package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Single Number I & II
 * Difficulty: Easy / Medium
 *
 * Description: Single Number I: given an array where every element appears twice except one,
 * find the single element. Single Number II: every element appears three times except one;
 * find the single element. Both must run in O(n) time and O(1) space.
 *
 * Example:
 *   Input I: [2,2,1] → Output: 1
 *   Input II: [2,2,3,2] → Output: 3
 *
 * Approach: Single I: XOR all elements. Pairs cancel to 0 (a^a=0), leaving the single element.
 * Single II: Count each bit across all numbers and take mod 3. Bits that appear 3k times cancel
 * to 0; the remaining bits belong to the single number. Reconstruct from bit counts mod 3.
 * Both approaches use O(1) space and O(n) time.
 *
 * Time Complexity: O(n) for both
 * Space Complexity: O(1) for both
 *
 * Test Cases:
 *   1. [2,2,1] → singleNumberI = 1
 *   2. [2,2,3,2] → singleNumberII = 3
 *   3. Edge: [1] → both return 1
 */
public class SingleNumber {

    public static int singleNumberI(int[] nums) {
        int result = 0;
        for (int n : nums) result ^= n;
        return result;
    }

    public static int singleNumberII(int[] nums) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            int bitSum = 0;
            for (int n : nums) {
                bitSum += (n >> i) & 1;
            }
            // If bit count mod 3 != 0, this bit belongs to the single number
            if (bitSum % 3 != 0) {
                result |= (1 << i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // Test Case 1: Basic XOR - single number among pairs
        int[] test1 = {2, 2, 1};
        System.out.println("singleI([2,2,1])=" + singleNumberI(test1)); // 1

        // Test Case 2: Single II - single number among triples
        int[] test2 = {2, 2, 3, 2};
        System.out.println("singleII([2,2,3,2])=" + singleNumberII(test2)); // 3

        // Test Case 3: Edge - single element array
        int[] test3 = {1};
        System.out.println("singleI([1])=" + singleNumberI(test3));   // 1
        System.out.println("singleII([1])=" + singleNumberII(test3)); // 1

        // Bonus: larger test
        int[] test4 = {0, 1, 0, 1, 0, 1, 99};
        System.out.println("singleII([0,1,0,1,0,1,99])=" + singleNumberII(test4)); // 99
    }
}
