package org.interview.system_design.lld.chess;

import java.util.List;

/**
 * Manages the full chess game lifecycle:
 *   - Initialises the board with standard piece placement
 *   - Alternates turns between two players
 *   - Validates moves (piece rules + no self-check)
 *   - Detects CHECK, CHECKMATE, and STALEMATE
 *
 * Design Patterns:
 *   • Strategy  — each Piece implements its own MoveStrategy logic
 *   • Composite — Board composes all 64 cells and 32 Piece instances
 */
public class ChessGame {
    private final Board board;
    private final Player white;
    private final Player black;
    private Player currentPlayer;
    private GameState gameState;
    private int moveCount;

    public ChessGame(Player white, Player black) {
        this.white = white;
        this.black = black;
        this.board = new Board();
        this.currentPlayer = white; // White moves first
        this.gameState = GameState.IN_PROGRESS;
        this.moveCount = 0;
        setupBoard();
    }

    // ── Board initialisation ───────────────────────────────────────────────────

    /** Sets up pieces in standard chess starting positions. */
    private void setupBoard() {
        // White pieces (rows 0-1)
        placeMajorPieces(Color.WHITE, 0);
        placePawns(Color.WHITE, 1);
        // Black pieces (rows 7-6)
        placeMajorPieces(Color.BLACK, 7);
        placePawns(Color.BLACK, 6);
    }

    private void placeMajorPieces(Color color, int row) {
        board.setPiece(new Position(row, 0), new Rook(color,   new Position(row, 0)));
        board.setPiece(new Position(row, 1), new Knight(color, new Position(row, 1)));
        board.setPiece(new Position(row, 2), new Bishop(color, new Position(row, 2)));
        board.setPiece(new Position(row, 3), new Queen(color,  new Position(row, 3)));
        board.setPiece(new Position(row, 4), new King(color,   new Position(row, 4)));
        board.setPiece(new Position(row, 5), new Bishop(color, new Position(row, 5)));
        board.setPiece(new Position(row, 6), new Knight(color, new Position(row, 6)));
        board.setPiece(new Position(row, 7), new Rook(color,   new Position(row, 7)));
    }

    private void placePawns(Color color, int row) {
        for (int c = 0; c < 8; c++) {
            board.setPiece(new Position(row, c), new Pawn(color, new Position(row, c)));
        }
    }

    // ── Move execution ─────────────────────────────────────────────────────────

    /**
     * Attempts to move a piece from {@code from} to {@code to}.
     *
     * @param from source square
     * @param to   destination square
     * @return true if the move was successfully made
     */
    public boolean makeMove(Position from, Position to) {
        if (gameState == GameState.CHECKMATE || gameState == GameState.STALEMATE) {
            System.out.println("Game is over: " + gameState);
            return false;
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            System.out.println("No piece at " + from);
            return false;
        }
        if (piece.getColor() != currentPlayer.getColor()) {
            System.out.println("It's " + currentPlayer.getName() + "'s turn (" + currentPlayer.getColor() + ")");
            return false;
        }
        if (!piece.isValidMove(from, to, board)) {
            System.out.println("Invalid move: " + piece + " to " + to);
            return false;
        }
        if (!piece.doesNotLeaveKingInCheck(from, to, board)) {
            System.out.println("Move would leave own king in check: " + from + " -> " + to);
            return false;
        }

        Piece captured = board.movePiece(from, to);
        moveCount++;

        System.out.printf("[Move %d] %s: %s %s → %s%s%n",
                moveCount, currentPlayer.getName(), piece.getClass().getSimpleName(),
                from, to, captured != null ? " (captures " + captured.getClass().getSimpleName() + ")" : "");

        // Update game state
        Color opponent = currentPlayer.getColor().opposite();
        gameState = computeGameState(opponent);

        if (gameState == GameState.CHECK) {
            System.out.println("CHECK! " + opponent + " king is in check.");
        } else if (gameState == GameState.CHECKMATE) {
            System.out.println("CHECKMATE! " + currentPlayer.getName() + " wins!");
        } else if (gameState == GameState.STALEMATE) {
            System.out.println("STALEMATE! It's a draw.");
        }

        // Switch turn
        currentPlayer = (currentPlayer == white) ? black : white;
        return true;
    }

    // ── Game state detection ───────────────────────────────────────────────────

    private GameState computeGameState(Color colorToCheck) {
        boolean inCheck = isInCheck(colorToCheck);
        boolean hasLegalMoves = hasAnyLegalMove(colorToCheck);

        if (inCheck && !hasLegalMoves) return GameState.CHECKMATE;
        if (!inCheck && !hasLegalMoves) return GameState.STALEMATE;
        if (inCheck) return GameState.CHECK;
        return GameState.IN_PROGRESS;
    }

    /** Returns true if the given color's king is currently attacked. */
    public boolean isInCheck(Color color) {
        Position kingPos = board.findKing(color);
        if (kingPos == null) return false;
        Color opponent = color.opposite();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(new Position(r, c));
                if (p != null && p.getColor() == opponent) {
                    if (p.isValidMove(new Position(r, c), kingPos, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Returns true if the given color has at least one legal move available. */
    private boolean hasAnyLegalMove(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(new Position(r, c));
                if (p != null && p.getColor() == color) {
                    List<Position> moves = p.getValidMoves(board);
                    if (!moves.isEmpty()) return true;
                }
            }
        }
        return false;
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public GameState getGameState() { return gameState; }
    public int getMoveCount() { return moveCount; }

    public void printBoard() { board.print(); }
}
