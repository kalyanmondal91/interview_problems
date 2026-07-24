package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Basic Calculator
 * Difficulty: Hard
 *
 * Description:
 * Given a string s representing a valid mathematical expression containing digits, '+', '-',
 * '(', ')' and spaces, evaluate and return the result of the expression. The expression is
 * always valid. No multiplication or division.
 *
 * Example:
 *   Input: s = "(1+(4+5+2)-3)+(6+8)"
 *   Output: 23
 *
 * Constraints:
 *   - 1 <= s.length <= 3 * 10^5
 *   - s consists of digits, '+', '-', '(', ')', and ' '
 *   - s is a valid expression
 *
 * Approach:
 *   Use a stack to handle parentheses. Maintain a running result, current number, and sign
 *   (+1 or -1). When encountering '(', push the current result and sign onto the stack,
 *   reset them. When encountering ')', compute sub-expression and combine with saved values.
 *   Digits are accumulated into the current number, applied when a sign/parenthesis is met.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: "1 + 1" → Output: 2
 *   2. Input: " 2-1 + 2 " → Output: 3
 *   3. Edge case: "(1+(4+5+2)-3)+(6+8)" → Output: 23
 */
public class BasicCalculator {

    public int calculate(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int result = 0, num = 0, sign = 1;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '+') {
                result += sign * num;
                num = 0; sign = 1;
            } else if (c == '-') {
                result += sign * num;
                num = 0; sign = -1;
            } else if (c == '(') {
                stack.push(result);
                stack.push(sign);
                result = 0; sign = 1;
            } else if (c == ')') {
                result += sign * num;
                num = 0;
                result *= stack.pop(); // sign before '('
                result += stack.pop(); // result before '('
            }
        }
        return result + sign * num;
    }

    public static void main(String[] args) {
        BasicCalculator sol = new BasicCalculator();
        // Test 1
        System.out.println(sol.calculate("1 + 1"));                // 2
        // Test 2
        System.out.println(sol.calculate(" 2-1 + 2 "));            // 3
        // Test 3 (edge case: nested parentheses)
        System.out.println(sol.calculate("(1+(4+5+2)-3)+(6+8)")); // 23
    }
}
