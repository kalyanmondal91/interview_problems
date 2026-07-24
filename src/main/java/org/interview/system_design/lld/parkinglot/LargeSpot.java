package org.interview.system_design.lld.parkinglot;

/**
 * Large spot — fits all vehicle types including trucks.
 */
public class LargeSpot extends ParkingSpot {
    public LargeSpot(String spotId) {
        super(spotId, SpotType.LARGE);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        return true; // All vehicles fit
    }
}
