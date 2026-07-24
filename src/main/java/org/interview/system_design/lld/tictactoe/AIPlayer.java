package org.interview.system_design.lld.tictactoe;

import java.util.List;
import java.util.Random;

/**
 * AI player that uses minimax to play optimally.
 * Falls back to a random available move if minimax is disabled.
 */
public class AIPlayer extends Player {
    private final boolean useMinMax;
    private final WinStrategy winStrategy;
    private final Random random;

    public AIPlayer(String name, Mark mark, boolean useMinMax) {
        super(name, mark);
        this.useMinMax = useMinMax;
        this.winStrategy = new StandardWinStrategy();
        this.random = new Random();
    }

    @Override
    public int[] makeMove(Board board) {
        System.out.printf("%s (%s) is thinking...%n", name, mark);
        if (useMinMax) {
            return minimaxMove(board);
        }
        return randomMove(board);
    }

    /** Returns a random available move. */
    private int[] randomMove(Board board) {
        List<int[]> moves = board.getAvailableMoves();
        return moves.get(random.nextInt(moves.size()));
    }

    /** Uses minimax to find the optimal move. */
    private int[] minimaxMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        for (int[] move : board.getAvailableMoves()) {
            board.makeMove(move[0], move[1], mark);
            int score = minimax(board, false, opponent);
            board.undoMove(move[0], move[1]);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Minimax recursive evaluation.
     *
     * @param isMaximizing true when it is the AI's turn, false for opponent
     */
    private int minimax(Board board, boolean isMaximizing, Mark currentMark) {
        Mark opponent = (mark == Mark.X) ? Mark.O : Mark.X;

        // Terminal state evaluation
        if (winStrategy.checkWin(board, mark)) return 10;
        if (winStrategy.checkWin(board, opponent)) return -10;
        if (board.isFull()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Mark nextMark = isMaximizing ? mark : opponent;

        for (int[] move : board.getAvailableMoves()) {
            board.makeMove(move[0], move[1], nextMark);
            int score = minimax(board, !isMaximizing, currentMark);
            board.undoMove(move[0], move[1]);
            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
            } else {
                bestScore = Math.min(bestScore, score);
            }
        }
        return bestScore;
    }
}
