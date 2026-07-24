---
layout: problem
title: "Coin Change"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Medium
time_complexity: "O(amount * coins.length)"
space_complexity: "O(amount)"
leetcode: 322
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

Coin Change Given an array of coin denominations and a target amount, return the minimum number of coins needed to make up that amount. If the amount cannot be made up, return -1. You may use each coin denomination an unlimited number of times.

## Approach

Bottom-up DP: initialize dp[0]=0 and dp[1..amount]=amount+1 (representing infinity). For each amount i from 1 to target, iterate over each coin denomination. If the coin value is <= i, update dp[i] = min(dp[i], dp[i-coin]+1). The recurrence leverages optimal substructure: best way to make i is to pick a coin and use the best way for the remainder. Return dp[amount] if it's not infinity, else -1.

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Coin Change
 * Difficulty: Medium
 *
 * Description:
 * Given an array of coin denominations and a target amount, return the minimum number
 * of coins needed to make up that amount. If the amount cannot be made up, return -1.
 * You may use each coin denomination an unlimited number of times.
 *
 * Example:
 *   Input: coins=[1,5,11], amount=15
 *   Output: 3 (11+3*1 → No, use 5+5+5=3 coins)
 *
 * Constraints:
 *   - 1 <= coins.length <= 12
 *   - 1 <= coins[i] <= 2^31 - 1
 *   - 0 <= amount <= 10^4
 *
 * Approach:
 *   Bottom-up DP: initialize dp[0]=0 and dp[1..amount]=amount+1 (representing infinity).
 *   For each amount i from 1 to target, iterate over each coin denomination. If the coin
 *   value is <= i, update dp[i] = min(dp[i], dp[i-coin]+1). The recurrence leverages
 *   optimal substructure: best way to make i is to pick a coin and use the best way for
 *   the remainder. Return dp[amount] if it's not infinity, else -1.
 *
 * Time Complexity: O(amount * coins.length)
 * Space Complexity: O(amount)
 *
 * Test Cases:
 *   1. Input: coins=[1,5,11], amount=15 → Output: 3
 *   2. Input: coins=[2], amount=3 → Output: -1 (impossible)
 *   3. Edge: amount=0 → Output: 0
 */
public class CoinChange {

    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        CoinChange sol = new CoinChange();

        System.out.println("Test 1 coins=[1,5,11] amount=15 (expect 3): "
                + sol.coinChange(new int[]{1, 5, 11}, 15));
        System.out.println("Test 2 coins=[2] amount=3 (expect -1): "
                + sol.coinChange(new int[]{2}, 3));
        System.out.println("Test 3 amount=0 (expect 0): "
                + sol.coinChange(new int[]{1, 2, 5}, 0));
    }
}
```

## Complexity

- **Time:** O(amount * coins.length)
- **Space:** O(amount)
