package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Reverse Bits
 * Difficulty: Easy
 *
 * Description: Reverse the bits of a 32-bit unsigned integer. For example, 43261596
 * (binary 00000010100101000001111010011100) reversed becomes 964176192
 * (binary 00111001011110000010100101000000).
 *
 * Example:
 *   Input: 43261596 → Output: 964176192
 *   Input: 0xFFFFFFFF → Output: 0xFFFFFFFF (all 1s reversed is still all 1s)
 *
 * Approach: Loop 32 times. In each iteration, shift result left by 1 and OR in the LSB of n.
 * Then unsigned right-shift n by 1 (>>> to treat as unsigned). The LSB of n at each step becomes
 * the MSB of result in reverse order. Use Integer.toUnsignedString() to display as unsigned.
 *
 * Time Complexity: O(1) — exactly 32 iterations
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. 43261596 → 964176192
 *   2. 0 → 0
 *   3. Edge: 1 (bit 0 set) → 0x80000000 (bit 31 set after reversal)
 */
public class ReverseBits {

    public static int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1);
            n >>>= 1;
        }
        return result;
    }

    public static void main(String[] args) {
        // Test Case 1: Standard example
        int input1 = 43261596; // 00000010100101000001111010011100
        int output1 = reverseBits(input1);
        System.out.println("reverseBits(43261596)=" + Integer.toUnsignedString(output1));
        System.out.println("  expected: 964176192, got: " + output1); // 964176192

        // Test Case 2: All zeros
        System.out.println("reverseBits(0)=" + reverseBits(0)); // 0

        // Test Case 3: Edge - only LSB set → becomes MSB after reversal
        int input3 = 1; // ...00001
        int output3 = reverseBits(input3); // 10000000...00 = Integer.MIN_VALUE as signed
        System.out.println("reverseBits(1) unsigned=" + Integer.toUnsignedString(output3)); // 2147483648
        System.out.println("reverseBits(1) signed=" + output3); // -2147483648

        // Bonus: 0xFFFFFFFF reversed = 0xFFFFFFFF
        System.out.println("reverseBits(0xFFFFFFFF)=" + Integer.toUnsignedString(reverseBits(-1))); // 4294967295
    }
}
