package org.interview.coding.hashing;

import java.util.HashSet;

/**
 * Problem: Valid Sudoku
 * Difficulty: Medium
 *
 * Description:
 * Determine if a 9×9 Sudoku board is valid. Only the filled cells need to be validated.
 * A valid board has each row, column, and each of the nine 3×3 sub-boxes containing
 * the digits 1-9 without repetition. Empty cells are represented by '.'.
 *
 * Example:
 *   Input: a partially filled 9x9 board
 *   Output: true or false
 *
 * Constraints:
 *   - board.length == 9, board[i].length == 9
 *   - board[i][j] is a digit '1'-'9' or '.'
 *
 * Approach:
 *   Use three arrays of HashSets: one for rows, one for columns, and one for the nine 3×3
 *   boxes (indexed as row/3*3 + col/3). Iterate every cell; for non-empty cells, check if
 *   the digit already exists in the corresponding row set, column set, or box set. If so,
 *   the board is invalid. Otherwise add it to all three sets.
 *
 * Time Complexity: O(1) — always 81 cells
 * Space Complexity: O(1) — fixed 27 sets with max 9 elements each
 *
 * Test Cases:
 *   1. Input: valid partially filled board → Output: true
 *   2. Input: board with duplicate in a row → Output: false
 *   3. Edge case: board with duplicate in a 3x3 box → Output: false
 */
public class ValidSudoku {

    public boolean isValidSudoku(char[][] board) {
        HashSet<Character>[] rows = new HashSet[9];
        HashSet<Character>[] cols = new HashSet[9];
        HashSet<Character>[] boxes = new HashSet[9];

        for (int i = 0; i < 9; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char val = board[r][c];
                if (val == '.') continue;

                int boxIdx = (r / 3) * 3 + (c / 3);
                if (rows[r].contains(val) || cols[c].contains(val) || boxes[boxIdx].contains(val)) {
                    return false;
                }
                rows[r].add(val);
                cols[c].add(val);
                boxes[boxIdx].add(val);
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ValidSudoku sol = new ValidSudoku();
        // Test 1: valid board
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
        System.out.println(sol.isValidSudoku(board1)); // true
        // Test 2: duplicate in row
        char[][] board2 = {
            {'8','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        System.out.println(sol.isValidSudoku(board2)); // false
        // Test 3 (edge case: duplicate in box)
        char[][] board3 = {
            {'.','.','.','.','.','.','.','.','9'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','9','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.','9'}
        };
        System.out.println(sol.isValidSudoku(board3)); // false (duplicate 9 in last col and last box)
    }
}
