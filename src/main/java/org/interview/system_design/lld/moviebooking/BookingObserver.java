package org.interview.system_design.lld.moviebooking;

/**
 * Observer interface for booking lifecycle events.
 * Implement to receive notifications on confirmation and cancellation.
 */
public interface BookingObserver {
    /** Called when a booking is confirmed and payment succeeds. */
    void onBookingConfirmed(Booking booking);

    /** Called when a booking is cancelled. */
    void onBookingCancelled(Booking booking);
}
