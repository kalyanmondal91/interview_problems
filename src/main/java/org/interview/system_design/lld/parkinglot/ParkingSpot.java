package org.interview.system_design.lld.parkinglot;

/**
 * Abstract base class for all parking spots.
 * Implements the State Pattern via SpotState, and defines
 * the canFit() contract for each concrete spot type.
 */
public abstract class ParkingSpot {
    protected final String spotId;
    protected final SpotType spotType;
    protected SpotState state;
    protected Vehicle parkedVehicle;

    protected ParkingSpot(String spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.state = SpotState.AVAILABLE;
    }

    /**
     * Determines whether the given vehicle can fit in this spot.
     * Each subclass implements vehicle-type compatibility rules.
     */
    public abstract boolean canFit(Vehicle vehicle);

    public boolean isAvailable() {
        return state == SpotState.AVAILABLE;
    }

    /**
     * Parks a vehicle in this spot, transitioning state to OCCUPIED.
     * @throws IllegalStateException if spot is not available
     */
    public void park(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot " + spotId + " is not available. Current state: " + state);
        }
        if (!canFit(vehicle)) {
            throw new IllegalArgumentException("Vehicle " + vehicle + " cannot fit in spot " + spotId);
        }
        this.parkedVehicle = vehicle;
        this.state = SpotState.OCCUPIED;
    }

    /**
     * Removes the parked vehicle, transitioning state back to AVAILABLE.
     * @throws IllegalStateException if spot is not occupied
     */
    public Vehicle unpark() {
        if (state != SpotState.OCCUPIED) {
            throw new IllegalStateException("Spot " + spotId + " is not occupied.");
        }
        Vehicle removed = this.parkedVehicle;
        this.parkedVehicle = null;
        this.state = SpotState.AVAILABLE;
        return removed;
    }

    public void reserve() {
        if (!isAvailable()) throw new IllegalStateException("Cannot reserve: spot " + spotId + " is " + state);
        this.state = SpotState.RESERVED;
    }

    public void setOutOfService() { this.state = SpotState.OUT_OF_SERVICE; }

    public String getSpotId() { return spotId; }
    public SpotType getSpotType() { return spotType; }
    public SpotState getState() { return state; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }

    @Override
    public String toString() {
        return spotType + "Spot[" + spotId + "](" + state + ")";
    }
}
