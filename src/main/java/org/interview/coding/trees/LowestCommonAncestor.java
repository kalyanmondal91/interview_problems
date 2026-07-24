package org.interview.coding.trees;

/**
 * Problem: Lowest Common Ancestor of a Binary Tree
 * Difficulty: Medium
 *
 * Description:
 * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes p and q.
 * The LCA is defined as the lowest node in the tree that has both p and q as descendants
 * (where we allow a node to be a descendant of itself).
 *
 * Example:
 *   Input: root=[3,5,1,6,2,0,8], p=5, q=1
 *   Output: 3
 *
 * Constraints:
 *   - Number of nodes: [2, 10^5]
 *   - All node values are unique
 *   - p != q, both p and q exist in the tree
 *
 * Approach:
 *   Use recursive DFS. If the current node is null, return null. If the current node equals p or q,
 *   return the current node (it could be the ancestor). Recurse into left and right subtrees.
 *   If both left and right return non-null results, the current node is the LCA (p and q are in
 *   different subtrees). Otherwise, return whichever side is non-null.
 *
 * Time Complexity: O(n) — visit each node once
 * Space Complexity: O(h) — recursion stack depth
 *
 * Test Cases:
 *   1. Input: tree=[3,5,1], p=5, q=1 → Output: 3
 *   2. Input: tree=[3,5,1,6,2], p=5, q=6 → Output: 5 (ancestor of itself)
 *   3. Edge case: p and q are root's direct children → Output: root
 */
public class LowestCommonAncestor {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root;
        return left != null ? left : right;
    }

    public static void main(String[] args) {
        LowestCommonAncestor sol = new LowestCommonAncestor();

        // Test 1: LCA of 5 and 1 in [3,5,1,6,2,0,8]
        TreeNode root1 = new TreeNode(3);
        TreeNode p1 = new TreeNode(5);
        TreeNode q1 = new TreeNode(1);
        root1.left = p1;
        root1.right = q1;
        p1.left = new TreeNode(6);
        p1.right = new TreeNode(2);
        q1.left = new TreeNode(0);
        q1.right = new TreeNode(8);
        System.out.println("Test 1 (expect 3): " + sol.lowestCommonAncestor(root1, p1, q1).val);

        // Test 2: LCA of 5 and 6 (ancestor of itself)
        TreeNode root2 = new TreeNode(3);
        TreeNode p2 = new TreeNode(5);
        TreeNode q2 = new TreeNode(6);
        root2.left = p2;
        p2.left = q2;
        p2.right = new TreeNode(2);
        System.out.println("Test 2 (expect 5): " + sol.lowestCommonAncestor(root2, p2, q2).val);

        // Test 3: LCA when p and q are root's direct children
        TreeNode root3 = new TreeNode(1);
        TreeNode p3 = new TreeNode(2);
        TreeNode q3 = new TreeNode(3);
        root3.left = p3;
        root3.right = q3;
        System.out.println("Test 3 (expect 1): " + sol.lowestCommonAncestor(root3, p3, q3).val);
    }
}
