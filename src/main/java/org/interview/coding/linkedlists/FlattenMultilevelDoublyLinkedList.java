package org.interview.coding.linkedlists;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Problem: Flatten a Multilevel Doubly Linked List
 * Difficulty: Medium
 *
 * Description:
 * Given a doubly linked list where nodes may have a child pointer to a separate doubly
 * linked list, flatten the list so all nodes appear in a single-level doubly linked list.
 * The child list should appear after the parent node and before the parent's next node.
 *
 * Example:
 *   Input: 1 - 2 - 3 - 4 - 5 - 6 (2 has child 7 - 8 - 9 - 10, 8 has child 11 - 12)
 *   Output: 1 - 2 - 7 - 8 - 11 - 12 - 9 - 10 - 3 - 4 - 5 - 6
 *
 * Constraints:
 *   - The number of nodes in the list is in the range [0, 1000]
 *   - 1 <= Node.val <= 10^5
 *
 * Approach:
 *   Use an iterative approach with a stack. When a node has a child, push its next
 *   onto the stack and connect the node to its child. When a node has no next but the
 *   stack is non-empty, pop from the stack and link it as the node's next (and update
 *   prev pointer). Continue until all nodes are processed.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n) — stack depth proportional to nesting
 *
 * Test Cases:
 *   1. Multi-level list → correctly flattened single level
 *   2. Already flat list → unchanged
 *   3. Edge case: empty list → null
 */
public class FlattenMultilevelDoublyLinkedList {

    static class Node {
        int val;
        Node prev, next, child;
        Node(int v) { val = v; }
    }

    public Node flatten(Node head) {
        if (head == null) return null;

        Deque<Node> stack = new ArrayDeque<>();
        Node curr = head;

        while (curr != null) {
            if (curr.child != null) {
                if (curr.next != null) stack.push(curr.next);
                curr.next = curr.child;
                curr.child.prev = curr;
                curr.child = null;
            }
            if (curr.next == null && !stack.isEmpty()) {
                Node top = stack.pop();
                curr.next = top;
                top.prev = curr;
            }
            curr = curr.next;
        }
        return head;
    }

    public static void main(String[] args) {
        FlattenMultilevelDoublyLinkedList sol = new FlattenMultilevelDoublyLinkedList();
        // Test 1: multilevel list
        Node n1=new Node(1),n2=new Node(2),n3=new Node(3);
        Node n4=new Node(4),n5=new Node(5);
        n1.next=n2; n2.prev=n1;
        n2.next=n3; n3.prev=n2;
        n3.next=n4; n4.prev=n3;
        n4.next=n5; n5.prev=n4;
        n2.child = new Node(7);
        n2.child.next = new Node(8);
        n2.child.next.prev = n2.child;
        Node flat = sol.flatten(n1);
        StringBuilder sb = new StringBuilder();
        while (flat != null) { sb.append(flat.val).append(flat.next != null ? "-" : ""); flat = flat.next; }
        System.out.println(sb); // 1-2-7-8-3-4-5
        // Test 2: null input
        System.out.println(sol.flatten(null)); // null
        // Test 3: single node
        System.out.println(sol.flatten(new Node(1)).val); // 1
    }
}
