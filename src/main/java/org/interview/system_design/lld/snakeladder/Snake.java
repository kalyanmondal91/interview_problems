package org.interview.system_design.lld.snakeladder;

/**
 * A snake — landing on the head sends the player down to the tail.
 * head > tail (snake goes downward).
 */
public class Snake extends BoardElement {

    public Snake(int head, int tail) {
        super(head, tail);
    }

    @Override
    protected void validate() {
        if (start <= end) {
            throw new IllegalArgumentException(
                    "Snake head (" + start + ") must be above tail (" + end + ").");
        }
    }

    @Override
    protected void markStartCell(Cell cell) {
        cell.setCellType(CellType.SNAKE_HEAD);
    }

    @Override
    protected void markEndCell(Cell cell) {
        cell.setCellType(CellType.SNAKE_TAIL);
    }

    @Override
    public String toString() {
        return "Snake[head=" + start + " -> tail=" + end + "]";
    }
}
