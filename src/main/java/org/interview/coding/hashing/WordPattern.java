package org.interview.coding.hashing;

import java.util.HashMap;

/**
 * Problem: Word Pattern
 * Difficulty: Easy
 *
 * Description:
 * Given a pattern and a string s, find if s follows the same pattern. Here "follow" means
 * a full match, such that there is a bijection between a letter in pattern and a non-empty
 * word in s. All words in s are separated by spaces.
 *
 * Example:
 *   Input: pattern = "abba", s = "dog cat cat dog"
 *   Output: true
 *
 * Constraints:
 *   - 1 <= pattern.length <= 300
 *   - pattern contains only lower-case English letters
 *   - 1 <= s.length <= 3000
 *   - s contains only lowercase English letters and spaces
 *
 * Approach:
 *   Split s by spaces to get the word array. Check lengths match. Use two HashMaps: one
 *   mapping pattern chars to words, and one mapping words to pattern chars. For each pair
 *   (pattern[i], words[i]), verify consistency in both maps. If there's any conflict in
 *   either direction, return false (ensures bijection, not just injection).
 *
 * Time Complexity: O(n) where n is the number of words
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: pattern="abba", s="dog cat cat dog" → Output: true
 *   2. Input: pattern="abba", s="dog cat cat fish" → Output: false
 *   3. Edge case: pattern="aaaa", s="dog cat cat dog" → Output: false
 */
public class WordPattern {

    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (pattern.length() != words.length) return false;

        HashMap<Character, String> charToWord = new HashMap<>();
        HashMap<String, Character> wordToChar = new HashMap<>();

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            String w = words[i];

            if (charToWord.containsKey(c) && !charToWord.get(c).equals(w)) return false;
            if (wordToChar.containsKey(w) && wordToChar.get(w) != c) return false;
            charToWord.put(c, w);
            wordToChar.put(w, c);
        }
        return true;
    }

    public static void main(String[] args) {
        WordPattern sol = new WordPattern();
        // Test 1
        System.out.println(sol.wordPattern("abba", "dog cat cat dog"));  // true
        // Test 2
        System.out.println(sol.wordPattern("abba", "dog cat cat fish")); // false
        // Test 3 (edge case: same word for different pattern chars)
        System.out.println(sol.wordPattern("aaaa", "dog cat cat dog"));  // false
    }
}
