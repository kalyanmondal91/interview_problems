package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight — moves in an L-shape (2+1 squares). Jumps over other pieces.
 */
public class Knight extends Piece {

    private static final int[][] OFFSETS = {
            {2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}
    };

    public Knight(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        if (board.isOccupiedByColor(to, color)) return false;
        int rd = from.rowDiff(to), cd = from.colDiff(to);
        return (rd == 2 && cd == 1) || (rd == 1 && cd == 2);
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        for (int[] off : OFFSETS) {
            Position to = new Position(position.getRow() + off[0], position.getCol() + off[1]);
            if (isValidMove(position, to, board) && doesNotLeaveKingInCheck(position, to, board)) {
                moves.add(to);
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? 'N' : 'n'; }

    @Override public Piece copy() { return new Knight(color, new Position(position.getRow(), position.getCol())); }
}
