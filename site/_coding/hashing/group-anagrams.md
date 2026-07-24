---
layout: problem
title: "Group Anagrams"
category: hashing
category_display: "Hashing"
difficulty: Medium
time_complexity: "O(n * k log k) where k is the max string length"
space_complexity: "O(n * k)"
tags: [hashing]
render_with_liquid: false
---

## Problem

Group Anagrams Given an array of strings strs, group the anagrams together. An anagram is a word formed by rearranging the letters of a different word, using all the original letters exactly once. The answer can be returned in any order.

## Approach

For each string, sort its characters to produce a canonical key. All anagrams of a word will produce the same sorted key. Use a HashMap where the sorted key maps to a list of original strings. After processing all strings, return the values of the map as the grouped result.

## Solution

```java
package org.interview.coding.hashing;

import java.util.*;

/**
 * Problem: Group Anagrams
 * Difficulty: Medium
 *
 * Description:
 * Given an array of strings strs, group the anagrams together. An anagram is a word formed
 * by rearranging the letters of a different word, using all the original letters exactly once.
 * The answer can be returned in any order.
 *
 * Example:
 *   Input: strs = ["eat","tea","tan","ate","nat","bat"]
 *   Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * Constraints:
 *   - 1 <= strs.length <= 10^4
 *   - 0 <= strs[i].length <= 100
 *   - strs[i] consists of lowercase English letters
 *
 * Approach:
 *   For each string, sort its characters to produce a canonical key. All anagrams of a word
 *   will produce the same sorted key. Use a HashMap where the sorted key maps to a list of
 *   original strings. After processing all strings, return the values of the map as the
 *   grouped result.
 *
 * Time Complexity: O(n * k log k) where k is the max string length
 * Space Complexity: O(n * k)
 *
 * Test Cases:
 *   1. Input: ["eat","tea","tan","ate","nat","bat"] → Output: 3 groups
 *   2. Input: [""] → Output: [[""]]
 *   3. Edge case: ["a"] → Output: [["a"]]
 */
public class GroupAnagrams {

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        GroupAnagrams sol = new GroupAnagrams();
        // Test 1
        System.out.println(sol.groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"}));
        // Test 2
        System.out.println(sol.groupAnagrams(new String[]{""}));    // [[""]]
        // Test 3 (edge case: single char)
        System.out.println(sol.groupAnagrams(new String[]{"a"}));   // [["a"]]
    }
}
```

## Complexity

- **Time:** O(n * k log k) where k is the max string length
- **Space:** O(n * k)
