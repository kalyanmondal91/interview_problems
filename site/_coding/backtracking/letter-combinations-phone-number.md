---
layout: problem
title: "Letter Combinations Phone Number"
category: backtracking
category_display: "Backtracking"
difficulty: Medium
time_complexity: "O(4^n * n)"
space_complexity: "O(n)"
tags: [backtracking]
render_with_liquid: false
---

## Problem

Letter Combinations of a Phone Number Given a string containing digits from 2-9, return all possible letter combinations that the number could represent, based on traditional phone keyboard mappings. Return the answer in any order.

## Approach

Create a mapping from each digit to its corresponding letters. Use backtracking: at each position in digits, iterate over all letters mapped to that digit, append the letter to the current combination, recurse to the next digit position, then remove the last character. When the current combination length equals digits length, add it to results. Handle empty input as a special case returning an empty list.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Letter Combinations of a Phone Number
 * Difficulty: Medium
 *
 * Description:
 * Given a string containing digits from 2-9, return all possible letter combinations that the
 * number could represent, based on traditional phone keyboard mappings. Return the answer in any order.
 *
 * Example:
 *   Input: digits = "23"
 *   Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *
 * Constraints:
 *   - 0 <= digits.length <= 4
 *   - digits[i] is a digit in the range ['2', '9'].
 *
 * Approach:
 *   Create a mapping from each digit to its corresponding letters. Use backtracking: at each
 *   position in digits, iterate over all letters mapped to that digit, append the letter to the
 *   current combination, recurse to the next digit position, then remove the last character.
 *   When the current combination length equals digits length, add it to results. Handle empty
 *   input as a special case returning an empty list.
 *
 * Time Complexity: O(4^n * n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: "23" → Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *   2. Input: "" → Output: []
 *   3. Edge: "2" → Output: ["a","b","c"]
 */
public class LetterCombinationsPhoneNumber {

    private static final Map<Character, String> PHONE_MAP = new HashMap<>();
    static {
        PHONE_MAP.put('2', "abc");
        PHONE_MAP.put('3', "def");
        PHONE_MAP.put('4', "ghi");
        PHONE_MAP.put('5', "jkl");
        PHONE_MAP.put('6', "mno");
        PHONE_MAP.put('7', "pqrs");
        PHONE_MAP.put('8', "tuv");
        PHONE_MAP.put('9', "wxyz");
    }

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return result;
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private void backtrack(String digits, int index, StringBuilder current, List<String> result) {
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }
        String letters = PHONE_MAP.get(digits.charAt(index));
        for (char letter : letters.toCharArray()) {
            current.append(letter);
            backtrack(digits, index + 1, current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }

    public static void main(String[] args) {
        LetterCombinationsPhoneNumber solution = new LetterCombinationsPhoneNumber();

        // Test 1: two digits
        System.out.println(solution.letterCombinations("23"));
        // Expected: [ad, ae, af, bd, be, bf, cd, ce, cf]

        // Test 2: empty input
        System.out.println(solution.letterCombinations(""));
        // Expected: []

        // Test 3: single digit
        System.out.println(solution.letterCombinations("2"));
        // Expected: [a, b, c]
    }
}
```

## Complexity

- **Time:** O(4^n * n)
- **Space:** O(n)
