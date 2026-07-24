package org.interview.system_design.lld.cabbooking;

/**
 * Standard fare calculator: baseFare + perKm rate, varying by cab type.
 */
public class StandardFareCalculator implements FareCalculator {

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = getBaseFare(ride.getCabType());
        double perKmRate = getPerKmRate(ride.getCabType());
        double distanceKm = ride.getDistanceKm();
        return baseFare + (perKmRate * distanceKm);
    }

    private double getBaseFare(CabType type) {
        return switch (type) {
            case AUTO    -> 15.0;
            case MINI    -> 20.0;
            case SEDAN   -> 30.0;
            case SUV     -> 40.0;
            case PREMIUM -> 60.0;
        };
    }

    private double getPerKmRate(CabType type) {
        return switch (type) {
            case AUTO    -> 6.0;
            case MINI    -> 8.0;
            case SEDAN   -> 12.0;
            case SUV     -> 15.0;
            case PREMIUM -> 20.0;
        };
    }
}
