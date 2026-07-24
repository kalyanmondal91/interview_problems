package org.interview.system_design.lld.parkinglot;

/**
 * Concrete vehicle: Motorcycle.
 * Fits in any spot type (COMPACT, REGULAR, LARGE, HANDICAPPED).
 */
public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}
