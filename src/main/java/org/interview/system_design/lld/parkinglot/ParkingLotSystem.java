package org.interview.system_design.lld.parkinglot;

/**
 * Entry point / demo for the Parking Lot LLD.
 *
 * Design Patterns used:
 *   • Singleton  — ParkingLot has one global instance
 *   • Factory    — SpotFactory creates concrete ParkingSpot objects
 *   • Strategy   — ParkingStrategy (NearestSpotStrategy) for spot selection;
 *                  FeeCalculator (HourlyFeeCalculator) for pricing
 *   • State      — SpotState tracks each spot's lifecycle
 */
public class ParkingLotSystem {

    public static void main(String[] args) {
        // 1. Get singleton instance and configure
        ParkingLot lot = ParkingLot.getInstance();
        lot.setName("Downtown Garage");
        lot.setStrategy(new NearestSpotStrategy());
        lot.setFeeCalculator(new HourlyFeeCalculator());

        // 2. Build floors using SpotFactory
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(SpotFactory.createSpot(SpotType.COMPACT,     "1-C1"));
        floor1.addSpot(SpotFactory.createSpot(SpotType.COMPACT,     "1-C2"));
        floor1.addSpot(SpotFactory.createSpot(SpotType.REGULAR,     "1-R1"));
        floor1.addSpot(SpotFactory.createSpot(SpotType.HANDICAPPED, "1-H1"));
        floor1.addSpot(SpotFactory.createSpot(SpotType.LARGE,       "1-L1"));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(SpotFactory.createSpot(SpotType.COMPACT, "2-C1"));
        floor2.addSpot(SpotFactory.createSpot(SpotType.REGULAR, "2-R1"));
        floor2.addSpot(SpotFactory.createSpot(SpotType.LARGE,   "2-L1"));

        lot.addFloor(floor1);
        lot.addFloor(floor2);

        // 3. Park vehicles
        Vehicle bike  = new Motorcycle("MOTO-001");
        Vehicle car1  = new Car("CAR-001");
        Vehicle truck = new Truck("TRUCK-001");
        Vehicle car2  = new Car("CAR-002");

        ParkingTicket t1 = lot.park(bike);
        ParkingTicket t2 = lot.park(car1);
        ParkingTicket t3 = lot.park(truck);
        ParkingTicket t4 = lot.park(car2);

        System.out.println("\nActive tickets: " + lot.getActiveTickets().size());

        // 4. Unpark and print fee
        lot.unpark(t1.getTicketId());
        lot.unpark(t2.getTicketId());
        lot.unpark(t3.getTicketId());
        lot.unpark(t4.getTicketId());

        System.out.println("\nAll vehicles unparked. Active tickets: " + lot.getActiveTickets().size());
    }
}
