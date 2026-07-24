---
layout: problem
title: "Reorganize String"
category: heaps
category_display: "Heaps"
difficulty: Medium
time_complexity: "O(n log k) where k is number of distinct characters"
space_complexity: "O(k)"
tags: [heaps]
render_with_liquid: false
---

## Problem

Reorganize String Given a string s, rearrange the characters of s so that any two adjacent characters are not the same. Return any possible rearrangement of s or return "" if not possible. A rearrangement is impossible if any character frequency exceeds (n+1)/2.

## Approach

Count frequency of each character. If any frequency > (n+1)/2, return "". Use a max-heap ordered by frequency. Greedily pick the most frequent character. If the last character added to result equals the heap top, pick the second most frequent. Decrease frequency and re-add to heap if remaining count > 0. This ensures no two adjacent characters are the same.

## Solution

```java
package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Reorganize String
 * Difficulty: Medium
 *
 * Description:
 * Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
 * Return any possible rearrangement of s or return "" if not possible.
 * A rearrangement is impossible if any character frequency exceeds (n+1)/2.
 *
 * Example:
 *   Input: s = "aab"
 *   Output: "aba"
 *
 * Constraints:
 *   - 1 <= s.length <= 500
 *   - s consists of lowercase English letters
 *
 * Approach:
 *   Count frequency of each character. If any frequency > (n+1)/2, return "".
 *   Use a max-heap ordered by frequency. Greedily pick the most frequent character.
 *   If the last character added to result equals the heap top, pick the second most frequent.
 *   Decrease frequency and re-add to heap if remaining count > 0.
 *   This ensures no two adjacent characters are the same.
 *
 * Time Complexity: O(n log k) where k is number of distinct characters
 * Space Complexity: O(k)
 *
 * Test Cases:
 *   1. Input: "aab" → Output: "aba"
 *   2. Input: "aaab" → Output: "" (impossible)
 *   3. Edge: "a" → Output: "a"
 */
public class ReorganizeString {

    public String reorganizeString(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c - 'a']++;

        // Max-heap by frequency
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) heap.offer(new int[]{i, freq[i]});
        }

        StringBuilder sb = new StringBuilder();
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) == (char)('a' + top[0])) {
                if (heap.isEmpty()) return "";
                int[] next = heap.poll();
                sb.append((char)('a' + next[0]));
                next[1]--;
                if (next[1] > 0) heap.offer(next);
                heap.offer(top);
            } else {
                sb.append((char)('a' + top[0]));
                top[1]--;
                if (top[1] > 0) heap.offer(top);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ReorganizeString sol = new ReorganizeString();

        // Test 1
        System.out.println("Test 1: " + sol.reorganizeString("aab")); // "aba"

        // Test 2
        System.out.println("Test 2: " + sol.reorganizeString("aaab")); // ""

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.reorganizeString("a")); // "a"
    }
}
```

## Complexity

- **Time:** O(n log k) where k is number of distinct characters
- **Space:** O(k)
