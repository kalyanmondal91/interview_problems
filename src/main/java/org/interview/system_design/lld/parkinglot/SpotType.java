package org.interview.system_design.lld.parkinglot;

/**
 * Enum representing different types of parking spots.
 * Used by Factory and Strategy patterns to match vehicles to appropriate spots.
 */
public enum SpotType {
    COMPACT,
    REGULAR,
    LARGE,
    HANDICAPPED
}
