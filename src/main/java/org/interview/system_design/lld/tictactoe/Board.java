package org.interview.system_design.lld.tictactoe;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite class representing the 3x3 Tic-Tac-Toe board.
 * Acts as the composite node containing 9 Cell (leaf) elements.
 */
public class Board {
    private static final int SIZE = 3;
    private final Cell[][] cells;

    public Board() {
        cells = new Cell[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
    }

    /**
     * Places a mark on the cell at (row, col).
     *
     * @return true if successful, false if cell already occupied or out of bounds
     */
    public boolean makeMove(int row, int col, Mark mark) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return false;
        if (!cells[row][col].isEmpty()) return false;
        cells[row][col].setMark(mark);
        return true;
    }

    /** Returns the mark at position (row, col). */
    public Mark getMarkAt(int row, int col) {
        return cells[row][col].getMark();
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public int getSize() { return SIZE; }

    /** Returns all cells that are still empty (available moves). */
    public List<int[]> getAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c].isEmpty()) {
                    moves.add(new int[]{r, c});
                }
            }
        }
        return moves;
    }

    /** Returns true if every cell has been filled. */
    public boolean isFull() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (cells[r][c].isEmpty()) return false;
            }
        }
        return true;
    }

    /** Undoes a move — sets cell back to EMPTY (used by minimax). */
    public void undoMove(int row, int col) {
        cells[row][col].setMark(Mark.EMPTY);
    }

    /** Prints the board to standard output. */
    public void display() {
        System.out.println("  0 1 2");
        for (int r = 0; r < SIZE; r++) {
            System.out.print(r + " ");
            for (int c = 0; c < SIZE; c++) {
                System.out.print(cells[r][c]);
                if (c < SIZE - 1) System.out.print("|");
            }
            System.out.println();
            if (r < SIZE - 1) System.out.println("  -----");
        }
    }
}
