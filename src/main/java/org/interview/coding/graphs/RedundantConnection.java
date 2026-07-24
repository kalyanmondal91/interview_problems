package org.interview.coding.graphs;

import java.util.Arrays;

/**
 * Problem: Redundant Connection
 * Difficulty: Medium
 *
 * Description:
 * In a tree with n nodes (labeled 1 to n), one extra edge was added. Given edges array
 * representing a graph that was once a tree plus one redundant edge, return the edge that
 * can be removed so the resulting graph is a tree. If multiple answers exist, return the
 * last one in the input.
 *
 * Example:
 *   Input: edges = [[1,2],[1,3],[2,3]]
 *   Output: [2,3]
 *
 * Constraints:
 *   - n == edges.length
 *   - 3 <= n <= 1000
 *   - edges[i] has no self-loops or repeated edges
 *
 * Approach:
 *   Use Union-Find (Disjoint Set Union) data structure. For each edge [u, v], find their
 *   roots. If they already have the same root, this edge creates a cycle — return it as the
 *   redundant connection. Otherwise, union the two sets. The path compression and union by
 *   rank optimizations keep the complexity near O(n).
 *
 * Time Complexity: O(n * α(n)) ≈ O(n) — α is inverse Ackermann function
 * Space Complexity: O(n) — parent and rank arrays
 *
 * Test Cases:
 *   1. Input: [[1,2],[1,3],[2,3]] → Output: [2,3]
 *   2. Input: [[1,2],[2,3],[3,4],[1,4],[1,5]] → Output: [1,4]
 *   3. Edge case: triangle graph [[1,2],[2,3],[1,3]] → Output: [1,3]
 */
public class RedundantConnection {

    private int[] parent, rank;

    private int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    private boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;
        if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
        return true;
    }

    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        parent = new int[n + 1];
        rank = new int[n + 1];
        for (int i = 0; i <= n; i++) parent[i] = i;
        for (int[] edge : edges) {
            if (!union(edge[0], edge[1])) return edge;
        }
        return new int[0];
    }

    public static void main(String[] args) {
        RedundantConnection sol = new RedundantConnection();

        // Test 1
        System.out.println("Test 1 (expect [2,3]): " + Arrays.toString(
            sol.findRedundantConnection(new int[][]{{1,2},{1,3},{2,3}})));

        // Test 2
        System.out.println("Test 2 (expect [1,4]): " + Arrays.toString(
            sol.findRedundantConnection(new int[][]{{1,2},{2,3},{3,4},{1,4},{1,5}})));

        // Test 3: triangle
        System.out.println("Test 3 (expect [1,3]): " + Arrays.toString(
            sol.findRedundantConnection(new int[][]{{1,2},{2,3},{1,3}})));
    }
}
