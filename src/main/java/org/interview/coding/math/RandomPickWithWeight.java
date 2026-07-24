package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Random Pick with Weight
 * Difficulty: Medium
 *
 * Description: Given an array of positive weights w, implement pickIndex() which randomly picks
 * an index in proportion to its weight. If w=[1,3], index 0 is picked ~25% of the time and
 * index 1 ~75% of the time.
 *
 * Example:
 *   Input: w=[1,3], pickIndex() called multiple times
 *   Output: ~25% index 0, ~75% index 1
 *
 * Approach: Build a prefix sum array where prefixSum[i] = w[0]+w[1]+...+w[i]. pickIndex()
 * generates a random integer in [1, totalSum], then binary searches the prefix sum array
 * for the first index where prefixSum[i] >= random. This index is returned.
 * Larger weights occupy larger ranges in the prefix sum, giving proportional probability.
 *
 * Time Complexity: O(n) constructor, O(log n) per pickIndex
 * Space Complexity: O(n) for prefix sums
 *
 * Test Cases:
 *   1. w=[1] → always picks index 0
 *   2. w=[1,3] → ~25% index 0, ~75% index 1 (verify over many trials)
 *   3. Edge: w=[1,1,1] → each index picked ~33% of the time
 */
public class RandomPickWithWeight {

    private final int[] prefixSum;
    private final int totalSum;
    private final Random random = new Random();

    public RandomPickWithWeight(int[] w) {
        prefixSum = new int[w.length];
        prefixSum[0] = w[0];
        for (int i = 1; i < w.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + w[i];
        }
        totalSum = prefixSum[w.length - 1];
    }

    public int pickIndex() {
        int target = random.nextInt(totalSum) + 1; // [1, totalSum]
        // Binary search for first prefixSum >= target
        int lo = 0, hi = prefixSum.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (prefixSum[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        // Test Case 1: Single weight → always index 0
        RandomPickWithWeight picker1 = new RandomPickWithWeight(new int[]{1});
        System.out.print("w=[1], 5 picks: ");
        for (int i = 0; i < 5; i++) System.out.print(picker1.pickIndex() + " ");
        System.out.println();

        // Test Case 2: w=[1,3] → ~25%/75%
        RandomPickWithWeight picker2 = new RandomPickWithWeight(new int[]{1, 3});
        int[] counts2 = new int[2];
        for (int i = 0; i < 10000; i++) counts2[picker2.pickIndex()]++;
        System.out.printf("w=[1,3] over 10000 picks: idx0=%.1f%%, idx1=%.1f%%%n",
                100.0 * counts2[0] / 10000, 100.0 * counts2[1] / 10000);

        // Test Case 3: Edge - w=[1,1,1] → ~33% each
        RandomPickWithWeight picker3 = new RandomPickWithWeight(new int[]{1, 1, 1});
        int[] counts3 = new int[3];
        for (int i = 0; i < 9000; i++) counts3[picker3.pickIndex()]++;
        System.out.printf("w=[1,1,1] over 9000 picks: idx0=%.1f%%, idx1=%.1f%%, idx2=%.1f%%%n",
                100.0 * counts3[0] / 9000, 100.0 * counts3[1] / 9000, 100.0 * counts3[2] / 9000);
    }
}
