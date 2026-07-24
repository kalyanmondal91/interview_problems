package org.interview.system_design.lld.cabbooking;

/**
 * Strategy interface for fare calculation.
 */
public interface FareCalculator {
    /**
     * Calculates the fare for a completed ride.
     *
     * @param ride the completed ride with distance data
     * @return the fare amount
     */
    double calculateFare(Ride ride);
}
