package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Bishop — moves any number of squares diagonally.
 */
public class Bishop extends Piece {

    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        if (board.isOccupiedByColor(to, color)) return false;
        if (from.rowDiff(to) != from.colDiff(to)) return false; // Must be diagonal
        return isPathClear(from, to, board);
    }

    private boolean isPathClear(Position from, Position to, Board board) {
        int rowStep = Integer.signum(to.getRow() - from.getRow());
        int colStep = Integer.signum(to.getCol() - from.getCol());
        int r = from.getRow() + rowStep, c = from.getCol() + colStep;
        while (r != to.getRow() || c != to.getCol()) {
            if (board.isOccupied(new Position(r, c))) return false;
            r += rowStep; c += colStep;
        }
        return true;
    }

    @Override
    public List<Position> getValidMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] d : directions) {
            int r = position.getRow() + d[0], c = position.getCol() + d[1];
            while (new Position(r, c).isValid()) {
                Position to = new Position(r, c);
                if (board.isOccupiedByColor(to, color)) break;
                if (doesNotLeaveKingInCheck(position, to, board)) moves.add(to);
                if (board.isOccupied(to)) break;
                r += d[0]; c += d[1];
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? 'B' : 'b'; }

    @Override public Piece copy() { return new Bishop(color, new Position(position.getRow(), position.getCol())); }
}
