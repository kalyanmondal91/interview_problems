package org.interview.coding.twopointers;

/**
 * Problem: Longest Palindromic Substring
 * Difficulty: Medium
 *
 * Description:
 * Given a string s, return the longest palindromic substring in s. A palindrome reads
 * the same backward as forward. If there are multiple answers of the same maximum length,
 * return any one of them.
 *
 * Example:
 *   Input: s = "babad"
 *   Output: "bab" (or "aba")
 *
 * Constraints:
 *   - 1 <= s.length <= 1000
 *   - s consist of only digits and English letters
 *
 * Approach:
 *   For each character (and each pair of adjacent characters for even-length palindromes),
 *   expand outward while characters on both sides match. This is the "expand around center"
 *   technique. There are 2n-1 possible centers (n single chars, n-1 pairs). Track the
 *   start and end of the longest palindrome found.
 *
 * Time Complexity: O(n^2)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: s="babad" → Output: "bab" or "aba"
 *   2. Input: s="cbbd" → Output: "bb"
 *   3. Edge case: s="a" → Output: "a"
 */
public class LongestPalindromicSubstring {

    private int start, maxLen;

    public String longestPalindrome(String s) {
        start = 0; maxLen = 1;
        for (int i = 0; i < s.length(); i++) {
            expand(s, i, i);     // odd length
            expand(s, i, i + 1); // even length
        }
        return s.substring(start, start + maxLen);
    }

    private void expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            if (right - left + 1 > maxLen) {
                maxLen = right - left + 1;
                start = left;
            }
            left--;
            right++;
        }
    }

    public static void main(String[] args) {
        LongestPalindromicSubstring sol = new LongestPalindromicSubstring();
        // Test 1
        System.out.println(sol.longestPalindrome("babad")); // "bab" or "aba"
        // Test 2
        System.out.println(sol.longestPalindrome("cbbd"));  // "bb"
        // Test 3 (edge case: single char)
        System.out.println(sol.longestPalindrome("a"));     // "a"
    }
}
