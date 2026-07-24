---
layout: lld
render_with_liquid: false
title: "Chess Game"
system: chess
description: "LLD of Chess Game"
files:
  - "Bishop.java"
  - "Board.java"
  - "ChessGame.java"
  - "Color.java"
  - "GameState.java"
  - "King.java"
  - "Knight.java"
  - "Main.java"
  - "MoveStrategy.java"
  - "Pawn.java"
  - "Piece.java"
  - "Player.java"
  - "Position.java"
  - "Queen.java"
  - "Rook.java"
---

## Chess Game

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="Bishop.java">Bishop.java</button>
<button class="tab-btn" data-tab="Board.java">Board.java</button>
<button class="tab-btn" data-tab="ChessGame.java">ChessGame.java</button>
<button class="tab-btn" data-tab="Color.java">Color.java</button>
<button class="tab-btn" data-tab="GameState.java">GameState.java</button>
<button class="tab-btn" data-tab="King.java">King.java</button>
<button class="tab-btn" data-tab="Knight.java">Knight.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="MoveStrategy.java">MoveStrategy.java</button>
<button class="tab-btn" data-tab="Pawn.java">Pawn.java</button>
<button class="tab-btn" data-tab="Piece.java">Piece.java</button>
<button class="tab-btn" data-tab="Player.java">Player.java</button>
<button class="tab-btn" data-tab="Position.java">Position.java</button>
<button class="tab-btn" data-tab="Queen.java">Queen.java</button>
<button class="tab-btn" data-tab="Rook.java">Rook.java</button>
</div>
<div class="tab-content active" id="Bishop-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

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
    public List&lt;Position&gt; getValidMoves(Board board) {
        List&lt;Position&gt; moves = new ArrayList&lt;&gt;();
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

    @Override public char symbol() { return color == Color.WHITE ? &#x27;B&#x27; : &#x27;b&#x27;; }

    @Override public Piece copy() { return new Bishop(color, new Position(position.getRow(), position.getCol())); }
}</code></pre>
</div>
<div class="tab-content" id="Board-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

/**
 * Represents the 8x8 chess board.
 * Board[row][col] where row 0 = rank 1, col 0 = file a (White&#x27;s perspective).
 */
public class Board {
    private final Piece[][] grid;

    public Board() {
        grid = new Piece[8][8];
    }

    /** Returns the piece at the given position, or null if empty. */
    public Piece getPiece(Position pos) {
        if (!pos.isValid()) return null;
        return grid[pos.getRow()][pos.getCol()];
    }

    /** Places a piece at the given position (null to clear). */
    public void setPiece(Position pos, Piece piece) {
        if (!pos.isValid()) throw new IllegalArgumentException(&quot;Invalid position: &quot; + pos);
        grid[pos.getRow()][pos.getCol()] = piece;
        if (piece != null) piece.setPosition(pos);
    }

    /** Returns true if a piece occupies the given position. */
    public boolean isOccupied(Position pos) {
        return getPiece(pos) != null;
    }

    /**
     * Returns true if a piece of the given color occupies the position.
     *
     * @param pos   board position to check
     * @param color the color to check for
     */
    public boolean isOccupiedByColor(Position pos, Color color) {
        Piece p = getPiece(pos);
        return p != null &amp;&amp; p.getColor() == color;
    }

    /**
     * Moves a piece from {@code from} to {@code to}, capturing any occupant.
     * Does NOT validate legality — callers must check validity first.
     *
     * @return the captured piece, or null if no capture
     */
    public Piece movePiece(Position from, Position to) {
        Piece moving = getPiece(from);
        if (moving == null) throw new IllegalArgumentException(&quot;No piece at &quot; + from);
        Piece captured = getPiece(to);
        grid[from.getRow()][from.getCol()] = null;
        grid[to.getRow()][to.getCol()] = moving;
        moving.setPosition(to);
        return captured;
    }

    /** Returns the position of the king of the given color, or null if not found. */
    public Position findKing(Color color) {
        for (int r = 0; r &lt; 8; r++) {
            for (int c = 0; c &lt; 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King &amp;&amp; p.getColor() == color) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }

    /** Returns a deep copy of this board for simulation (e.g., check detection). */
    public Board copy() {
        Board copy = new Board();
        for (int r = 0; r &lt; 8; r++) {
            for (int c = 0; c &lt; 8; c++) {
                Piece p = grid[r][c];
                if (p != null) {
                    copy.grid[r][c] = p.copy();
                }
            }
        }
        return copy;
    }

    /**
     * Prints a simple ASCII representation of the board for debugging.
     */
    public void print() {
        System.out.println(&quot;  a b c d e f g h&quot;);
        for (int r = 7; r &gt;= 0; r--) {
            System.out.print((r + 1) + &quot; &quot;);
            for (int c = 0; c &lt; 8; c++) {
                Piece p = grid[r][c];
                System.out.print((p == null ? &quot;.&quot; : p.symbol()) + &quot; &quot;);
            }
            System.out.println();
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="ChessGame-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

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
        for (int c = 0; c &lt; 8; c++) {
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
            System.out.println(&quot;Game is over: &quot; + gameState);
            return false;
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            System.out.println(&quot;No piece at &quot; + from);
            return false;
        }
        if (piece.getColor() != currentPlayer.getColor()) {
            System.out.println(&quot;It&#x27;s &quot; + currentPlayer.getName() + &quot;&#x27;s turn (&quot; + currentPlayer.getColor() + &quot;)&quot;);
            return false;
        }
        if (!piece.isValidMove(from, to, board)) {
            System.out.println(&quot;Invalid move: &quot; + piece + &quot; to &quot; + to);
            return false;
        }
        if (!piece.doesNotLeaveKingInCheck(from, to, board)) {
            System.out.println(&quot;Move would leave own king in check: &quot; + from + &quot; -&gt; &quot; + to);
            return false;
        }

        Piece captured = board.movePiece(from, to);
        moveCount++;

        System.out.printf(&quot;[Move %d] %s: %s %s → %s%s%n&quot;,
                moveCount, currentPlayer.getName(), piece.getClass().getSimpleName(),
                from, to, captured != null ? &quot; (captures &quot; + captured.getClass().getSimpleName() + &quot;)&quot; : &quot;&quot;);

        // Update game state
        Color opponent = currentPlayer.getColor().opposite();
        gameState = computeGameState(opponent);

        if (gameState == GameState.CHECK) {
            System.out.println(&quot;CHECK! &quot; + opponent + &quot; king is in check.&quot;);
        } else if (gameState == GameState.CHECKMATE) {
            System.out.println(&quot;CHECKMATE! &quot; + currentPlayer.getName() + &quot; wins!&quot;);
        } else if (gameState == GameState.STALEMATE) {
            System.out.println(&quot;STALEMATE! It&#x27;s a draw.&quot;);
        }

        // Switch turn
        currentPlayer = (currentPlayer == white) ? black : white;
        return true;
    }

    // ── Game state detection ───────────────────────────────────────────────────

    private GameState computeGameState(Color colorToCheck) {
        boolean inCheck = isInCheck(colorToCheck);
        boolean hasLegalMoves = hasAnyLegalMove(colorToCheck);

        if (inCheck &amp;&amp; !hasLegalMoves) return GameState.CHECKMATE;
        if (!inCheck &amp;&amp; !hasLegalMoves) return GameState.STALEMATE;
        if (inCheck) return GameState.CHECK;
        return GameState.IN_PROGRESS;
    }

    /** Returns true if the given color&#x27;s king is currently attacked. */
    public boolean isInCheck(Color color) {
        Position kingPos = board.findKing(color);
        if (kingPos == null) return false;
        Color opponent = color.opposite();
        for (int r = 0; r &lt; 8; r++) {
            for (int c = 0; c &lt; 8; c++) {
                Piece p = board.getPiece(new Position(r, c));
                if (p != null &amp;&amp; p.getColor() == opponent) {
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
        for (int r = 0; r &lt; 8; r++) {
            for (int c = 0; c &lt; 8; c++) {
                Piece p = board.getPiece(new Position(r, c));
                if (p != null &amp;&amp; p.getColor() == color) {
                    List&lt;Position&gt; moves = p.getValidMoves(board);
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
}</code></pre>
</div>
<div class="tab-content" id="Color-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

/**
 * Represents the color of a chess piece or player.
 */
public enum Color {
    WHITE,
    BLACK;

    /** Returns the opposite color. */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}</code></pre>
</div>
<div class="tab-content" id="GameState-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

/**
 * Enum representing the current state of a chess game.
 */
public enum GameState {
    IN_PROGRESS,
    CHECK,
    CHECKMATE,
    STALEMATE,
    DRAW
}</code></pre>
</div>
<div class="tab-content" id="King-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * King — moves exactly one square in any direction.
 * Cannot move into check.
 */
public class King extends Piece {

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        if (board.isOccupiedByColor(to, color)) return false;
        int rd = from.rowDiff(to);
        int cd = from.colDiff(to);
        // King moves at most 1 square in any direction
        return rd &lt;= 1 &amp;&amp; cd &lt;= 1 &amp;&amp; (rd + cd) &gt; 0;
    }

    @Override
    public List&lt;Position&gt; getValidMoves(Board board) {
        List&lt;Position&gt; moves = new ArrayList&lt;&gt;();
        int r = position.getRow(), c = position.getCol();
        for (int dr = -1; dr &lt;= 1; dr++) {
            for (int dc = -1; dc &lt;= 1; dc++) {
                if (dr == 0 &amp;&amp; dc == 0) continue;
                Position to = new Position(r + dr, c + dc);
                if (isValidMove(position, to, board) &amp;&amp; doesNotLeaveKingInCheck(position, to, board)) {
                    moves.add(to);
                }
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? &#x27;K&#x27; : &#x27;k&#x27;; }

    @Override public Piece copy() { return new King(color, new Position(position.getRow(), position.getCol())); }
}</code></pre>
</div>
<div class="tab-content" id="Knight-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

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
        return (rd == 2 &amp;&amp; cd == 1) || (rd == 1 &amp;&amp; cd == 2);
    }

    @Override
    public List&lt;Position&gt; getValidMoves(Board board) {
        List&lt;Position&gt; moves = new ArrayList&lt;&gt;();
        for (int[] off : OFFSETS) {
            Position to = new Position(position.getRow() + off[0], position.getCol() + off[1]);
            if (isValidMove(position, to, board) &amp;&amp; doesNotLeaveKingInCheck(position, to, board)) {
                moves.add(to);
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? &#x27;N&#x27; : &#x27;n&#x27;; }

    @Override public Piece copy() { return new Knight(color, new Position(position.getRow(), position.getCol())); }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

/**
 * Demo runner for the Chess Game LLD.
 * Patterns: Strategy (per-piece move rules), Composite (Board)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) {

        // -- Scenario 1: Start game, display initial board --
        banner(&quot;Scenario 1: Initial board setup&quot;);
        Player alice = new Player(&quot;Alice&quot;, Color.WHITE);
        Player bob   = new Player(&quot;Bob&quot;,   Color.BLACK);
        ChessGame game = new ChessGame(alice, bob);
        game.printBoard();
        System.out.println(&quot;  Current player: &quot; + game.getCurrentPlayer().getName()
                + &quot; (&quot; + game.getCurrentPlayer().getColor() + &quot;)&quot;);

        // -- Scenario 2: Valid opening moves --
        banner(&quot;Scenario 2: Valid opening moves (e2-&gt;e4, e7-&gt;e5)&quot;);
        // Board uses row 0=rank1, row 7=rank8; White pawns at row 1, Black pawns at row 6
        boolean m1 = game.makeMove(new Position(1, 4), new Position(3, 4)); // White pawn e2-&gt;e4
        System.out.println(&quot;  White pawn e2-&gt;e4: &quot; + (m1 ? &quot;OK&quot; : &quot;REJECTED&quot;));
        game.printBoard();

        boolean m2 = game.makeMove(new Position(6, 4), new Position(4, 4)); // Black pawn e7-&gt;e5
        System.out.println(&quot;  Black pawn e7-&gt;e5: &quot; + (m2 ? &quot;OK&quot; : &quot;REJECTED&quot;));

        // -- Scenario 3: Illegal move -- move to own piece&#x27;s square --
        banner(&quot;Scenario 3: Illegal move -- pawn tries to capture own piece&quot;);
        boolean illegal = game.makeMove(new Position(1, 3), new Position(1, 4)); // d2-&gt;e2 (occupied by own)
        System.out.println(&quot;  d2-&gt;e2 (own piece): &quot; + (illegal ? &quot;ACCEPTED (wrong!)&quot; : &quot;REJECTED (correct)&quot;));

        // -- Scenario 4: Move out of turn --
        banner(&quot;Scenario 4: Move out of turn (White tries to move twice)&quot;);
        // It&#x27;s White&#x27;s turn now
        boolean validMove = game.makeMove(new Position(1, 3), new Position(3, 3)); // d2-&gt;d4
        System.out.println(&quot;  White d2-&gt;d4: &quot; + (validMove ? &quot;OK&quot; : &quot;REJECTED&quot;));
        // White again -- should be rejected
        boolean outOfTurn = game.makeMove(new Position(1, 2), new Position(3, 2)); // c2-&gt;c4 (White again)
        System.out.println(&quot;  White c2-&gt;c4 (out of turn): &quot; + (outOfTurn ? &quot;ACCEPTED (wrong!)&quot; : &quot;REJECTED (correct)&quot;));

        // -- Scenario 5: Scholar&#x27;s Mate -- fastest checkmate (4 moves) --
        banner(&quot;Scenario 5: Scholar&#x27;s Mate sequence&quot;);
        Player scholar = new Player(&quot;Scholar&quot;, Color.WHITE);
        Player victim  = new Player(&quot;Victim&quot;,  Color.BLACK);
        ChessGame game2 = new ChessGame(scholar, victim);
        // 1. e4 / e5
        game2.makeMove(new Position(1, 4), new Position(3, 4));  // e2-&gt;e4
        game2.makeMove(new Position(6, 4), new Position(4, 4));  // e7-&gt;e5
        // 2. Bc4 / Nc6
        game2.makeMove(new Position(0, 5), new Position(3, 2));  // Bf1-&gt;c4
        game2.makeMove(new Position(7, 1), new Position(5, 2));  // Nb8-&gt;c6
        // 3. Qh5 / Nf6
        game2.makeMove(new Position(0, 3), new Position(4, 7));  // Qd1-&gt;h5
        game2.makeMove(new Position(7, 6), new Position(5, 5));  // Ng8-&gt;f6
        // 4. Qxf7# (checkmate)
        boolean mate = game2.makeMove(new Position(4, 7), new Position(6, 5)); // Qh5-&gt;f7#
        System.out.println(&quot;  Qxf7#: &quot; + (mate ? &quot;Move made&quot; : &quot;Rejected&quot;));
        System.out.println(&quot;  Game state: &quot; + game2.getGameState());
        game2.printBoard();

        banner(&quot;Chess Game Demo Complete&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="MoveStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

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
     * @param position the piece&#x27;s current position
     * @param board    the current board state
     * @return list of valid destination positions (may be empty, never null)
     */
    List&lt;Position&gt; getValidMoves(Position position, Board board);
}</code></pre>
</div>
<div class="tab-content" id="Pawn-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Pawn — moves forward one square (or two from starting rank),
 * captures diagonally. Promotion and en-passant are not modelled for brevity.
 */
public class Pawn extends Piece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRow  = (color == Color.WHITE) ? 1 : 6;

        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        // Standard one-square forward move
        if (colDiff == 0 &amp;&amp; rowDiff == direction &amp;&amp; !board.isOccupied(to)) {
            return true;
        }
        // Two-square advance from starting rank
        if (colDiff == 0 &amp;&amp; rowDiff == 2 * direction &amp;&amp; from.getRow() == startRow) {
            Position intermediate = new Position(from.getRow() + direction, from.getCol());
            return !board.isOccupied(intermediate) &amp;&amp; !board.isOccupied(to);
        }
        // Diagonal capture
        if (Math.abs(colDiff) == 1 &amp;&amp; rowDiff == direction) {
            return board.isOccupied(to) &amp;&amp; !board.isOccupiedByColor(to, color);
        }
        return false;
    }

    @Override
    public List&lt;Position&gt; getValidMoves(Board board) {
        List&lt;Position&gt; moves = new ArrayList&lt;&gt;();
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRow  = (color == Color.WHITE) ? 1 : 6;
        int r = position.getRow(), c = position.getCol();

        // Forward
        Position oneStep = new Position(r + direction, c);
        if (oneStep.isValid() &amp;&amp; isValidMove(position, oneStep, board)
                &amp;&amp; doesNotLeaveKingInCheck(position, oneStep, board)) {
            moves.add(oneStep);
        }
        // Double forward
        if (r == startRow) {
            Position twoStep = new Position(r + 2 * direction, c);
            if (twoStep.isValid() &amp;&amp; isValidMove(position, twoStep, board)
                    &amp;&amp; doesNotLeaveKingInCheck(position, twoStep, board)) {
                moves.add(twoStep);
            }
        }
        // Diagonal captures
        for (int dc : new int[]{-1, 1}) {
            Position cap = new Position(r + direction, c + dc);
            if (cap.isValid() &amp;&amp; isValidMove(position, cap, board)
                    &amp;&amp; doesNotLeaveKingInCheck(position, cap, board)) {
                moves.add(cap);
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? &#x27;P&#x27; : &#x27;p&#x27;; }

    @Override public Piece copy() { return new Pawn(color, new Position(position.getRow(), position.getCol())); }
}</code></pre>
</div>
<div class="tab-content" id="Piece-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

import java.util.List;

/**
 * Abstract base class for all chess pieces.
 * Encapsulates color and position; delegates move validation to subclasses.
 * Each subclass also exposes a MoveStrategy for computing reachable squares.
 */
public abstract class Piece {
    protected Color color;
    protected Position position;

    protected Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Returns true if moving this piece from {@code from} to {@code to}
     * is a valid move given the board state (ignoring check).
     */
    public abstract boolean isValidMove(Position from, Position to, Board board);

    /**
     * Returns all positions this piece can legally reach from its current position.
     * Used for check/checkmate detection.
     */
    public abstract List&lt;Position&gt; getValidMoves(Board board);

    /** Single-char symbol for board display (uppercase = WHITE, lowercase = BLACK). */
    public abstract char symbol();

    /** Creates a deep copy of this piece at the same position. */
    public abstract Piece copy();

    // ── Shared helpers ─────────────────────────────────────────────────────────

    /**
     * Returns true if moving from {@code from} to {@code to} does NOT put the
     * moving side&#x27;s own king in check. Used for final legality validation.
     */
    public boolean doesNotLeaveKingInCheck(Position from, Position to, Board board) {
        Board simBoard = board.copy();
        simBoard.movePiece(from, to);
        Position kingPos = simBoard.findKing(color);
        if (kingPos == null) return false;
        // Check if any opponent piece attacks the king&#x27;s position
        for (int r = 0; r &lt; 8; r++) {
            for (int c = 0; c &lt; 8; c++) {
                Piece p = simBoard.getPiece(new Position(r, c));
                if (p != null &amp;&amp; p.getColor() != color) {
                    if (p.isValidMove(new Position(r, c), kingPos, simBoard)) {
                        return false; // King would be in check
                    }
                }
            }
        }
        return true;
    }

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    @Override
    public String toString() {
        return color + &quot; &quot; + getClass().getSimpleName() + &quot;@&quot; + position;
    }
}</code></pre>
</div>
<div class="tab-content" id="Player-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

/**
 * Represents a chess player with a name and assigned color.
 */
public class Player {
    private final String name;
    private final Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() { return name; }
    public Color getColor() { return color; }

    @Override
    public String toString() { return name + &quot;(&quot; + color + &quot;)&quot;; }
}</code></pre>
</div>
<div class="tab-content" id="Position-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

import java.util.Objects;

/**
 * Represents a square on the chess board (0-indexed, row 0 = rank 1, col 0 = file a).
 */
public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** Returns true if the position is within the 8x8 board. */
    public boolean isValid() {
        return row &gt;= 0 &amp;&amp; row &lt; 8 &amp;&amp; col &gt;= 0 &amp;&amp; col &lt; 8;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public int rowDiff(Position other) { return Math.abs(this.row - other.row); }
    public int colDiff(Position other) { return Math.abs(this.col - other.col); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position that = (Position) o;
        return row == that.row &amp;&amp; col == that.col;
    }

    @Override
    public int hashCode() { return Objects.hash(row, col); }

    @Override
    public String toString() {
        return &quot;&quot; + (char) (&#x27;a&#x27; + col) + (row + 1);
    }
}</code></pre>
</div>
<div class="tab-content" id="Queen-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen — combines Rook and Bishop movement (any number of squares in 8 directions).
 */
public class Queen extends Piece {

    public Queen(Color color, Position position) {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (!to.isValid()) return false;
        if (board.isOccupiedByColor(to, color)) return false;
        int rd = to.getRow() - from.getRow();
        int cd = to.getCol() - from.getCol();
        boolean straightLine = rd == 0 || cd == 0;
        boolean diagonal     = Math.abs(rd) == Math.abs(cd);
        if (!straightLine &amp;&amp; !diagonal) return false;
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
    public List&lt;Position&gt; getValidMoves(Board board) {
        List&lt;Position&gt; moves = new ArrayList&lt;&gt;();
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] d : directions) {
            int r = position.getRow() + d[0], c = position.getCol() + d[1];
            while (new Position(r, c).isValid()) {
                Position to = new Position(r, c);
                if (board.isOccupiedByColor(to, color)) break;
                if (isValidMove(position, to, board) &amp;&amp; doesNotLeaveKingInCheck(position, to, board)) {
                    moves.add(to);
                }
                if (board.isOccupied(to)) break;
                r += d[0]; c += d[1];
            }
        }
        return moves;
    }

    @Override public char symbol() { return color == Color.WHITE ? &#x27;Q&#x27; : &#x27;q&#x27;; }

    @Override public Piece copy() { return new Queen(color, new Position(position.getRow(), position.getCol())); }
}</code></pre>
</div>
<div class="tab-content" id="Rook-java">
<pre><code class="language-java">package org.interview.system_design.lld.chess;

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
        if (rd != 0 &amp;&amp; cd != 0) return false; // Must be straight line
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
    public List&lt;Position&gt; getValidMoves(Board board) {
        List&lt;Position&gt; moves = new ArrayList&lt;&gt;();
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

    @Override public char symbol() { return color == Color.WHITE ? &#x27;R&#x27; : &#x27;r&#x27;; }

    @Override public Piece copy() { return new Rook(color, new Position(position.getRow(), position.getCol())); }
}</code></pre>
</div>
</div>
