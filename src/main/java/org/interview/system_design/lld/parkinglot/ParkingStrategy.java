package org.interview.system_design.lld.parkinglot;

import java.util.List;

/**
 * Strategy Pattern interface for selecting a parking spot.
 * Different implementations can offer nearest-spot, cheapest-spot, or
 * load-balanced strategies without changing the ParkingLot core.
 */
public interface ParkingStrategy {
    /**
     * Finds a suitable available spot for the given vehicle across all floors.
     *
     * @param vehicle the vehicle that needs parking
     * @param floors  all floors in the parking lot
     * @return a suitable ParkingSpot, or null if none is available
     */
    ParkingSpot findSpot(Vehicle vehicle, List<ParkingFloor> floors);
}
