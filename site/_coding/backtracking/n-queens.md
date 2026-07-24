---
layout: problem
title: "N Queens"
category: backtracking
category_display: "Backtracking"
difficulty: Hard
time_complexity: "O(n!)"
space_complexity: "O(n^2)"
leetcode: 51
tags: [backtracking]
render_with_liquid: false
---

## Problem

N-Queens Given an integer n, return all distinct solutions to the n-queens puzzle. Each solution contains a distinct board configuration where 'Q' indicates a queen and '.' indicates an empty cell. No two queens can share the same row, column, or diagonal.

## Approach

Place queens row by row using backtracking. Track occupied columns, diagonals (row-col), and anti-diagonals (row+col) in HashSets for O(1) conflict checking. For each row, try placing a queen in each column; if valid, mark the sets and recurse to the next row, then undo. When row == n, all queens are placed and we record the board configuration.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: N-Queens
 * Difficulty: Hard
 *
 * Description:
 * Given an integer n, return all distinct solutions to the n-queens puzzle. Each solution contains
 * a distinct board configuration where 'Q' indicates a queen and '.' indicates an empty cell.
 * No two queens can share the same row, column, or diagonal.
 *
 * Example:
 *   Input: n = 4
 *   Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
 *
 * Constraints:
 *   - 1 <= n <= 9
 *
 * Approach:
 *   Place queens row by row using backtracking. Track occupied columns, diagonals (row-col), and
 *   anti-diagonals (row+col) in HashSets for O(1) conflict checking. For each row, try placing a
 *   queen in each column; if valid, mark the sets and recurse to the next row, then undo.
 *   When row == n, all queens are placed and we record the board configuration.
 *
 * Time Complexity: O(n!)
 * Space Complexity: O(n^2)
 *
 * Test Cases:
 *   1. Input: n=4 → Output: 2 solutions
 *   2. Input: n=1 → Output: [["Q"]]
 *   3. Edge: n=2 → Output: [] (no solutions)
 */
public class NQueens {

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');

        Set<Integer> cols = new HashSet<>();
        Set<Integer> diags = new HashSet<>();     // row - col
        Set<Integer> antiDiags = new HashSet<>(); // row + col

        backtrack(board, 0, n, cols, diags, antiDiags, result);
        return result;
    }

    private void backtrack(char[][] board, int row, int n,
                           Set<Integer> cols, Set<Integer> diags, Set<Integer> antiDiags,
                           List<List<String>> result) {
        if (row == n) {
            List<String> solution = new ArrayList<>();
            for (char[] r : board) solution.add(new String(r));
            result.add(solution);
            return;
        }
        for (int col = 0; col < n; col++) {
            if (cols.contains(col) || diags.contains(row - col) || antiDiags.contains(row + col)) {
                continue;
            }
            board[row][col] = 'Q';
            cols.add(col);
            diags.add(row - col);
            antiDiags.add(row + col);

            backtrack(board, row + 1, n, cols, diags, antiDiags, result);

            board[row][col] = '.';
            cols.remove(col);
            diags.remove(row - col);
            antiDiags.remove(row + col);
        }
    }

    public static void main(String[] args) {
        NQueens solution = new NQueens();

        // Test 1: n=4 has 2 solutions
        List<List<String>> result4 = solution.solveNQueens(4);
        System.out.println("n=4 solutions count: " + result4.size()); // Expected: 2
        System.out.println(result4);

        // Test 2: n=1 trivial case
        System.out.println(solution.solveNQueens(1)); // Expected: [["Q"]]

        // Test 3: n=2 no solution
        System.out.println(solution.solveNQueens(2)); // Expected: []
    }
}
```

## Complexity

- **Time:** O(n!)
- **Space:** O(n^2)
