package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Decode String
 * Difficulty: Medium
 *
 * Description:
 * Given an encoded string, return its decoded string. The encoding rule is: k[encoded_string]
 * means the encoded_string is repeated exactly k times. The input is always valid — no extra
 * white spaces, brackets are well-formed, and digits are positive integers.
 *
 * Example:
 *   Input: s = "3[a2[c]]"
 *   Output: "accaccacc"
 *
 * Constraints:
 *   - 1 <= s.length <= 30
 *   - s consists of lowercase English letters, digits, and brackets
 *   - All integers in s are in range [1, 300]
 *
 * Approach:
 *   Use two stacks: one for counts and one for strings. Accumulate digits into currentNum
 *   and characters into currentStr. On '[', push currentNum and currentStr onto their
 *   stacks and reset both. On ']', pop count and prefix string; repeat currentStr count
 *   times and append to prefix. This handles arbitrary nesting depth iteratively.
 *
 * Time Complexity: O(n * maxK) where maxK is the maximum repeat count
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: s="3[a2[c]]" → Output: "accaccacc"
 *   2. Input: s="2[abc]3[cd]ef" → Output: "abcabccdcdcdef"
 *   3. Edge case: s="abc" → Output: "abc" (no encoding)
 */
public class DecodeString {

    public String decodeString(String s) {
        Deque<Integer> countStack = new ArrayDeque<>();
        Deque<StringBuilder> strStack = new ArrayDeque<>();
        StringBuilder current = new StringBuilder();
        int k = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                k = k * 10 + (c - '0');
            } else if (c == '[') {
                countStack.push(k);
                strStack.push(current);
                current = new StringBuilder();
                k = 0;
            } else if (c == ']') {
                int repeat = countStack.pop();
                String repeated = current.toString().repeat(repeat);
                current = strStack.pop().append(repeated);
            } else {
                current.append(c);
            }
        }
        return current.toString();
    }

    public static void main(String[] args) {
        DecodeString sol = new DecodeString();
        // Test 1
        System.out.println(sol.decodeString("3[a2[c]]"));      // accaccacc
        // Test 2
        System.out.println(sol.decodeString("2[abc]3[cd]ef")); // abcabccdcdcdef
        // Test 3 (edge case: no encoding)
        System.out.println(sol.decodeString("abc"));           // abc
    }
}
