---
layout: problem
title: "Word Ladder"
category: graphs
category_display: "Graphs"
difficulty: Hard
time_complexity: "O(M^2 * N) — M is word length, N is dictionary size"
space_complexity: "O(M^2 * N) — queue and visited set"
leetcode: 127
tags: [graphs]
render_with_liquid: false
---

## Problem

Word Ladder Given two words beginWord and endWord, and a dictionary wordList, return the number of words in the shortest transformation sequence from beginWord to endWord, where each step changes exactly one letter and every intermediate word must be in the wordList. Return 0 if no such sequence exists.

## Approach

Use BFS starting from beginWord. For each word in the queue, try replacing each character with all 26 letters to generate all possible single-character variations. If a variation exists in the wordList and hasn't been visited, add it to the queue and mark as visited. When endWord is found, return the current level (transformation count).

## Solution

```java
package org.interview.coding.graphs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Problem: Word Ladder
 * Difficulty: Hard
 *
 * Description:
 * Given two words beginWord and endWord, and a dictionary wordList, return the number of
 * words in the shortest transformation sequence from beginWord to endWord, where each step
 * changes exactly one letter and every intermediate word must be in the wordList.
 * Return 0 if no such sequence exists.
 *
 * Example:
 *   Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 *   Output: 5 ("hit" -> "hot" -> "dot" -> "dog" -> "cog")
 *
 * Constraints:
 *   - 1 <= beginWord.length <= 10
 *   - beginWord.length == endWord.length
 *   - 1 <= wordList.length <= 5000
 *   - All words consist of lowercase English letters
 *
 * Approach:
 *   Use BFS starting from beginWord. For each word in the queue, try replacing each character
 *   with all 26 letters to generate all possible single-character variations. If a variation
 *   exists in the wordList and hasn't been visited, add it to the queue and mark as visited.
 *   When endWord is found, return the current level (transformation count).
 *
 * Time Complexity: O(M^2 * N) — M is word length, N is dictionary size
 * Space Complexity: O(M^2 * N) — queue and visited set
 *
 * Test Cases:
 *   1. Input: beginWord="hit", endWord="cog", wordList=[hot,dot,dog,lot,log,cog] → Output: 5
 *   2. Input: beginWord="hit", endWord="cog", wordList=[hot,dot,dog,lot,log] → Output: 0 (no cog)
 *   3. Edge case: beginWord equals endWord → Output: 0
 */
public class WordLadder {

    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        int steps = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                char[] chars = word.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char orig = chars[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        chars[j] = c;
                        String next = new String(chars);
                        if (next.equals(endWord)) return steps + 1;
                        if (wordSet.contains(next) && !visited.contains(next)) {
                            visited.add(next);
                            queue.offer(next);
                        }
                    }
                    chars[j] = orig;
                }
            }
            steps++;
        }
        return 0;
    }

    public static void main(String[] args) {
        WordLadder sol = new WordLadder();

        // Test 1: valid path exists
        List<String> wl1 = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        System.out.println("Test 1 (expect 5): " + sol.ladderLength("hit", "cog", wl1));

        // Test 2: endWord not in list
        List<String> wl2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        System.out.println("Test 2 (expect 0): " + sol.ladderLength("hit", "cog", wl2));

        // Test 3: direct one-step transformation
        List<String> wl3 = Arrays.asList("hot");
        System.out.println("Test 3 (expect 2): " + sol.ladderLength("hit", "hot", wl3));
    }
}
```

## Complexity

- **Time:** O(M^2 * N) — M is word length, N is dictionary size
- **Space:** O(M^2 * N) — queue and visited set
