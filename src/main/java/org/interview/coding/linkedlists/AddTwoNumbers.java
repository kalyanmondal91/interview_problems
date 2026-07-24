package org.interview.coding.linkedlists;

/**
 * Problem: Add Two Numbers
 * Difficulty: Medium
 *
 * Description:
 * Given two non-empty linked lists representing two non-negative integers stored in
 * reverse order (each node contains a single digit), add the two numbers and return
 * the sum as a linked list, also in reverse order.
 *
 * Example:
 *   Input: l1 = [2,4,3], l2 = [5,6,4]
 *   Output: [7,0,8]  (342 + 465 = 807)
 *
 * Constraints:
 *   - The number of nodes in each list is in the range [1, 100]
 *   - 0 <= Node.val <= 9
 *   - It is guaranteed that the list represents a number that does not have leading zeros
 *
 * Approach:
 *   Simulate digit-by-digit addition using a carry variable. Iterate both lists, summing
 *   corresponding digits plus carry. Each sum % 10 is the new digit, sum / 10 is the new
 *   carry. Continue until both lists are exhausted AND carry is 0. Use a dummy head to
 *   simplify pointer management.
 *
 * Time Complexity: O(max(m, n))
 * Space Complexity: O(max(m, n))
 *
 * Test Cases:
 *   1. Input: [2,4,3], [5,6,4] → Output: [7,0,8] (342 + 465 = 807)
 *   2. Input: [0], [0] → Output: [0]
 *   3. Edge case: [9,9,9,9], [9,9,9] → Output: [8,9,9,0,1] (9999 + 999 = 10998)
 */
public class AddTwoNumbers {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), curr = dummy;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) { sum += l1.val; l1 = l1.next; }
            if (l2 != null) { sum += l2.val; l2 = l2.next; }
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
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
        AddTwoNumbers sol = new AddTwoNumbers();
        // Test 1
        System.out.println(print(sol.addTwoNumbers(build(2,4,3), build(5,6,4)))); // [7,0,8]
        // Test 2
        System.out.println(print(sol.addTwoNumbers(build(0), build(0))));          // [0]
        // Test 3 (edge case: carry propagation)
        System.out.println(print(sol.addTwoNumbers(build(9,9,9,9), build(9,9,9)))); // [8,9,9,0,1]
    }
}
