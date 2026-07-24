---
layout: problem
title: "Alien Dictionary"
category: graphs
category_display: "Graphs"
difficulty: Hard
time_complexity: "O(C) — C is total number of characters across all words"
space_complexity: "O(U + min(U^2, N)) — U is unique chars, N is word count"
tags: [graphs]
render_with_liquid: false
---

## Problem

Alien Dictionary Given a sorted list of words from an alien language dictionary, derive the order of characters in the alien alphabet. The words are sorted lexicographically by the alien alphabet's rules. Return the characters in order, or "" if no valid ordering exists.

## Approach

Compare adjacent words to derive ordering constraints. For each pair of adjacent words, find the first differing character: the character in the first word comes before the one in the second word. Build a directed graph from these constraints. Then perform topological sort (Kahn's BFS) to produce the character order. Return "" if a cycle is detected.

## Solution

```java
package org.interview.coding.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Problem: Alien Dictionary
 * Difficulty: Hard
 *
 * Description:
 * Given a sorted list of words from an alien language dictionary, derive the order of
 * characters in the alien alphabet. The words are sorted lexicographically by the alien
 * alphabet's rules. Return the characters in order, or "" if no valid ordering exists.
 *
 * Example:
 *   Input: words = ["wrt","wrf","er","ett","rftt"]
 *   Output: "wertf"
 *
 * Constraints:
 *   - 1 <= words.length <= 100
 *   - 1 <= words[i].length <= 100
 *   - All characters are lowercase English letters
 *
 * Approach:
 *   Compare adjacent words to derive ordering constraints. For each pair of adjacent words,
 *   find the first differing character: the character in the first word comes before the one
 *   in the second word. Build a directed graph from these constraints. Then perform topological
 *   sort (Kahn's BFS) to produce the character order. Return "" if a cycle is detected.
 *
 * Time Complexity: O(C) — C is total number of characters across all words
 * Space Complexity: O(U + min(U^2, N)) — U is unique chars, N is word count
 *
 * Test Cases:
 *   1. Input: ["wrt","wrf","er","ett","rftt"] → Output: "wertf"
 *   2. Input: ["z","x"] → Output: "zx"
 *   3. Edge case: invalid (prefix word after full word) → Output: ""
 */
public class AlienDictionary {

    public String alienOrder(String[] words) {
        Map<Character, List<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new ArrayList<>());
                inDegree.putIfAbsent(c, 0);
            }
        }

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            if (w1.length() > w2.length() && w1.startsWith(w2)) return "";
            for (int j = 0; j < Math.min(w1.length(), w2.length()); j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    adj.get(w1.charAt(j)).add(w2.charAt(j));
                    inDegree.merge(w2.charAt(j), 1, Integer::sum);
                    break;
                }
            }
        }

        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) queue.offer(c);
        }

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            char c = queue.poll();
            sb.append(c);
            for (char next : adj.get(c)) {
                inDegree.merge(next, -1, Integer::sum);
                if (inDegree.get(next) == 0) queue.offer(next);
            }
        }

        return sb.length() == inDegree.size() ? sb.toString() : "";
    }

    public static void main(String[] args) {
        AlienDictionary sol = new AlienDictionary();

        // Test 1
        System.out.println("Test 1 (expect wertf): " + sol.alienOrder(new String[]{"wrt","wrf","er","ett","rftt"}));

        // Test 2: simple
        System.out.println("Test 2 (expect zx): " + sol.alienOrder(new String[]{"z","x"}));

        // Test 3: invalid - prefix issue
        System.out.println("Test 3 (expect empty): " + sol.alienOrder(new String[]{"abc","ab"}));
    }
}
```

## Complexity

- **Time:** O(C) — C is total number of characters across all words
- **Space:** O(U + min(U^2, N)) — U is unique chars, N is word count
