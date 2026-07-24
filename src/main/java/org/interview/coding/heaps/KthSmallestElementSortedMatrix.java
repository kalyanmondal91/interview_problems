package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Kth Smallest Element in a Sorted Matrix
 * Difficulty: Medium
 *
 * Description:
 * Given an n x n matrix where each row and column is sorted in ascending order,
 * return the kth smallest element in the matrix.
 * Note that it is the kth smallest element in sorted order, not the kth distinct element.
 *
 * Example:
 *   Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
 *   Output: 13
 *
 * Constraints:
 *   - n == matrix.length == matrix[i].length
 *   - 1 <= n <= 300
 *   - -10^9 <= matrix[i][j] <= 10^9
 *   - 1 <= k <= n^2
 *
 * Approach:
 *   Use a min-heap initialized with the first element of each row (value, row, col).
 *   Poll k-1 times, each time pushing the next element in the same row if it exists.
 *   The kth poll gives the kth smallest element.
 *   This approach leverages the row-sorted property efficiently.
 *
 * Time Complexity: O(k log n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [[1,5,9],[10,11,13],[12,13,15]], k=8 → Output: 13
 *   2. Input: [[1,2],[1,3]], k=2 → Output: 1
 *   3. Edge: [[1]], k=1 → Output: 1
 */
public class KthSmallestElementSortedMatrix {

    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        // heap: [value, row, col]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        for (int i = 0; i < n; i++) {
            minHeap.offer(new int[]{matrix[i][0], i, 0});
        }

        int result = 0;
        for (int i = 0; i < k; i++) {
            int[] curr = minHeap.poll();
            result = curr[0];
            int row = curr[1], col = curr[2];
            if (col + 1 < n) {
                minHeap.offer(new int[]{matrix[row][col + 1], row, col + 1});
            }
        }
        return result;
    }

    public static void main(String[] args) {
        KthSmallestElementSortedMatrix sol = new KthSmallestElementSortedMatrix();

        // Test 1
        int[][] m1 = {{1, 5, 9}, {10, 11, 13}, {12, 13, 15}};
        System.out.println("Test 1: " + sol.kthSmallest(m1, 8)); // Expected: 13

        // Test 2
        int[][] m2 = {{1, 2}, {1, 3}};
        System.out.println("Test 2: " + sol.kthSmallest(m2, 2)); // Expected: 1

        // Test 3 (edge case)
        int[][] m3 = {{1}};
        System.out.println("Test 3: " + sol.kthSmallest(m3, 1)); // Expected: 1
    }
}
