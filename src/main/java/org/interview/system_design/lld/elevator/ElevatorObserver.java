package org.interview.system_design.lld.elevator;

/**
 * Observer Pattern interface for elevator events.
 * Implement to receive notifications on floor changes and state transitions.
 */
public interface ElevatorObserver {
    /**
     * Called whenever the elevator moves to a new floor.
     *
     * @param elevatorId the elevator's unique ID
     * @param floor      the floor number just reached
     */
    void onFloorChanged(int elevatorId, int floor);

    /**
     * Called whenever the elevator transitions to a new state.
     *
     * @param elevatorId the elevator's unique ID
     * @param state      the new ElevatorState
     */
    void onStateChanged(int elevatorId, ElevatorState state);
}
