package org.interview.system_design.lld.parkinglot;

/**
 * Handicapped spot — reserved for motorcycles and cars with handicap permits.
 * For simplicity, any motorcycle or car can use this spot.
 */
public class HandicappedSpot extends ParkingSpot {
    public HandicappedSpot(String spotId) {
        super(spotId, SpotType.HANDICAPPED);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        VehicleType t = vehicle.getVehicleType();
        return t == VehicleType.MOTORCYCLE || t == VehicleType.CAR;
    }
}
