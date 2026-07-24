---
layout: problem
title: "Regular Expression Matching"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Hard
time_complexity: "O(m * n)"
space_complexity: "O(m * n)"
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

Regular Expression Matching Given an input string s and a pattern p, implement regular expression matching with support for '.' (matches any single character) and '*' (matches zero or more of the preceding element). The matching must cover the entire string s.

## Approach

2D DP where dp[i][j] = true if s[0..i-1] matches p[0..j-1]. Base: dp[0][0] = true. For patterns like "a*b*", dp[0][j] = dp[0][j-2] when p[j-1]='*'. Transition: if p[j-1] == '*', dp[i][j] = dp[i][j-2] (zero occurrences of p[j-2]) OR (dp[i-1][j] if s[i-1] matches p[j-2], meaning one more occurrence). Otherwise, dp[i][j] = dp[i-1][j-1] if s[i-1] matches p[j-1] (exact or dot).

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Regular Expression Matching
 * Difficulty: Hard
 *
 * Description:
 * Given an input string s and a pattern p, implement regular expression matching with
 * support for '.' (matches any single character) and '*' (matches zero or more of the
 * preceding element). The matching must cover the entire string s.
 *
 * Example:
 *   Input: s="aab", p="c*a*b"
 *   Output: true
 *
 * Constraints:
 *   - 1 <= s.length <= 20
 *   - 1 <= p.length <= 30
 *   - s contains only lowercase letters; p contains lowercase letters, '.', and '*'
 *
 * Approach:
 *   2D DP where dp[i][j] = true if s[0..i-1] matches p[0..j-1].
 *   Base: dp[0][0] = true. For patterns like "a*b*", dp[0][j] = dp[0][j-2] when p[j-1]='*'.
 *   Transition: if p[j-1] == '*', dp[i][j] = dp[i][j-2] (zero occurrences of p[j-2])
 *   OR (dp[i-1][j] if s[i-1] matches p[j-2], meaning one more occurrence).
 *   Otherwise, dp[i][j] = dp[i-1][j-1] if s[i-1] matches p[j-1] (exact or dot).
 *
 * Time Complexity: O(m * n)
 * Space Complexity: O(m * n)
 *
 * Test Cases:
 *   1. Input: s="aa", p="a*" → Output: true
 *   2. Input: s="aab", p="c*a*b" → Output: true
 *   3. Edge: s="mississippi", p="mis*is*p*." → Output: false
 */
public class RegularExpressionMatching {

    public boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        // Patterns like a*, a*b*, a*b*c* can match empty string
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char pc = p.charAt(j - 1);
                char sc = s.charAt(i - 1);
                if (pc == '*') {
                    // Zero occurrences of p[j-2]
                    dp[i][j] = (j >= 2) && dp[i][j - 2];
                    // One or more occurrences: s[i-1] must match p[j-2]
                    if (j >= 2 && (p.charAt(j - 2) == '.' || p.charAt(j - 2) == sc)) {
                        dp[i][j] = dp[i][j] || dp[i - 1][j];
                    }
                } else if (pc == '.' || pc == sc) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        RegularExpressionMatching sol = new RegularExpressionMatching();

        System.out.println("Test 1 s='aa' p='a*' (expect true): "
                + sol.isMatch("aa", "a*"));
        System.out.println("Test 2 s='aab' p='c*a*b' (expect true): "
                + sol.isMatch("aab", "c*a*b"));
        System.out.println("Test 3 s='mississippi' p='mis*is*p*.' (expect false): "
                + sol.isMatch("mississippi", "mis*is*p*."));
    }
}
```

## Complexity

- **Time:** O(m * n)
- **Space:** O(m * n)
