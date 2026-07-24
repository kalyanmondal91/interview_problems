package org.interview.system_design.lld.snakeladder;

import java.util.List;

/**
 * Demo runner for the Snake and Ladder LLD.
 * Patterns: Factory (BoardElementFactory), Template Method (GameTemplate)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // -- Scenario 1: Standard 2-player game --
        banner("Scenario 1: Standard 2-player Snake & Ladder game");
        // Board takes no args (fixed size 100)
        // SnakeLadderGame(List<Player>, int numberOfDice) -- board is created internally
        Player p1 = new Player("P1", "Alice");
        Player p2 = new Player("P2", "Bob");

        SnakeLadderGame game1 = new SnakeLadderGame(List.of(p1, p2), 1);
        game1.play();

        // -- Scenario 2: 3-player game with step-by-step trace --
        banner("Scenario 2: 3-player game -- first 15 turns shown");
        Player q1 = new Player("Q1", "Carol");
        Player q2 = new Player("Q2", "Dave");
        Player q3 = new Player("Q3", "Eve");

        // Create game with a step-limited runner so demo doesn't run forever
        SnakeLadderGame game2 = new SnakeLadderGame(List.of(q1, q2, q3), 1) {
            private int round = 0;
            @Override
            protected boolean checkWin(Player player) {
                if (player.getCurrentPosition() >= 100) return true;
                if (++round >= 15) { // cap at 15 turns for demo
                    System.out.println("  (Demo capped at 15 turns -- stopping early)");
                    return true;
                }
                return false;
            }
        };
        game2.play();

        // -- Scenario 3: BoardElementFactory usage --
        banner("Scenario 3: BoardElementFactory creates snakes and ladders");
        // Factory methods: createSnake(head, tail) and createLadder(bottom, top) -- 2 args only
        BoardElement snake  = BoardElementFactory.createSnake(80, 10);
        BoardElement ladder = BoardElementFactory.createLadder(5, 50);
        System.out.println("  Snake  -- start: " + snake.getStart()  + " -> end: " + snake.getEnd());
        System.out.println("  Ladder -- start: " + ladder.getStart() + " -> end: " + ladder.getEnd());

        // Apply elements to a fresh board to verify linked positions
        Board board3 = new Board();
        snake.apply(board3);
        ladder.apply(board3);

        Cell snakeCell  = board3.getCell(80);
        Cell ladderCell = board3.getCell(5);
        System.out.println("  Cell 80 type : " + snakeCell.getCellType()
                + " | linked to: " + snakeCell.getLinkedPosition());
        System.out.println("  Cell 5  type : " + ladderCell.getCellType()
                + " | linked to: " + ladderCell.getLinkedPosition());

        // -- Scenario 4: Land exactly on snake head and ladder bottom --
        banner("Scenario 4: Verify snake & ladder cell transitions");
        Board board4 = new Board();
        board4.addSnake(30, 5);
        board4.addLadder(20, 70);

        Player solo = new Player("S1", "Solo");
        solo.setCurrentPosition(29); // one step before snake
        // Simulate dice roll of 1 -> lands on 30 (snake head)
        int newPos = board4.getCell(30).getCellType() == CellType.SNAKE_HEAD
                ? board4.getCell(30).getLinkedPosition()
                : 30;
        System.out.printf("  Player on 29, rolls 1 -> lands 30 (Snake Head) -> slides to %d%n", newPos);

        solo.setCurrentPosition(19); // one step before ladder
        int ladderPos = board4.getCell(20).getCellType() == CellType.LADDER_BOTTOM
                ? board4.getCell(20).getLinkedPosition()
                : 20;
        System.out.printf("  Player on 19, rolls 1 -> lands 20 (Ladder Bottom) -> climbs to %d%n", ladderPos);

        banner("Snake & Ladder Demo Complete");
    }
}
