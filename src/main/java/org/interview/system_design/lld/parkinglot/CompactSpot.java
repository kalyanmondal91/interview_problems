package org.interview.system_design.lld.parkinglot;

/**
 * Compact spot — fits only motorcycles and cars.
 */
public class CompactSpot extends ParkingSpot {
    public CompactSpot(String spotId) {
        super(spotId, SpotType.COMPACT);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        VehicleType t = vehicle.getVehicleType();
        return t == VehicleType.MOTORCYCLE || t == VehicleType.CAR;
    }
}
