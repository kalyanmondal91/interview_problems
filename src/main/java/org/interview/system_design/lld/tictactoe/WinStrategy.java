package org.interview.system_design.lld.tictactoe;

/**
 * Strategy interface for win-checking logic.
 * Allows plugging in different win conditions (standard, connect-4, etc.).
 */
public interface WinStrategy {
    /**
     * Checks whether the given mark has won on the board.
     *
     * @param board the current board state
     * @param mark  the mark to check (X or O)
     * @return true if the mark has achieved a winning configuration
     */
    boolean checkWin(Board board, Mark mark);
}
