package org.interview.system_design.lld.cabbooking;

import java.time.LocalDateTime;

/**
 * Represents a single cab ride from pickup to drop-off.
 * Models the State pattern: status transitions through REQUESTED → DRIVER_ASSIGNED
 * → IN_PROGRESS → COMPLETED / CANCELLED.
 */
public class Ride {
    private final String rideId;
    private final Rider rider;
    private Driver driver;
    private final Location pickupLocation;
    private final Location dropLocation;
    private final CabType cabType;
    private RideStatus status;
    private double fare;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Ride(String rideId, Rider rider, Location pickupLocation,
                Location dropLocation, CabType cabType) {
        this.rideId = rideId;
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.cabType = cabType;
        this.status = RideStatus.REQUESTED;
    }

    public String getRideId() { return rideId; }
    public Rider getRider() { return rider; }
    public Driver getDriver() { return driver; }
    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropLocation() { return dropLocation; }
    public CabType getCabType() { return cabType; }
    public RideStatus getStatus() { return status; }
    public double getFare() { return fare; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public void setDriver(Driver driver) { this.driver = driver; }
    public void setStatus(RideStatus status) { this.status = status; }
    public void setFare(double fare) { this.fare = fare; }
    public void setStartTime(LocalDateTime time) { this.startTime = time; }
    public void setEndTime(LocalDateTime time) { this.endTime = time; }

    /** Computes the straight-line distance between pickup and drop in km. */
    public double getDistanceKm() {
        return pickupLocation.distanceTo(dropLocation);
    }

    @Override
    public String toString() {
        return String.format("Ride{id='%s', rider='%s', driver='%s', status=%s, fare=%.2f}",
                rideId, rider.getName(),
                driver != null ? driver.getName() : "unassigned",
                status, fare);
    }
}
