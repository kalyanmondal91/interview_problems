---
layout: problem
title: "Merge Two Sorted Lists"
category: linkedlists
category_display: "Linked Lists"
difficulty: Easy
time_complexity: "O(n + m)"
space_complexity: "O(1)"
tags: [linkedlists]
render_with_liquid: false
---

## Problem

Merge Two Sorted Lists Given the heads of two sorted linked lists list1 and list2, merge them into a single sorted linked list and return its head. The merged list should be made by splicing together the nodes of the two original lists.

## Approach

Use a dummy head node to avoid special-casing the first node. Maintain a current pointer starting at dummy. While both lists have nodes, compare their values and attach the smaller one to current.next, advancing that list's pointer. After the loop, attach the remaining nodes of whichever list is non-empty.

## Solution

```java
package org.interview.coding.linkedlists;

/**
 * Problem: Merge Two Sorted Lists
 * Difficulty: Easy
 *
 * Description:
 * Given the heads of two sorted linked lists list1 and list2, merge them into a single
 * sorted linked list and return its head. The merged list should be made by splicing
 * together the nodes of the two original lists.
 *
 * Example:
 *   Input: list1 = [1,2,4], list2 = [1,3,4]
 *   Output: [1,1,2,3,4,4]
 *
 * Constraints:
 *   - The number of nodes in both lists is in the range [0, 50]
 *   - -100 <= Node.val <= 100
 *   - Both lists are sorted in non-decreasing order
 *
 * Approach:
 *   Use a dummy head node to avoid special-casing the first node. Maintain a current
 *   pointer starting at dummy. While both lists have nodes, compare their values and
 *   attach the smaller one to current.next, advancing that list's pointer. After the
 *   loop, attach the remaining nodes of whichever list is non-empty.
 *
 * Time Complexity: O(n + m)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,4], [1,3,4] → Output: [1,1,2,3,4,4]
 *   2. Input: [], [] → Output: []
 *   3. Edge case: [], [0] → Output: [0]
 */
public class MergeTwoSortedLists {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0), curr = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) { curr.next = list1; list1 = list1.next; }
            else { curr.next = list2; list2 = list2.next; }
            curr = curr.next;
        }
        curr.next = (list1 != null) ? list1 : list2;
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
        MergeTwoSortedLists sol = new MergeTwoSortedLists();
        // Test 1
        System.out.println(print(sol.mergeTwoLists(build(1,2,4), build(1,3,4)))); // [1,1,2,3,4,4]
        // Test 2
        System.out.println(print(sol.mergeTwoLists(null, null)));                  // []
        // Test 3 (edge case: one empty list)
        System.out.println(print(sol.mergeTwoLists(null, build(0))));             // [0]
    }
}
```

## Complexity

- **Time:** O(n + m)
- **Space:** O(1)
