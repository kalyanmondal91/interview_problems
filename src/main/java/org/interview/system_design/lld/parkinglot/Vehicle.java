package org.interview.system_design.lld.parkinglot;

/**
 * Abstract base class for all vehicles.
 * Each vehicle has a license plate and a type that determines compatible spots.
 */
public abstract class Vehicle {
    protected final String licensePlate;
    protected final VehicleType vehicleType;

    protected Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() { return licensePlate; }
    public VehicleType getVehicleType() { return vehicleType; }

    @Override
    public String toString() {
        return vehicleType + "[" + licensePlate + "]";
    }
}
