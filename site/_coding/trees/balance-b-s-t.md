---
layout: problem
title: "Balance B S T"
category: trees
category_display: "Trees"
difficulty: Medium
time_complexity: "O(n) — inorder traversal + tree construction"
space_complexity: "O(n) — sorted list plus recursion stack"
leetcode: 1382
tags: [trees]
render_with_liquid: false
---

## Problem

Balance a Binary Search Tree Given the root of a binary search tree, return a balanced binary search tree with the same node values. A balanced BST is defined as a BST where the depth of the two subtrees of every node never differs by more than 1.

## Approach

First perform an inorder traversal to collect all node values in sorted order into a list. Then recursively build a balanced BST by always selecting the middle element of the current subarray as the root. This ensures the left and right subtrees have equal (or ±1) sizes, producing a height-balanced result in O(n) time.

## Solution

```java
package org.interview.coding.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Balance a Binary Search Tree
 * Difficulty: Medium
 *
 * Description:
 * Given the root of a binary search tree, return a balanced binary search tree with the
 * same node values. A balanced BST is defined as a BST where the depth of the two subtrees
 * of every node never differs by more than 1.
 *
 * Example:
 *   Input: root = [1,null,2,null,3,null,4] (right-skewed)
 *   Output: [2,1,3,null,null,null,4] (balanced)
 *
 * Constraints:
 *   - Number of nodes: [1, 10^4]
 *   - 1 <= Node.val <= 10^5
 *
 * Approach:
 *   First perform an inorder traversal to collect all node values in sorted order into a list.
 *   Then recursively build a balanced BST by always selecting the middle element of the current
 *   subarray as the root. This ensures the left and right subtrees have equal (or ±1) sizes,
 *   producing a height-balanced result in O(n) time.
 *
 * Time Complexity: O(n) — inorder traversal + tree construction
 * Space Complexity: O(n) — sorted list plus recursion stack
 *
 * Test Cases:
 *   1. Input: [1,null,2,null,3,null,4] → Output: balanced BST with root=2 or 3
 *   2. Input: [2,1,3] → Output: already balanced, same structure
 *   3. Edge case: single node → Output: same node
 */
public class BalanceBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public TreeNode balanceBST(TreeNode root) {
        List<Integer> sorted = new ArrayList<>();
        inorder(root, sorted);
        return buildBalanced(sorted, 0, sorted.size() - 1);
    }

    private void inorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.val);
        inorder(node.right, list);
    }

    private TreeNode buildBalanced(List<Integer> sorted, int lo, int hi) {
        if (lo > hi) return null;
        int mid = lo + (hi - lo) / 2;
        TreeNode node = new TreeNode(sorted.get(mid));
        node.left = buildBalanced(sorted, lo, mid - 1);
        node.right = buildBalanced(sorted, mid + 1, hi);
        return node;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public static void main(String[] args) {
        BalanceBST sol = new BalanceBST();

        // Test 1: Right-skewed tree
        TreeNode t1 = new TreeNode(1);
        t1.right = new TreeNode(2);
        t1.right.right = new TreeNode(3);
        t1.right.right.right = new TreeNode(4);
        TreeNode r1 = sol.balanceBST(t1);
        System.out.println("Test 1 root (expect 2 or 3): " + r1.val);
        System.out.println("Test 1 height (expect <=3): " + sol.height(r1));

        // Test 2: Already balanced
        TreeNode t2 = new TreeNode(2);
        t2.left = new TreeNode(1);
        t2.right = new TreeNode(3);
        TreeNode r2 = sol.balanceBST(t2);
        System.out.println("Test 2 root (expect 2): " + r2.val);

        // Test 3: Single node
        TreeNode t3 = new TreeNode(5);
        TreeNode r3 = sol.balanceBST(t3);
        System.out.println("Test 3 (expect 5): " + r3.val);
    }
}
```

## Complexity

- **Time:** O(n) — inorder traversal + tree construction
- **Space:** O(n) — sorted list plus recursion stack
