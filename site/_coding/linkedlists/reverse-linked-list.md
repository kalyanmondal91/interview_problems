---
layout: problem
title: "Reverse Linked List"
category: linkedlists
category_display: "Linked Lists"
difficulty: Easy
time_complexity: "O(n)"
space_complexity: "O(1)"
leetcode: 206
tags: [linkedlists]
render_with_liquid: false
---

## Problem

Reverse Linked List Given the head of a singly linked list, reverse the list and return the reversed list's head. The reversal should be done iteratively in-place without allocating extra space for a new list.

## Approach

Maintain three pointers: prev (initially null), curr (initially head), and next. In each iteration, save curr.next in next, point curr.next to prev, advance prev to curr, and advance curr to next. After the loop, prev points to the new head. This processes each node exactly once.

## Solution

```java
package org.interview.coding.linkedlists;

/**
 * Problem: Reverse Linked List
 * Difficulty: Easy
 *
 * Description:
 * Given the head of a singly linked list, reverse the list and return the reversed list's
 * head. The reversal should be done iteratively in-place without allocating extra space
 * for a new list.
 *
 * Example:
 *   Input: head = [1,2,3,4,5]
 *   Output: [5,4,3,2,1]
 *
 * Constraints:
 *   - The number of nodes in the list is in the range [0, 5000]
 *   - -5000 <= Node.val <= 5000
 *
 * Approach:
 *   Maintain three pointers: prev (initially null), curr (initially head), and next.
 *   In each iteration, save curr.next in next, point curr.next to prev, advance prev to
 *   curr, and advance curr to next. After the loop, prev points to the new head. This
 *   processes each node exactly once.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,3,4,5] → Output: [5,4,3,2,1]
 *   2. Input: [1,2] → Output: [2,1]
 *   3. Edge case: [] → Output: []
 */
public class ReverseLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }

    public ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
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
        ReverseLinkedList sol = new ReverseLinkedList();
        // Test 1
        System.out.println(print(sol.reverseList(build(1,2,3,4,5)))); // [5,4,3,2,1]
        // Test 2
        System.out.println(print(sol.reverseList(build(1,2))));        // [2,1]
        // Test 3 (edge case: empty list)
        System.out.println(print(sol.reverseList(null)));              // []
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
