package org.interview.system_design.lld.cabbooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Core service for the cab booking system.
 *
 * Design patterns:
 *  - Strategy: DriverMatchStrategy and FareCalculator are injectable
 *  - Observer: RideObserver notified on every status change
 *  - State: Ride transitions through REQUESTED → DRIVER_ASSIGNED → IN_PROGRESS
 *            → COMPLETED / CANCELLED
 */
public class CabBookingService {
    private final Map<String, Driver> drivers = new HashMap<>();
    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Ride> rides = new HashMap<>();
    private final List<RideObserver> observers = new ArrayList<>();
    private DriverMatchStrategy matchStrategy;
    private FareCalculator fareCalculator;

    public CabBookingService() {
        this.matchStrategy = new NearestDriverStrategy();
        this.fareCalculator = new StandardFareCalculator();
    }

    public CabBookingService(DriverMatchStrategy matchStrategy, FareCalculator fareCalculator) {
        this.matchStrategy = matchStrategy;
        this.fareCalculator = fareCalculator;
    }

    // ---- Observer management ----

    public void addObserver(RideObserver observer) { observers.add(observer); }
    public void removeObserver(RideObserver observer) { observers.remove(observer); }

    private void notifyStatusChange(Ride ride, RideStatus status) {
        observers.forEach(o -> o.onRideStatusChanged(ride, status));
    }

    // ---- Registration ----

    public void registerDriver(Driver driver) { drivers.put(driver.getDriverId(), driver); }
    public void registerRider(Rider rider) { riders.put(rider.getRiderId(), rider); }

    // ---- Ride lifecycle ----

    /**
     * Creates a ride request and immediately attempts driver assignment.
     *
     * @param riderId        the requesting rider ID
     * @param pickupLocation rider's pickup location
     * @param dropLocation   destination
     * @param cabType        requested cab type
     * @return the created Ride (status = DRIVER_ASSIGNED or REQUESTED if no driver)
     */
    public Ride requestRide(String riderId, Location pickupLocation,
                            Location dropLocation, CabType cabType) {
        Rider rider = Optional.ofNullable(riders.get(riderId))
                .orElseThrow(() -> new IllegalArgumentException("Rider not found: " + riderId));

        String rideId = "RD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ride ride = new Ride(rideId, rider, pickupLocation, dropLocation, cabType);
        rides.put(rideId, ride);

        notifyStatusChange(ride, RideStatus.REQUESTED);
        System.out.printf("Ride requested by %s [%s]%n", rider.getName(), rideId);

        // Attempt driver assignment immediately
        Optional<Driver> driverOpt = matchStrategy.findBestDriver(pickupLocation, cabType,
                new ArrayList<>(drivers.values()));
        if (driverOpt.isPresent()) {
            assignDriver(ride, driverOpt.get());
        } else {
            System.out.println("No available driver found. Ride remains in REQUESTED state.");
        }
        return ride;
    }

    /**
     * Assigns a specific driver to a ride (also callable manually).
     */
    public void assignDriver(Ride ride, Driver driver) {
        ride.setDriver(driver);
        ride.setStatus(RideStatus.DRIVER_ASSIGNED);
        driver.setStatus(DriverStatus.ON_TRIP);
        notifyStatusChange(ride, RideStatus.DRIVER_ASSIGNED);
        System.out.printf("Driver %s assigned to ride %s%n", driver.getName(), ride.getRideId());
    }

    /**
     * Marks a ride as IN_PROGRESS (driver has picked up the rider).
     */
    public void startRide(String rideId) {
        Ride ride = getRide(rideId);
        if (ride.getStatus() != RideStatus.DRIVER_ASSIGNED) {
            throw new IllegalStateException("Ride must be DRIVER_ASSIGNED to start: " + rideId);
        }
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartTime(LocalDateTime.now());
        notifyStatusChange(ride, RideStatus.IN_PROGRESS);
        System.out.printf("Ride %s started.%n", rideId);
    }

    /**
     * Completes a ride, calculates fare, and frees the driver.
     *
     * @return the calculated fare
     */
    public double completeRide(String rideId) {
        Ride ride = getRide(rideId);
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ride must be IN_PROGRESS to complete: " + rideId);
        }
        ride.setEndTime(LocalDateTime.now());
        double fare = fareCalculator.calculateFare(ride);
        ride.setFare(fare);
        ride.setStatus(RideStatus.COMPLETED);

        // Free the driver
        if (ride.getDriver() != null) {
            ride.getDriver().setStatus(DriverStatus.AVAILABLE);
            ride.getDriver().setCurrentLocation(ride.getDropLocation());
        }

        notifyStatusChange(ride, RideStatus.COMPLETED);
        System.out.printf("Ride %s completed. Fare: $%.2f (%.2f km)%n",
                rideId, fare, ride.getDistanceKm());
        return fare;
    }

    /**
     * Cancels a ride. Driver is freed if one was assigned.
     */
    public void cancelRide(String rideId) {
        Ride ride = getRide(rideId);
        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel a finished ride: " + rideId);
        }
        // Free driver if assigned
        if (ride.getDriver() != null) {
            ride.getDriver().setStatus(DriverStatus.AVAILABLE);
        }
        ride.setStatus(RideStatus.CANCELLED);
        notifyStatusChange(ride, RideStatus.CANCELLED);
        System.out.printf("Ride %s cancelled.%n", rideId);
    }

    // ---- Helpers / Accessors ----

    private Ride getRide(String rideId) {
        return Optional.ofNullable(rides.get(rideId))
                .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));
    }

    public Optional<Ride> findRide(String rideId) { return Optional.ofNullable(rides.get(rideId)); }
    public List<Driver> getAllDrivers() { return new ArrayList<>(drivers.values()); }

    public void setMatchStrategy(DriverMatchStrategy strategy) { this.matchStrategy = strategy; }
    public void setFareCalculator(FareCalculator calculator) { this.fareCalculator = calculator; }

    /**
     * Demo main method.
     */
    public static void main(String[] args) {
        CabBookingService service = new CabBookingService();

        service.addObserver((ride, status) ->
                System.out.printf("[Observer] Ride %s → %s%n", ride.getRideId(), status));

        Driver d1 = new Driver("D1", "Raju", "9876543210", CabType.SEDAN,
                new Location(28.6139, 77.2090));
        Driver d2 = new Driver("D2", "Vijay", "9876543211", CabType.MINI,
                new Location(28.6200, 77.2150));
        service.registerDriver(d1);
        service.registerDriver(d2);

        Rider rider = new Rider("R1", "Priya", "9123456789",
                new Location(28.6145, 77.2095));
        service.registerRider(rider);

        Ride ride = service.requestRide("R1",
                new Location(28.6145, 77.2095),
                new Location(28.7041, 77.1025),
                CabType.SEDAN);

        service.startRide(ride.getRideId());
        double fare = service.completeRide(ride.getRideId());
        System.out.printf("Final fare paid: $%.2f%n", fare);
    }
}
