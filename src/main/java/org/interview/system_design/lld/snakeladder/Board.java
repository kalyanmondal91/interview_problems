package org.interview.system_design.lld.snakeladder;

/**
 * Represents the Snake & Ladder board (100 cells, 1-indexed).
 * Cells are initialised as NORMAL and then modified via addSnake / addLadder.
 */
public class Board {
    private static final int SIZE = 100;
    private final Cell[] cells;

    public Board() {
        cells = new Cell[SIZE + 1]; // index 0 unused; positions 1..100
        for (int i = 1; i <= SIZE; i++) {
            cells[i] = new Cell(i);
        }
    }

    /**
     * Returns the cell at the given 1-based position.
     *
     * @param position board position (1..100)
     */
    public Cell getCell(int position) {
        if (position < 1 || position > SIZE) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        return cells[position];
    }

    /**
     * Adds a snake using the Factory + Template pattern.
     * Marks head as SNAKE_HEAD, tail as SNAKE_TAIL, links head -> tail.
     */
    public void addSnake(int head, int tail) {
        BoardElement snake = BoardElementFactory.createSnake(head, tail);
        snake.apply(this);
    }

    /**
     * Adds a ladder using the Factory + Template pattern.
     * Marks bottom as LADDER_BOTTOM, top as LADDER_TOP, links bottom -> top.
     */
    public void addLadder(int bottom, int top) {
        BoardElement ladder = BoardElementFactory.createLadder(bottom, top);
        ladder.apply(this);
    }

    /**
     * Returns the effective landing position after applying any snake or ladder.
     * If the cell has no effect, the same position is returned.
     *
     * @param position the rolled-to position
     * @return final position after applying effects
     */
    public int applyEffects(int position) {
        Cell cell = getCell(position);
        if (cell.hasEffect()) {
            return cell.getLinkedPosition();
        }
        return position;
    }

    public int getSize() { return SIZE; }
}
