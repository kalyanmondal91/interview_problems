package org.interview.system_design.lld.cabbooking;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Driver matching strategy that selects the nearest available driver
 * of the requested cab type.
 */
public class NearestDriverStrategy implements DriverMatchStrategy {

    @Override
    public Optional<Driver> findBestDriver(Location pickupLocation, CabType cabType,
                                           List<Driver> drivers) {
        return drivers.stream()
                .filter(d -> d.isAvailable() && d.getCabType() == cabType)
                .min(Comparator.comparingDouble(
                        d -> d.getCurrentLocation().distanceTo(pickupLocation)));
    }
}
