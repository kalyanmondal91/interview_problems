package org.interview.coding.trees;

/**
 * Problem: Validate Binary Search Tree
 * Difficulty: Medium
 *
 * Description:
 * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
 * A valid BST is defined as: left subtree contains only nodes with keys less than the node's key,
 * right subtree contains only nodes with keys greater than the node's key,
 * and both left and right subtrees must also be valid BSTs.
 *
 * Example:
 *   Input: [2,1,3]
 *   Output: true
 *
 * Constraints:
 *   - Number of nodes: [1, 10^4]
 *   - -2^31 <= Node.val <= 2^31 - 1
 *
 * Approach:
 *   Pass min and max bounds recursively to each node. For every node, its value must be strictly
 *   greater than min and strictly less than max. When going left, update max to current node value.
 *   When going right, update min to current node value. This ensures all ancestors' constraints
 *   are propagated correctly throughout the tree.
 *
 * Time Complexity: O(n) — visit each node once
 * Space Complexity: O(h) — recursion stack depth h (height of tree)
 *
 * Test Cases:
 *   1. Input: [2,1,3] → Output: true
 *   2. Input: [5,1,4,null,null,3,6] → Output: false (4 is in right subtree but less than 5)
 *   3. Edge case: single node → Output: true
 */
public class ValidateBinarySearchTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }

    // Helper to build tree from array (level-order, -1 = null)
    private static TreeNode buildTree(int[] vals) {
        if (vals == null || vals.length == 0) return null;
        TreeNode[] nodes = new TreeNode[vals.length];
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] != -1) nodes[i] = new TreeNode(vals[i]);
        }
        for (int i = 0; i < vals.length; i++) {
            if (nodes[i] == null) continue;
            int left = 2 * i + 1, right = 2 * i + 2;
            if (left < vals.length) nodes[i].left = nodes[left];
            if (right < vals.length) nodes[i].right = nodes[right];
        }
        return nodes[0];
    }

    public static void main(String[] args) {
        ValidateBinarySearchTree sol = new ValidateBinarySearchTree();

        // Test 1: Valid BST [2,1,3]
        TreeNode t1 = buildTree(new int[]{2, 1, 3});
        System.out.println("Test 1 (expect true): " + sol.isValidBST(t1));

        // Test 2: Invalid BST [5,1,4,null,null,3,6]
        TreeNode t2 = new TreeNode(5);
        t2.left = new TreeNode(1);
        t2.right = new TreeNode(4);
        t2.right.left = new TreeNode(3);
        t2.right.right = new TreeNode(6);
        System.out.println("Test 2 (expect false): " + sol.isValidBST(t2));

        // Test 3: Single node
        TreeNode t3 = new TreeNode(42);
        System.out.println("Test 3 (expect true): " + sol.isValidBST(t3));
    }
}
