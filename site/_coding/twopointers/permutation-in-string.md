---
layout: problem
title: "Permutation In String"
category: twopointers
category_display: "Two Pointers"
difficulty: Medium
time_complexity: "O(n) where n = s2.length"
space_complexity: "O(1) — fixed 26-length arrays"
tags: [twopointers]
render_with_liquid: false
---

## Problem

Permutation in String Given two strings s1 and s2, return true if s2 contains a permutation of s1 as a substring. In other words, return true if one of s1's permutations is a substring of s2.

## Approach

Use a fixed-size sliding window of size s1.length over s2. Maintain frequency arrays for s1 and the current window. Use a 'matches' counter tracking how many of the 26 character counts currently match. Slide the window right, updating both the right addition and left removal while updating 'matches'. Return true if matches == 26.

## Solution

```java
package org.interview.coding.twopointers;

/**
 * Problem: Permutation in String
 * Difficulty: Medium
 *
 * Description:
 * Given two strings s1 and s2, return true if s2 contains a permutation of s1 as a
 * substring. In other words, return true if one of s1's permutations is a substring of s2.
 *
 * Example:
 *   Input: s1 = "ab", s2 = "eidbaooo"
 *   Output: true
 *
 * Constraints:
 *   - 1 <= s1.length, s2.length <= 10^4
 *   - s1 and s2 consist of lowercase English letters
 *
 * Approach:
 *   Use a fixed-size sliding window of size s1.length over s2. Maintain frequency arrays
 *   for s1 and the current window. Use a 'matches' counter tracking how many of the 26
 *   character counts currently match. Slide the window right, updating both the right
 *   addition and left removal while updating 'matches'. Return true if matches == 26.
 *
 * Time Complexity: O(n) where n = s2.length
 * Space Complexity: O(1) — fixed 26-length arrays
 *
 * Test Cases:
 *   1. Input: s1="ab", s2="eidbaooo" → Output: true
 *   2. Input: s1="ab", s2="eidboaoo" → Output: false
 *   3. Edge case: s1="adc", s2="dcda" → Output: true
 */
public class PermutationInString {

    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] s1Freq = new int[26], s2Freq = new int[26];
        for (char c : s1.toCharArray()) s1Freq[c - 'a']++;

        int matches = 0;
        for (int i = 0; i < s1.length(); i++) {
            s2Freq[s2.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) if (s1Freq[i] == s2Freq[i]) matches++;

        for (int right = s1.length(); right < s2.length(); right++) {
            if (matches == 26) return true;

            int add = s2.charAt(right) - 'a';
            s2Freq[add]++;
            if (s2Freq[add] == s1Freq[add]) matches++;
            else if (s2Freq[add] - 1 == s1Freq[add]) matches--;

            int rem = s2.charAt(right - s1.length()) - 'a';
            s2Freq[rem]--;
            if (s2Freq[rem] == s1Freq[rem]) matches++;
            else if (s2Freq[rem] + 1 == s1Freq[rem]) matches--;
        }
        return matches == 26;
    }

    public static void main(String[] args) {
        PermutationInString sol = new PermutationInString();
        // Test 1
        System.out.println(sol.checkInclusion("ab", "eidbaooo")); // true
        // Test 2
        System.out.println(sol.checkInclusion("ab", "eidboaoo")); // false
        // Test 3 (edge case)
        System.out.println(sol.checkInclusion("adc", "dcda"));    // true
    }
}
```

## Complexity

- **Time:** O(n) where n = s2.length
- **Space:** O(1) — fixed 26-length arrays
