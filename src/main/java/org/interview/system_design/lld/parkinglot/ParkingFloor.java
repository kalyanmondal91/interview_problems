package org.interview.system_design.lld.parkinglot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single floor in the parking lot.
 * Holds an ordered list of spots; their index within the list
 * is used by strategies as a proximity proxy.
 */
public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }

    /** Adds a spot to this floor. */
    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    /** Returns an unmodifiable view of all spots on this floor. */
    public List<ParkingSpot> getSpots() {
        return Collections.unmodifiableList(spots);
    }

    /** Returns all available spots that can accommodate the given vehicle. */
    public List<ParkingSpot> getAvailableSpots(Vehicle vehicle) {
        List<ParkingSpot> available = new ArrayList<>();
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.canFit(vehicle)) {
                available.add(spot);
            }
        }
        return available;
    }

    public int getFloorNumber() { return floorNumber; }

    /** Total count of spots on this floor. */
    public int totalSpots() { return spots.size(); }

    /** Count of available spots that fit the vehicle. */
    public long availableCount(Vehicle vehicle) {
        return spots.stream().filter(s -> s.isAvailable() && s.canFit(vehicle)).count();
    }

    @Override
    public String toString() {
        return "Floor " + floorNumber + " [" + spots.size() + " spots]";
    }
}
