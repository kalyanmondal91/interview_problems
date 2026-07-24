package org.interview.system_design.lld.elevator;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * SCAN (Elevator) Algorithm.
 * The elevator moves in one direction, picking up all requests along the way.
 * When it reaches the last request in that direction it reverses.
 * This minimises average wait time compared to FCFS.
 *
 * Implementation:
 *   1. If moving UP  — service the nearest floor ABOVE current floor first.
 *   2. If moving DOWN — service the nearest floor BELOW current floor first.
 *   3. If IDLE — choose the nearest request in either direction.
 */
public class SCANStrategy implements ElevatorStrategy {

    @Override
    public int selectNextFloor(Elevator elevator, List<ElevatorRequest> requests) {
        if (requests.isEmpty()) return -1;

        int current = elevator.getCurrentFloor();
        Direction dir = elevator.getDirection();

        // Collect all relevant stop floors (source + destination of every request)
        int[] stops = requests.stream()
                .flatMapToInt(r -> IntStream.of(r.getSourceFloor(), r.getDestinationFloor()))
                .distinct()
                .toArray();

        if (dir == Direction.UP || dir == Direction.IDLE) {
            // Try to find the nearest floor above
            OptionalInt above = IntStream.of(stops)
                    .filter(f -> f > current)
                    .min();
            if (above.isPresent()) return above.getAsInt();

            // No floors above — find nearest below (direction will reverse)
            OptionalInt below = IntStream.of(stops)
                    .filter(f -> f < current)
                    .max();
            if (below.isPresent()) return below.getAsInt();
        }

        if (dir == Direction.DOWN) {
            // Try to find the nearest floor below
            OptionalInt below = IntStream.of(stops)
                    .filter(f -> f < current)
                    .max();
            if (below.isPresent()) return below.getAsInt();

            // No floors below — find nearest above (direction will reverse)
            OptionalInt above = IntStream.of(stops)
                    .filter(f -> f > current)
                    .min();
            if (above.isPresent()) return above.getAsInt();
        }

        return -1;
    }
}
