---
layout: problem
title: "Reverse Linked List I I"
category: linkedlists
category_display: "Linked Lists"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
tags: [linkedlists]
render_with_liquid: false
---

## Problem

Reverse Linked List II Given the head of a singly linked list and two integers left and right where left <= right, reverse the nodes of the list from position left to position right, and return the reversed list. Positions are 1-indexed.

## Approach

Use a dummy node to simplify edge cases where left == 1. Walk to the node just before position 'left' (call it prev). Then iteratively reverse the sublist from left to right using an insertion-at-front technique: detach the node after 'prev.next', insert it right after 'prev'. Repeat (right - left) times. This is a single pass.

## Solution

```java
package org.interview.coding.linkedlists;

/**
 * Problem: Reverse Linked List II
 * Difficulty: Medium
 *
 * Description:
 * Given the head of a singly linked list and two integers left and right where left <= right,
 * reverse the nodes of the list from position left to position right, and return the reversed
 * list. Positions are 1-indexed.
 *
 * Example:
 *   Input: head = [1,2,3,4,5], left = 2, right = 4
 *   Output: [1,4,3,2,5]
 *
 * Constraints:
 *   - The number of nodes in the list is n
 *   - 1 <= n <= 500
 *   - 1 <= left <= right <= n
 *
 * Approach:
 *   Use a dummy node to simplify edge cases where left == 1. Walk to the node just before
 *   position 'left' (call it prev). Then iteratively reverse the sublist from left to right
 *   using an insertion-at-front technique: detach the node after 'prev.next', insert it
 *   right after 'prev'. Repeat (right - left) times. This is a single pass.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,3,4,5], left=2, right=4 → Output: [1,4,3,2,5]
 *   2. Input: [5], left=1, right=1 → Output: [5]
 *   3. Edge case: [1,2,3], left=1, right=3 → Output: [3,2,1]
 */
public class ReverseLinkedListII {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;

        for (int i = 0; i < left - 1; i++) prev = prev.next;

        ListNode curr = prev.next;
        for (int i = 0; i < right - left; i++) {
            ListNode next = curr.next;
            curr.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        return dummy.next;
    }

    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    private static String print(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) { sb.append(head.val); if (head.next != null) sb.append(","); head = head.next; }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        ReverseLinkedListII sol = new ReverseLinkedListII();
        // Test 1
        System.out.println(print(sol.reverseBetween(build(1,2,3,4,5), 2, 4))); // [1,4,3,2,5]
        // Test 2
        System.out.println(print(sol.reverseBetween(build(5), 1, 1)));          // [5]
        // Test 3 (edge case: reverse entire list)
        System.out.println(print(sol.reverseBetween(build(1,2,3), 1, 3)));      // [3,2,1]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
