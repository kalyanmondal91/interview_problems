---
layout: problem
title: "Longest Repeating Character Replacement"
category: twopointers
category_display: "Two Pointers"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1) — 26 uppercase letters"
tags: [twopointers]
render_with_liquid: false
---

## Problem

Longest Repeating Character Replacement Given a string s and an integer k, you can replace at most k characters in the string to any other uppercase letter. Return the length of the longest substring containing the same letter you can get after performing at most k replacements.

## Approach

Use a sliding window. Track the count of each character in the current window using an array. The key insight is: if (window size - max frequency char count) <= k, the window is valid. Expand right always; shrink left only when the window becomes invalid. Track maxFreq (the highest count of any single character seen in any valid window state).

## Solution

```java
package org.interview.coding.twopointers;

/**
 * Problem: Longest Repeating Character Replacement
 * Difficulty: Medium
 *
 * Description:
 * Given a string s and an integer k, you can replace at most k characters in the string
 * to any other uppercase letter. Return the length of the longest substring containing
 * the same letter you can get after performing at most k replacements.
 *
 * Example:
 *   Input: s = "AABABBA", k = 1
 *   Output: 4
 *
 * Constraints:
 *   - 1 <= s.length <= 10^5
 *   - s consists of only uppercase English letters
 *   - 0 <= k <= s.length
 *
 * Approach:
 *   Use a sliding window. Track the count of each character in the current window using
 *   an array. The key insight is: if (window size - max frequency char count) <= k, the
 *   window is valid. Expand right always; shrink left only when the window becomes invalid.
 *   Track maxFreq (the highest count of any single character seen in any valid window state).
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1) — 26 uppercase letters
 *
 * Test Cases:
 *   1. Input: s="AABABBA", k=1 → Output: 4
 *   2. Input: s="ABAB", k=2 → Output: 4
 *   3. Edge case: s="AAAA", k=0 → Output: 4 (no replacements needed)
 */
public class LongestRepeatingCharacterReplacement {

    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int left = 0, maxFreq = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            count[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);

            while ((right - left + 1) - maxFreq > k) {
                count[s.charAt(left) - 'A']--;
                left++;
            }
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        LongestRepeatingCharacterReplacement sol = new LongestRepeatingCharacterReplacement();
        // Test 1
        System.out.println(sol.characterReplacement("AABABBA", 1)); // 4
        // Test 2
        System.out.println(sol.characterReplacement("ABAB", 2));    // 4
        // Test 3 (edge case: no replacements needed)
        System.out.println(sol.characterReplacement("AAAA", 0));    // 4
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1) — 26 uppercase letters
