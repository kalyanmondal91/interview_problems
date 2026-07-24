package org.interview.system_design.lld.chess;

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
}
