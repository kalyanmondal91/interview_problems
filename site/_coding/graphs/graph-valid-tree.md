---
layout: problem
title: "Graph Valid Tree"
category: graphs
category_display: "Graphs"
difficulty: Medium
time_complexity: "O(n * alpha(n)) ≈ O(n)"
space_complexity: "O(n)"
tags: [graphs]
render_with_liquid: false
---

## Problem

Graph Valid Tree Given n nodes labeled 0 to n-1 and a list of undirected edges, determine if the edges form a valid tree. A valid tree must have exactly n-1 edges and be fully connected with no cycles.

## Approach

Use Union-Find (Disjoint Set Union). Initialize each node as its own parent. For each edge, find the roots of both nodes. If they share the same root, a cycle exists and we return false. Otherwise, union the two components. After processing all edges, verify there is exactly one connected component by checking that component count equals 1. Quick check: edges must be n-1.

## Solution

```java
package org.interview.coding.graphs;

import java.util.*;

/**
 * Problem: Graph Valid Tree
 * Difficulty: Medium
 *
 * Description:
 * Given n nodes labeled 0 to n-1 and a list of undirected edges, determine if
 * the edges form a valid tree. A valid tree must have exactly n-1 edges and be
 * fully connected with no cycles.
 *
 * Example:
 *   Input: n=5, edges=[[0,1],[0,2],[0,3],[1,4]]
 *   Output: true
 *
 * Constraints:
 *   - 1 <= n <= 2000
 *   - 0 <= edges.length <= 5000
 *   - edges[i].length == 2
 *   - 0 <= ai, bi < n
 *
 * Approach:
 *   Use Union-Find (Disjoint Set Union). Initialize each node as its own parent.
 *   For each edge, find the roots of both nodes. If they share the same root,
 *   a cycle exists and we return false. Otherwise, union the two components.
 *   After processing all edges, verify there is exactly one connected component
 *   by checking that component count equals 1. Quick check: edges must be n-1.
 *
 * Time Complexity: O(n * alpha(n)) ≈ O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: n=5, edges=[[0,1],[0,2],[0,3],[1,4]] → Output: true
 *   2. Input: n=5, edges=[[0,1],[1,2],[2,3],[1,3],[1,4]] → Output: false (cycle)
 *   3. Edge: n=1, edges=[] → Output: true (single node is a valid tree)
 */
public class GraphValidTree {

    private int[] parent;
    private int[] rank;
    private int components;

    public GraphValidTree(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    private boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false; // cycle detected
        if (rank[px] < rank[py]) {
            parent[px] = py;
        } else if (rank[px] > rank[py]) {
            parent[py] = px;
        } else {
            parent[py] = px;
            rank[px]++;
        }
        components--;
        return true;
    }

    public boolean validTree(int n, int[][] edges) {
        // A tree must have exactly n-1 edges
        if (edges.length != n - 1) return false;

        GraphValidTree uf = new GraphValidTree(n);
        for (int[] edge : edges) {
            if (!uf.union(edge[0], edge[1])) {
                return false; // cycle detected
            }
        }
        return uf.components == 1;
    }

    public static void main(String[] args) {
        GraphValidTree sol = new GraphValidTree(1);

        // Test 1: valid tree
        int[][] edges1 = {{0,1},{0,2},{0,3},{1,4}};
        System.out.println("Test 1 (expect true): " + sol.validTree(5, edges1));

        // Test 2: cycle present
        int[][] edges2 = {{0,1},{1,2},{2,3},{1,3},{1,4}};
        System.out.println("Test 2 (expect false): " + sol.validTree(5, edges2));

        // Test 3: single node, no edges
        int[][] edges3 = {};
        System.out.println("Test 3 (expect true): " + sol.validTree(1, edges3));
    }
}
```

## Complexity

- **Time:** O(n * alpha(n)) ≈ O(n)
- **Space:** O(n)
