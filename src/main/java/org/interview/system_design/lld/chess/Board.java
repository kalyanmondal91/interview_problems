package org.interview.system_design.lld.chess;

/**
 * Represents the 8x8 chess board.
 * Board[row][col] where row 0 = rank 1, col 0 = file a (White's perspective).
 */
public class Board {
    private final Piece[][] grid;

    public Board() {
        grid = new Piece[8][8];
    }

    /** Returns the piece at the given position, or null if empty. */
    public Piece getPiece(Position pos) {
        if (!pos.isValid()) return null;
        return grid[pos.getRow()][pos.getCol()];
    }

    /** Places a piece at the given position (null to clear). */
    public void setPiece(Position pos, Piece piece) {
        if (!pos.isValid()) throw new IllegalArgumentException("Invalid position: " + pos);
        grid[pos.getRow()][pos.getCol()] = piece;
        if (piece != null) piece.setPosition(pos);
    }

    /** Returns true if a piece occupies the given position. */
    public boolean isOccupied(Position pos) {
        return getPiece(pos) != null;
    }

    /**
     * Returns true if a piece of the given color occupies the position.
     *
     * @param pos   board position to check
     * @param color the color to check for
     */
    public boolean isOccupiedByColor(Position pos, Color color) {
        Piece p = getPiece(pos);
        return p != null && p.getColor() == color;
    }

    /**
     * Moves a piece from {@code from} to {@code to}, capturing any occupant.
     * Does NOT validate legality — callers must check validity first.
     *
     * @return the captured piece, or null if no capture
     */
    public Piece movePiece(Position from, Position to) {
        Piece moving = getPiece(from);
        if (moving == null) throw new IllegalArgumentException("No piece at " + from);
        Piece captured = getPiece(to);
        grid[from.getRow()][from.getCol()] = null;
        grid[to.getRow()][to.getCol()] = moving;
        moving.setPosition(to);
        return captured;
    }

    /** Returns the position of the king of the given color, or null if not found. */
    public Position findKing(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King && p.getColor() == color) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }

    /** Returns a deep copy of this board for simulation (e.g., check detection). */
    public Board copy() {
        Board copy = new Board();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null) {
                    copy.grid[r][c] = p.copy();
                }
            }
        }
        return copy;
    }

    /**
     * Prints a simple ASCII representation of the board for debugging.
     */
    public void print() {
        System.out.println("  a b c d e f g h");
        for (int r = 7; r >= 0; r--) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                System.out.print((p == null ? "." : p.symbol()) + " ");
            }
            System.out.println();
        }
    }
}
