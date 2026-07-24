package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Number of 1 Bits (Hamming Weight)
 * Difficulty: Easy
 *
 * Description: Count the number of set bits (1s) in the binary representation of a 32-bit
 * unsigned integer. Also known as computing the Hamming weight or popcount.
 *
 * Example:
 *   Input: 11 (binary: 1011) → Output: 3
 *   Input: 128 (binary: 10000000) → Output: 1
 *
 * Approach: Method 1 (Brian Kernighan): n &= (n-1) clears the lowest set bit each iteration;
 * count iterations until n=0. This runs in O(k) where k = number of set bits.
 * Method 2 (Shift): check LSB with n&1, then unsigned right shift n>>>1 to handle sign bit
 * correctly for negative integers. Repeat 32 times.
 *
 * Time Complexity: O(1) (at most 32 iterations)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. 11 (1011 in binary) → 3 set bits
 *   2. 128 (10000000) → 1 set bit
 *   3. Edge: 0xFFFFFFFF (all 1s, 32-bit) → 32 set bits
 */
public class NumberOf1Bits {

    // Method 1: Brian Kernighan's bit trick
    public static int hammingWeightKernighan(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1); // clears lowest set bit
            count++;
        }
        return count;
    }

    // Method 2: Shift approach (using >>> for unsigned right shift)
    public static int hammingWeightShift(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            count += n & 1;
            n >>>= 1;
        }
        return count;
    }

    public static void main(String[] args) {
        // Test Case 1: 11 = 1011 in binary → 3 set bits
        System.out.println("hammingWeight(11) Kernighan=" + hammingWeightKernighan(11)); // 3
        System.out.println("hammingWeight(11) Shift="     + hammingWeightShift(11));     // 3

        // Test Case 2: 128 = 10000000 → 1 set bit
        System.out.println("hammingWeight(128) Kernighan=" + hammingWeightKernighan(128)); // 1
        System.out.println("hammingWeight(128) Shift="     + hammingWeightShift(128));     // 1

        // Test Case 3: Edge - 0xFFFFFFFF (all 32 bits set, -1 as signed int)
        System.out.println("hammingWeight(-1/0xFFFFFFFF) Kernighan=" + hammingWeightKernighan(-1)); // 32
        System.out.println("hammingWeight(-1/0xFFFFFFFF) Shift="     + hammingWeightShift(-1));     // 32
    }
}
