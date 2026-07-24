package org.interview.system_design.lld.elevator;

import java.util.List;

/**
 * Demo runner for the Elevator System LLD.
 * Patterns: State Machine, Observer, Strategy
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        banner("Scenario 1: SCAN strategy -- 3 elevators, 10 floors");
        ElevatorController controller = new ElevatorController(new SCANStrategy());
        Elevator e1 = new Elevator(1, 1, 10);
        Elevator e2 = new Elevator(2, 1, 10);
        Elevator e3 = new Elevator(3, 1, 10);

        ElevatorObserver observer = new ElevatorObserver() {
            @Override
            public void onFloorChanged(int elevatorId, int floor) {
                System.out.printf("  [Observer] Elevator %d moved to floor %d%n", elevatorId, floor);
            }
            @Override
            public void onStateChanged(int elevatorId, ElevatorState state) {
                System.out.printf("  [Observer] Elevator %d state -> %s%n", elevatorId, state);
            }
        };
        e1.addObserver(observer);
        e2.addObserver(observer);
        e3.addObserver(observer);

        controller.addElevator(e1);
        controller.addElevator(e2);
        controller.addElevator(e3);

        controller.requestElevator(new ElevatorRequest(1, 7));
        controller.requestElevator(new ElevatorRequest(3, 9));
        controller.requestElevator(new ElevatorRequest(8, 2));
        System.out.println("  Requests submitted: 1->7, 3->9, 8->2");
        controller.processAll();

        banner("Scenario 2: FCFS strategy -- requests processed in order");
        ElevatorController fcfsController = new ElevatorController(new FCFSStrategy());
        Elevator f1 = new Elevator(1, 1, 15);
        Elevator f2 = new Elevator(2, 1, 15);
        fcfsController.addElevator(f1);
        fcfsController.addElevator(f2);
        fcfsController.requestElevator(new ElevatorRequest(5, 12));
        fcfsController.requestElevator(new ElevatorRequest(2, 6));
        fcfsController.requestElevator(new ElevatorRequest(10, 1));
        System.out.println("  Requests: 5->12, 2->6, 10->1");
        fcfsController.processAll();
        System.out.println("  Final elevator positions:");
        fcfsController.getElevators().forEach(e ->
                System.out.printf("    Elevator %d: floor=%d, state=%s%n",
                        e.getId(), e.getCurrentFloor(), e.getState()));

        banner("Scenario 3: Edge case -- request from and to same floor");
        ElevatorController edgeController = new ElevatorController(new SCANStrategy());
        Elevator edge1 = new Elevator(1, 1, 10);
        edgeController.addElevator(edge1);
        edgeController.requestElevator(new ElevatorRequest(5, 5));
        System.out.println("  Request: floor 5 -> floor 5 (no-op expected)");
        edgeController.processAll();
        System.out.println("  Elevator final floor: " + edgeController.getElevators().get(0).getCurrentFloor());

        banner("Scenario 4: Requests to top floor (10) and ground floor (1)");
        ElevatorController extremeController = new ElevatorController(new SCANStrategy());
        Elevator ext1 = new Elevator(1, 1, 10);
        extremeController.addElevator(ext1);
        extremeController.requestElevator(new ElevatorRequest(1, 10));
        extremeController.requestElevator(new ElevatorRequest(10, 1));
        System.out.println("  Requests: 1->10, 10->1");
        extremeController.processAll();
        System.out.println("  Done. Final floor: " + extremeController.getElevators().get(0).getCurrentFloor());

        banner("Scenario 5: High load -- 5 requests on 1 elevator (SCAN)");
        ElevatorController loadController = new ElevatorController(new SCANStrategy());
        Elevator load1 = new Elevator(1, 1, 20);
        loadController.addElevator(load1);
        List<int[]> requests = List.of(
                new int[]{1, 15}, new int[]{3, 18}, new int[]{7, 2},
                new int[]{12, 4}, new int[]{6, 20});
        requests.forEach(r -> {
            loadController.requestElevator(new ElevatorRequest(r[0], r[1]));
            System.out.printf("  Queued: %d -> %d%n", r[0], r[1]);
        });
        loadController.processAll();
        System.out.println("  All requests processed.");

        System.out.println();
        System.out.println("=== Elevator System Demo Complete ===");
    }
}
