package org.interview.system_design.lld.tictactoe;

/**
 * Standard win strategy: checks all rows, columns, and both diagonals.
 * Implements the Strategy pattern for win detection.
 */
public class StandardWinStrategy implements WinStrategy {

    @Override
    public boolean checkWin(Board board, Mark mark) {
        int size = board.getSize();

        // Check all rows
        for (int r = 0; r < size; r++) {
            if (checkRow(board, r, mark, size)) return true;
        }

        // Check all columns
        for (int c = 0; c < size; c++) {
            if (checkCol(board, c, mark, size)) return true;
        }

        // Check main diagonal (top-left to bottom-right)
        if (checkMainDiagonal(board, mark, size)) return true;

        // Check anti-diagonal (top-right to bottom-left)
        return checkAntiDiagonal(board, mark, size);
    }

    private boolean checkRow(Board board, int row, Mark mark, int size) {
        for (int c = 0; c < size; c++) {
            if (board.getMarkAt(row, c) != mark) return false;
        }
        return true;
    }

    private boolean checkCol(Board board, int col, Mark mark, int size) {
        for (int r = 0; r < size; r++) {
            if (board.getMarkAt(r, col) != mark) return false;
        }
        return true;
    }

    private boolean checkMainDiagonal(Board board, Mark mark, int size) {
        for (int i = 0; i < size; i++) {
            if (board.getMarkAt(i, i) != mark) return false;
        }
        return true;
    }

    private boolean checkAntiDiagonal(Board board, Mark mark, int size) {
        for (int i = 0; i < size; i++) {
            if (board.getMarkAt(i, size - 1 - i) != mark) return false;
        }
        return true;
    }
}
