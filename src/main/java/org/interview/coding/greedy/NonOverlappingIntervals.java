package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Non-overlapping Intervals
 * Difficulty: Medium
 *
 * Description:
 * Given an array of intervals, return the minimum number of intervals you need to
 * remove to make the rest of the intervals non-overlapping.
 *
 * Example:
 *   Input: [[1,2],[2,3],[3,4],[1,3]]
 *   Output: 1 (remove [1,3])
 *
 * Constraints:
 *   - 1 <= intervals.length <= 10^5
 *   - intervals[i].length == 2
 *   - -5*10^4 <= starti < endi <= 5*10^4
 *
 * Approach:
 *   Greedy: sort intervals by end time. Maintain lastEnd (end of the last kept interval).
 *   For each interval, if its start < lastEnd, it overlaps — remove it (increment count).
 *   If no overlap, keep it and update lastEnd to this interval's end. Sorting by end
 *   ensures we greedily keep intervals that end earliest, leaving most room for future.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [[1,2],[2,3],[3,4],[1,3]] → Output: 1
 *   2. Input: [[1,2],[1,2],[1,2]] → Output: 2
 *   3. Edge: [[1,2],[2,3]] → Output: 0 (no overlap, touching is fine)
 */
public class NonOverlappingIntervals {

    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int count = 0, lastEnd = Integer.MIN_VALUE;
        for (int[] interval : intervals) {
            if (interval[0] < lastEnd) {
                count++; // overlap: remove this interval
            } else {
                lastEnd = interval[1]; // keep this interval
            }
        }
        return count;
    }

    public static void main(String[] args) {
        NonOverlappingIntervals sol = new NonOverlappingIntervals();

        System.out.println("Test 1 (expect 1): "
                + sol.eraseOverlapIntervals(new int[][]{{1,2},{2,3},{3,4},{1,3}}));
        System.out.println("Test 2 (expect 2): "
                + sol.eraseOverlapIntervals(new int[][]{{1,2},{1,2},{1,2}}));
        System.out.println("Test 3 (expect 0): "
                + sol.eraseOverlapIntervals(new int[][]{{1,2},{2,3}}));
    }
}
