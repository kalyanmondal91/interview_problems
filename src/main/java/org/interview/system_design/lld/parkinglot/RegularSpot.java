package org.interview.system_design.lld.parkinglot;

/**
 * Regular spot — fits motorcycles and cars (not trucks).
 */
public class RegularSpot extends ParkingSpot {
    public RegularSpot(String spotId) {
        super(spotId, SpotType.REGULAR);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        VehicleType t = vehicle.getVehicleType();
        return t == VehicleType.MOTORCYCLE || t == VehicleType.CAR;
    }
}
