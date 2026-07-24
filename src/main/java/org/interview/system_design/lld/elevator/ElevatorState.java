package org.interview.system_design.lld.elevator;

/**
 * State machine states for an elevator (State Pattern).
 * Valid transitions:
 *   IDLE -> MOVING_UP | MOVING_DOWN | DOOR_OPEN | MAINTENANCE
 *   MOVING_UP -> IDLE | DOOR_OPEN
 *   MOVING_DOWN -> IDLE | DOOR_OPEN
 *   DOOR_OPEN -> IDLE | MOVING_UP | MOVING_DOWN
 *   MAINTENANCE -> IDLE (after servicing)
 */
public enum ElevatorState {
    MOVING_UP,
    MOVING_DOWN,
    IDLE,
    DOOR_OPEN,
    MAINTENANCE
}
