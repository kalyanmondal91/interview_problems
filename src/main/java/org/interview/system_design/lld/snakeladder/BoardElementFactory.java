package org.interview.system_design.lld.snakeladder;

/**
 * Factory Pattern — creates BoardElement instances (Snake or Ladder).
 * Callers depend only on the abstract type; concrete classes are hidden.
 */
public class BoardElementFactory {

    /**
     * Creates a Snake with the given head and tail positions.
     *
     * @param head snake's head position (must be > tail)
     * @param tail snake's tail position
     * @return a new Snake BoardElement
     */
    public static BoardElement createSnake(int head, int tail) {
        return new Snake(head, tail);
    }

    /**
     * Creates a Ladder with the given bottom and top positions.
     *
     * @param bottom ladder's bottom position (must be < top)
     * @param top    ladder's top position
     * @return a new Ladder BoardElement
     */
    public static BoardElement createLadder(int bottom, int top) {
        return new Ladder(bottom, top);
    }
}
