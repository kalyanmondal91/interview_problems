---
layout: problem
title: "Palindrome Partitioning"
category: backtracking
category_display: "Backtracking"
difficulty: Medium
time_complexity: "O(n * 2^n)"
space_complexity: "O(n)"
tags: [backtracking]
render_with_liquid: false
---

## Problem

Palindrome Partitioning Given a string s, partition s such that every substring of the partition is a palindrome. Return all possible palindrome partitioning of s.

## Approach

Use backtracking starting from index 0. For each end index from start to s.length()-1, check if the substring s[start..end] is a palindrome using a two-pointer helper. If it is, add the substring to the current partition and recurse from end+1. When start reaches s.length(), the full string has been partitioned into palindromes and we record the current partition.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Palindrome Partitioning
 * Difficulty: Medium
 *
 * Description:
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 * Return all possible palindrome partitioning of s.
 *
 * Example:
 *   Input: s = "aab"
 *   Output: [["a","a","b"],["aa","b"]]
 *
 * Constraints:
 *   - 1 <= s.length <= 16
 *   - s contains only lowercase English letters.
 *
 * Approach:
 *   Use backtracking starting from index 0. For each end index from start to s.length()-1, check
 *   if the substring s[start..end] is a palindrome using a two-pointer helper. If it is, add the
 *   substring to the current partition and recurse from end+1. When start reaches s.length(), the
 *   full string has been partitioned into palindromes and we record the current partition.
 *
 * Time Complexity: O(n * 2^n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: "aab" → Output: [["a","a","b"],["aa","b"]]
 *   2. Input: "a" → Output: [["a"]]
 *   3. Edge: "racecar" → Output includes ["racecar"] as a single palindrome partition
 */
public class PalindromePartitioning {

    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(String s, int start, List<String> current, List<List<String>> result) {
        if (start == s.length()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome(s, start, end)) {
                current.add(s.substring(start, end + 1));
                backtrack(s, end + 1, current, result);
                current.remove(current.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String s, int lo, int hi) {
        while (lo < hi) {
            if (s.charAt(lo++) != s.charAt(hi--)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        PalindromePartitioning solution = new PalindromePartitioning();

        // Test 1: standard case
        System.out.println(solution.partition("aab"));
        // Expected: [[a, a, b], [aa, b]]

        // Test 2: single character
        System.out.println(solution.partition("a"));
        // Expected: [[a]]

        // Test 3: full palindrome string
        System.out.println(solution.partition("racecar"));
        // Expected includes ["racecar"] and individual char partitions
    }
}
```

## Complexity

- **Time:** O(n * 2^n)
- **Space:** O(n)
