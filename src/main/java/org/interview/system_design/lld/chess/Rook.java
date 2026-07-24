package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Rook — moves any number of squares horizontally or vertically.
 */
public class Rook extends Piece {

    public Rook(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        if (board.isOccupiedByColor(to, color)) return false;
        int rd = to.getRow() - from.getRow();
        int cd = to.getCol() - from.getCol();
        if (rd != 0 && cd != 0) return false; // Must be straight line
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
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
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

    @Override public char symbol() { return color == Color.WHITE ? 'R' : 'r'; }

    @Override public Piece copy() { return new Rook(color, new Position(position.getRow(), position.getCol())); }
}
