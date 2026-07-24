package org.interview.system_design.lld.chess;

import java.util.List;

/**
 * Strategy Pattern interface for computing valid moves.
 * Each piece type implements (or delegates to) a MoveStrategy
 * that encapsulates movement rules independently of the Piece class.
 */
public interface MoveStrategy {
    /**
     * Returns all valid destination positions from {@code position} on {@code board}.
     *
     * @param position the piece's current position
     * @param board    the current board state
     * @return list of valid destination positions (may be empty, never null)
     */
    List<Position> getValidMoves(Position position, Board board);
}
