package org.interview.system_design.lld.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Hourly fee calculator.
 * Rates (per hour):
 *   MOTORCYCLE → $1.00
 *   CAR        → $2.00
 *   TRUCK      → $3.50
 * Minimum charge: 1 hour. Partial hours are rounded up.
 */
public class HourlyFeeCalculator implements FeeCalculator {

    private static final double MOTORCYCLE_RATE = 1.00;
    private static final double CAR_RATE        = 2.00;
    private static final double TRUCK_RATE      = 3.50;

    @Override
    public double calculateFee(ParkingTicket ticket) {
        LocalDateTime exit = LocalDateTime.now(); // Use current time if not yet set
        Duration duration = Duration.between(ticket.getEntryTime(), exit);

        // Minimum 1 hour, round up partial hours
        long hours = Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));

        double rate = rateFor(ticket.getVehicle().getVehicleType());
        return Math.round(hours * rate * 100.0) / 100.0;
    }

    private double rateFor(VehicleType type) {
        switch (type) {
            case MOTORCYCLE: return MOTORCYCLE_RATE;
            case CAR:        return CAR_RATE;
            case TRUCK:      return TRUCK_RATE;
            default:         return CAR_RATE;
        }
    }
}
