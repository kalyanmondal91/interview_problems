package org.interview.system_design.lld.moviebooking;

/**
 * Strategy interface for seat pricing.
 * Allows different pricing algorithms (standard, surge, discount) to be swapped in.
 */
public interface PricingStrategy {
    /**
     * Calculates the price for a single seat in a given show.
     *
     * @param seat the seat being priced
     * @param show the show context (for time-of-day pricing, etc.)
     * @return the price in currency units
     */
    double calculatePrice(Seat seat, Show show);
}
