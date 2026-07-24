package org.interview.system_design.lld.snakeladder;

/**
 * Abstract Template Method base class for board elements (Snake and Ladder).
 *
 * Template method: apply(board) defines the skeleton algorithm:
 *   1. validate()       — subclass checks start/end invariants
 *   2. markStartCell()  — subclass marks the starting cell type
 *   3. markEndCell()    — subclass marks the ending cell type
 *   4. linkCells()      — links start to end on the board
 */
public abstract class BoardElement {
    protected final int start;  // Snake head or Ladder bottom
    protected final int end;    // Snake tail or Ladder top

    protected BoardElement(int start, int end) {
        this.start = start;
        this.end   = end;
    }

    /**
     * Template method — applies this element to the board.
     * Subclasses customise steps via the abstract/hook methods below.
     */
    public final void apply(Board board) {
        validate();
        Cell startCell = board.getCell(start);
        Cell endCell   = board.getCell(end);
        markStartCell(startCell);
        markEndCell(endCell);
        startCell.setLinkedPosition(end);
    }

    /** Validates start/end constraints (e.g., snake head > tail). */
    protected abstract void validate();

    /** Sets the cell type for the start cell (SNAKE_HEAD or LADDER_BOTTOM). */
    protected abstract void markStartCell(Cell cell);

    /** Sets the cell type for the end cell (SNAKE_TAIL or LADDER_TOP). */
    protected abstract void markEndCell(Cell cell);

    public int getStart() { return start; }
    public int getEnd()   { return end; }
}
