package org.interview.system_design.lld.tictactoe;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Demo runner for the Tic-Tac-Toe LLD.
 * Patterns: Strategy (WinStrategy), Composite (Board of Cells)
 *
 * Uses ScriptedPlayer to pre-program move sequences for deterministic testing.
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    // ── ScriptedPlayer: feeds pre-programmed moves for deterministic demo ─────
    static class ScriptedPlayer extends Player {
        private final Deque<int[]> moves;

        ScriptedPlayer(String name, Mark mark, int[]... moveList) {
            super(name, mark);
            this.moves = new ArrayDeque<>();
            for (int[] m : moveList) this.moves.add(m);
        }

        @Override
        public int[] makeMove(Board board) {
            if (!moves.isEmpty()) {
                int[] move = moves.poll();
                System.out.printf("  %s (%s) plays (%d,%d)%n", getName(), getMark(), move[0], move[1]);
                return move;
            }
            // Fallback: first available move
            for (int[] m : board.getAvailableMoves()) return m;
            return new int[]{0, 0};
        }
    }

    public static void main(String[] args) {

        // ── Scenario 1: X wins via top row ────────────────────────────────────
        banner("Scenario 1: X wins via top row (0,0)(0,1)(0,2)");
        // X: (0,0)(0,1)(0,2) | O: (1,0)(1,1)
        Player x1 = new ScriptedPlayer("Alice", Mark.X, new int[]{0,0}, new int[]{0,1}, new int[]{0,2});
        Player o1 = new ScriptedPlayer("Bob",   Mark.O, new int[]{1,0}, new int[]{1,1});
        TicTacToeGame game1 = new TicTacToeGame(x1, o1, new StandardWinStrategy());
        GameState result1 = game1.play();
        System.out.println("  Board state:");
        game1.getBoard().display();
        System.out.println("  Result: " + result1);

        // ── Scenario 2: O wins via main diagonal ──────────────────────────────
        banner("Scenario 2: O wins via main diagonal (0,0)(1,1)(2,2)");
        // X: (0,1)(1,2)(2,1) | O: (0,0)(1,1)(2,2)
        Player x2 = new ScriptedPlayer("Carol", Mark.X, new int[]{0,1}, new int[]{1,2}, new int[]{2,1});
        Player o2 = new ScriptedPlayer("Dave",  Mark.O, new int[]{0,0}, new int[]{1,1}, new int[]{2,2});
        TicTacToeGame game2 = new TicTacToeGame(x2, o2, new StandardWinStrategy());
        GameState result2 = game2.play();
        System.out.println("  Board state:");
        game2.getBoard().display();
        System.out.println("  Result: " + result2);

        // ── Scenario 3: Draw ───────────────────────────────────────────────────
        banner("Scenario 3: Draw -- all cells filled, no winner");
        // Sequence that produces no winner:
        // X O X
        // X X O
        // O X O
        Player x3 = new ScriptedPlayer("Eve",   Mark.X, new int[]{0,0}, new int[]{0,2}, new int[]{1,0}, new int[]{1,1}, new int[]{2,1});
        Player o3 = new ScriptedPlayer("Frank",  Mark.O, new int[]{0,1}, new int[]{1,2}, new int[]{2,0}, new int[]{2,2});
        TicTacToeGame game3 = new TicTacToeGame(x3, o3, new StandardWinStrategy());
        GameState result3 = game3.play();
        System.out.println("  Board state:");
        game3.getBoard().display();
        System.out.println("  Result: " + result3);

        // ── Scenario 4: X wins via right column ───────────────────────────────
        banner("Scenario 4: X wins via right column (0,2)(1,2)(2,2)");
        Player x4 = new ScriptedPlayer("Grace", Mark.X, new int[]{0,2}, new int[]{1,2}, new int[]{2,2});
        Player o4 = new ScriptedPlayer("Hank",  Mark.O, new int[]{0,0}, new int[]{1,0});
        TicTacToeGame game4 = new TicTacToeGame(x4, o4, new StandardWinStrategy());
        GameState result4 = game4.play();
        System.out.println("  Board state:");
        game4.getBoard().display();
        System.out.println("  Result: " + result4);

        // ── Scenario 5: O wins via middle column ─────────────────────────────
        banner("Scenario 5: O wins via middle column (0,1)(1,1)(2,1)");
        Player x5 = new ScriptedPlayer("Ivy",   Mark.X, new int[]{0,0}, new int[]{1,0}, new int[]{2,0});
        Player o5 = new ScriptedPlayer("Jack",  Mark.O, new int[]{0,1}, new int[]{1,1}, new int[]{2,1});
        TicTacToeGame game5 = new TicTacToeGame(x5, o5, new StandardWinStrategy());
        GameState result5 = game5.play();
        System.out.println("  Board state:");
        game5.getBoard().display();
        System.out.println("  Result: " + result5);

        System.out.println();
        System.out.println("=== Tic-Tac-Toe Demo Complete ===");
    }
}
