---
layout: problem
title: "Generate Parentheses"
category: backtracking
category_display: "Backtracking"
difficulty: Medium
time_complexity: "O(4^n / sqrt(n))"
space_complexity: "O(n)"
tags: [backtracking]
render_with_liquid: false
---

## Problem

Generate Parentheses Given n pairs of parentheses, generate all combinations of well-formed (valid) parentheses.

## Approach

Use backtracking tracking the count of open and close parentheses added so far. At each step, add '(' if open count is less than n, and add ')' if close count is less than open count (to maintain validity). The base case is when the current string length equals 2*n, at which point the combination is complete and added to the result. This generates only valid combinations.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Generate Parentheses
 * Difficulty: Medium
 *
 * Description:
 * Given n pairs of parentheses, generate all combinations of well-formed (valid) parentheses.
 *
 * Example:
 *   Input: n = 3
 *   Output: ["((()))","(()())","(())()","()(())","()()()"]
 *
 * Constraints:
 *   - 1 <= n <= 8
 *
 * Approach:
 *   Use backtracking tracking the count of open and close parentheses added so far. At each step,
 *   add '(' if open count is less than n, and add ')' if close count is less than open count (to
 *   maintain validity). The base case is when the current string length equals 2*n, at which point
 *   the combination is complete and added to the result. This generates only valid combinations.
 *
 * Time Complexity: O(4^n / sqrt(n))
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: n=3 → Output: ["((()))","(()())","(())()","()(())","()()()"]
 *   2. Input: n=1 → Output: ["()"]
 *   3. Edge: n=2 → Output: ["(())","()()"]
 */
public class GenerateParentheses {

    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(n, 0, 0, new StringBuilder(), result);
        return result;
    }

    private void backtrack(int n, int open, int close, StringBuilder current, List<String> result) {
        if (current.length() == 2 * n) {
            result.add(current.toString());
            return;
        }
        if (open < n) {
            current.append('(');
            backtrack(n, open + 1, close, current, result);
            current.deleteCharAt(current.length() - 1);
        }
        if (close < open) {
            current.append(')');
            backtrack(n, open, close + 1, current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }

    public static void main(String[] args) {
        GenerateParentheses solution = new GenerateParentheses();

        // Test 1: n=3 produces 5 combinations
        System.out.println(solution.generateParenthesis(3));
        // Expected: [((())) , (()()) , (())() , ()(()) , ()()()]

        // Test 2: n=1 trivial case
        System.out.println(solution.generateParenthesis(1));
        // Expected: [()]

        // Test 3: n=2
        System.out.println(solution.generateParenthesis(2));
        // Expected: [(()) , ()()]
    }
}
```

## Complexity

- **Time:** O(4^n / sqrt(n))
- **Space:** O(n)
