---
layout: problem
title: "Merge K Sorted Lists"
category: linkedlists
category_display: "Linked Lists"
difficulty: Hard
time_complexity: "O(n log k)"
space_complexity: "O(k)"
leetcode: 23
tags: [linkedlists]
render_with_liquid: false
---

## Problem

Merge K Sorted Lists Given an array of k linked-list heads, each linked list is sorted in ascending order. Merge all the linked lists into one sorted linked list and return its head.

## Approach

Use a min-heap (PriorityQueue) that always contains at most k elements — one from each list. Initialize by adding the head of each non-null list. Extract the minimum node, attach it to the result list, and push its next node (if any) back into the heap. This runs in O(n log k) time where n is the total number of nodes.

## Solution

```java
package org.interview.coding.linkedlists;

import java.util.PriorityQueue;

/**
 * Problem: Merge K Sorted Lists
 * Difficulty: Hard
 *
 * Description:
 * Given an array of k linked-list heads, each linked list is sorted in ascending order.
 * Merge all the linked lists into one sorted linked list and return its head.
 *
 * Example:
 *   Input: lists = [[1,4,5],[1,3,4],[2,6]]
 *   Output: [1,1,2,3,4,4,5,6]
 *
 * Constraints:
 *   - k == lists.length
 *   - 0 <= k <= 10^4
 *   - 0 <= lists[i].length <= 500
 *   - -10^4 <= lists[i][j] <= 10^4
 *
 * Approach:
 *   Use a min-heap (PriorityQueue) that always contains at most k elements — one from each
 *   list. Initialize by adding the head of each non-null list. Extract the minimum node,
 *   attach it to the result list, and push its next node (if any) back into the heap.
 *   This runs in O(n log k) time where n is the total number of nodes.
 *
 * Time Complexity: O(n log k)
 * Space Complexity: O(k)
 *
 * Test Cases:
 *   1. Input: [[1,4,5],[1,3,4],[2,6]] → Output: [1,1,2,3,4,4,5,6]
 *   2. Input: [] → Output: []
 *   3. Edge case: [[]] → Output: []
 */
public class MergeKSortedLists {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode node : lists) if (node != null) pq.offer(node);

        ListNode dummy = new ListNode(0), curr = dummy;
        while (!pq.isEmpty()) {
            ListNode min = pq.poll();
            curr.next = min;
            curr = curr.next;
            if (min.next != null) pq.offer(min.next);
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
        MergeKSortedLists sol = new MergeKSortedLists();
        // Test 1
        ListNode[] lists1 = {build(1,4,5), build(1,3,4), build(2,6)};
        System.out.println(print(sol.mergeKLists(lists1))); // [1,1,2,3,4,4,5,6]
        // Test 2
        System.out.println(print(sol.mergeKLists(new ListNode[]{}))); // []
        // Test 3 (edge case: list with empty node)
        System.out.println(print(sol.mergeKLists(new ListNode[]{null}))); // []
    }
}
```

## Complexity

- **Time:** O(n log k)
- **Space:** O(k)
