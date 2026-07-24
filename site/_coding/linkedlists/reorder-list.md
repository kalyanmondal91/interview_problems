---
layout: problem
title: "Reorder List"
category: linkedlists
category_display: "Linked Lists"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
tags: [linkedlists]
render_with_liquid: false
---

## Problem

Reorder List Given the head of a singly linked-list L0→L1→…→Ln-1→Ln, reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→… You may not modify the node values; only node pointers.

## Approach

Step 1: Find the middle of the list using fast/slow pointers. Step 2: Reverse the second half of the list. Step 3: Merge the two halves by alternately taking one node from each. The first half ends at middle; the reversed second half follows. Each step takes O(n) time and O(1) extra space.

## Solution

```java
package org.interview.coding.linkedlists;

/**
 * Problem: Reorder List
 * Difficulty: Medium
 *
 * Description:
 * Given the head of a singly linked-list L0→L1→…→Ln-1→Ln, reorder it to:
 * L0→Ln→L1→Ln-1→L2→Ln-2→… You may not modify the node values; only node pointers.
 *
 * Example:
 *   Input: head = [1,2,3,4]
 *   Output: [1,4,2,3]
 *
 * Constraints:
 *   - The number of nodes in the list is in the range [1, 5 * 10^4]
 *   - 1 <= Node.val <= 1000
 *
 * Approach:
 *   Step 1: Find the middle of the list using fast/slow pointers. Step 2: Reverse the
 *   second half of the list. Step 3: Merge the two halves by alternately taking one node
 *   from each. The first half ends at middle; the reversed second half follows. Each step
 *   takes O(n) time and O(1) extra space.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,3,4] → Output: [1,4,2,3]
 *   2. Input: [1,2,3,4,5] → Output: [1,5,2,4,3]
 *   3. Edge case: [1] → Output: [1]
 */
public class ReorderList {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        // Step 1: Find middle
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Step 2: Reverse second half
        ListNode prev = null, curr = slow.next;
        slow.next = null;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        // Step 3: Merge
        ListNode first = head, second = prev;
        while (second != null) {
            ListNode tmp1 = first.next, tmp2 = second.next;
            first.next = second;
            second.next = tmp1;
            first = tmp1;
            second = tmp2;
        }
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
        ReorderList sol = new ReorderList();
        // Test 1
        ListNode l1 = build(1, 2, 3, 4); sol.reorderList(l1);
        System.out.println(print(l1)); // [1,4,2,3]
        // Test 2
        ListNode l2 = build(1, 2, 3, 4, 5); sol.reorderList(l2);
        System.out.println(print(l2)); // [1,5,2,4,3]
        // Test 3 (edge case: single node)
        ListNode l3 = build(1); sol.reorderList(l3);
        System.out.println(print(l3)); // [1]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
