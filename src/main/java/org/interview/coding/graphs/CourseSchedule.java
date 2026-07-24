package org.interview.coding.graphs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Problem: Course Schedule
 * Difficulty: Medium
 *
 * Description:
 * There are numCourses courses labeled 0 to numCourses-1. Given an array prerequisites
 * where prerequisites[i] = [ai, bi] means you must take course bi before course ai,
 * return true if you can finish all courses, false if there is a cycle.
 *
 * Example:
 *   Input: numCourses = 2, prerequisites = [[1,0]]
 *   Output: true
 *
 * Constraints:
 *   - 1 <= numCourses <= 2000
 *   - 0 <= prerequisites.length <= 5000
 *   - prerequisites[i].length == 2, ai != bi
 *
 * Approach:
 *   Use Kahn's algorithm (topological sort via BFS). Build an adjacency list and in-degree
 *   array. Enqueue all nodes with in-degree 0. Process each node: decrement the in-degree
 *   of its neighbors; if any neighbor's in-degree becomes 0, enqueue it. Count processed nodes.
 *   If the count equals numCourses, no cycle exists and all courses can be finished.
 *
 * Time Complexity: O(V + E) — process each node and edge once
 * Space Complexity: O(V + E) — adjacency list and in-degree array
 *
 * Test Cases:
 *   1. Input: numCourses=2, [[1,0]] → Output: true
 *   2. Input: numCourses=2, [[1,0],[0,1]] → Output: false (cycle)
 *   3. Edge case: numCourses=1, no prerequisites → Output: true
 */
public class CourseSchedule {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
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
        int processed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int next : adj.get(course)) {
                if (--inDegree[next] == 0) queue.offer(next);
            }
        }
        return processed == numCourses;
    }

    public static void main(String[] args) {
        CourseSchedule sol = new CourseSchedule();

        // Test 1: no cycle
        System.out.println("Test 1 (expect true): " + sol.canFinish(2, new int[][]{{1, 0}}));

        // Test 2: cycle
        System.out.println("Test 2 (expect false): " + sol.canFinish(2, new int[][]{{1, 0}, {0, 1}}));

        // Test 3: single course, no prerequisites
        System.out.println("Test 3 (expect true): " + sol.canFinish(1, new int[][]{}));
    }
}
