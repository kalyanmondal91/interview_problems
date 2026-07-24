---
layout: problem
title: "Basic Calculator I I"
category: math
category_display: "Math"
difficulty: Medium
time_complexity: "O(n) where n = length of expression"
space_complexity: "O(n) for the stack"
tags: [math]
render_with_liquid: false
---

## Problem

Basic Calculator II Evaluate a string expression containing non-negative integers and the operators +, -, *, /. There are no parentheses. Integer division should truncate toward zero. The expression is guaranteed to be valid.

## Approach

Scan left to right tracking the current number and last operator sign. On each operator or end of string: '+' pushes num, '-' pushes -num, '*' pops and pushes pop*num, '/' pops and pushes pop/num. This handles operator precedence by deferring + and - to the stack while immediately applying * and /. Sum the stack at the end.

## Solution

```java
package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Basic Calculator II
 * Difficulty: Medium
 *
 * Description: Evaluate a string expression containing non-negative integers and the operators
 * +, -, *, /. There are no parentheses. Integer division should truncate toward zero. The
 * expression is guaranteed to be valid.
 *
 * Example:
 *   Input: "3+2*2" → Output: 7
 *   Input: " 3/2 " → Output: 1
 *   Input: " 3+5 / 2 " → Output: 5
 *
 * Approach: Scan left to right tracking the current number and last operator sign. On each
 * operator or end of string: '+' pushes num, '-' pushes -num, '*' pops and pushes pop*num,
 * '/' pops and pushes pop/num. This handles operator precedence by deferring + and - to the
 * stack while immediately applying * and /. Sum the stack at the end.
 *
 * Time Complexity: O(n) where n = length of expression
 * Space Complexity: O(n) for the stack
 *
 * Test Cases:
 *   1. "3+2*2" → 7 (* has higher precedence than +)
 *   2. " 3/2 " → 1 (integer truncation)
 *   3. Edge: "14-3/2" → 13 (3/2=1, 14-1=13)
 */
public class BasicCalculatorII {

    public static int calculate(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int num = 0;
        char lastSign = '+';

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }

            // Process operator or last character
            if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
                switch (lastSign) {
                    case '+': stack.push(num);              break;
                    case '-': stack.push(-num);             break;
                    case '*': stack.push(stack.pop() * num); break;
                    case '/': stack.push(stack.pop() / num); break;
                }
                lastSign = c;
                num = 0;
            }
        }

        int result = 0;
        while (!stack.isEmpty()) result += stack.pop();
        return result;
    }

    public static void main(String[] args) {
        // Test Case 1: Multiplication before addition
        System.out.println("calculate(3+2*2)=" + calculate("3+2*2"));      // 7

        // Test Case 2: Integer division truncation
        System.out.println("calculate(3/2)=" + calculate(" 3/2 "));     // 1

        // Test Case 3: Edge - mixed operations
        System.out.println("calculate(14-3/2)=" + calculate("14-3/2"));   // 13

        // Bonus tests
        System.out.println("calculate(3+5/2)=" + calculate(" 3+5 / 2 ")); // 5
        System.out.println("calculate(100000000/1/2/3)=" + calculate("100000000/1/2/3")); // 16666666
    }
}
```

## Complexity

- **Time:** O(n) where n = length of expression
- **Space:** O(n) for the stack
