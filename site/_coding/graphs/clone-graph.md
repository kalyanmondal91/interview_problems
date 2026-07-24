---
layout: problem
title: "Clone Graph"
category: graphs
category_display: "Graphs"
difficulty: Medium
time_complexity: "O(V + E) — visit each node and edge once"
space_complexity: "O(V) — HashMap stores all node clones"
leetcode: 133
tags: [graphs]
render_with_liquid: false
---

## Problem

Clone Graph Given a reference to a node in a connected undirected graph, return a deep copy (clone) of the graph. Each node contains a value and a list of its neighbors. The cloned graph must be a completely independent copy — no shared references with the original.

## Approach

Use BFS with a HashMap that maps each original node to its clone. Start by creating a clone of the source node and adding it to the map. For each node dequeued, iterate through its neighbors: if a neighbor hasn't been cloned yet, create its clone, add to the map, and enqueue it. Then add the neighbor's clone to the current clone's neighbor list.

## Solution

```java
package org.interview.coding.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Problem: Clone Graph
 * Difficulty: Medium
 *
 * Description:
 * Given a reference to a node in a connected undirected graph, return a deep copy (clone)
 * of the graph. Each node contains a value and a list of its neighbors. The cloned graph
 * must be a completely independent copy — no shared references with the original.
 *
 * Example:
 *   Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 *   Output: Deep copy of the graph with same structure
 *
 * Constraints:
 *   - Number of nodes: [0, 100]
 *   - 1 <= Node.val <= 100
 *   - No repeated edges, no self-loops
 *
 * Approach:
 *   Use BFS with a HashMap that maps each original node to its clone. Start by creating a
 *   clone of the source node and adding it to the map. For each node dequeued, iterate
 *   through its neighbors: if a neighbor hasn't been cloned yet, create its clone, add to
 *   the map, and enqueue it. Then add the neighbor's clone to the current clone's neighbor list.
 *
 * Time Complexity: O(V + E) — visit each node and edge once
 * Space Complexity: O(V) — HashMap stores all node clones
 *
 * Test Cases:
 *   1. Input: [[2,4],[1,3],[2,4],[1,3]] → 4-node cycle graph cloned correctly
 *   2. Input: single node with no neighbors → single cloned node
 *   3. Edge case: null input → return null
 */
public class CloneGraph {

    static class Node {
        int val;
        List<Node> neighbors;
        Node(int v) { val = v; neighbors = new ArrayList<>(); }
    }

    public Node cloneGraph(Node node) {
        if (node == null) return null;
        Map<Node, Node> visited = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        visited.put(node, new Node(node.val));
        queue.offer(node);
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Node neighbor : curr.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                visited.get(curr).neighbors.add(visited.get(neighbor));
            }
        }
        return visited.get(node);
    }

    public static void main(String[] args) {
        CloneGraph sol = new CloneGraph();

        // Test 1: 4-node cycle [[2,4],[1,3],[2,4],[1,3]]
        Node n1 = new Node(1), n2 = new Node(2), n3 = new Node(3), n4 = new Node(4);
        n1.neighbors.add(n2); n1.neighbors.add(n4);
        n2.neighbors.add(n1); n2.neighbors.add(n3);
        n3.neighbors.add(n2); n3.neighbors.add(n4);
        n4.neighbors.add(n1); n4.neighbors.add(n3);
        Node cloned = sol.cloneGraph(n1);
        System.out.println("Test 1 - cloned != original: " + (cloned != n1));
        System.out.println("Test 1 - cloned val: " + cloned.val);
        System.out.println("Test 1 - neighbor count: " + cloned.neighbors.size());

        // Test 2: single node
        Node s = new Node(1);
        Node cs = sol.cloneGraph(s);
        System.out.println("Test 2 - cloned single node val: " + cs.val + ", neighbors: " + cs.neighbors.size());

        // Test 3: null input
        Node cn = sol.cloneGraph(null);
        System.out.println("Test 3 (expect null): " + cn);
    }
}
```

## Complexity

- **Time:** O(V + E) — visit each node and edge once
- **Space:** O(V) — HashMap stores all node clones
