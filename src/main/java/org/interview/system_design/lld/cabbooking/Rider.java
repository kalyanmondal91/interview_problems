package org.interview.system_design.lld.cabbooking;

/**
 * Represents a rider who requests cab rides.
 */
public class Rider {
    private final String riderId;
    private final String name;
    private final String phone;
    private Location currentLocation;

    public Rider(String riderId, String name, String phone, Location currentLocation) {
        this.riderId = riderId;
        this.name = name;
        this.phone = phone;
        this.currentLocation = currentLocation;
    }

    public String getRiderId() { return riderId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Location getCurrentLocation() { return currentLocation; }

    public void setCurrentLocation(Location location) { this.currentLocation = location; }

    @Override
    public String toString() {
        return String.format("Rider{id='%s', name='%s', location=%s}", riderId, name, currentLocation);
    }
}
