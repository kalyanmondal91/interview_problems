package org.interview.system_design.lld.tictactoe;

/**
 * Enum representing the marks used in Tic-Tac-Toe.
 */
public enum Mark {
    X, O, EMPTY;

    @Override
    public String toString() {
        return this == EMPTY ? "." : name();
    }
}
