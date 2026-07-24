package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * King — moves exactly one square in any direction.
 * Cannot move into check.
 */
public class King extends Piece {

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        if (board.isOccupiedByColor(to, color)) return false;
        int rd = from.rowDiff(to);
        int cd = from.colDiff(to);
        // King moves at most 1 square in any direction
        return rd <= 1 && cd <= 1 && (rd + cd) > 0;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int r = position.getRow(), c = position.getCol();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Position to = new Position(r + dr, c + dc);
                if (isValidMove(position, to, board) && doesNotLeaveKingInCheck(position, to, board)) {
                    moves.add(to);
                }
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? 'K' : 'k'; }

    @Override public Piece copy() { return new King(color, new Position(position.getRow(), position.getCol())); }
}
