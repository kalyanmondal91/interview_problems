package org.interview.system_design.lld.snakeladder;

/**
 * Represents a single cell on the board.
 * NORMAL cells have linkedPosition == -1 (no effect).
 * SNAKE_HEAD cells link to their tail (lower position).
 * LADDER_BOTTOM cells link to their top (higher position).
 */
public class Cell {
    private final int position;     // 1-based board position (1..100)
    private CellType cellType;
    private int linkedPosition;     // -1 if no link

    public Cell(int position) {
        this.position       = position;
        this.cellType       = CellType.NORMAL;
        this.linkedPosition = -1;
    }

    public int getPosition() { return position; }
    public CellType getCellType() { return cellType; }
    public int getLinkedPosition() { return linkedPosition; }

    public void setCellType(CellType cellType) { this.cellType = cellType; }
    public void setLinkedPosition(int linkedPosition) { this.linkedPosition = linkedPosition; }

    /** Returns true if this cell has a special effect (snake or ladder). */
    public boolean hasEffect() { return linkedPosition != -1; }

    @Override
    public String toString() {
        return position + "(" + cellType + (hasEffect() ? "->" + linkedPosition : "") + ")";
    }
}
