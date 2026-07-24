---
layout: problem
title: "Longest Substring Without Repeating Chars"
category: arrays
category_display: "Arrays"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(min(n, m)) where m is the character set size"
leetcode: 3
tags: [arrays]
render_with_liquid: false
---

## Problem

Longest Substring Without Repeating Characters Given a string s, find the length of the longest substring that contains no repeating characters. A substring is a contiguous sequence of characters within the string.

## Approach

Use a sliding window defined by two pointers (left and right). Expand right by one character at a time, adding it to a HashSet. If the character already exists in the set, shrink the window from the left, removing characters until the duplicate is removed. Track the maximum window size seen throughout the traversal.

## Solution

```java
package org.interview.coding.arrays;

import java.util.HashSet;

/**
 * Problem: Longest Substring Without Repeating Characters
 * Difficulty: Medium
 *
 * Description:
 * Given a string s, find the length of the longest substring that contains no repeating
 * characters. A substring is a contiguous sequence of characters within the string.
 *
 * Example:
 *   Input: s = "abcabcbb"
 *   Output: 3
 *
 * Constraints:
 *   - 0 <= s.length <= 5 * 10^4
 *   - s consists of English letters, digits, symbols, and spaces
 *
 * Approach:
 *   Use a sliding window defined by two pointers (left and right). Expand right by one
 *   character at a time, adding it to a HashSet. If the character already exists in the
 *   set, shrink the window from the left, removing characters until the duplicate is
 *   removed. Track the maximum window size seen throughout the traversal.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(min(n, m)) where m is the character set size
 *
 * Test Cases:
 *   1. Input: s="abcabcbb" → Output: 3 ("abc")
 *   2. Input: s="bbbbb" → Output: 1 ("b")
 *   3. Edge case: s="" → Output: 0 (empty string)
 */
public class LongestSubstringWithoutRepeatingChars {

    public int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        LongestSubstringWithoutRepeatingChars sol = new LongestSubstringWithoutRepeatingChars();
        // Test 1
        System.out.println(sol.lengthOfLongestSubstring("abcabcbb")); // 3
        // Test 2
        System.out.println(sol.lengthOfLongestSubstring("bbbbb"));    // 1
        // Test 3 (edge case: empty string)
        System.out.println(sol.lengthOfLongestSubstring(""));         // 0
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(min(n, m)) where m is the character set size
