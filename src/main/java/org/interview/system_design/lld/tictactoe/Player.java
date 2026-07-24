package org.interview.system_design.lld.tictactoe;

/**
 * Abstract base class for Tic-Tac-Toe players.
 * Supports human and AI player subtypes via the Template Method pattern.
 */
public abstract class Player {
    protected final String name;
    protected final Mark mark;

    public Player(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    public String getName() { return name; }
    public Mark getMark() { return mark; }

    /**
     * Makes a move on the given board.
     * Concrete subclasses decide how the move is chosen (human input vs. AI).
     *
     * @param board the current game board
     * @return int[] with {row, col} of the chosen move
     */
    public abstract int[] makeMove(Board board);

    @Override
    public String toString() {
        return String.format("Player{name='%s', mark=%s}", name, mark);
    }
}
