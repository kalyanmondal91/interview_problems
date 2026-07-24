package org.interview.coding.trees;

/**
 * Problem: Binary Tree Maximum Path Sum
 * Difficulty: Hard
 *
 * Description:
 * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes has an edge.
 * A node can only appear in the path at most once. The path does not need to pass through root.
 * Given the root of a binary tree, return the maximum path sum of any non-empty path.
 *
 * Example:
 *   Input: root = [-10,9,20,null,null,15,7]
 *   Output: 42 (path: 15 -> 20 -> 7)
 *
 * Constraints:
 *   - Number of nodes: [1, 3*10^4]
 *   - -1000 <= Node.val <= 1000
 *
 * Approach:
 *   Use recursive DFS. For each node, compute the maximum gain from its left and right subtrees
 *   (ignoring negative gains by taking max with 0). The local path sum through the current node
 *   is node.val + leftGain + rightGain; update the global maximum with this value. Return only
 *   the best single-branch gain (node.val + max(leftGain, rightGain)) to the parent, since a
 *   path can only branch once.
 *
 * Time Complexity: O(n) — visit each node once
 * Space Complexity: O(h) — recursion stack height
 *
 * Test Cases:
 *   1. Input: [1,2,3] → Output: 6 (2+1+3)
 *   2. Input: [-10,9,20,null,null,15,7] → Output: 42 (15+20+7)
 *   3. Edge case: all negative [-3,-2,-1] → Output: -1 (best single node)
 */
public class BinaryTreeMaximumPathSum {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private int maxSum;

    public int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        dfs(root);
        return maxSum;
    }

    private int dfs(TreeNode node) {
        if (node == null) return 0;
        int leftGain = Math.max(dfs(node.left), 0);
        int rightGain = Math.max(dfs(node.right), 0);
        maxSum = Math.max(maxSum, node.val + leftGain + rightGain);
        return node.val + Math.max(leftGain, rightGain);
    }

    public static void main(String[] args) {
        BinaryTreeMaximumPathSum sol = new BinaryTreeMaximumPathSum();

        // Test 1: [1,2,3]
        TreeNode t1 = new TreeNode(1);
        t1.left = new TreeNode(2);
        t1.right = new TreeNode(3);
        System.out.println("Test 1 (expect 6): " + sol.maxPathSum(t1));

        // Test 2: [-10,9,20,null,null,15,7]
        TreeNode t2 = new TreeNode(-10);
        t2.left = new TreeNode(9);
        t2.right = new TreeNode(20);
        t2.right.left = new TreeNode(15);
        t2.right.right = new TreeNode(7);
        System.out.println("Test 2 (expect 42): " + sol.maxPathSum(t2));

        // Test 3: all negative
        TreeNode t3 = new TreeNode(-3);
        t3.left = new TreeNode(-2);
        t3.right = new TreeNode(-1);
        System.out.println("Test 3 (expect -1): " + sol.maxPathSum(t3));
    }
}
