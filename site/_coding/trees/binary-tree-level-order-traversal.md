---
layout: problem
title: "Binary Tree Level Order Traversal"
category: trees
category_display: "Trees"
difficulty: Medium
time_complexity: "O(n) — visit each node once"
space_complexity: "O(w) — w is max width of tree (queue size)"
leetcode: 102
tags: [trees]
render_with_liquid: false
---

## Problem

Binary Tree Level Order Traversal Given the root of a binary tree, return the level order traversal of its nodes' values (i.e., from left to right, level by level). Each level's nodes should be grouped into a separate list.

## Approach

Use BFS with a queue. Start by adding root to the queue. In each iteration, record the current queue size (number of nodes at this level), then process exactly that many nodes. For each node dequeued, add its value to the current level list and enqueue its non-null children. After processing all nodes at a level, add the level list to the result.

## Solution

```java
package org.interview.coding.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Problem: Binary Tree Level Order Traversal
 * Difficulty: Medium
 *
 * Description:
 * Given the root of a binary tree, return the level order traversal of its nodes' values
 * (i.e., from left to right, level by level). Each level's nodes should be grouped
 * into a separate list.
 *
 * Example:
 *   Input: root = [3,9,20,null,null,15,7]
 *   Output: [[3],[9,20],[15,7]]
 *
 * Constraints:
 *   - Number of nodes: [0, 2000]
 *   - -1000 <= Node.val <= 1000
 *
 * Approach:
 *   Use BFS with a queue. Start by adding root to the queue. In each iteration, record the
 *   current queue size (number of nodes at this level), then process exactly that many nodes.
 *   For each node dequeued, add its value to the current level list and enqueue its non-null
 *   children. After processing all nodes at a level, add the level list to the result.
 *
 * Time Complexity: O(n) — visit each node once
 * Space Complexity: O(w) — w is max width of tree (queue size)
 *
 * Test Cases:
 *   1. Input: [3,9,20,null,null,15,7] → Output: [[3],[9,20],[15,7]]
 *   2. Input: [1] → Output: [[1]]
 *   3. Edge case: empty tree → Output: []
 */
public class BinaryTreeLevelOrderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    public static void main(String[] args) {
        BinaryTreeLevelOrderTraversal sol = new BinaryTreeLevelOrderTraversal();

        // Test 1: [3,9,20,null,null,15,7]
        TreeNode t1 = new TreeNode(3);
        t1.left = new TreeNode(9);
        t1.right = new TreeNode(20);
        t1.right.left = new TreeNode(15);
        t1.right.right = new TreeNode(7);
        System.out.println("Test 1 (expect [[3],[9,20],[15,7]]): " + sol.levelOrder(t1));

        // Test 2: single node
        TreeNode t2 = new TreeNode(1);
        System.out.println("Test 2 (expect [[1]]): " + sol.levelOrder(t2));

        // Test 3: empty tree
        System.out.println("Test 3 (expect []): " + sol.levelOrder(null));
    }
}
```

## Complexity

- **Time:** O(n) — visit each node once
- **Space:** O(w) — w is max width of tree (queue size)
