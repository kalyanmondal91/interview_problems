package org.interview.system_design.lld.elevator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * State Machine — models a single elevator cabin.
 *
 * State transitions driven by move(), openDoor(), closeDoor(), setMaintenance():
 *   IDLE        → MOVING_UP | MOVING_DOWN | DOOR_OPEN | MAINTENANCE
 *   MOVING_UP   → DOOR_OPEN | IDLE
 *   MOVING_DOWN → DOOR_OPEN | IDLE
 *   DOOR_OPEN   → IDLE | MOVING_UP | MOVING_DOWN
 *   MAINTENANCE → IDLE
 *
 * Observer Pattern — registered ElevatorObserver instances are notified on
 * every floor change and state transition.
 */
public class Elevator {
    private final int id;
    private int currentFloor;
    private ElevatorState state;
    private Direction direction;
    private final int minFloor;
    private final int maxFloor;
    private final List<ElevatorObserver> observers = new ArrayList<>();

    public Elevator(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.currentFloor = minFloor;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.state = ElevatorState.IDLE;
        this.direction = Direction.IDLE;
    }

    // ── Observer management ────────────────────────────────────────────────────

    public void addObserver(ElevatorObserver observer) { observers.add(observer); }
    public void removeObserver(ElevatorObserver observer) { observers.remove(observer); }

    private void notifyFloorChanged(int floor) {
        observers.forEach(o -> o.onFloorChanged(id, floor));
    }

    private void notifyStateChanged(ElevatorState newState) {
        observers.forEach(o -> o.onStateChanged(id, newState));
    }

    // ── State transitions ──────────────────────────────────────────────────────

    /**
     * Moves the elevator one floor towards the target floor.
     * Updates direction and state accordingly.
     *
     * @param targetFloor the floor to move toward
     */
    public void moveTowards(int targetFloor) {
        if (targetFloor == currentFloor) {
            openDoor();
            return;
        }
        if (targetFloor > currentFloor) {
            transitionState(ElevatorState.MOVING_UP);
            direction = Direction.UP;
            currentFloor++;
        } else {
            transitionState(ElevatorState.MOVING_DOWN);
            direction = Direction.DOWN;
            currentFloor--;
        }
        notifyFloorChanged(currentFloor);

        if (currentFloor == targetFloor) {
            openDoor();
        }
    }

    /** Opens the elevator door (transitions to DOOR_OPEN). */
    public void openDoor() {
        transitionState(ElevatorState.DOOR_OPEN);
    }

    /** Closes the door and returns to IDLE. */
    public void closeDoor() {
        if (state != ElevatorState.DOOR_OPEN) {
            throw new IllegalStateException("Cannot close door: elevator " + id + " is " + state);
        }
        direction = Direction.IDLE;
        transitionState(ElevatorState.IDLE);
    }

    /** Puts the elevator in MAINTENANCE mode. */
    public void setMaintenance() {
        transitionState(ElevatorState.MAINTENANCE);
        direction = Direction.IDLE;
    }

    /** Returns elevator to IDLE from MAINTENANCE. */
    public void completeMaintenance() {
        if (state != ElevatorState.MAINTENANCE) {
            throw new IllegalStateException("Elevator " + id + " is not in MAINTENANCE.");
        }
        transitionState(ElevatorState.IDLE);
    }

    private void transitionState(ElevatorState newState) {
        this.state = newState;
        notifyStateChanged(newState);
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public ElevatorState getState() { return state; }
    public Direction getDirection() { return direction; }
    public int getMinFloor() { return minFloor; }
    public int getMaxFloor() { return maxFloor; }
    public boolean isIdle() { return state == ElevatorState.IDLE; }
    public boolean isAvailable() {
        return state != ElevatorState.MAINTENANCE;
    }

    public List<ElevatorObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }

    @Override
    public String toString() {
        return "Elevator[" + id + "] floor=" + currentFloor + " state=" + state + " dir=" + direction;
    }
}
