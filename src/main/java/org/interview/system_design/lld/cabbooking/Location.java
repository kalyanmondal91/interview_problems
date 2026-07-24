package org.interview.system_design.lld.cabbooking;

/**
 * Represents a geographic location with latitude and longitude.
 * Uses the Haversine formula to compute distances between coordinates.
 */
public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    /**
     * Computes the great-circle distance between two locations using the Haversine formula.
     *
     * @param other the other location
     * @return distance in kilometers
     */
    public double distanceTo(Location other) {
        final double EARTH_RADIUS_KM = 6371.0;
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public String toString() {
        return String.format("Location(%.4f, %.4f)", latitude, longitude);
    }
}
