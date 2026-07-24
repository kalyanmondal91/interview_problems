package org.interview.system_design.lld.cabbooking;

/**
 * Represents a cab driver in the system.
 */
public class Driver {
    private final String driverId;
    private final String name;
    private final String phone;
    private final CabType cabType;
    private Location currentLocation;
    private DriverStatus status;
    private double rating;
    private int totalRatings;

    public Driver(String driverId, String name, String phone, CabType cabType, Location initialLocation) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.cabType = cabType;
        this.currentLocation = initialLocation;
        this.status = DriverStatus.AVAILABLE;
        this.rating = 5.0;
        this.totalRatings = 0;
    }

    public String getDriverId() { return driverId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public CabType getCabType() { return cabType; }
    public Location getCurrentLocation() { return currentLocation; }
    public DriverStatus getStatus() { return status; }
    public double getRating() { return rating; }

    public void setCurrentLocation(Location location) { this.currentLocation = location; }
    public void setStatus(DriverStatus status) { this.status = status; }

    /** Updates the driver's rating using a running average. */
    public void addRating(double newRating) {
        totalRatings++;
        rating = ((rating * (totalRatings - 1)) + newRating) / totalRatings;
    }

    public boolean isAvailable() { return status == DriverStatus.AVAILABLE; }

    @Override
    public String toString() {
        return String.format("Driver{id='%s', name='%s', type=%s, rating=%.1f, status=%s}",
                driverId, name, cabType, rating, status);
    }
}
