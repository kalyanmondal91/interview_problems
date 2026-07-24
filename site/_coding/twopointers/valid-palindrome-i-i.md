---
layout: problem
title: "Valid Palindrome I I"
category: twopointers
category_display: "Two Pointers"
difficulty: Easy
time_complexity: "O(n)"
space_complexity: "O(1)"
tags: [twopointers]
render_with_liquid: false
---

## Problem

Valid Palindrome II Given a string s, return true if the string can be a palindrome after deleting at most one character from it. The string consists only of lowercase English letters.

## Approach

Use two pointers from both ends. When characters match, move both inward. When they don't match, we must skip one character: try skipping either the left or right character and check if the remaining substring is a palindrome. Use a helper method that checks if a substring [l, r] is a palindrome using two pointers.

## Solution

```java
package org.interview.coding.twopointers;

/**
 * Problem: Valid Palindrome II
 * Difficulty: Easy
 *
 * Description:
 * Given a string s, return true if the string can be a palindrome after deleting at most
 * one character from it. The string consists only of lowercase English letters.
 *
 * Example:
 *   Input: s = "abca"
 *   Output: true
 *
 * Constraints:
 *   - 1 <= s.length <= 10^5
 *   - s consists of lowercase English letters
 *
 * Approach:
 *   Use two pointers from both ends. When characters match, move both inward. When they
 *   don't match, we must skip one character: try skipping either the left or right character
 *   and check if the remaining substring is a palindrome. Use a helper method that checks
 *   if a substring [l, r] is a palindrome using two pointers.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: s="abca" → Output: true (remove 'c' or 'b')
 *   2. Input: s="raceacar" → Output: false
 *   3. Edge case: s="a" → Output: true (single char is palindrome)
 */
public class ValidPalindromeII {

    public boolean validPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalin(s, left + 1, right) || isPalin(s, left, right - 1);
            }
            left++;
            right--;
        }
        return true;
    }

    private boolean isPalin(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) return false;
            l++; r--;
        }
        return true;
    }

    public static void main(String[] args) {
        ValidPalindromeII sol = new ValidPalindromeII();
        // Test 1
        System.out.println(sol.validPalindrome("abca"));    // true
        // Test 2
        System.out.println(sol.validPalindrome("raceacar")); // false
        // Test 3 (edge case: single char)
        System.out.println(sol.validPalindrome("a"));       // true
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
