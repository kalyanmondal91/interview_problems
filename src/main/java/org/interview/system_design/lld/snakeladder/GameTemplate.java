package org.interview.system_design.lld.snakeladder;

import java.util.List;

/**
 * Template Method Pattern — defines the skeleton algorithm for a Snake & Ladder game.
 *
 * The fixed sequence in play():
 *   1. initializeBoard()  — set up snakes and ladders
 *   2. Loop until a winner is found:
 *      a. rollDice()        — determine the dice value
 *      b. movePlayer()      — advance current player
 *      c. checkWin()        — test for winning condition
 *
 * Subclasses implement the abstract steps; the high-level flow is unchanged.
 */
public abstract class GameTemplate {

    /**
     * Runs the full game from setup to winner announcement.
     */
    public final void play() {
        initializeBoard();
        System.out.println("Game started!\n");

        List<Player> players = getPlayers();
        int currentIndex = 0;

        while (true) {
            Player current = players.get(currentIndex);
            int diceValue = rollDice();
            movePlayer(current, diceValue);

            if (checkWin(current)) {
                System.out.println("\n*** " + current.getName() + " wins the game! ***");
                break;
            }
            currentIndex = (currentIndex + 1) % players.size();
        }
    }

    /** Sets up the board — subclass adds snakes and ladders. */
    protected abstract void initializeBoard();

    /** Returns the ordered list of players participating in the game. */
    protected abstract List<Player> getPlayers();

    /**
     * Rolls the dice and returns the result.
     *
     * @return dice total (typically 1–6 for a single die)
     */
    protected abstract int rollDice();

    /**
     * Moves the given player by {@code diceValue} squares, applying board effects.
     *
     * @param player    the player to move
     * @param diceValue the number rolled
     */
    protected abstract void movePlayer(Player player, int diceValue);

    /**
     * Checks whether the given player has reached or passed position 100.
     *
     * @param player the player to check
     * @return true if the player has won
     */
    protected abstract boolean checkWin(Player player);
}
