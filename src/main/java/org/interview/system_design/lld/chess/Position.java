package org.interview.system_design.lld.chess;

import java.util.Objects;

/**
 * Represents a square on the chess board (0-indexed, row 0 = rank 1, col 0 = file a).
 */
public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** Returns true if the position is within the 8x8 board. */
    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public int rowDiff(Position other) { return Math.abs(this.row - other.row); }
    public int colDiff(Position other) { return Math.abs(this.col - other.col); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position that = (Position) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() { return Objects.hash(row, col); }

    @Override
    public String toString() {
        return "" + (char) ('a' + col) + (row + 1);
    }
}
