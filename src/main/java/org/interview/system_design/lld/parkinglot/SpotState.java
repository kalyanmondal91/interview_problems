package org.interview.system_design.lld.parkinglot;

/**
 * Enum representing the state of a parking spot (State Pattern).
 * Transitions: AVAILABLE <-> OCCUPIED, AVAILABLE -> RESERVED, any -> OUT_OF_SERVICE
 */
public enum SpotState {
    AVAILABLE,
    OCCUPIED,
    RESERVED,
    OUT_OF_SERVICE
}
