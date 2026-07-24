package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Word Search
 * Difficulty: Medium
 *
 * Description:
 * Given an m x n grid of characters and a string word, return true if word exists in the grid.
 * The word must be constructed from letters of sequentially adjacent cells (horizontally or vertically
 * adjacent), and the same cell may not be used more than once.
 *
 * Example:
 *   Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 *   Output: true
 *
 * Constraints:
 *   - m == board.length, n == board[i].length
 *   - 1 <= m, n <= 6
 *   - 1 <= word.length <= 15
 *   - board and word consist of only lowercase and uppercase English letters.
 *
 * Approach:
 *   Iterate over every cell as a potential starting point. From each matching cell, perform DFS.
 *   Mark the current cell as visited by replacing it with '#', then recursively check all four
 *   neighbors for the next character. After recursion, restore the cell's original character.
 *   The search succeeds when all characters in the word have been matched sequentially.
 *
 * Time Complexity: O(m*n*4^L)
 * Space Complexity: O(L) where L is word length
 *
 * Test Cases:
 *   1. Input: board=[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word="ABCCED" → Output: true
 *   2. Input: board=[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word="SEE" → Output: true
 *   3. Edge: board=[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word="ABCB" → Output: false
 */
public class WordSearch {

    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(board, word, i, j, 0)) return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int r, int c, int idx) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        char temp = board[r][c];
        board[r][c] = '#'; // mark visited

        boolean found = dfs(board, word, r + 1, c, idx + 1)
                     || dfs(board, word, r - 1, c, idx + 1)
                     || dfs(board, word, r, c + 1, idx + 1)
                     || dfs(board, word, r, c - 1, idx + 1);

        board[r][c] = temp; // restore
        return found;
    }

    public static void main(String[] args) {
        WordSearch solution = new WordSearch();

        char[][] board1 = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };

        // Test 1: word exists with backtracking
        System.out.println(solution.exist(board1, "ABCCED")); // Expected: true

        // Test 2: word exists at bottom-right
        System.out.println(solution.exist(board1, "SEE")); // Expected: true

        // Test 3: word requires reusing a cell
        System.out.println(solution.exist(board1, "ABCB")); // Expected: false
    }
}
