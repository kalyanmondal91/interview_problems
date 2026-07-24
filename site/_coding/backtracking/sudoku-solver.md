---
layout: problem
title: "Sudoku Solver"
category: backtracking
category_display: "Backtracking"
difficulty: Hard
time_complexity: "O(9^(empty cells))"
space_complexity: "O(81)"
tags: [backtracking]
render_with_liquid: false
---

## Problem

Sudoku Solver Write a program to solve a Sudoku puzzle by filling the empty cells. A sudoku solution must satisfy all the following rules: each of the digits 1-9 must occur exactly once in each row, each column, and each of the nine 3x3 sub-boxes of the grid. Empty cells are indicated by the character '.'.

## Approach

Use backtracking to fill empty cells. Scan for the next empty cell ('.'), then try placing digits '1' through '9'. For each digit, call isValid to check no conflict in the same row, column, or 3x3 box. If valid, place the digit and recurse. If recursion fails, reset to '.' and try the next digit. isValid checks the 9-element row, column, and corresponding 3x3 box.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Sudoku Solver
 * Difficulty: Hard
 *
 * Description:
 * Write a program to solve a Sudoku puzzle by filling the empty cells. A sudoku solution must satisfy
 * all the following rules: each of the digits 1-9 must occur exactly once in each row, each column,
 * and each of the nine 3x3 sub-boxes of the grid. Empty cells are indicated by the character '.'.
 *
 * Example:
 *   Input: board with some cells filled in
 *   Output: the same board, filled with the solution
 *
 * Constraints:
 *   - board.length == 9, board[i].length == 9
 *   - board[i][j] is a digit or '.'.
 *   - It is guaranteed that the input board has only one solution.
 *
 * Approach:
 *   Use backtracking to fill empty cells. Scan for the next empty cell ('.'), then try placing
 *   digits '1' through '9'. For each digit, call isValid to check no conflict in the same row,
 *   column, or 3x3 box. If valid, place the digit and recurse. If recursion fails, reset to '.'
 *   and try the next digit. isValid checks the 9-element row, column, and corresponding 3x3 box.
 *
 * Time Complexity: O(9^(empty cells))
 * Space Complexity: O(81)
 *
 * Test Cases:
 *   1. Input: classic sudoku puzzle → Output: solved board
 *   2. Input: nearly complete board → Output: filled correctly
 *   3. Edge: board with minimum given clues → Output: valid solution
 */
public class SudokuSolver {

    public void solveSudoku(char[][] board) {
        solve(board);
    }

    private boolean solve(char[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') {
                    for (char digit = '1'; digit <= '9'; digit++) {
                        if (isValid(board, r, c, digit)) {
                            board[r][c] = digit;
                            if (solve(board)) return true;
                            board[r][c] = '.';
                        }
                    }
                    return false; // no valid digit found
                }
            }
        }
        return true; // all cells filled
    }

    private boolean isValid(char[][] board, int row, int col, char digit) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == digit) return false;           // check row
            if (board[i][col] == digit) return false;           // check col
            int r = 3 * (row / 3) + i / 3;
            int c = 3 * (col / 3) + i % 3;
            if (board[r][c] == digit) return false;             // check 3x3 box
        }
        return true;
    }

    public static void main(String[] args) {
        SudokuSolver solution = new SudokuSolver();

        // Test 1: classic sudoku puzzle
        char[][] board1 = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        solution.solveSudoku(board1);
        System.out.println("Solved board row 0: " + Arrays.toString(board1[0]));
        // Expected first row: [5, 3, 4, 6, 7, 8, 9, 1, 2]

        // Test 2: print full solved board
        for (char[] row : board1) {
            System.out.println(Arrays.toString(row));
        }

        // Test 3: verify a specific cell
        System.out.println("Cell [0][2] should be 4: " + board1[0][2]); // Expected: 4
    }
}
```

## Complexity

- **Time:** O(9^(empty cells))
- **Space:** O(81)
