package org.interview.system_design.lld.snakeladder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete implementation of the Snake & Ladder game template.
 *
 * Design Patterns used:
 *   • Template Method — GameTemplate.play() orchestrates the game loop;
 *                       this class provides all concrete step implementations
 *   • Factory         — BoardElementFactory creates Snake and Ladder elements
 *
 * Standard board setup from the classic game (10x10, positions 1–100).
 */
public class SnakeLadderGame extends GameTemplate {

    private final Board board;
    private final Dice dice;
    private final List<Player> players;

    public SnakeLadderGame(List<Player> players, int numberOfDice) {
        this.board   = new Board();
        this.dice    = new Dice(numberOfDice);
        this.players = new ArrayList<>(players);
    }

    // ── Template steps ─────────────────────────────────────────────────────────

    @Override
    protected void initializeBoard() {
        // Standard snakes (head -> tail)
        board.addSnake(99, 54);
        board.addSnake(70, 55);
        board.addSnake(52, 42);
        board.addSnake(25, 2);
        board.addSnake(34, 6);
        board.addSnake(65, 45);
        board.addSnake(17, 7);

        // Standard ladders (bottom -> top)
        board.addLadder(4,  14);
        board.addLadder(9,  31);
        board.addLadder(20, 38);
        board.addLadder(28, 84);
        board.addLadder(40, 59);
        board.addLadder(51, 67);
        board.addLadder(63, 81);
        board.addLadder(71, 91);

        System.out.println("Board initialized with snakes and ladders.");
    }

    @Override
    protected List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    protected int rollDice() {
        return dice.roll();
    }

    @Override
    protected void movePlayer(Player player, int diceValue) {
        int current = player.getCurrentPosition();
        int newPos  = current + diceValue;

        if (newPos > board.getSize()) {
            // Over-shoot: player stays put (must roll exact or less)
            System.out.printf("  %s rolled %d (pos %d) — over-shoot, stays at %d%n",
                    player.getName(), diceValue, current, current);
            return;
        }

        // Apply snake or ladder effect
        int finalPos = board.applyEffects(newPos);

        Cell landedCell = board.getCell(newPos);
        String effect = "";
        if (landedCell.getCellType() == CellType.SNAKE_HEAD) {
            effect = " [SNAKE!] slides to " + finalPos;
        } else if (landedCell.getCellType() == CellType.LADDER_BOTTOM) {
            effect = " [LADDER!] climbs to " + finalPos;
        }

        player.setCurrentPosition(finalPos);
        System.out.printf("  %s rolled %d: %d → %d%s%n",
                player.getName(), diceValue, current, finalPos, effect);
    }

    @Override
    protected boolean checkWin(Player player) {
        return player.getCurrentPosition() >= board.getSize();
    }

    // ── Accessors ──────────────────────────────────────────────────────────────

    public Board getBoard() { return board; }
    public Dice getDice()   { return dice; }

    // ── Entry point ────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("p1", "Alice"));
        players.add(new Player("p2", "Bob"));
        players.add(new Player("p3", "Carol"));

        SnakeLadderGame game = new SnakeLadderGame(players, 1);
        game.play();
    }
}
