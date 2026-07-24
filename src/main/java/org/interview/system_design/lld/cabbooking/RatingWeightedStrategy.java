package org.interview.system_design.lld.cabbooking;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Driver matching strategy that balances proximity and driver rating.
 *
 * Score formula: score = distance_weight * distanceKm - rating_weight * rating
 * Lower score = better match (closer and higher rated).
 */
public class RatingWeightedStrategy implements DriverMatchStrategy {

    /** How much distance (km) penalises the score. */
    private final double distanceWeight;

    /** How much rating rewards the score. */
    private final double ratingWeight;

    public RatingWeightedStrategy() {
        this(1.0, 2.0);  // default: rating counts twice as much per unit
    }

    public RatingWeightedStrategy(double distanceWeight, double ratingWeight) {
        this.distanceWeight = distanceWeight;
        this.ratingWeight = ratingWeight;
    }

    @Override
    public Optional<Driver> findBestDriver(Location pickupLocation, CabType cabType,
                                           List<Driver> drivers) {
        return drivers.stream()
                .filter(d -> d.isAvailable() && d.getCabType() == cabType)
                .min(Comparator.comparingDouble(d -> score(d, pickupLocation)));
    }

    private double score(Driver driver, Location pickup) {
        double distance = driver.getCurrentLocation().distanceTo(pickup);
        return distanceWeight * distance - ratingWeight * driver.getRating();
    }
}
