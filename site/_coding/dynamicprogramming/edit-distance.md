---
layout: problem
title: "Edit Distance"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Hard
time_complexity: "O(m * n)"
space_complexity: "O(m * n)"
leetcode: 72
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

Edit Distance Given two strings word1 and word2, return the minimum number of operations (insert, delete, or replace a character) required to transform word1 into word2. This is also known as the Levenshtein distance between the two strings.

## Approach

2D DP where dp[i][j] represents the edit distance between word1[0..i-1] and word2[0..j-1]. If characters match: dp[i][j] = dp[i-1][j-1]. Otherwise: dp[i][j] = 1 + min(dp[i-1][j] (delete), dp[i][j-1] (insert), dp[i-1][j-1] (replace)). Base cases: dp[i][0] = i (delete i chars), dp[0][j] = j (insert j chars).

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Edit Distance
 * Difficulty: Hard
 *
 * Description:
 * Given two strings word1 and word2, return the minimum number of operations (insert,
 * delete, or replace a character) required to transform word1 into word2. This is also
 * known as the Levenshtein distance between the two strings.
 *
 * Example:
 *   Input: word1="horse", word2="ros"
 *   Output: 3
 *
 * Constraints:
 *   - 0 <= word1.length, word2.length <= 500
 *   - word1 and word2 consist of lowercase English letters
 *
 * Approach:
 *   2D DP where dp[i][j] represents the edit distance between word1[0..i-1] and
 *   word2[0..j-1]. If characters match: dp[i][j] = dp[i-1][j-1]. Otherwise:
 *   dp[i][j] = 1 + min(dp[i-1][j] (delete), dp[i][j-1] (insert), dp[i-1][j-1] (replace)).
 *   Base cases: dp[i][0] = i (delete i chars), dp[0][j] = j (insert j chars).
 *
 * Time Complexity: O(m * n)
 * Space Complexity: O(m * n)
 *
 * Test Cases:
 *   1. Input: "horse", "ros" → Output: 3
 *   2. Input: "intention", "execution" → Output: 5
 *   3. Edge: "", "abc" → Output: 3
 */
public class EditDistance {

    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                    Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        EditDistance sol = new EditDistance();

        System.out.println("Test 1 'horse'->'ros' (expect 3): "
                + sol.minDistance("horse", "ros"));
        System.out.println("Test 2 'intention'->'execution' (expect 5): "
                + sol.minDistance("intention", "execution"));
        System.out.println("Test 3 ''->'abc' (expect 3): "
                + sol.minDistance("", "abc"));
    }
}
```

## Complexity

- **Time:** O(m * n)
- **Space:** O(m * n)
