package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Minimum Number of Arrows to Burst Balloons
 * Difficulty: Medium
 *
 * Description:
 * Balloons are represented as intervals [start, end]. An arrow shot vertically at x
 * bursts all balloons where start <= x <= end. Return the minimum number of arrows
 * needed to burst all balloons.
 *
 * Example:
 *   Input: [[10,16],[2,8],[1,6],[7,12]]
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= points.length <= 10^5
 *   - points[i].length == 2
 *   - -2^31 <= start <= end <= 2^31 - 1
 *
 * Approach:
 *   Greedy: sort balloons by end position. Shoot arrow at the first balloon's end.
 *   This arrow bursts all balloons that overlap it. When we encounter a balloon whose
 *   start > current arrow position, it cannot be burst by the current arrow — shoot a
 *   new arrow at this balloon's end. Count total arrows fired.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [[10,16],[2,8],[1,6],[7,12]] → Output: 2
 *   2. Input: [[1,2],[3,4],[5,6],[7,8]] → Output: 4 (no overlaps)
 *   3. Edge: [[1,2],[2,3],[3,4],[4,5]] → Output: 2
 */
public class MinArrowsToBurstBalloons {

    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
        int arrows = 1;
        int arrowPos = points[0][1];
        for (int i = 1; i < points.length; i++) {
            if (points[i][0] > arrowPos) {
                arrows++;
                arrowPos = points[i][1];
            }
        }
        return arrows;
    }

    public static void main(String[] args) {
        MinArrowsToBurstBalloons sol = new MinArrowsToBurstBalloons();

        System.out.println("Test 1 (expect 2): "
                + sol.findMinArrowShots(new int[][]{{10,16},{2,8},{1,6},{7,12}}));
        System.out.println("Test 2 (expect 4): "
                + sol.findMinArrowShots(new int[][]{{1,2},{3,4},{5,6},{7,8}}));
        System.out.println("Test 3 (expect 2): "
                + sol.findMinArrowShots(new int[][]{{1,2},{2,3},{3,4},{4,5}}));
    }
}
