package org.interview.coding.trees;

/**
 * Problem: Diameter of Binary Tree
 * Difficulty: Easy
 *
 * Description:
 * Given the root of a binary tree, return the length of the diameter of the tree.
 * The diameter of a binary tree is the length of the longest path between any two nodes.
 * This path may or may not pass through the root. The length is the number of edges.
 *
 * Example:
 *   Input: root = [1,2,3,4,5]
 *   Output: 3 (path 4->2->1->3 or 5->2->1->3)
 *
 * Constraints:
 *   - Number of nodes: [1, 10^4]
 *   - -100 <= Node.val <= 100
 *
 * Approach:
 *   Use a recursive DFS that returns the depth of each subtree. At each node, the diameter
 *   contribution through that node equals leftDepth + rightDepth. Maintain a global maximum
 *   that is updated at every node. The function returns the maximum depth from the current
 *   node downward (1 + max(leftDepth, rightDepth)) so the parent can compute its own diameter.
 *
 * Time Complexity: O(n) — visit each node once
 * Space Complexity: O(h) — recursion stack depth
 *
 * Test Cases:
 *   1. Input: [1,2,3,4,5] → Output: 3
 *   2. Input: [1,2] → Output: 1
 *   3. Edge case: single node → Output: 0
 */
public class DiameterOfBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private int diameter;

    public int diameterOfBinaryTree(TreeNode root) {
        diameter = 0;
        depth(root);
        return diameter;
    }

    private int depth(TreeNode node) {
        if (node == null) return 0;
        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);
        diameter = Math.max(diameter, leftDepth + rightDepth);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    public static void main(String[] args) {
        DiameterOfBinaryTree sol = new DiameterOfBinaryTree();

        // Test 1: [1,2,3,4,5]
        TreeNode t1 = new TreeNode(1);
        t1.left = new TreeNode(2);
        t1.right = new TreeNode(3);
        t1.left.left = new TreeNode(4);
        t1.left.right = new TreeNode(5);
        System.out.println("Test 1 (expect 3): " + sol.diameterOfBinaryTree(t1));

        // Test 2: [1,2]
        TreeNode t2 = new TreeNode(1);
        t2.left = new TreeNode(2);
        System.out.println("Test 2 (expect 1): " + sol.diameterOfBinaryTree(t2));

        // Test 3: single node
        TreeNode t3 = new TreeNode(1);
        System.out.println("Test 3 (expect 0): " + sol.diameterOfBinaryTree(t3));
    }
}
