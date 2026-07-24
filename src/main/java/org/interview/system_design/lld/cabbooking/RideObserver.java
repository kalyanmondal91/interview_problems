package org.interview.system_design.lld.cabbooking;

/**
 * Observer interface for ride status change events.
 */
public interface RideObserver {
    /**
     * Called whenever the status of a ride changes.
     *
     * @param ride      the ride whose status changed
     * @param newStatus the new status of the ride
     */
    void onRideStatusChanged(Ride ride, RideStatus newStatus);
}
