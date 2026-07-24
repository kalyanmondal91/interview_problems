package org.interview.system_design.lld.chess;

import java.util.List;

/**
 * Abstract base class for all chess pieces.
 * Encapsulates color and position; delegates move validation to subclasses.
 * Each subclass also exposes a MoveStrategy for computing reachable squares.
 */
public abstract class Piece {
    protected Color color;
    protected Position position;

    protected Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Returns true if moving this piece from {@code from} to {@code to}
     * is a valid move given the board state (ignoring check).
     */
    public abstract boolean isValidMove(Position from, Position to, Board board);

    /**
     * Returns all positions this piece can legally reach from its current position.
     * Used for check/checkmate detection.
     */
    public abstract List<Position> getValidMoves(Board board);

    /** Single-char symbol for board display (uppercase = WHITE, lowercase = BLACK). */
    public abstract char symbol();

    /** Creates a deep copy of this piece at the same position. */
    public abstract Piece copy();

    // ── Shared helpers ─────────────────────────────────────────────────────────

    /**
     * Returns true if moving from {@code from} to {@code to} does NOT put the
     * moving side's own king in check. Used for final legality validation.
     */
    public boolean doesNotLeaveKingInCheck(Position from, Position to, Board board) {
        Board simBoard = board.copy();
        simBoard.movePiece(from, to);
        Position kingPos = simBoard.findKing(color);
        if (kingPos == null) return false;
        // Check if any opponent piece attacks the king's position
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = simBoard.getPiece(new Position(r, c));
                if (p != null && p.getColor() != color) {
                    if (p.isValidMove(new Position(r, c), kingPos, simBoard)) {
                        return false; // King would be in check
                    }
                }
            }
        }
        return true;
    }

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    @Override
    public String toString() {
        return color + " " + getClass().getSimpleName() + "@" + position;
    }
}
