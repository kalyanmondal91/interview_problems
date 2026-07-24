package org.interview.system_design.lld.tictactoe;

/**
 * Orchestrates the Tic-Tac-Toe game flow.
 * Manages two players, the board, the win strategy, and the game loop.
 *
 * Design patterns used:
 *  - Strategy: WinStrategy is injected and can be swapped
 *  - Composite: Board composes 9 Cell elements
 */
public class TicTacToeGame {
    private final Board board;
    private final Player[] players;
    private final WinStrategy winStrategy;
    private int currentPlayerIndex;
    private GameState gameState;

    public TicTacToeGame(Player player1, Player player2, WinStrategy winStrategy) {
        this.board = new Board();
        this.players = new Player[]{player1, player2};
        this.winStrategy = winStrategy;
        this.currentPlayerIndex = 0;
        this.gameState = GameState.IN_PROGRESS;
    }

    /** Convenience constructor using the standard win strategy. */
    public TicTacToeGame(Player player1, Player player2) {
        this(player1, player2, new StandardWinStrategy());
    }

    /**
     * Runs the game loop until a player wins or the board is full (draw).
     */
    public GameState play() {
        System.out.println("=== Tic-Tac-Toe ===");
        System.out.printf("%s (X) vs %s (O)%n%n", players[0].getName(), players[1].getName());

        while (gameState == GameState.IN_PROGRESS) {
            board.display();
            Player current = players[currentPlayerIndex];
            System.out.println();

            // Get and apply move
            int[] move = current.makeMove(board);
            boolean moved = board.makeMove(move[0], move[1], current.getMark());

            if (!moved) {
                System.out.println("Move failed — trying again.");
                continue;
            }

            // Check terminal conditions
            if (winStrategy.checkWin(board, current.getMark())) {
                board.display();
                gameState = (current.getMark() == Mark.X) ? GameState.X_WINS : GameState.O_WINS;
                System.out.printf("%n%s wins!%n", current.getName());
            } else if (board.isFull()) {
                board.display();
                gameState = GameState.DRAW;
                System.out.println("\nIt's a draw!");
            } else {
                // Switch player
                currentPlayerIndex = 1 - currentPlayerIndex;
            }
        }
        return gameState;
    }

    public GameState getGameState() { return gameState; }
    public Board getBoard() { return board; }

    /**
     * Demo main method: AI vs AI game.
     */
    public static void main(String[] args) {
        Player ai1 = new AIPlayer("AI-X", Mark.X, true);
        Player ai2 = new AIPlayer("AI-O", Mark.O, false);
        TicTacToeGame game = new TicTacToeGame(ai1, ai2);
        GameState result = game.play();
        System.out.println("Final result: " + result);
    }
}
