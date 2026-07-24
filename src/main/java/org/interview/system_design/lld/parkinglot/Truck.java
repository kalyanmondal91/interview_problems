package org.interview.system_design.lld.parkinglot;

/**
 * Concrete vehicle: Truck.
 * Requires a LARGE spot.
 */
public class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}
