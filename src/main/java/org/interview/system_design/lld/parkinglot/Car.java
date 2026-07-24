package org.interview.system_design.lld.parkinglot;

/**
 * Concrete vehicle: Car.
 * Can be parked in COMPACT, REGULAR, or HANDICAPPED spots.
 */
public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}
