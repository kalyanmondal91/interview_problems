package org.interview.system_design.lld.elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages a fleet of elevators and dispatches incoming requests.
 * Uses the configured ElevatorStrategy to select the next floor for each elevator.
 *
 * Dispatch policy: assigns each new request to the closest available elevator
 * to minimise passenger wait time.
 */
public class ElevatorController {
    private final List<Elevator> elevators;
    private final List<ElevatorRequest> pendingRequests;
    private ElevatorStrategy strategy;

    public ElevatorController(ElevatorStrategy strategy) {
        this.strategy = strategy;
        this.elevators = new ArrayList<>();
        this.pendingRequests = new CopyOnWriteArrayList<>();
    }

    // ── Fleet management ───────────────────────────────────────────────────────

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public List<Elevator> getElevators() {
        return Collections.unmodifiableList(elevators);
    }

    public void setStrategy(ElevatorStrategy strategy) {
        this.strategy = strategy;
    }

    // ── Request dispatch ───────────────────────────────────────────────────────

    /**
     * Accepts a new passenger request and queues it.
     * Immediately triggers a dispatch step to assign it to the best elevator.
     */
    public void requestElevator(ElevatorRequest request) {
        System.out.println("[Controller] New " + request);
        pendingRequests.add(request);
        dispatch();
    }

    /**
     * Runs one dispatch step: for each idle elevator, use the strategy to
     * determine its next target and move it one floor in that direction.
     * This simulates a single clock tick in a step-driven simulation.
     */
    public void dispatch() {
        for (Elevator elevator : elevators) {
            if (!elevator.isAvailable()) continue;

            int nextFloor = strategy.selectNextFloor(elevator, pendingRequests);
            if (nextFloor == -1) continue;

            System.out.printf("[Controller] Elevator %d targeting floor %d (currently at %d)%n",
                    elevator.getId(), nextFloor, elevator.getCurrentFloor());

            // Move elevator step-by-step until it reaches the target
            while (elevator.getCurrentFloor() != nextFloor) {
                elevator.moveTowards(nextFloor);
            }

            // Arrived — open and close door
            if (elevator.getState() != ElevatorState.DOOR_OPEN) {
                elevator.openDoor();
            }
            elevator.closeDoor();

            // Remove all requests whose source or destination equals the current floor
            int arrivedAt = elevator.getCurrentFloor();
            pendingRequests.removeIf(r ->
                    r.getSourceFloor() == arrivedAt || r.getDestinationFloor() == arrivedAt);
        }
    }

    /** Returns the list of unserviced requests. */
    public List<ElevatorRequest> getPendingRequests() {
        return Collections.unmodifiableList(pendingRequests);
    }

    /**
     * Simulates processing until all pending requests are serviced.
     * Bounded to 1000 iterations to prevent infinite loops in edge cases.
     */
    public void processAll() {
        int maxIterations = 1000;
        while (!pendingRequests.isEmpty() && maxIterations-- > 0) {
            dispatch();
        }
    }
}
