package org.interview.coding.design;

import java.util.*;

/**
 * Problem: Implement Trie (Prefix Tree)
 * Difficulty: Medium
 *
 * Description: Implement a Trie data structure with insert(word), search(word), and startsWith(prefix).
 * search returns true if the word is in the trie. startsWith returns true if any previously inserted
 * word has the given prefix.
 *
 * Example:
 *   Input: insert("apple"), search("apple"), search("app"), startsWith("app"), insert("app"), search("app")
 *   Output: true, false, true, true
 *
 * Approach: Each TrieNode contains an array of 26 child nodes (one per lowercase letter) and a boolean
 * isEnd marking word completion. insert traverses (creating nodes as needed) and marks the last node.
 * search traverses and returns isEnd of the last node. startsWith traverses and returns true if all
 * characters are found regardless of isEnd.
 *
 * Time Complexity: O(L) for all operations where L = length of word/prefix
 * Space Complexity: O(N*L) for N words of average length L
 *
 * Test Cases:
 *   1. insert("apple"), search("apple") → true
 *   2. search("app") → false, startsWith("app") → true, insert("app"), search("app") → true
 *   3. Edge: search("") → true (empty string, root isEnd=false by default → false), startsWith("") → true
 */
public class ImplementTrie {

    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd = false;
    }

    private final TrieNode root;

    public ImplementTrie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) {
                node.children[idx] = new TrieNode();
            }
            node = node.children[idx];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            node = node.children[idx];
        }
        return node.isEnd;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return false;
            node = node.children[idx];
        }
        return true;
    }

    public static void main(String[] args) {
        // Test Case 1: Basic insert and search
        ImplementTrie trie = new ImplementTrie();
        trie.insert("apple");
        System.out.println("search(apple)=" + trie.search("apple"));     // true
        System.out.println("search(app)=" + trie.search("app"));         // false

        // Test Case 2: startsWith and then insert prefix
        System.out.println("startsWith(app)=" + trie.startsWith("app")); // true
        trie.insert("app");
        System.out.println("search(app)=" + trie.search("app"));         // true

        // Test Case 3: Edge - word not inserted at all
        System.out.println("search(banana)=" + trie.search("banana"));   // false
        System.out.println("startsWith(ap)=" + trie.startsWith("ap"));   // true
        System.out.println("startsWith(b)=" + trie.startsWith("b"));     // false
    }
}
