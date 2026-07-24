package org.interview.coding.trees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Serialize and Deserialize Binary Tree
 * Difficulty: Hard
 *
 * Description:
 * Design an algorithm to serialize a binary tree to a string and deserialize that string
 * back to the original tree structure. Serialization is the process of converting a data
 * structure into a sequence of bits so that it can be stored or transmitted.
 *
 * Example:
 *   Input: root = [1,2,3,null,null,4,5]
 *   Output: "1,2,3,null,null,4,5" (or equivalent), then deserialized back to original tree
 *
 * Constraints:
 *   - Number of nodes: [0, 10^4]
 *   - -1000 <= Node.val <= 1000
 *
 * Approach:
 *   Use BFS (level-order) for both serialization and deserialization. During serialization,
 *   use a queue to process nodes level by level, appending "null" for missing children.
 *   During deserialization, split the string by commas, use a queue to assign left and right
 *   children to each parent node in order. This ensures the structure is perfectly reconstructed.
 *
 * Time Complexity: O(n) — visit each node once in both operations
 * Space Complexity: O(n) — queue and string storage
 *
 * Test Cases:
 *   1. Input: [1,2,3,null,null,4,5] → serialize then deserialize → same tree
 *   2. Input: single node [1] → Output: "1,null,null"
 *   3. Edge case: empty tree (null) → Output: "null"
 */
public class SerializeDeserializeBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public String serialize(TreeNode root) {
        if (root == null) return "null";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                sb.append("null,");
            } else {
                sb.append(node.val).append(",");
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        if (data == null || data.equals("null")) return null;
        String[] parts = data.split(",");
        if (parts[0].equals("null")) return null;
        TreeNode root = new TreeNode(Integer.parseInt(parts[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < parts.length) {
            TreeNode node = queue.poll();
            if (i < parts.length && !parts[i].equals("null")) {
                node.left = new TreeNode(Integer.parseInt(parts[i]));
                queue.offer(node.left);
            }
            i++;
            if (i < parts.length && !parts[i].equals("null")) {
                node.right = new TreeNode(Integer.parseInt(parts[i]));
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }

    private String inorder(TreeNode root) {
        if (root == null) return "null";
        return "[" + inorder(root.left) + "," + root.val + "," + inorder(root.right) + "]";
    }

    public static void main(String[] args) {
        SerializeDeserializeBinaryTree sol = new SerializeDeserializeBinaryTree();

        // Test 1: Full tree
        TreeNode t1 = new TreeNode(1);
        t1.left = new TreeNode(2);
        t1.right = new TreeNode(3);
        t1.right.left = new TreeNode(4);
        t1.right.right = new TreeNode(5);
        String s1 = sol.serialize(t1);
        TreeNode d1 = sol.deserialize(s1);
        System.out.println("Test 1 serialized: " + s1);
        System.out.println("Test 1 deserialized inorder: " + sol.inorder(d1));

        // Test 2: Single node
        TreeNode t2 = new TreeNode(42);
        String s2 = sol.serialize(t2);
        TreeNode d2 = sol.deserialize(s2);
        System.out.println("Test 2 (expect 42): " + d2.val);

        // Test 3: Empty tree
        String s3 = sol.serialize(null);
        TreeNode d3 = sol.deserialize(s3);
        System.out.println("Test 3 (expect null): " + d3);
    }
}
