package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Min Stack
 * Difficulty: Medium
 *
 * Description:
 * Design a stack that supports push, pop, top, and retrieving the minimum element in O(1)
 * time. Implement the MinStack class with these four operations. The stack will always be
 * non-empty when pop(), top(), and getMin() are called.
 *
 * Example:
 *   Input: push(-2), push(0), push(-3), getMin(), pop(), top(), getMin()
 *   Output: -3, 0, -2
 *
 * Constraints:
 *   - -2^31 <= val <= 2^31 - 1
 *   - At most 3 * 10^4 calls to push, pop, top, and getMin
 *
 * Approach:
 *   Use two stacks: the main stack for regular operations, and an auxiliary minStack that
 *   records the current minimum at each state. When pushing, push the new minimum (min of
 *   val and current top of minStack) onto minStack. When popping, pop from both stacks.
 *   getMin() simply peeks the top of minStack.
 *
 * Time Complexity: O(1) for all operations
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. push(-2),push(0),push(-3),getMin() → Output: -3
 *   2. pop(), top() → Output: 0
 *   3. Edge case: getMin() after pop → Output: -2
 */
public class MinStack {

    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Deque<Integer> minStack = new ArrayDeque<>();

    public void push(int val) {
        stack.push(val);
        minStack.push(minStack.isEmpty() ? val : Math.min(val, minStack.peek()));
    }

    public void pop() {
        stack.pop();
        minStack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }

    public static void main(String[] args) {
        MinStack sol = new MinStack();
        // Test 1
        sol.push(-2); sol.push(0); sol.push(-3);
        System.out.println(sol.getMin()); // -3
        // Test 2
        sol.pop();
        System.out.println(sol.top());    // 0
        // Test 3 (edge case: min after pop)
        System.out.println(sol.getMin()); // -2
    }
}
