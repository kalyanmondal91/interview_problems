---
layout: problem
title: "Valid Parentheses"
category: stacks
category_display: "Stacks"
difficulty: Easy
time_complexity: "O(n)"
space_complexity: "O(n)"
leetcode: 20
tags: [stacks]
render_with_liquid: false
---

## Problem

Valid Parentheses Given a string s containing only the characters '(', ')', '{', '}', '[', ']', determine if the input string is valid. A string is valid if every open bracket is closed by the same type in the correct order, and every close bracket has a corresponding open bracket.

## Approach

Use a stack. For each character: if it's an opening bracket push it. If it's a closing bracket, check if the stack is non-empty and the top matches the expected opening bracket. If not, return false. After processing all characters, the stack must be empty for the string to be valid.

## Solution

```java
package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Valid Parentheses
 * Difficulty: Easy
 *
 * Description:
 * Given a string s containing only the characters '(', ')', '{', '}', '[', ']', determine
 * if the input string is valid. A string is valid if every open bracket is closed by the
 * same type in the correct order, and every close bracket has a corresponding open bracket.
 *
 * Example:
 *   Input: s = "()[]{}"
 *   Output: true
 *
 * Constraints:
 *   - 1 <= s.length <= 10^4
 *   - s consists of parentheses only '()[]{}'
 *
 * Approach:
 *   Use a stack. For each character: if it's an opening bracket push it. If it's a closing
 *   bracket, check if the stack is non-empty and the top matches the expected opening bracket.
 *   If not, return false. After processing all characters, the stack must be empty for the
 *   string to be valid.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: s="()[]{}" → Output: true
 *   2. Input: s="(]" → Output: false
 *   3. Edge case: s="([)]" → Output: false (interleaved brackets)
 */
public class ValidParentheses {

    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == ']' && top != '[') return false;
                if (c == '}' && top != '{') return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        ValidParentheses sol = new ValidParentheses();
        // Test 1
        System.out.println(sol.isValid("()[]{}"));  // true
        // Test 2
        System.out.println(sol.isValid("(]"));      // false
        // Test 3 (edge case: interleaved)
        System.out.println(sol.isValid("([)]"));    // false
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
