---
layout: problem
title: "Evaluate Reverse Polish Notation"
category: stacks
category_display: "Stacks"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(n)"
tags: [stacks]
render_with_liquid: false
---

## Problem

Evaluate Reverse Polish Notation Evaluate an expression in Reverse Polish Notation (postfix). Valid operators are +, -, *, and /. Each operand may be an integer or another expression. Integer division truncates toward zero.

## Approach

Iterate through tokens with a stack. For each token: if it's an operator, pop two operands (second then first), apply the operator, and push the result. If it's a number, parse and push it. The final result is the single remaining element on the stack. Note: operand order matters for subtraction and division.

## Solution

```java
package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Evaluate Reverse Polish Notation
 * Difficulty: Medium
 *
 * Description:
 * Evaluate an expression in Reverse Polish Notation (postfix). Valid operators are +, -, *,
 * and /. Each operand may be an integer or another expression. Integer division truncates
 * toward zero.
 *
 * Example:
 *   Input: tokens = ["2","1","+","3","*"]
 *   Output: 9  ((2+1)*3 = 9)
 *
 * Constraints:
 *   - 1 <= tokens.length <= 10^4
 *   - tokens[i] is either an operator or an integer in the range [-200, 200]
 *
 * Approach:
 *   Iterate through tokens with a stack. For each token: if it's an operator, pop two
 *   operands (second then first), apply the operator, and push the result. If it's a
 *   number, parse and push it. The final result is the single remaining element on the stack.
 *   Note: operand order matters for subtraction and division.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: ["2","1","+","3","*"] → Output: 9
 *   2. Input: ["4","13","5","/","+"] → Output: 6 (4 + 13/5 = 4+2 = 6)
 *   3. Edge case: ["10","6","9","3","+","-11","*","/","*","17","+","5","+"] → Output: 22
 */
public class EvaluateReversePolishNotation {

    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : tokens) {
            switch (token) {
                case "+": stack.push(stack.pop() + stack.pop()); break;
                case "-": { int b = stack.pop(), a = stack.pop(); stack.push(a - b); break; }
                case "*": stack.push(stack.pop() * stack.pop()); break;
                case "/": { int b = stack.pop(), a = stack.pop(); stack.push(a / b); break; }
                default: stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        EvaluateReversePolishNotation sol = new EvaluateReversePolishNotation();
        // Test 1
        System.out.println(sol.evalRPN(new String[]{"2","1","+","3","*"})); // 9
        // Test 2
        System.out.println(sol.evalRPN(new String[]{"4","13","5","/","+"})); // 6
        // Test 3 (edge case: complex expression)
        System.out.println(sol.evalRPN(new String[]{"10","6","9","3","+","-11","*","/","*","17","+","5","+"})); // 22
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
