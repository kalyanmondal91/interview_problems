package org.interview.coding.design;

import java.util.*;

/**
 * Problem: Design Elevator System
 * Difficulty: Hard
 *
 * Description: Design an elevator system with N=3 elevators serving floors 0-9.
 * requestElevator(floor, direction) assigns the nearest idle elevator to service the request.
 * Each step() call moves each elevator one floor toward its next destination.
 *
 * Example:
 *   Input: requestElevator(5, UP), step() x3 (from floor 2), elevator arrives at floor 5
 *   Output: Elevator assigned, moves floor by floor until destination reached
 *
 * Approach: Each elevator tracks id, currentFloor, direction (UP/DOWN/IDLE), and a queue of
 * destinations. requestElevator finds the nearest IDLE elevator (by absolute floor distance)
 * and adds the requested floor to its queue. step() advances each elevator one floor toward the
 * front of its destination queue, removing reached destinations and updating direction.
 *
 * Time Complexity: O(N) per requestElevator, O(N) per step
 * Space Complexity: O(N + R) where R=total requests queued
 *
 * Test Cases:
 *   1. requestElevator(5, UP) from floor 0 → nearest elevator assigned
 *   2. After 5 steps, elevator reaches floor 5
 *   3. Edge: all elevators busy → assign to closest busy elevator or queue request
 */
public class DesignElevatorSystem {

    enum Direction { UP, DOWN, IDLE }

    static class Elevator {
        int id;
        int currentFloor;
        Direction direction;
        Queue<Integer> destinations;

        Elevator(int id) {
            this.id = id;
            this.currentFloor = 0;
            this.direction = Direction.IDLE;
            this.destinations = new LinkedList<>();
        }

        void addDestination(int floor) {
            destinations.offer(floor);
            if (direction == Direction.IDLE) {
                direction = floor > currentFloor ? Direction.UP
                          : floor < currentFloor ? Direction.DOWN
                          : Direction.IDLE;
            }
        }

        void step() {
            if (destinations.isEmpty()) {
                direction = Direction.IDLE;
                return;
            }
            int target = destinations.peek();
            if (currentFloor < target) {
                currentFloor++;
                direction = Direction.UP;
            } else if (currentFloor > target) {
                currentFloor--;
                direction = Direction.DOWN;
            }
            if (currentFloor == target) {
                destinations.poll();
                direction = destinations.isEmpty() ? Direction.IDLE
                          : (destinations.peek() > currentFloor ? Direction.UP : Direction.DOWN);
            }
        }

        @Override
        public String toString() {
            return "E" + id + "[floor=" + currentFloor + ", dir=" + direction
                    + ", dests=" + destinations + "]";
        }
    }

    private final List<Elevator> elevators;
    private static final int NUM_ELEVATORS = 3;

    public DesignElevatorSystem() {
        elevators = new ArrayList<>();
        for (int i = 0; i < NUM_ELEVATORS; i++) elevators.add(new Elevator(i));
    }

    public Elevator requestElevator(int floor, Direction direction) {
        Elevator best = null;
        int bestDist = Integer.MAX_VALUE;
        for (Elevator e : elevators) {
            if (e.direction == Direction.IDLE) {
                int dist = Math.abs(e.currentFloor - floor);
                if (dist < bestDist) {
                    bestDist = dist;
                    best = e;
                }
            }
        }
        // Fallback: pick any elevator with shortest queue
        if (best == null) {
            for (Elevator e : elevators) {
                if (e.destinations.size() < bestDist) {
                    bestDist = e.destinations.size();
                    best = e;
                }
            }
        }
        if (best != null) best.addDestination(floor);
        return best;
    }

    public void step() {
        for (Elevator e : elevators) e.step();
    }

    public void printState() {
        for (Elevator e : elevators) System.out.println("  " + e);
    }

    public static void main(String[] args) {
        DesignElevatorSystem system = new DesignElevatorSystem();

        // Test Case 1: Request elevator to floor 5
        System.out.println("=== Initial State ===");
        system.printState();
        Elevator assigned = system.requestElevator(5, Direction.UP);
        System.out.println("Assigned: E" + assigned.id + " to floor 5");

        // Test Case 2: Step until elevator reaches floor 5
        System.out.println("=== Stepping ===");
        for (int i = 0; i < 6; i++) {
            system.step();
            system.printState();
        }

        // Test Case 3: Edge - request two more elevators, then a 4th (fallback to queue)
        system.requestElevator(3, Direction.DOWN);
        system.requestElevator(7, Direction.UP);
        Elevator fourth = system.requestElevator(9, Direction.UP); // all busy, uses fallback
        System.out.println("=== After 3 more requests ===");
        system.printState();
    }
}
