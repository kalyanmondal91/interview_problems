package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Implement Queue using Stacks
 * Difficulty: Easy
 *
 * Description:
 * Implement a first-in-first-out (FIFO) queue using only two stacks. The queue should
 * support push, pop, peek, and empty operations. Using only standard stack operations
 * (push to top, peek/pop from top, size, and is empty) is allowed.
 *
 * Example:
 *   Input: push(1), push(2), peek(), pop(), empty()
 *   Output: 1, 1, false
 *
 * Constraints:
 *   - 1 <= x <= 9 for push
 *   - At most 100 calls will be made to push, pop, peek, and empty
 *   - Guaranteed that pop and peek are called on non-empty queues
 *
 * Approach:
 *   Use two stacks: pushStack and popStack. Push always goes to pushStack. For pop/peek,
 *   if popStack is empty, transfer all elements from pushStack to popStack (reversing order).
 *   Then serve from popStack. This gives amortized O(1) for all operations since each
 *   element is moved at most once.
 *
 * Time Complexity: O(1) amortized for all operations
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. push(1), push(2), peek() → Output: 1
 *   2. pop() → Output: 1
 *   3. Edge case: empty() → Output: false (still has 2)
 */
public class ImplementQueueUsingStacks {

    private final Deque<Integer> pushStack = new ArrayDeque<>();
    private final Deque<Integer> popStack = new ArrayDeque<>();

    public void push(int x) {
        pushStack.push(x);
    }

    public int pop() {
        move();
        return popStack.pop();
    }

    public int peek() {
        move();
        return popStack.peek();
    }

    public boolean empty() {
        return pushStack.isEmpty() && popStack.isEmpty();
    }

    private void move() {
        if (popStack.isEmpty()) {
            while (!pushStack.isEmpty()) popStack.push(pushStack.pop());
        }
    }

    public static void main(String[] args) {
        ImplementQueueUsingStacks sol = new ImplementQueueUsingStacks();
        // Test 1
        sol.push(1); sol.push(2);
        System.out.println(sol.peek()); // 1
        // Test 2
        System.out.println(sol.pop());  // 1
        // Test 3 (edge case: check non-empty)
        System.out.println(sol.empty()); // false
    }
}
