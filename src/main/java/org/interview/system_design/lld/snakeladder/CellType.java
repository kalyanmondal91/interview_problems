package org.interview.system_design.lld.snakeladder;

/**
 * Enum representing the type of a board cell.
 * NORMAL cells have no special effect.
 * SNAKE_HEAD and LADDER_BOTTOM cells trigger movement.
 */
public enum CellType {
    NORMAL,
    SNAKE_HEAD,    // Player slides down to SNAKE_TAIL
    LADDER_BOTTOM, // Player climbs up to LADDER_TOP
    SNAKE_TAIL,    // Destination of a snake
    LADDER_TOP     // Destination of a ladder
}
