package org.interview.coding.linkedlists;

import java.util.HashMap;

/**
 * Problem: Copy List with Random Pointer
 * Difficulty: Medium
 *
 * Description:
 * A linked list is given where each node has an additional random pointer that could point
 * to any node in the list or null. Construct a deep copy of the list. The copy should
 * consist of exactly n brand new nodes where each new node has the same value as its
 * original and correct next and random pointers.
 *
 * Example:
 *   Input: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 *   Output: deep copy with same structure
 *
 * Constraints:
 *   - 0 <= n <= 1000
 *   - -10^4 <= Node.val <= 10^4
 *   - Node.random is null or pointing to some node in the list
 *
 * Approach:
 *   Use a HashMap mapping each original node to its copy. First pass: create all copies
 *   and store the mapping. Second pass: wire up the next and random pointers of each
 *   copy by looking up the corresponding copy in the map. Return the copy of head.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [[7,null],[13,0]] → Output: deep copy with same values
 *   2. Input: [[1,1],[2,1]] → Output: deep copy, verifying independence
 *   3. Edge case: [] → Output: null
 */
public class CopyListWithRandomPointer {

    static class Node {
        int val;
        Node next;
        Node random;
        Node(int v) { val = v; }
    }

    public Node copyRandomList(Node head) {
        if (head == null) return null;

        HashMap<Node, Node> map = new HashMap<>();
        Node curr = head;

        // First pass: create all copies
        while (curr != null) {
            map.put(curr, new Node(curr.val));
            curr = curr.next;
        }

        // Second pass: wire pointers
        curr = head;
        while (curr != null) {
            map.get(curr).next = map.get(curr.next);
            map.get(curr).random = map.get(curr.random);
            curr = curr.next;
        }
        return map.get(head);
    }

    public static void main(String[] args) {
        CopyListWithRandomPointer sol = new CopyListWithRandomPointer();
        // Test 1
        Node n1 = new Node(7), n2 = new Node(13), n3 = new Node(11);
        n1.next = n2; n2.next = n3;
        n1.random = null; n2.random = n1; n3.random = n1;
        Node copy = sol.copyRandomList(n1);
        System.out.println(copy.val + " -> " + copy.next.val + " -> " + copy.next.next.val); // 7->13->11
        System.out.println(copy != n1); // true (different object)
        // Test 2: single node with self-reference
        Node s = new Node(1); s.random = s;
        Node sc = sol.copyRandomList(s);
        System.out.println(sc.random == sc); // true
        // Test 3 (edge case: null)
        System.out.println(sol.copyRandomList(null)); // null
    }
}
