package org.interview.system_design.lld.snakeladder;

/**
 * A ladder — landing on the bottom sends the player up to the top.
 * bottom < top (ladder goes upward).
 */
public class Ladder extends BoardElement {

    public Ladder(int bottom, int top) {
        super(bottom, top);
    }

    @Override
    protected void validate() {
        if (start >= end) {
            throw new IllegalArgumentException(
                    "Ladder bottom (" + start + ") must be below top (" + end + ").");
        }
    }

    @Override
    protected void markStartCell(Cell cell) {
        cell.setCellType(CellType.LADDER_BOTTOM);
    }

    @Override
    protected void markEndCell(Cell cell) {
        cell.setCellType(CellType.LADDER_TOP);
    }

    @Override
    public String toString() {
        return "Ladder[bottom=" + start + " -> top=" + end + "]";
    }
}
