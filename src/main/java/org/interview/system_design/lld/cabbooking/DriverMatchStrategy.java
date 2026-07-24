package org.interview.system_design.lld.cabbooking;

import java.util.List;
import java.util.Optional;

/**
 * Strategy interface for matching drivers to ride requests.
 * Allows different algorithms (nearest, highest-rated, etc.) to be plugged in.
 */
public interface DriverMatchStrategy {
    /**
     * Finds the best available driver for a ride request.
     *
     * @param pickupLocation the rider's pickup location
     * @param cabType        the requested cab type
     * @param drivers        the full list of registered drivers
     * @return the best matched driver, or empty if none available
     */
    Optional<Driver> findBestDriver(Location pickupLocation, CabType cabType, List<Driver> drivers);
}
