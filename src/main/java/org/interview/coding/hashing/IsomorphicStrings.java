package org.interview.coding.hashing;

import java.util.HashMap;

/**
 * Problem: Isomorphic Strings
 * Difficulty: Easy
 *
 * Description:
 * Given two strings s and t, determine if they are isomorphic. Two strings are isomorphic
 * if the characters in s can be replaced to get t, with a consistent one-to-one mapping.
 * No two characters may map to the same character, but a character may map to itself.
 *
 * Example:
 *   Input: s = "egg", t = "add"
 *   Output: true
 *
 * Constraints:
 *   - 1 <= s.length <= 5 * 10^4
 *   - t.length == s.length
 *   - s and t consist of any valid ASCII character
 *
 * Approach:
 *   Maintain two HashMaps: one mapping characters from s to t, and another mapping from t
 *   to s. For each pair of characters, check both maps for consistency. If s[i] is already
 *   mapped to a different t[i], or t[i] is already mapped to a different s[i], return false.
 *   This bidirectional check ensures the mapping is a true bijection.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1) — at most 256 unique ASCII characters
 *
 * Test Cases:
 *   1. Input: s="egg", t="add" → Output: true
 *   2. Input: s="foo", t="bar" → Output: false (both 'o' would map to different chars)
 *   3. Edge case: s="paper", t="title" → Output: true
 */
public class IsomorphicStrings {

    public boolean isIsomorphic(String s, String t) {
        HashMap<Character, Character> sToT = new HashMap<>();
        HashMap<Character, Character> tToS = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (sToT.containsKey(sc) && sToT.get(sc) != tc) return false;
            if (tToS.containsKey(tc) && tToS.get(tc) != sc) return false;
            sToT.put(sc, tc);
            tToS.put(tc, sc);
        }
        return true;
    }

    public static void main(String[] args) {
        IsomorphicStrings sol = new IsomorphicStrings();
        // Test 1
        System.out.println(sol.isIsomorphic("egg", "add"));     // true
        // Test 2
        System.out.println(sol.isIsomorphic("foo", "bar"));     // false
        // Test 3 (edge case: longer strings)
        System.out.println(sol.isIsomorphic("paper", "title")); // true
    }
}
