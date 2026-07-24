---
layout: problem
title: "Word Break"
category: dynamicprogramming
category_display: "Dynamic Programming"
difficulty: Medium
time_complexity: "O(n^2 * m) where n=s.length, m=avg word length"
space_complexity: "O(n + dict size)"
leetcode: 139
tags: [dynamicprogramming]
render_with_liquid: false
---

## Problem

Word Break Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words. The same word in the dictionary may be reused multiple times.

## Approach

Boolean DP: dp[i] = true if s[0..i-1] can be segmented using dictionary words. For each position i, check all j from 0 to i: if dp[j] is true and s[j..i-1] is in the dictionary, then dp[i] = true. Use a HashSet for O(1) word lookups. Base case: dp[0] = true (empty string is always "segmentable").

## Solution

```java
package org.interview.coding.dynamicprogramming;

import java.util.*;

/**
 * Problem: Word Break
 * Difficulty: Medium
 *
 * Description:
 * Given a string s and a dictionary of strings wordDict, return true if s can be
 * segmented into a space-separated sequence of one or more dictionary words.
 * The same word in the dictionary may be reused multiple times.
 *
 * Example:
 *   Input: s="leetcode", wordDict=["leet","code"]
 *   Output: true
 *
 * Constraints:
 *   - 1 <= s.length <= 300
 *   - 1 <= wordDict.length <= 1000
 *   - 1 <= wordDict[i].length <= 20
 *
 * Approach:
 *   Boolean DP: dp[i] = true if s[0..i-1] can be segmented using dictionary words.
 *   For each position i, check all j from 0 to i: if dp[j] is true and s[j..i-1] is
 *   in the dictionary, then dp[i] = true. Use a HashSet for O(1) word lookups.
 *   Base case: dp[0] = true (empty string is always "segmentable").
 *
 * Time Complexity: O(n^2 * m) where n=s.length, m=avg word length
 * Space Complexity: O(n + dict size)
 *
 * Test Cases:
 *   1. Input: "leetcode", ["leet","code"] → Output: true
 *   2. Input: "applepenapple", ["apple","pen"] → Output: true
 *   3. Edge: "catsandog", ["cats","dog","sand","and","cat"] → Output: false
 */
public class WordBreak {

    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        WordBreak sol = new WordBreak();

        System.out.println("Test 1 'leetcode' (expect true): "
                + sol.wordBreak("leetcode", Arrays.asList("leet", "code")));
        System.out.println("Test 2 'applepenapple' (expect true): "
                + sol.wordBreak("applepenapple", Arrays.asList("apple", "pen")));
        System.out.println("Test 3 'catsandog' (expect false): "
                + sol.wordBreak("catsandog", Arrays.asList("cats","dog","sand","and","cat")));
    }
}
```

## Complexity

- **Time:** O(n^2 * m) where n=s.length, m=avg word length
- **Space:** O(n + dict size)
