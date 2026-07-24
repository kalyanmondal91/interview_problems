package org.interview.system_design.lld.tictactoe;

/**
 * Represents a single cell on the Tic-Tac-Toe board.
 * Part of the Composite pattern — leaf element.
 */
public class Cell {
    private final int row;
    private final int col;
    private Mark mark;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.mark = Mark.EMPTY;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Mark getMark() { return mark; }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public boolean isEmpty() {
        return mark == Mark.EMPTY;
    }

    @Override
    public String toString() {
        return mark.toString();
    }
}
