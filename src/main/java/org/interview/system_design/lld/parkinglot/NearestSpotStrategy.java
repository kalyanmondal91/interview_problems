package org.interview.system_design.lld.parkinglot;

import java.util.List;

/**
 * Strategy: finds the nearest available spot.
 * Iterates floors in order (lower floor = nearer entrance) and returns
 * the first spot within that floor that can accommodate the vehicle.
 * Within a floor, spots are ordered by their position index (index 0 = nearest).
 */
public class NearestSpotStrategy implements ParkingStrategy {

    @Override
    public ParkingSpot findSpot(Vehicle vehicle, List<ParkingFloor> floors) {
        for (ParkingFloor floor : floors) {
            List<ParkingSpot> spots = floor.getSpots();
            for (ParkingSpot spot : spots) {
                if (spot.isAvailable() && spot.canFit(vehicle)) {
                    return spot;
                }
            }
        }
        return null; // No suitable spot found
    }
}
