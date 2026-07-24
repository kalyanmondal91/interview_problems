package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Search a 2D Matrix
 * Difficulty: Medium
 *
 * Description:
 * Write an efficient algorithm that searches for a value target in an m x n integer matrix.
 * The matrix has the following properties: integers in each row are sorted from left to right,
 * and the first integer of each row is greater than the last integer of the previous row.
 *
 * Example:
 *   Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
 *   Output: true
 *
 * Constraints:
 *   - m == matrix.length, n == matrix[i].length
 *   - 1 <= m, n <= 100
 *   - -10^4 <= matrix[i][j], target <= 10^4
 *
 * Approach:
 *   Treat the m x n matrix as a virtual 1D sorted array of size m*n.
 *   Binary search on virtual index [0, m*n-1]. Convert index to row = idx/n, col = idx%n.
 *   Compare matrix[row][col] with target and adjust bounds accordingly.
 *   This approach is clean and achieves O(log(m*n)).
 *
 * Time Complexity: O(log(m*n))
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target=3 → Output: true
 *   2. Input: [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target=13 → Output: false
 *   3. Edge: [[1]], target=1 → Output: true
 */
public class SearchA2DMatrix {

    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int lo = 0, hi = m * n - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int val = matrix[mid / n][mid % n];
            if (val == target) return true;
            else if (val < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }

    public static void main(String[] args) {
        SearchA2DMatrix sol = new SearchA2DMatrix();

        // Test 1
        int[][] m1 = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        System.out.println("Test 1: " + sol.searchMatrix(m1, 3)); // Expected: true

        // Test 2
        System.out.println("Test 2: " + sol.searchMatrix(m1, 13)); // Expected: false

        // Test 3 (edge case)
        int[][] m3 = {{1}};
        System.out.println("Test 3: " + sol.searchMatrix(m3, 1)); // Expected: true
    }
}
