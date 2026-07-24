package org.interview.system_design.lld.tictactoe;

import java.util.Scanner;

/**
 * Human player that reads row/col from standard input via Scanner.
 * In a test environment the Scanner can be constructed with a custom InputStream.
 */
public class HumanPlayer extends Player {
    private final Scanner scanner;

    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
        this.scanner = new Scanner(System.in);
    }

    /** Constructor that accepts a custom Scanner (useful for simulation/testing). */
    public HumanPlayer(String name, Mark mark, Scanner scanner) {
        super(name, mark);
        this.scanner = scanner;
    }

    @Override
    public int[] makeMove(Board board) {
        System.out.printf("%s (%s), enter your move (row col): ", name, mark);
        while (true) {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            if (row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize()
                    && board.getCell(row, col).isEmpty()) {
                return new int[]{row, col};
            }
            System.out.print("Invalid move. Try again (row col): ");
        }
    }
}
