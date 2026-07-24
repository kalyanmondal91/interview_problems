package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Pawn — moves forward one square (or two from starting rank),
 * captures diagonally. Promotion and en-passant are not modelled for brevity.
 */
public class Pawn extends Piece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRow  = (color == Color.WHITE) ? 1 : 6;

        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        // Standard one-square forward move
        if (colDiff == 0 && rowDiff == direction && !board.isOccupied(to)) {
            return true;
        }
        // Two-square advance from starting rank
        if (colDiff == 0 && rowDiff == 2 * direction && from.getRow() == startRow) {
            Position intermediate = new Position(from.getRow() + direction, from.getCol());
            return !board.isOccupied(intermediate) && !board.isOccupied(to);
        }
        // Diagonal capture
        if (Math.abs(colDiff) == 1 && rowDiff == direction) {
            return board.isOccupied(to) && !board.isOccupiedByColor(to, color);
        }
        return false;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRow  = (color == Color.WHITE) ? 1 : 6;
        int r = position.getRow(), c = position.getCol();

        // Forward
        Position oneStep = new Position(r + direction, c);
        if (oneStep.isValid() && isValidMove(position, oneStep, board)
                && doesNotLeaveKingInCheck(position, oneStep, board)) {
            moves.add(oneStep);
        }
        // Double forward
        if (r == startRow) {
            Position twoStep = new Position(r + 2 * direction, c);
            if (twoStep.isValid() && isValidMove(position, twoStep, board)
                    && doesNotLeaveKingInCheck(position, twoStep, board)) {
                moves.add(twoStep);
            }
        }
        // Diagonal captures
        for (int dc : new int[]{-1, 1}) {
            Position cap = new Position(r + direction, c + dc);
            if (cap.isValid() && isValidMove(position, cap, board)
                    && doesNotLeaveKingInCheck(position, cap, board)) {
                moves.add(cap);
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? 'P' : 'p'; }

    @Override public Piece copy() { return new Pawn(color, new Position(position.getRow(), position.getCol())); }
}
