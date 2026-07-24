package org.interview.system_design.lld.elevator;

/**
 * Entry point / builder for the Elevator LLD.
 *
 * Design Patterns used:
 *   • State Machine — Elevator transitions between IDLE, MOVING_UP, MOVING_DOWN,
 *                     DOOR_OPEN, MAINTENANCE
 *   • Observer      — ElevatorObserver receives floor-change and state-change events
 *   • Strategy      — ElevatorStrategy (SCAN / FCFS) decides which floor to visit next
 */
public class ElevatorSystem {

    public static void main(String[] args) {
        // 1. Choose strategy (swap to FCFSStrategy to compare)
        ElevatorStrategy strategy = new SCANStrategy();
        ElevatorController controller = new ElevatorController(strategy);

        // 2. Create elevators (2 elevators, floors 1–10)
        Elevator e1 = new Elevator(1, 1, 10);
        Elevator e2 = new Elevator(2, 1, 10);

        // 3. Attach a logging observer to each elevator
        ElevatorObserver logger = new ElevatorObserver() {
            @Override
            public void onFloorChanged(int elevatorId, int floor) {
                System.out.printf("  [Elevator %d] → Floor %d%n", elevatorId, floor);
            }
            @Override
            public void onStateChanged(int elevatorId, ElevatorState state) {
                System.out.printf("  [Elevator %d] State → %s%n", elevatorId, state);
            }
        };
        e1.addObserver(logger);
        e2.addObserver(logger);

        controller.addElevator(e1);
        controller.addElevator(e2);

        // 4. Submit requests
        controller.requestElevator(new ElevatorRequest(1, 5));
        controller.requestElevator(new ElevatorRequest(3, 7));
        controller.requestElevator(new ElevatorRequest(8, 2));

        // 5. Process all requests
        controller.processAll();

        System.out.println("\nAll requests processed.");
        System.out.println(e1);
        System.out.println(e2);
    }
}
