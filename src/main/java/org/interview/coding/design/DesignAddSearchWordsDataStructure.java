package org.interview.coding.design;

import java.util.*;

/**
 * Problem: Design Add and Search Words Data Structure
 * Difficulty: Medium
 *
 * Description: Design a data structure that supports adding new words and searching them.
 * addWord(word) adds the word to the structure. search(word) returns true if there is a previously
 * added word that matches. The search word may contain '.' which can match any letter.
 *
 * Example:
 *   Input: addWord("bad"), addWord("dad"), addWord("mad"), search("pad"), search("bad"), search(".ad"), search("b..")
 *   Output: false, true, true, true
 *
 * Approach: Use a Trie where each TrieNode has children[26] and isEnd. addWord traverses/creates nodes
 * normally. search uses recursive DFS: when the current character is '.', try all 26 non-null children
 * recursively; when it's a letter, follow only the matching child. Return isEnd when the word is exhausted.
 *
 * Time Complexity: O(L) for addWord, O(26^L) worst case for search with all dots
 * Space Complexity: O(N*L) total trie space
 *
 * Test Cases:
 *   1. addWord("bad"), search("bad") → true, search("pad") → false
 *   2. search(".ad") → true (matches "bad","dad","mad"), search("b..") → true (matches "bad")
 *   3. Edge: search("....") → false (no 4-letter words stored)
 */
public class DesignAddSearchWordsDataStructure {

    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd = false;
    }

    private final TrieNode root;

    public DesignAddSearchWordsDataStructure() {
        root = new TrieNode();
    }

    public void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) node.children[idx] = new TrieNode();
            node = node.children[idx];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        return dfs(word, 0, root);
    }

    private boolean dfs(String word, int index, TrieNode node) {
        if (index == word.length()) return node.isEnd;
        char c = word.charAt(index);
        if (c == '.') {
            for (TrieNode child : node.children) {
                if (child != null && dfs(word, index + 1, child)) return true;
            }
            return false;
        } else {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            return dfs(word, index + 1, node.children[idx]);
        }
    }

    public static void main(String[] args) {
        // Test Case 1: Basic add and search
        DesignAddSearchWordsDataStructure dict = new DesignAddSearchWordsDataStructure();
        dict.addWord("bad");
        dict.addWord("dad");
        dict.addWord("mad");
        System.out.println("search(pad)=" + dict.search("pad"));  // false
        System.out.println("search(bad)=" + dict.search("bad"));  // true

        // Test Case 2: Wildcard '.' matching
        System.out.println("search(.ad)=" + dict.search(".ad")); // true
        System.out.println("search(b..)=" + dict.search("b..")); // true

        // Test Case 3: Edge - wrong length
        System.out.println("search(....)=" + dict.search("....")); // false (all words are 3 chars)
        System.out.println("search(...)=" + dict.search("..."));   // true (matches any 3-char word)
    }
}
