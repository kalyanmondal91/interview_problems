package org.interview.system_design.lld.elevator;

import java.time.LocalDateTime;

/**
 * Represents a passenger elevator request.
 * Contains source floor, destination floor, and the direction of travel
 * (determines the hall-call button pressed).
 */
public class ElevatorRequest {
    private final int sourceFloor;
    private final int destinationFloor;
    private final Direction direction;
    private final LocalDateTime timestamp;

    public ElevatorRequest(int sourceFloor, int destinationFloor) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = destinationFloor > sourceFloor ? Direction.UP : Direction.DOWN;
        this.timestamp = LocalDateTime.now();
    }

    public int getSourceFloor() { return sourceFloor; }
    public int getDestinationFloor() { return destinationFloor; }
    public Direction getDirection() { return direction; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Request[" + sourceFloor + " -> " + destinationFloor + " (" + direction + ")]";
    }
}
