package org.interview.system_design.lld.parkinglot;

/**
 * Factory Pattern — creates ParkingSpot instances based on SpotType.
 * Centralizes spot construction logic so callers don't depend on concrete classes.
 */
public class SpotFactory {

    /**
     * Creates a parking spot of the specified type with the given ID.
     *
     * @param type   the spot type
     * @param spotId unique identifier for the spot
     * @return a new ParkingSpot instance
     */
    public static ParkingSpot createSpot(SpotType type, String spotId) {
        switch (type) {
            case COMPACT:     return new CompactSpot(spotId);
            case REGULAR:     return new RegularSpot(spotId);
            case LARGE:       return new LargeSpot(spotId);
            case HANDICAPPED: return new HandicappedSpot(spotId);
            default: throw new IllegalArgumentException("Unknown spot type: " + type);
        }
    }
}
