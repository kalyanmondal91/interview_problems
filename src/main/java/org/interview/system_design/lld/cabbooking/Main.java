package org.interview.system_design.lld.cabbooking;

/**
 * Demo runner for the Cab Booking LLD (Uber/Ola style).
 * Patterns: Strategy (driver matching, fare), Observer, State
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // -- Setup: locations --
        Location riderLoc  = new Location(12.9716, 77.5946); // Bangalore center
        Location dropLoc   = new Location(12.9352, 77.6245); // ~5 km away

        Location driverLoc1 = new Location(12.9720, 77.5950); // very close (0.05 km)
        Location driverLoc2 = new Location(12.9800, 77.5900); // medium (1.0 km)
        Location driverLoc3 = new Location(12.9600, 77.6100); // farther (2.5 km)

        // -- Scenario 1: Nearest driver match --
        banner("Scenario 1: NearestDriverStrategy -- 3 SEDAN drivers");
        CabBookingService service1 = new CabBookingService(
                new NearestDriverStrategy(), new StandardFareCalculator());

        service1.addObserver(new RideObserver() {
            @Override public void onRideStatusChanged(Ride ride, RideStatus status) {
                System.out.printf("  [Event] Ride %s -> %s%n", ride.getRideId(), status);
            }
        });

        Driver d1 = new Driver("dr1", "Ravi",   "9000001111", CabType.SEDAN, driverLoc1);
        Driver d2 = new Driver("dr2", "Suresh", "9000002222", CabType.SEDAN, driverLoc2);
        Driver d3 = new Driver("dr3", "Kumar",  "9000003333", CabType.SEDAN, driverLoc3);
        Rider  r1 = new Rider("rd1", "Alice", "8000001111", riderLoc);

        service1.registerDriver(d1); service1.registerDriver(d2); service1.registerDriver(d3);
        service1.registerRider(r1);

        Ride ride1 = service1.requestRide("rd1", riderLoc, dropLoc, CabType.SEDAN);
        if (ride1 != null) {
            System.out.println("  Assigned driver: " + ride1.getDriver().getName()
                    + " (expected: Ravi -- nearest)");
        } else {
            System.out.println("  No driver found.");
        }

        // -- Scenario 2: Full ride lifecycle --
        banner("Scenario 2: Full ride lifecycle");
        service1.startRide(ride1.getRideId());
        System.out.println("  Ride started. Status: " + ride1.getStatus());

        double fare = service1.completeRide(ride1.getRideId());
        System.out.printf("  Ride completed. Fare: $%.2f | Distance: ~%.2f km%n",
                fare, riderLoc.distanceTo(dropLoc));
        System.out.println("  Ride status: " + ride1.getStatus());

        // -- Scenario 3: Cancel a ride before start --
        banner("Scenario 3: Cancel ride before it starts");
        CabBookingService service2 = new CabBookingService(
                new NearestDriverStrategy(), new StandardFareCalculator());
        Driver d4  = new Driver("dr4", "Anand", "9111111111", CabType.MINI, driverLoc1);
        Rider  r2  = new Rider("rd2", "Bob", "8222222222", riderLoc);
        service2.registerDriver(d4);
        service2.registerRider(r2);

        Ride ride2 = service2.requestRide("rd2", riderLoc, dropLoc, CabType.MINI);
        if (ride2 != null) {
            System.out.println("  Ride requested. Status: " + ride2.getStatus());
            service2.cancelRide(ride2.getRideId());
            System.out.println("  Ride cancelled. Status: " + ride2.getStatus());
            System.out.println("  Driver status after cancel: " + d4.getStatus());
        }

        // -- Scenario 4: RatingWeightedStrategy --
        banner("Scenario 4: RatingWeightedStrategy -- rating matters");
        CabBookingService service3 = new CabBookingService(
                new RatingWeightedStrategy(), new StandardFareCalculator());
        Driver near    = new Driver("dr5", "LowRating",  "9333333333", CabType.SEDAN, driverLoc1);
        Driver farHigh = new Driver("dr6", "HighRating", "9444444444", CabType.SEDAN, driverLoc2);
        near.addRating(3.0);
        farHigh.addRating(4.9);
        Rider r3 = new Rider("rd3", "Carol", "8333333333", riderLoc);
        service3.registerDriver(near); service3.registerDriver(farHigh);
        service3.registerRider(r3);

        Ride ride3 = service3.requestRide("rd3", riderLoc, dropLoc, CabType.SEDAN);
        if (ride3 != null) {
            System.out.println("  Assigned: " + ride3.getDriver().getName()
                    + " (rating=" + ride3.getDriver().getRating() + ")");
        }

        // -- Scenario 5: No available driver --
        banner("Scenario 5: No available driver");
        CabBookingService service4 = new CabBookingService(
                new NearestDriverStrategy(), new StandardFareCalculator());
        Driver busy = new Driver("dr7", "Busy", "9555555555", CabType.SUV, driverLoc1);
        busy.setStatus(DriverStatus.ON_TRIP);
        Rider  r4   = new Rider("rd4", "Dave", "8444444444", riderLoc);
        service4.registerDriver(busy);
        service4.registerRider(r4);

        Ride ride4 = service4.requestRide("rd4", riderLoc, dropLoc, CabType.SUV);
        System.out.println("  Ride result: " + (ride4 == null ? "No driver available" : ride4.getRideId()));

        System.out.println();
        System.out.println("=== Cab Booking Demo Complete ===");
    }
}
