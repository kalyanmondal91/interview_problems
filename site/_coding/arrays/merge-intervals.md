---
layout: problem
title: "Merge Intervals"
category: arrays
category_display: "Arrays"
difficulty: Medium
time_complexity: "O(n log n) due to sorting"
space_complexity: "O(n) for the output"
leetcode: 56
tags: [arrays]
render_with_liquid: false
---

## Problem

Merge Intervals Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals and return an array of the non-overlapping intervals that cover all the intervals in the input.

## Approach

Sort the intervals by their start time. Then iterate through the sorted list and greedily merge overlapping intervals: if the current interval's start is <= the last merged interval's end, merge by extending the end to the maximum of both ends. Otherwise, start a new interval. Convert the result list back to a 2D array.

## Solution

```java
package org.interview.coding.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Merge Intervals
 * Difficulty: Medium
 *
 * Description:
 * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping
 * intervals and return an array of the non-overlapping intervals that cover all the intervals
 * in the input.
 *
 * Example:
 *   Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
 *   Output: [[1,6],[8,10],[15,18]]
 *
 * Constraints:
 *   - 1 <= intervals.length <= 10^4
 *   - intervals[i].length == 2
 *   - 0 <= starti <= endi <= 10^4
 *
 * Approach:
 *   Sort the intervals by their start time. Then iterate through the sorted list and greedily
 *   merge overlapping intervals: if the current interval's start is <= the last merged interval's
 *   end, merge by extending the end to the maximum of both ends. Otherwise, start a new interval.
 *   Convert the result list back to a 2D array.
 *
 * Time Complexity: O(n log n) due to sorting
 * Space Complexity: O(n) for the output
 *
 * Test Cases:
 *   1. Input: [[1,3],[2,6],[8,10],[15,18]] → Output: [[1,6],[8,10],[15,18]]
 *   2. Input: [[1,4],[4,5]] → Output: [[1,5]] (touching intervals merge)
 *   3. Edge case: [[1,4]] → Output: [[1,4]] (single interval)
 */
public class MergeIntervals {

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> merged = new ArrayList<>();

        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }
        return merged.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        MergeIntervals sol = new MergeIntervals();
        // Test 1
        int[][] r1 = sol.merge(new int[][]{{1,3},{2,6},{8,10},{15,18}});
        for (int[] r : r1) System.out.print(Arrays.toString(r) + " "); System.out.println(); // [1,6] [8,10] [15,18]
        // Test 2
        int[][] r2 = sol.merge(new int[][]{{1,4},{4,5}});
        for (int[] r : r2) System.out.print(Arrays.toString(r) + " "); System.out.println(); // [1,5]
        // Test 3 (edge case: single interval)
        int[][] r3 = sol.merge(new int[][]{{1,4}});
        for (int[] r : r3) System.out.print(Arrays.toString(r) + " "); System.out.println(); // [1,4]
    }
}
```

## Complexity

- **Time:** O(n log n) due to sorting
- **Space:** O(n) for the output
