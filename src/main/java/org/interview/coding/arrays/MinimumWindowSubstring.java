package org.interview.coding.arrays;

import java.util.HashMap;

/**
 * Problem: Minimum Window Substring
 * Difficulty: Hard
 *
 * Description:
 * Given strings s and t, return the minimum window substring of s such that every character
 * in t (including duplicates) is included in the window. If no such substring exists, return
 * an empty string "". The answer is guaranteed to be unique.
 *
 * Example:
 *   Input: s = "ADOBECODEBANC", t = "ABC"
 *   Output: "BANC"
 *
 * Constraints:
 *   - 1 <= s.length, t.length <= 10^5
 *   - s and t consist of uppercase and lowercase English letters
 *
 * Approach:
 *   Build a frequency map for t. Use a sliding window with two pointers. Expand right,
 *   decrementing the character count in the window map. When all required characters are
 *   satisfied (tracked by a 'formed' counter), try to contract from the left to minimize
 *   the window. Record the minimum window found. Continue expanding and contracting until
 *   the right pointer exhausts the string.
 *
 * Time Complexity: O(|s| + |t|)
 * Space Complexity: O(|s| + |t|)
 *
 * Test Cases:
 *   1. Input: s="ADOBECODEBANC", t="ABC" → Output: "BANC"
 *   2. Input: s="a", t="a" → Output: "a"
 *   3. Edge case: s="a", t="aa" → Output: "" (not enough characters)
 */
public class MinimumWindowSubstring {

    public String minWindow(String s, String t) {
        if (s.length() == 0 || t.length() == 0) return "";

        HashMap<Character, Integer> tFreq = new HashMap<>();
        for (char c : t.toCharArray()) tFreq.merge(c, 1, Integer::sum);

        int required = tFreq.size();
        int formed = 0;
        HashMap<Character, Integer> windowFreq = new HashMap<>();

        int left = 0, minLen = Integer.MAX_VALUE, minLeft = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            windowFreq.merge(c, 1, Integer::sum);
            if (tFreq.containsKey(c) && windowFreq.get(c).intValue() == tFreq.get(c).intValue()) {
                formed++;
            }

            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minLeft = left;
                }
                char leftChar = s.charAt(left);
                windowFreq.merge(leftChar, -1, Integer::sum);
                if (tFreq.containsKey(leftChar) && windowFreq.get(leftChar) < tFreq.get(leftChar)) {
                    formed--;
                }
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
    }

    public static void main(String[] args) {
        MinimumWindowSubstring sol = new MinimumWindowSubstring();
        // Test 1
        System.out.println(sol.minWindow("ADOBECODEBANC", "ABC")); // "BANC"
        // Test 2
        System.out.println(sol.minWindow("a", "a"));               // "a"
        // Test 3 (edge case: t requires more chars than available)
        System.out.println(sol.minWindow("a", "aa"));              // ""
    }
}
