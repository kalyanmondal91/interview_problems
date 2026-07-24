---
layout: problem
title: "Rotate Image"
category: arrays
category_display: "Arrays"
difficulty: Medium
time_complexity: "O(n^2)"
space_complexity: "O(1)"
leetcode: 48
tags: [arrays]
render_with_liquid: false
---

## Problem

Rotate Image Given an n×n 2D matrix representing an image, rotate the image by 90 degrees clockwise in-place. You must modify the input matrix directly; do not allocate another 2D matrix.

## Approach

A 90-degree clockwise rotation can be decomposed into two steps performed in-place: first transpose the matrix (swap matrix[i][j] with matrix[j][i] for i < j), then reverse each row. The transpose converts rows to columns, and reversing the rows corrects the direction to achieve the clockwise rotation.

## Solution

```java
package org.interview.coding.arrays;

import java.util.Arrays;

/**
 * Problem: Rotate Image
 * Difficulty: Medium
 *
 * Description:
 * Given an n×n 2D matrix representing an image, rotate the image by 90 degrees clockwise
 * in-place. You must modify the input matrix directly; do not allocate another 2D matrix.
 *
 * Example:
 *   Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 *   Output: [[7,4,1],[8,5,2],[9,6,3]]
 *
 * Constraints:
 *   - n == matrix.length == matrix[i].length
 *   - 1 <= n <= 20
 *   - -1000 <= matrix[i][j] <= 1000
 *
 * Approach:
 *   A 90-degree clockwise rotation can be decomposed into two steps performed in-place:
 *   first transpose the matrix (swap matrix[i][j] with matrix[j][i] for i < j), then
 *   reverse each row. The transpose converts rows to columns, and reversing the rows
 *   corrects the direction to achieve the clockwise rotation.
 *
 * Time Complexity: O(n^2)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [[1,2,3],[4,5,6],[7,8,9]] → Output: [[7,4,1],[8,5,2],[9,6,3]]
 *   2. Input: [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]] → Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 *   3. Edge case: [[1]] → Output: [[1]] (1x1 matrix)
 */
public class RotateImage {

    public void rotate(int[][] matrix) {
        int n = matrix.length;
        // Step 1: Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            int left = 0, right = n - 1;
            while (left < right) {
                int temp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = temp;
                left++;
                right--;
            }
        }
    }

    public static void main(String[] args) {
        RotateImage sol = new RotateImage();
        // Test 1
        int[][] m1 = {{1,2,3},{4,5,6},{7,8,9}};
        sol.rotate(m1);
        for (int[] row : m1) System.out.println(Arrays.toString(row)); // [7,4,1] [8,5,2] [9,6,3]
        // Test 2
        int[][] m2 = {{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}};
        sol.rotate(m2);
        for (int[] row : m2) System.out.println(Arrays.toString(row));
        // Test 3 (edge case: 1x1 matrix)
        int[][] m3 = {{1}};
        sol.rotate(m3);
        for (int[] row : m3) System.out.println(Arrays.toString(row)); // [1]
    }
}
```

## Complexity

- **Time:** O(n^2)
- **Space:** O(1)
