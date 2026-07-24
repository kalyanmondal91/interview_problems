package org.interview.coding.trees;

/**
 * Problem: Kth Smallest Element in a BST
 * Difficulty: Medium
 *
 * Description:
 * Given the root of a binary search tree and an integer k, return the kth smallest value
 * (1-indexed) of all the values of the nodes in the tree.
 *
 * Example:
 *   Input: root = [3,1,4,null,2], k = 1
 *   Output: 1
 *
 * Constraints:
 *   - Number of nodes n: [1, 10^4]
 *   - 0 <= Node.val <= 10^4
 *   - 1 <= k <= n
 *
 * Approach:
 *   Perform an inorder traversal (left → root → right) of the BST, which visits nodes in
 *   ascending sorted order. Maintain a counter that increments with each visited node.
 *   When the counter reaches k, the current node contains the kth smallest element.
 *   Use iterative approach with a stack for O(h+k) time rather than O(n).
 *
 * Time Complexity: O(h + k) — h is tree height, traverse up to h+k nodes
 * Space Complexity: O(h) — stack stores at most h nodes
 *
 * Test Cases:
 *   1. Input: [3,1,4,null,2], k=1 → Output: 1
 *   2. Input: [5,3,6,2,4,null,null,1], k=3 → Output: 3
 *   3. Edge case: single node, k=1 → Output: that node's value
 */
public class KthSmallestElementBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public int kthSmallest(TreeNode root, int k) {
        java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
        TreeNode curr = root;
        int count = 0;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            count++;
            if (count == k) return curr.val;
            curr = curr.right;
        }
        return -1; // k is out of range
    }

    public static void main(String[] args) {
        KthSmallestElementBST sol = new KthSmallestElementBST();

        // Test 1: [3,1,4,null,2], k=1
        TreeNode t1 = new TreeNode(3);
        t1.left = new TreeNode(1);
        t1.right = new TreeNode(4);
        t1.left.right = new TreeNode(2);
        System.out.println("Test 1 (expect 1): " + sol.kthSmallest(t1, 1));

        // Test 2: [5,3,6,2,4,null,null,1], k=3
        TreeNode t2 = new TreeNode(5);
        t2.left = new TreeNode(3);
        t2.right = new TreeNode(6);
        t2.left.left = new TreeNode(2);
        t2.left.right = new TreeNode(4);
        t2.left.left.left = new TreeNode(1);
        System.out.println("Test 2 (expect 3): " + sol.kthSmallest(t2, 3));

        // Test 3: Single node
        TreeNode t3 = new TreeNode(7);
        System.out.println("Test 3 (expect 7): " + sol.kthSmallest(t3, 1));
    }
}
