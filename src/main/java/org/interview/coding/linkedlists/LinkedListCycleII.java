package org.interview.coding.linkedlists;

/**
 * Problem: Linked List Cycle II
 * Difficulty: Medium
 *
 * Description:
 * Given the head of a linked list, return the node where the cycle begins. If there is no
 * cycle, return null. The cycle is detected without modifying the linked list.
 *
 * Example:
 *   Input: head = [3,2,0,-4], pos = 1
 *   Output: node with value 2
 *
 * Constraints:
 *   - The number of nodes in the list is in the range [0, 10^4]
 *   - -10^5 <= Node.val <= 10^5
 *
 * Approach:
 *   Use Floyd's two-pointer algorithm. Move slow by 1 and fast by 2. If they meet, a cycle
 *   exists. To find the cycle start, reset one pointer to head. Now advance both pointers
 *   by 1 at a time; they will meet exactly at the cycle entry node. This works due to the
 *   mathematical relationship between distances in the cycle.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [3,2,0,-4] with cycle at pos=1 → Output: node with val=2
 *   2. Input: [1,2] with cycle at pos=0 → Output: node with val=1
 *   3. Edge case: [1] with no cycle → Output: null
 */
public class LinkedListCycleII {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;

        // Phase 1: Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                // Phase 2: Find cycle start
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        LinkedListCycleII sol = new LinkedListCycleII();
        // Test 1: cycle at index 1
        ListNode n1 = new ListNode(3), n2 = new ListNode(2), n3 = new ListNode(0), n4 = new ListNode(-4);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n2; // cycle back to n2
        System.out.println(sol.detectCycle(n1).val); // 2

        // Test 2: cycle at index 0
        ListNode m1 = new ListNode(1), m2 = new ListNode(2);
        m1.next = m2; m2.next = m1;
        System.out.println(sol.detectCycle(m1).val); // 1

        // Test 3 (edge case: no cycle)
        ListNode p = new ListNode(1);
        System.out.println(sol.detectCycle(p)); // null
    }
}
