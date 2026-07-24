---
layout: problem
title: "Binary Tree Right Side View"
category: trees
category_display: "Trees"
difficulty: Medium
time_complexity: "O(n) — visit each node once"
space_complexity: "O(w) — w is maximum width of the tree"
tags: [trees]
render_with_liquid: false
---

## Problem

Binary Tree Right Side View Given the root of a binary tree, imagine yourself standing on the right side of it. Return the values of the nodes you can see ordered from top to bottom. A node is visible if it is the rightmost node at its level.

## Approach

Use BFS (level-order traversal) with a queue. At each level, process all nodes at that level and record the last node's value — this is the rightmost node visible from the right side. Add this value to the result list. This gives a clean O(n) solution without needing to track depth separately.

## Solution

```java
package org.interview.coding.trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Problem: Binary Tree Right Side View
 * Difficulty: Medium
 *
 * Description:
 * Given the root of a binary tree, imagine yourself standing on the right side of it.
 * Return the values of the nodes you can see ordered from top to bottom.
 * A node is visible if it is the rightmost node at its level.
 *
 * Example:
 *   Input: root = [1,2,3,null,5,null,4]
 *   Output: [1,3,4]
 *
 * Constraints:
 *   - Number of nodes: [0, 100]
 *   - -100 <= Node.val <= 100
 *
 * Approach:
 *   Use BFS (level-order traversal) with a queue. At each level, process all nodes at that
 *   level and record the last node's value — this is the rightmost node visible from the right
 *   side. Add this value to the result list. This gives a clean O(n) solution without needing
 *   to track depth separately.
 *
 * Time Complexity: O(n) — visit each node once
 * Space Complexity: O(w) — w is maximum width of the tree
 *
 * Test Cases:
 *   1. Input: [1,2,3,null,5,null,4] → Output: [1,3,4]
 *   2. Input: [1,null,3] → Output: [1,3]
 *   3. Edge case: empty tree → Output: []
 */
public class BinaryTreeRightSideView {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (i == levelSize - 1) result.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        BinaryTreeRightSideView sol = new BinaryTreeRightSideView();

        // Test 1: [1,2,3,null,5,null,4]
        TreeNode t1 = new TreeNode(1);
        t1.left = new TreeNode(2);
        t1.right = new TreeNode(3);
        t1.left.right = new TreeNode(5);
        t1.right.right = new TreeNode(4);
        System.out.println("Test 1 (expect [1,3,4]): " + sol.rightSideView(t1));

        // Test 2: [1,null,3]
        TreeNode t2 = new TreeNode(1);
        t2.right = new TreeNode(3);
        System.out.println("Test 2 (expect [1,3]): " + sol.rightSideView(t2));

        // Test 3: empty tree
        System.out.println("Test 3 (expect []): " + sol.rightSideView(null));
    }
}
```

## Complexity

- **Time:** O(n) — visit each node once
- **Space:** O(w) — w is maximum width of the tree
