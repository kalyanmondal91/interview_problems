package org.interview.system_design.lld.elevator;

import java.util.List;

/**
 * Strategy Pattern interface for elevator dispatch algorithms.
 * Implementations decide which floor an elevator services next
 * given its current state and the queue of pending requests.
 */
public interface ElevatorStrategy {
    /**
     * Selects the next floor to move to.
     *
     * @param elevator the elevator whose next floor is being determined
     * @param requests the current queue of unserviced requests
     * @return the next floor number, or -1 if there is nothing to do
     */
    int selectNextFloor(Elevator elevator, List<ElevatorRequest> requests);
}
