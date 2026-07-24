package org.interview.system_design.lld.chess;

/**
 * Demo runner for the Chess Game LLD.
 * Patterns: Strategy (per-piece move rules), Composite (Board)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // -- Scenario 1: Start game, display initial board --
        banner("Scenario 1: Initial board setup");
        Player alice = new Player("Alice", Color.WHITE);
        Player bob   = new Player("Bob",   Color.BLACK);
        ChessGame game = new ChessGame(alice, bob);
        game.printBoard();
        System.out.println("  Current player: " + game.getCurrentPlayer().getName()
                + " (" + game.getCurrentPlayer().getColor() + ")");

        // -- Scenario 2: Valid opening moves --
        banner("Scenario 2: Valid opening moves (e2->e4, e7->e5)");
        // Board uses row 0=rank1, row 7=rank8; White pawns at row 1, Black pawns at row 6
        boolean m1 = game.makeMove(new Position(1, 4), new Position(3, 4)); // White pawn e2->e4
        System.out.println("  White pawn e2->e4: " + (m1 ? "OK" : "REJECTED"));
        game.printBoard();

        boolean m2 = game.makeMove(new Position(6, 4), new Position(4, 4)); // Black pawn e7->e5
        System.out.println("  Black pawn e7->e5: " + (m2 ? "OK" : "REJECTED"));

        // -- Scenario 3: Illegal move -- move to own piece's square --
        banner("Scenario 3: Illegal move -- pawn tries to capture own piece");
        boolean illegal = game.makeMove(new Position(1, 3), new Position(1, 4)); // d2->e2 (occupied by own)
        System.out.println("  d2->e2 (own piece): " + (illegal ? "ACCEPTED (wrong!)" : "REJECTED (correct)"));

        // -- Scenario 4: Move out of turn --
        banner("Scenario 4: Move out of turn (White tries to move twice)");
        // It's White's turn now
        boolean validMove = game.makeMove(new Position(1, 3), new Position(3, 3)); // d2->d4
        System.out.println("  White d2->d4: " + (validMove ? "OK" : "REJECTED"));
        // White again -- should be rejected
        boolean outOfTurn = game.makeMove(new Position(1, 2), new Position(3, 2)); // c2->c4 (White again)
        System.out.println("  White c2->c4 (out of turn): " + (outOfTurn ? "ACCEPTED (wrong!)" : "REJECTED (correct)"));

        // -- Scenario 5: Scholar's Mate -- fastest checkmate (4 moves) --
        banner("Scenario 5: Scholar's Mate sequence");
        Player scholar = new Player("Scholar", Color.WHITE);
        Player victim  = new Player("Victim",  Color.BLACK);
        ChessGame game2 = new ChessGame(scholar, victim);
        // 1. e4 / e5
        game2.makeMove(new Position(1, 4), new Position(3, 4));  // e2->e4
        game2.makeMove(new Position(6, 4), new Position(4, 4));  // e7->e5
        // 2. Bc4 / Nc6
        game2.makeMove(new Position(0, 5), new Position(3, 2));  // Bf1->c4
        game2.makeMove(new Position(7, 1), new Position(5, 2));  // Nb8->c6
        // 3. Qh5 / Nf6
        game2.makeMove(new Position(0, 3), new Position(4, 7));  // Qd1->h5
        game2.makeMove(new Position(7, 6), new Position(5, 5));  // Ng8->f6
        // 4. Qxf7# (checkmate)
        boolean mate = game2.makeMove(new Position(4, 7), new Position(6, 5)); // Qh5->f7#
        System.out.println("  Qxf7#: " + (mate ? "Move made" : "Rejected"));
        System.out.println("  Game state: " + game2.getGameState());
        game2.printBoard();

        banner("Chess Game Demo Complete");
    }
}
