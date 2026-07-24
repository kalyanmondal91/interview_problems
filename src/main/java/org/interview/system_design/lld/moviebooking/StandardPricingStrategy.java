package org.interview.system_design.lld.moviebooking;

/**
 * Standard pricing strategy: applies a base price per seat type
 * plus an evening/weekend surge multiplier based on show start time.
 */
public class StandardPricingStrategy implements PricingStrategy {

    // Base prices by seat type
    private static final double STANDARD_PRICE = 10.0;
    private static final double PREMIUM_PRICE  = 15.0;
    private static final double VIP_PRICE       = 25.0;
    private static final double WHEELCHAIR_PRICE = 8.0;

    // Evening shows (18:00+) attract a 25% surge
    private static final double EVENING_SURGE_MULTIPLIER = 1.25;
    private static final int EVENING_HOUR = 18;

    @Override
    public double calculatePrice(Seat seat, Show show) {
        double basePrice = getBasePrice(seat.getType());
        double multiplier = isSurgeTime(show) ? EVENING_SURGE_MULTIPLIER : 1.0;
        return basePrice * multiplier;
    }

    private double getBasePrice(SeatType type) {
        return switch (type) {
            case STANDARD   -> STANDARD_PRICE;
            case PREMIUM    -> PREMIUM_PRICE;
            case VIP        -> VIP_PRICE;
            case WHEELCHAIR -> WHEELCHAIR_PRICE;
        };
    }

    private boolean isSurgeTime(Show show) {
        return show.getStartTime().getHour() >= EVENING_HOUR;
    }
}
