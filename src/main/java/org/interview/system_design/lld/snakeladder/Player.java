package org.interview.system_design.lld.snakeladder;

/**
 * Represents a game player.
 * Position 0 means "before the board" (not yet entered).
 * Position 100 means the player has won.
 */
public class Player {
    private final String playerId;
    private final String name;
    private int currentPosition;

    public Player(String playerId, String name) {
        this.playerId        = playerId;
        this.name            = name;
        this.currentPosition = 0; // start position (off-board)
    }

    public String getPlayerId() { return playerId; }
    public String getName() { return name; }
    public int getCurrentPosition() { return currentPosition; }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    @Override
    public String toString() { return name + "@" + currentPosition; }
}
