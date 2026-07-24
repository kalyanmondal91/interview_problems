package org.interview.coding.arrays;

/**
 * Problem: Best Time to Buy and Sell Stock
 * Difficulty: Easy
 *
 * Description:
 * Given an array prices where prices[i] is the price of a stock on day i, find the maximum
 * profit you can achieve from a single buy and a single sell. You must buy before you sell.
 * If no profit is possible, return 0.
 *
 * Example:
 *   Input: prices = [7,1,5,3,6,4]
 *   Output: 5
 *
 * Constraints:
 *   - 1 <= prices.length <= 10^5
 *   - 0 <= prices[i] <= 10^4
 *
 * Approach:
 *   Track the minimum price seen so far as we iterate left to right. For each day, compute
 *   the potential profit by subtracting the minimum price from the current price. Update
 *   the maximum profit if this potential profit is greater. This greedy single-pass approach
 *   avoids needing nested loops to compare all buy/sell pairs.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: prices=[7,1,5,3,6,4] → Output: 5 (buy at 1, sell at 6)
 *   2. Input: prices=[7,6,4,3,1] → Output: 0 (prices only decrease)
 *   3. Edge case: prices=[1] → Output: 0 (only one day, no transaction possible)
 */
public class BestTimeToBuyAndSellStock {

    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;
            }
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        BestTimeToBuyAndSellStock sol = new BestTimeToBuyAndSellStock();
        // Test 1
        System.out.println(sol.maxProfit(new int[]{7, 1, 5, 3, 6, 4})); // 5
        // Test 2
        System.out.println(sol.maxProfit(new int[]{7, 6, 4, 3, 1}));    // 0
        // Test 3 (edge case: single element)
        System.out.println(sol.maxProfit(new int[]{1}));                  // 0
    }
}
