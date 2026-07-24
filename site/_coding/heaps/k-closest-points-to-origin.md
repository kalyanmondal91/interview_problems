---
layout: problem
title: "K Closest Points To Origin"
category: heaps
category_display: "Heaps"
difficulty: Medium
time_complexity: "O(n log k)"
space_complexity: "O(k)"
tags: [heaps]
render_with_liquid: false
---

## Problem

K Closest Points to Origin Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane and an integer k, return the k closest points to the origin (0, 0). The distance between two points on the X-Y plane is the Euclidean distance.

## Approach

Use a max-heap ordered by distance squared (to avoid sqrt). For each point, add it to the heap. If heap size exceeds k, poll the farthest point. After processing all points, the remaining k points in the heap are the k closest. Using distance squared avoids floating point arithmetic.

## Solution

```java
package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: K Closest Points to Origin
 * Difficulty: Medium
 *
 * Description:
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane
 * and an integer k, return the k closest points to the origin (0, 0).
 * The distance between two points on the X-Y plane is the Euclidean distance.
 *
 * Example:
 *   Input: points = [[1,3],[-2,2]], k = 1
 *   Output: [[-2,2]]
 *
 * Constraints:
 *   - 1 <= k <= points.length <= 10^4
 *   - -10^4 <= xi, yi <= 10^4
 *
 * Approach:
 *   Use a max-heap ordered by distance squared (to avoid sqrt).
 *   For each point, add it to the heap. If heap size exceeds k, poll the farthest point.
 *   After processing all points, the remaining k points in the heap are the k closest.
 *   Using distance squared avoids floating point arithmetic.
 *
 * Time Complexity: O(n log k)
 * Space Complexity: O(k)
 *
 * Test Cases:
 *   1. Input: [[1,3],[-2,2]], k=1 → Output: [[-2,2]]
 *   2. Input: [[3,3],[5,-1],[-2,4]], k=2 → Output: [[3,3],[-2,4]]
 *   3. Edge: [[0,0],[1,1]], k=1 → Output: [[0,0]]
 */
public class KClosestPointsToOrigin {

    public int[][] kClosest(int[][] points, int k) {
        // Max-heap by distance squared
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> (b[0] * b[0] + b[1] * b[1]) - (a[0] * a[0] + a[1] * a[1])
        );

        for (int[] p : points) {
            maxHeap.offer(p);
            if (maxHeap.size() > k) maxHeap.poll();
        }

        return maxHeap.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        KClosestPointsToOrigin sol = new KClosestPointsToOrigin();

        // Test 1
        int[][] p1 = {{1, 3}, {-2, 2}};
        int[][] r1 = sol.kClosest(p1, 1);
        System.out.println("Test 1: " + Arrays.deepToString(r1)); // [[-2,2]]

        // Test 2
        int[][] p2 = {{3, 3}, {5, -1}, {-2, 4}};
        int[][] r2 = sol.kClosest(p2, 2);
        System.out.println("Test 2: " + Arrays.deepToString(r2)); // [[3,3],[-2,4]]

        // Test 3 (edge case)
        int[][] p3 = {{0, 0}, {1, 1}};
        int[][] r3 = sol.kClosest(p3, 1);
        System.out.println("Test 3: " + Arrays.deepToString(r3)); // [[0,0]]
    }
}
```

## Complexity

- **Time:** O(n log k)
- **Space:** O(k)
