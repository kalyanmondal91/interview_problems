package org.interview.coding.twopointers;

import java.util.HashMap;

/**
 * Problem: Fruit Into Baskets
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array fruits where fruits[i] is the type of fruit at position i, you
 * have two baskets, each holding only one type of fruit (any number of that type). Starting
 * from any position, pick one fruit per tree moving to the right. Return the maximum number
 * of fruits you can pick (i.e., longest subarray with at most 2 distinct values).
 *
 * Example:
 *   Input: fruits = [1,2,1]
 *   Output: 3
 *
 * Constraints:
 *   - 1 <= fruits.length <= 10^5
 *   - 0 <= fruits[i] < fruits.length
 *
 * Approach:
 *   Use a sliding window with a HashMap to count the frequency of each fruit type in the
 *   current window. Expand the right pointer to add fruits. When the window has more than
 *   2 distinct types, shrink from the left by decrementing counts and removing entries
 *   that drop to zero. Track the maximum valid window size.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1) — at most 3 entries in map at any time
 *
 * Test Cases:
 *   1. Input: fruits=[1,2,1] → Output: 3
 *   2. Input: fruits=[0,1,2,2] → Output: 3 ([1,2,2])
 *   3. Edge case: fruits=[3,3,3,1,2,1,1,2,3,3,4] → Output: 5 ([1,2,1,1,2])
 */
public class FruitIntoBaskets {

    public int totalFruit(int[] fruits) {
        HashMap<Integer, Integer> basket = new HashMap<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < fruits.length; right++) {
            basket.merge(fruits[right], 1, Integer::sum);

            while (basket.size() > 2) {
                int leftFruit = fruits[left];
                basket.merge(leftFruit, -1, Integer::sum);
                if (basket.get(leftFruit) == 0) basket.remove(leftFruit);
                left++;
            }
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        FruitIntoBaskets sol = new FruitIntoBaskets();
        // Test 1
        System.out.println(sol.totalFruit(new int[]{1, 2, 1}));                      // 3
        // Test 2
        System.out.println(sol.totalFruit(new int[]{0, 1, 2, 2}));                   // 3
        // Test 3 (edge case: longer mixed array)
        System.out.println(sol.totalFruit(new int[]{3,3,3,1,2,1,1,2,3,3,4}));        // 5
    }
}
