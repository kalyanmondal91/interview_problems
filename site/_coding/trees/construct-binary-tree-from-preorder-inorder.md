---
layout: problem
title: "Construct Binary Tree From Preorder Inorder"
category: trees
category_display: "Trees"
difficulty: Medium
time_complexity: "O(n) — each node processed once with O(1) index lookup"
space_complexity: "O(n) — HashMap plus recursion stack"
tags: [trees]
render_with_liquid: false
---

## Problem

Construct Binary Tree from Preorder and Inorder Traversal Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.

## Approach

The first element of preorder is always the root of the current subtree. Find this root in the inorder array — everything to its left forms the left subtree and everything to its right forms the right subtree. Use a HashMap to store inorder indices for O(1) lookup. Recursively build left and right subtrees using the computed index ranges.

## Solution

```java
package org.interview.coding.trees;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Construct Binary Tree from Preorder and Inorder Traversal
 * Difficulty: Medium
 *
 * Description:
 * Given two integer arrays preorder and inorder where preorder is the preorder traversal
 * of a binary tree and inorder is the inorder traversal of the same tree, construct and
 * return the binary tree.
 *
 * Example:
 *   Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 *   Output: [3,9,20,null,null,15,7]
 *
 * Constraints:
 *   - 1 <= preorder.length <= 3000
 *   - preorder.length == inorder.length
 *   - All values are unique
 *
 * Approach:
 *   The first element of preorder is always the root of the current subtree. Find this root
 *   in the inorder array — everything to its left forms the left subtree and everything to
 *   its right forms the right subtree. Use a HashMap to store inorder indices for O(1) lookup.
 *   Recursively build left and right subtrees using the computed index ranges.
 *
 * Time Complexity: O(n) — each node processed once with O(1) index lookup
 * Space Complexity: O(n) — HashMap plus recursion stack
 *
 * Test Cases:
 *   1. Input: preorder=[3,9,20,15,7], inorder=[9,3,15,20,7] → root=3
 *   2. Input: preorder=[1,2], inorder=[2,1] → root.left=2
 *   3. Edge case: single element → single node tree
 */
public class ConstructBinaryTreeFromPreorderInorder {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private Map<Integer, Integer> inorderIndex = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            inorderIndex.put(inorder[i], i);
        }
        return build(preorder, 0, preorder.length - 1, 0, inorder.length - 1);
    }

    private TreeNode build(int[] preorder, int preStart, int preEnd, int inStart, int inEnd) {
        if (preStart > preEnd) return null;
        int rootVal = preorder[preStart];
        TreeNode root = new TreeNode(rootVal);
        int inRoot = inorderIndex.get(rootVal);
        int leftSize = inRoot - inStart;
        root.left = build(preorder, preStart + 1, preStart + leftSize, inStart, inRoot - 1);
        root.right = build(preorder, preStart + leftSize + 1, preEnd, inRoot + 1, inEnd);
        return root;
    }

    private String levelOrder(TreeNode root) {
        if (root == null) return "null";
        StringBuilder sb = new StringBuilder("[");
        java.util.Queue<TreeNode> q = new java.util.LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            if (n == null) { sb.append("null,"); continue; }
            sb.append(n.val).append(",");
            q.offer(n.left);
            q.offer(n.right);
        }
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        ConstructBinaryTreeFromPreorderInorder sol = new ConstructBinaryTreeFromPreorderInorder();

        // Test 1
        TreeNode t1 = sol.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        System.out.println("Test 1 root (expect 3): " + t1.val);
        System.out.println("Test 1 level order: " + sol.levelOrder(t1));

        // Test 2
        sol.inorderIndex.clear();
        TreeNode t2 = sol.buildTree(new int[]{1, 2}, new int[]{2, 1});
        System.out.println("Test 2 root.left (expect 2): " + t2.left.val);

        // Test 3: single element
        sol.inorderIndex.clear();
        TreeNode t3 = sol.buildTree(new int[]{5}, new int[]{5});
        System.out.println("Test 3 (expect 5): " + t3.val);
    }
}
```

## Complexity

- **Time:** O(n) — each node processed once with O(1) index lookup
- **Space:** O(n) — HashMap plus recursion stack
