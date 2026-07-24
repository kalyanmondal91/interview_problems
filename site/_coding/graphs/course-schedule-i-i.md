---
layout: problem
title: "Course Schedule I I"
category: graphs
category_display: "Graphs"
difficulty: Medium
time_complexity: "O(V + E) — each vertex and edge processed once"
space_complexity: "O(V + E) — adjacency list storage"
tags: [graphs]
render_with_liquid: false
---

## Problem

Course Schedule II There are numCourses courses labeled 0 to numCourses-1. Given prerequisites array, return the ordering of courses you should take to finish all courses. If it is impossible (there's a cycle), return an empty array.

## Approach

Extend Course Schedule I with Kahn's BFS topological sort. Build adjacency list and in-degree array. Enqueue all zero in-degree nodes. As each course is processed, add it to the result order array and decrement neighbors' in-degrees, enqueuing newly zero-degree ones. If all numCourses are processed, return the order; otherwise return empty array.

## Solution

```java
package org.interview.coding.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Problem: Course Schedule II
 * Difficulty: Medium
 *
 * Description:
 * There are numCourses courses labeled 0 to numCourses-1. Given prerequisites array,
 * return the ordering of courses you should take to finish all courses. If it is impossible
 * (there's a cycle), return an empty array.
 *
 * Example:
 *   Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 *   Output: [0,1,2,3] or [0,2,1,3]
 *
 * Constraints:
 *   - 1 <= numCourses <= 2000
 *   - 0 <= prerequisites.length <= numCourses * (numCourses - 1)
 *
 * Approach:
 *   Extend Course Schedule I with Kahn's BFS topological sort. Build adjacency list and
 *   in-degree array. Enqueue all zero in-degree nodes. As each course is processed, add it
 *   to the result order array and decrement neighbors' in-degrees, enqueuing newly zero-degree
 *   ones. If all numCourses are processed, return the order; otherwise return empty array.
 *
 * Time Complexity: O(V + E) — each vertex and edge processed once
 * Space Complexity: O(V + E) — adjacency list storage
 *
 * Test Cases:
 *   1. Input: numCourses=2, [[1,0]] → Output: [0,1]
 *   2. Input: numCourses=4, [[1,0],[2,0],[3,1],[3,2]] → Output: valid topo order
 *   3. Edge case: cycle [[0,1],[1,0]] → Output: []
 */
public class CourseScheduleII {

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }
        int[] order = new int[numCourses];
        int idx = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            order[idx++] = course;
            for (int next : adj.get(course)) {
                if (--inDegree[next] == 0) queue.offer(next);
            }
        }
        return idx == numCourses ? order : new int[0];
    }

    public static void main(String[] args) {
        CourseScheduleII sol = new CourseScheduleII();

        // Test 1: simple order
        System.out.println("Test 1 (expect [0,1]): " + Arrays.toString(sol.findOrder(2, new int[][]{{1, 0}})));

        // Test 2: multiple valid orderings
        System.out.println("Test 2 (expect valid order): " + Arrays.toString(
            sol.findOrder(4, new int[][]{{1,0},{2,0},{3,1},{3,2}})));

        // Test 3: cycle
        System.out.println("Test 3 (expect []): " + Arrays.toString(sol.findOrder(2, new int[][]{{0,1},{1,0}})));
    }
}
```

## Complexity

- **Time:** O(V + E) — each vertex and edge processed once
- **Space:** O(V + E) — adjacency list storage
