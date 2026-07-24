package org.interview.coding.linkedlists;

/**
 * Problem: Palindrome Linked List
 * Difficulty: Easy
 *
 * Description:
 * Given the head of a singly linked list, return true if it is a palindrome, or false
 * otherwise. The algorithm should run in O(n) time and O(1) space.
 *
 * Example:
 *   Input: head = [1,2,2,1]
 *   Output: true
 *
 * Constraints:
 *   - The number of nodes in the list is in the range [1, 10^5]
 *   - 0 <= Node.val <= 9
 *
 * Approach:
 *   Step 1: Find the middle of the list using fast/slow pointers. Step 2: Reverse the
 *   second half of the list. Step 3: Compare the first half with the reversed second half
 *   node by node. If all values match, it's a palindrome. Optionally restore the list
 *   after checking.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,2,1] → Output: true
 *   2. Input: [1,2] → Output: false
 *   3. Edge case: [1] → Output: true (single node is palindrome)
 */
public class PalindromeLinkedList {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
    }

    public boolean isPalindrome(ListNode head) {
        // Find middle
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Reverse second half
        ListNode prev = null, curr = slow;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        // Compare
        ListNode left = head, right = prev;
        while (right != null) {
            if (left.val != right.val) return false;
            left = left.next;
            right = right.next;
        }
        return true;
    }

    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int v : vals) { cur.next = new ListNode(v); cur = cur.next; }
        return dummy.next;
    }

    public static void main(String[] args) {
        PalindromeLinkedList sol = new PalindromeLinkedList();
        // Test 1
        System.out.println(sol.isPalindrome(build(1, 2, 2, 1))); // true
        // Test 2
        System.out.println(sol.isPalindrome(build(1, 2)));        // false
        // Test 3 (edge case: single node)
        System.out.println(sol.isPalindrome(build(1)));           // true
    }
}
