---
layout: problem
title: "Network Delay Time"
category: graphs
category_display: "Graphs"
difficulty: Medium
time_complexity: "O((V + E) log V) — Dijkstra with min-heap"
space_complexity: "O(V + E) — adjacency list and distance array"
tags: [graphs]
render_with_liquid: false
---

## Problem

Network Delay Time You are given a network of n nodes, labeled from 1 to n. Given times array where times[i] = [ui, vi, wi] (directed edge from ui to vi with travel time wi), and an integer k (source), return the minimum time for all nodes to receive the signal. Return -1 if it is impossible for all nodes to receive the signal.

## Approach

Use Dijkstra's algorithm with a min-heap priority queue. Initialize all distances to infinity except the source k which is 0. Extract the minimum distance node from the heap, then relax all its outgoing edges — if a shorter path is found, update the distance and push the new state to the heap. After processing, the answer is the maximum of all shortest distances; return -1 if any node is still at infinity.

## Solution

```java
package org.interview.coding.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Problem: Network Delay Time
 * Difficulty: Medium
 *
 * Description:
 * You are given a network of n nodes, labeled from 1 to n. Given times array where
 * times[i] = [ui, vi, wi] (directed edge from ui to vi with travel time wi), and an
 * integer k (source), return the minimum time for all nodes to receive the signal.
 * Return -1 if it is impossible for all nodes to receive the signal.
 *
 * Example:
 *   Input: times = [[2,1,1],[2,3,1],[3,4,1]], n=4, k=2
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= k <= n <= 100
 *   - 1 <= times.length <= 6000
 *   - 0 <= wi <= 100
 *
 * Approach:
 *   Use Dijkstra's algorithm with a min-heap priority queue. Initialize all distances to
 *   infinity except the source k which is 0. Extract the minimum distance node from the heap,
 *   then relax all its outgoing edges — if a shorter path is found, update the distance and
 *   push the new state to the heap. After processing, the answer is the maximum of all
 *   shortest distances; return -1 if any node is still at infinity.
 *
 * Time Complexity: O((V + E) log V) — Dijkstra with min-heap
 * Space Complexity: O(V + E) — adjacency list and distance array
 *
 * Test Cases:
 *   1. Input: [[2,1,1],[2,3,1],[3,4,1]], n=4, k=2 → Output: 2
 *   2. Input: [[1,2,1]], n=2, k=1 → Output: 1
 *   3. Edge case: disconnected node → Output: -1
 */
public class NetworkDelayTime {

    public int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] t : times) adj.get(t[0]).add(new int[]{t[1], t[2]});

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, k});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int d = curr[0], u = curr[1];
            if (d > dist[u]) continue;
            for (int[] edge : adj.get(u)) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }

        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1;
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }

    public static void main(String[] args) {
        NetworkDelayTime sol = new NetworkDelayTime();

        // Test 1
        System.out.println("Test 1 (expect 2): " + sol.networkDelayTime(
            new int[][]{{2,1,1},{2,3,1},{3,4,1}}, 4, 2));

        // Test 2: simple 2 nodes
        System.out.println("Test 2 (expect 1): " + sol.networkDelayTime(new int[][]{{1,2,1}}, 2, 1));

        // Test 3: disconnected (no path to node 2 from node 1)
        System.out.println("Test 3 (expect -1): " + sol.networkDelayTime(new int[][]{{2,1,1}}, 2, 1));
    }
}
```

## Complexity

- **Time:** O((V + E) log V) — Dijkstra with min-heap
- **Space:** O(V + E) — adjacency list and distance array
