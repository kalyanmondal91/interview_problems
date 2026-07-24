---
layout: problem
title: "Longest Common Subsequence"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Medium
time_complexity: "O(m * n)"
space_complexity: "O(m * n)"
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

Longest Common Subsequence Given two strings text1 and text2, return the length of their longest common subsequence. A subsequence maintains relative order but need not be contiguous. If no common subsequence exists, return 0.

## Approach

2D DP where dp[i][j] represents the LCS length of text1[0..i-1] and text2[0..j-1]. If text1[i-1] == text2[j-1], then dp[i][j] = dp[i-1][j-1] + 1 (extend LCS). Otherwise dp[i][j] = max(dp[i-1][j], dp[i][j-1]) (take best without current char). Base case: dp[0][j] = dp[i][0] = 0 (empty string has LCS 0 with anything).

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Longest Common Subsequence
 * Difficulty: Medium
 *
 * Description:
 * Given two strings text1 and text2, return the length of their longest common
 * subsequence. A subsequence maintains relative order but need not be contiguous.
 * If no common subsequence exists, return 0.
 *
 * Example:
 *   Input: text1="abcde", text2="ace"
 *   Output: 3 ("ace")
 *
 * Constraints:
 *   - 1 <= text1.length, text2.length <= 1000
 *   - text1 and text2 consist of lowercase English letters
 *
 * Approach:
 *   2D DP where dp[i][j] represents the LCS length of text1[0..i-1] and text2[0..j-1].
 *   If text1[i-1] == text2[j-1], then dp[i][j] = dp[i-1][j-1] + 1 (extend LCS).
 *   Otherwise dp[i][j] = max(dp[i-1][j], dp[i][j-1]) (take best without current char).
 *   Base case: dp[0][j] = dp[i][0] = 0 (empty string has LCS 0 with anything).
 *
 * Time Complexity: O(m * n)
 * Space Complexity: O(m * n)
 *
 * Test Cases:
 *   1. Input: "abcde", "ace" → Output: 3
 *   2. Input: "abc", "abc" → Output: 3
 *   3. Edge: "abc", "def" → Output: 0
 */
public class LongestCommonSubsequence {

    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        LongestCommonSubsequence sol = new LongestCommonSubsequence();

        System.out.println("Test 1 'abcde','ace' (expect 3): "
                + sol.longestCommonSubsequence("abcde", "ace"));
        System.out.println("Test 2 'abc','abc' (expect 3): "
                + sol.longestCommonSubsequence("abc", "abc"));
        System.out.println("Test 3 'abc','def' (expect 0): "
                + sol.longestCommonSubsequence("abc", "def"));
    }
}
```

## Complexity

- **Time:** O(m * n)
- **Space:** O(m * n)
