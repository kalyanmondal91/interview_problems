package org.interview.system_design.lld.elevator;

import java.util.List;

/**
 * First-Come-First-Served (FCFS) strategy.
 * Always services the oldest pending request first, regardless of direction.
 * Simple but can result in high travel distances.
 */
public class FCFSStrategy implements ElevatorStrategy {

    @Override
    public int selectNextFloor(Elevator elevator, List<ElevatorRequest> requests) {
        if (requests.isEmpty()) return -1;
        // The head of the queue is the oldest request
        ElevatorRequest next = requests.get(0);
        // First travel to the source floor, then the destination will be handled next
        if (elevator.getCurrentFloor() != next.getSourceFloor()) {
            return next.getSourceFloor();
        }
        return next.getDestinationFloor();
    }
}
