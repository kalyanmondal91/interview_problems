package org.interview.system_design.lld.parkinglot;

/**
 * Strategy Pattern interface for computing parking fees.
 * Implementations can apply flat rates, hourly rates, surge pricing, etc.
 */
public interface FeeCalculator {
    /**
     * Calculates the parking fee for the given ticket.
     *
     * @param ticket the parking ticket (must have exitTime set)
     * @return total fee in currency units (e.g. dollars)
     */
    double calculateFee(ParkingTicket ticket);
}
