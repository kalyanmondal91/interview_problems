---
layout: problem
title: "Last Stone Weight"
category: heaps
category_display: "Heaps"
difficulty: Easy
time_complexity: "O(n log n)"
space_complexity: "O(n)"
tags: [heaps]
render_with_liquid: false
---

## Problem

Last Stone Weight You are given an array of integers stones where stones[i] is the weight of the ith stone. Smash the two heaviest stones: if equal both are destroyed; if different, the smaller is destroyed and the larger gets weight reduced by the smaller. Return the weight of the last stone, or 0.

## Approach

Use a max-heap to always access the two heaviest stones efficiently. Poll the two largest, compute their difference, and push back if non-zero. Repeat until at most one stone remains. The answer is the remaining stone's weight, or 0 if the heap is empty.

## Solution

```java
package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Last Stone Weight
 * Difficulty: Easy
 *
 * Description:
 * You are given an array of integers stones where stones[i] is the weight of the ith stone.
 * Smash the two heaviest stones: if equal both are destroyed; if different, the smaller is destroyed
 * and the larger gets weight reduced by the smaller. Return the weight of the last stone, or 0.
 *
 * Example:
 *   Input: stones = [2,7,4,1,8,1]
 *   Output: 1
 *
 * Constraints:
 *   - 1 <= stones.length <= 30
 *   - 1 <= stones[i] <= 1000
 *
 * Approach:
 *   Use a max-heap to always access the two heaviest stones efficiently.
 *   Poll the two largest, compute their difference, and push back if non-zero.
 *   Repeat until at most one stone remains.
 *   The answer is the remaining stone's weight, or 0 if the heap is empty.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [2,7,4,1,8,1] → Output: 1
 *   2. Input: [1] → Output: 1
 *   3. Edge: [2,2] → Output: 0
 */
public class LastStoneWeight {

    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int s : stones) maxHeap.offer(s);

        while (maxHeap.size() > 1) {
            int y = maxHeap.poll();
            int x = maxHeap.poll();
            if (y != x) maxHeap.offer(y - x);
        }
        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }

    public static void main(String[] args) {
        LastStoneWeight sol = new LastStoneWeight();

        // Test 1
        System.out.println("Test 1: " + sol.lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1})); // Expected: 1

        // Test 2
        System.out.println("Test 2: " + sol.lastStoneWeight(new int[]{1})); // Expected: 1

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.lastStoneWeight(new int[]{2, 2})); // Expected: 0
    }
}
```

## Complexity

- **Time:** O(n log n)
- **Space:** O(n)
