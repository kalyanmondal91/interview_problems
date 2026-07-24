package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Smallest Range Covering Elements from K Lists
 * Difficulty: Hard
 *
 * Description:
 * You have k lists of sorted integers in non-decreasing order.
 * Find the smallest range [a, b] such that there is at least one number from each of the k lists.
 * If there are multiple answers, return the one with the smallest left value.
 *
 * Example:
 *   Input: nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]
 *   Output: [20,24]
 *
 * Constraints:
 *   - nums.length == k, 1 <= k <= 3500
 *   - 1 <= nums[i].length <= 50
 *   - -10^5 <= nums[i][j] <= 10^5
 *
 * Approach:
 *   Use a min-heap initialized with the first element of each list.
 *   Track the current maximum value across all heap elements.
 *   The range [heap.min, currentMax] always covers all k lists.
 *   Pop the minimum, update range if smaller, push the next element from that list.
 *   Stop when any list is exhausted.
 *
 * Time Complexity: O(n log k) where n is total elements
 * Space Complexity: O(k)
 *
 * Test Cases:
 *   1. Input: [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]] → Output: [20,24]
 *   2. Input: [[1,2,3],[1,2,3],[1,2,3]] → Output: [1,1]
 *   3. Edge: [[10],[11]] → Output: [10,11]
 */
public class SmallestRangeCoveringKLists {

    public int[] smallestRange(List<List<Integer>> nums) {
        // heap: [value, listIndex, elementIndex]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        int curMax = Integer.MIN_VALUE;

        for (int i = 0; i < nums.size(); i++) {
            int val = nums.get(i).get(0);
            minHeap.offer(new int[]{val, i, 0});
            curMax = Math.max(curMax, val);
        }

        int[] result = {minHeap.peek()[0], curMax};

        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int val = curr[0], listIdx = curr[1], elemIdx = curr[2];

            if (elemIdx + 1 >= nums.get(listIdx).size()) break;

            int nextVal = nums.get(listIdx).get(elemIdx + 1);
            minHeap.offer(new int[]{nextVal, listIdx, elemIdx + 1});
            curMax = Math.max(curMax, nextVal);

            int newMin = minHeap.peek()[0];
            if (curMax - newMin < result[1] - result[0]) {
                result[0] = newMin;
                result[1] = curMax;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        SmallestRangeCoveringKLists sol = new SmallestRangeCoveringKLists();

        // Test 1
        List<List<Integer>> n1 = Arrays.asList(
            Arrays.asList(4, 10, 15, 24, 26),
            Arrays.asList(0, 9, 12, 20),
            Arrays.asList(5, 18, 22, 30)
        );
        System.out.println("Test 1: " + Arrays.toString(sol.smallestRange(n1))); // [20, 24]

        // Test 2
        List<List<Integer>> n2 = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(1, 2, 3),
            Arrays.asList(1, 2, 3)
        );
        System.out.println("Test 2: " + Arrays.toString(sol.smallestRange(n2))); // [1, 1]

        // Test 3 (edge case)
        List<List<Integer>> n3 = Arrays.asList(
            Arrays.asList(10),
            Arrays.asList(11)
        );
        System.out.println("Test 3: " + Arrays.toString(sol.smallestRange(n3))); // [10, 11]
    }
}
