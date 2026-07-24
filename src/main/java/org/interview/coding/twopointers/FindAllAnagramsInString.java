package org.interview.coding.twopointers;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Find All Anagrams in a String
 * Difficulty: Medium
 *
 * Description:
 * Given two strings s and p, return an array of all the start indices of p's anagrams
 * in s. An anagram is a permutation of a string. The answer may be returned in any order.
 *
 * Example:
 *   Input: s = "cbaebabacd", p = "abc"
 *   Output: [0,6]
 *
 * Constraints:
 *   - 1 <= s.length, p.length <= 3 * 10^4
 *   - s and p consist of lowercase English letters
 *
 * Approach:
 *   Similar to Permutation in String. Use a fixed sliding window of size p.length. Maintain
 *   character frequency arrays for p and the current window, plus a 'matches' counter for
 *   how many of the 26 character frequencies currently agree. Each time matches == 26, record
 *   the current window's start index as an anagram start.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: s="cbaebabacd", p="abc" → Output: [0,6]
 *   2. Input: s="abab", p="ab" → Output: [0,1,2]
 *   3. Edge case: s="aa", p="bb" → Output: []
 */
public class FindAllAnagramsInString {

    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;

        int[] pFreq = new int[26], sFreq = new int[26];
        for (char c : p.toCharArray()) pFreq[c - 'a']++;

        for (int i = 0; i < p.length(); i++) sFreq[s.charAt(i) - 'a']++;

        int matches = 0;
        for (int i = 0; i < 26; i++) if (pFreq[i] == sFreq[i]) matches++;
        if (matches == 26) result.add(0);

        for (int right = p.length(); right < s.length(); right++) {
            int add = s.charAt(right) - 'a';
            sFreq[add]++;
            if (sFreq[add] == pFreq[add]) matches++;
            else if (sFreq[add] - 1 == pFreq[add]) matches--;

            int rem = s.charAt(right - p.length()) - 'a';
            sFreq[rem]--;
            if (sFreq[rem] == pFreq[rem]) matches++;
            else if (sFreq[rem] + 1 == pFreq[rem]) matches--;

            if (matches == 26) result.add(right - p.length() + 1);
        }
        return result;
    }

    public static void main(String[] args) {
        FindAllAnagramsInString sol = new FindAllAnagramsInString();
        // Test 1
        System.out.println(sol.findAnagrams("cbaebabacd", "abc")); // [0,6]
        // Test 2
        System.out.println(sol.findAnagrams("abab", "ab"));        // [0,1,2]
        // Test 3 (edge case: no anagram)
        System.out.println(sol.findAnagrams("aa", "bb"));          // []
    }
}
