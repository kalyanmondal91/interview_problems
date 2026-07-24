package org.interview.coding.graphs;

/**
 * Problem: Number of Islands
 * Difficulty: Medium
 *
 * Description:
 * Given an m x n 2D binary grid where '1' represents land and '0' represents water,
 * return the number of islands. An island is surrounded by water and is formed by
 * connecting adjacent lands horizontally or vertically.
 *
 * Example:
 *   Input: grid = [["1","1","0"],["1","1","0"],["0","0","1"]]
 *   Output: 2
 *
 * Constraints:
 *   - m == grid.length, n == grid[i].length
 *   - 1 <= m, n <= 300
 *   - grid[i][j] is '0' or '1'
 *
 * Approach:
 *   Iterate over every cell. When a '1' is found, increment the island count and perform
 *   DFS flood-fill to mark all connected land cells as visited (set to '0') to avoid
 *   counting them again. The DFS explores all 4 directions (up, down, left, right) recursively.
 *   This in-place modification avoids the need for a separate visited array.
 *
 * Time Complexity: O(m*n) — each cell visited at most once
 * Space Complexity: O(m*n) — recursion stack in worst case
 *
 * Test Cases:
 *   1. Input: 3x3 grid with 2 islands → Output: 2
 *   2. Input: all '1's → Output: 1
 *   3. Edge case: all '0's → Output: 0
 */
public class NumberOfIslands {

    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, int r, int c) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] != '1') return;
        grid[r][c] = '0';
        dfs(grid, r + 1, c);
        dfs(grid, r - 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }

    public static void main(String[] args) {
        NumberOfIslands sol = new NumberOfIslands();

        // Test 1: 2 islands
        char[][] g1 = {
            {'1','1','0','0'},
            {'1','1','0','0'},
            {'0','0','1','0'},
            {'0','0','0','1'}
        };
        System.out.println("Test 1 (expect 3): " + sol.numIslands(g1));

        // Test 2: all land = 1 island
        char[][] g2 = {
            {'1','1','1'},
            {'1','1','1'},
            {'1','1','1'}
        };
        System.out.println("Test 2 (expect 1): " + sol.numIslands(g2));

        // Test 3: all water
        char[][] g3 = {
            {'0','0','0'},
            {'0','0','0'}
        };
        System.out.println("Test 3 (expect 0): " + sol.numIslands(g3));
    }
}
