package org.interview.system_design.lld.parkinglot;

import java.util.List;

/**
 * Demo runner for the Parking Lot LLD.
 * Patterns: Strategy, Factory, State, Singleton
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) throws InterruptedException {

        // -- Setup --
        ParkingLot lot = ParkingLot.getInstance();
        lot.setName("CityCenter Parking");
        lot.setStrategy(new NearestSpotStrategy());
        lot.setFeeCalculator(new HourlyFeeCalculator());
        lot.reset();

        ParkingFloor ground = new ParkingFloor(0);
        ground.addSpot(SpotFactory.createSpot(SpotType.COMPACT,    "G-C1"));
        ground.addSpot(SpotFactory.createSpot(SpotType.COMPACT,    "G-C2"));
        ground.addSpot(SpotFactory.createSpot(SpotType.REGULAR,    "G-R1"));
        ground.addSpot(SpotFactory.createSpot(SpotType.REGULAR,    "G-R2"));
        ground.addSpot(SpotFactory.createSpot(SpotType.LARGE,      "G-L1"));
        ground.addSpot(SpotFactory.createSpot(SpotType.HANDICAPPED,"G-H1"));

        ParkingFloor upper = new ParkingFloor(1);
        upper.addSpot(SpotFactory.createSpot(SpotType.COMPACT, "U-C1"));
        upper.addSpot(SpotFactory.createSpot(SpotType.REGULAR, "U-R1"));

        lot.addFloor(ground);
        lot.addFloor(upper);

        // -- Scenario 1: Park multiple vehicle types --
        banner("Scenario 1: Park a car, motorcycle, and truck");
        Vehicle car1   = new Car("KA-01-AB-1234");
        Vehicle car2   = new Car("KA-02-CD-5678");
        Vehicle moto   = new Motorcycle("MH-12-EF-9999");
        Vehicle truck  = new Truck("DL-01-TRK-001");

        ParkingTicket t1 = lot.park(car1);
        ParkingTicket t2 = lot.park(car2);
        ParkingTicket t3 = lot.park(moto);
        ParkingTicket t4 = lot.park(truck);

        for (ParkingTicket t : List.of(t1, t2, t3, t4)) {
            System.out.printf("  Parked %-20s | Ticket: %-10s | Spot: %s%n",
                    t.getVehicle().getLicensePlate(), t.getTicketId(), t.getSpot().getSpotId());
        }

        // -- Scenario 2: Check available spots --
        banner("Scenario 2: Available spots by vehicle type");
        System.out.println("  Available for Car:        " + lot.availableSpots(new Car("X")));
        System.out.println("  Available for Motorcycle: " + lot.availableSpots(new Motorcycle("X")));
        System.out.println("  Available for Truck:      " + lot.availableSpots(new Truck("X")));

        // -- Scenario 3: Unpark and calculate fee --
        banner("Scenario 3: Unpark car1 after brief time");
        Thread.sleep(1500); // simulate 1.5 seconds parked
        ParkingTicket unparkResult = lot.unpark(t1.getTicketId());
        if (unparkResult != null) {
            System.out.printf("  Unparked: %s | Fee: $%.2f | Duration: %d ms%n",
                    unparkResult.getVehicle().getLicensePlate(),
                    unparkResult.getFee(),
                    java.time.temporal.ChronoUnit.MILLIS.between(unparkResult.getEntryTime(), unparkResult.getExitTime()));
        }

        // -- Scenario 4: Fill lot and attempt overflow --
        banner("Scenario 4: Fill lot and try to park when full");
        // Park remaining spots
        Vehicle car3 = new Car("TN-01-XX-0001");
        Vehicle car4 = new Car("TN-01-XX-0002");
        Vehicle car5 = new Car("TN-01-XX-0003");
        lot.park(car3); lot.park(car4); lot.park(car5);

        Vehicle overflow = new Car("OVERFLOW-001");
        try {
            ParkingTicket overflowTicket = lot.park(overflow);
            System.out.println("  Unexpected: got spot " + overflowTicket.getSpot().getSpotId());
        } catch (IllegalStateException e) {
            System.out.println("  No spot available for " + overflow.getLicensePlate() + " -- lot is full!");
        }

        // -- Scenario 5: Handicapped spot usage --
        banner("Scenario 5: Park in handicapped spot (any vehicle)");
        lot.reset();
        ParkingFloor hFloor = new ParkingFloor(0);
        hFloor.addSpot(SpotFactory.createSpot(SpotType.HANDICAPPED, "H-01"));
        lot.addFloor(hFloor);
        try {
            ParkingTicket hTicket = lot.park(new Car("HANDI-CAR"));
            System.out.println("  Parked in: " + hTicket.getSpot().getSpotId()
                    + " (type=" + hTicket.getSpot().getSpotType() + ")");
        } catch (IllegalStateException e) {
            System.out.println("  No handicapped spot matched for car.");
        }

        banner("Parking Lot Demo Complete");
    }
}
