---
layout: problem
title: "Meeting Rooms I I"
category: heaps
category_display: "Heaps"
difficulty: Medium
time_complexity: "O(n log n)"
space_complexity: "O(n)"
tags: [heaps]
render_with_liquid: false
---

## Problem

Meeting Rooms II Given an array of meeting time intervals where intervals[i] = [starti, endi], return the minimum number of conference rooms required. Overlapping meetings cannot use the same room.

## Approach

Sort intervals by start time. Use a min-heap of end times (rooms in use). For each meeting, if its start time >= the earliest end time (heap top), that room is free so poll it (the meeting replaces it). Always push the current meeting's end time. The heap size after processing all meetings is the answer.

## Solution

```java
package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Meeting Rooms II
 * Difficulty: Medium
 *
 * Description:
 * Given an array of meeting time intervals where intervals[i] = [starti, endi],
 * return the minimum number of conference rooms required.
 * Overlapping meetings cannot use the same room.
 *
 * Example:
 *   Input: intervals = [[0,30],[5,10],[15,20]]
 *   Output: 2
 *
 * Constraints:
 *   - 1 <= intervals.length <= 10^4
 *   - 0 <= starti < endi <= 10^6
 *
 * Approach:
 *   Sort intervals by start time. Use a min-heap of end times (rooms in use).
 *   For each meeting, if its start time >= the earliest end time (heap top), that room is free
 *   so poll it (the meeting replaces it). Always push the current meeting's end time.
 *   The heap size after processing all meetings is the answer.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [[0,30],[5,10],[15,20]] → Output: 2
 *   2. Input: [[7,10],[2,4]] → Output: 1
 *   3. Edge: [[1,5],[2,6],[3,7]] → Output: 3
 */
public class MeetingRoomsII {

    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();

        for (int[] interval : intervals) {
            if (!endTimes.isEmpty() && interval[0] >= endTimes.peek()) {
                endTimes.poll(); // reuse this room
            }
            endTimes.offer(interval[1]);
        }
        return endTimes.size();
    }

    public static void main(String[] args) {
        MeetingRoomsII sol = new MeetingRoomsII();

        // Test 1
        int[][] i1 = {{0, 30}, {5, 10}, {15, 20}};
        System.out.println("Test 1: " + sol.minMeetingRooms(i1)); // Expected: 2

        // Test 2
        int[][] i2 = {{7, 10}, {2, 4}};
        System.out.println("Test 2: " + sol.minMeetingRooms(i2)); // Expected: 1

        // Test 3 (edge case)
        int[][] i3 = {{1, 5}, {2, 6}, {3, 7}};
        System.out.println("Test 3: " + sol.minMeetingRooms(i3)); // Expected: 3
    }
}
```

## Complexity

- **Time:** O(n log n)
- **Space:** O(n)
