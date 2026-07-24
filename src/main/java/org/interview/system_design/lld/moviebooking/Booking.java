package org.interview.system_design.lld.moviebooking;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Represents a movie ticket booking by a user.
 * Tracks the seats, payment, and current booking status (State pattern).
 */
public class Booking {
    private final String bookingId;
    private final Show show;
    private final List<Seat> seats;
    private final String userId;
    private BookingStatus status;
    private double totalAmount;
    private final LocalDateTime timestamp;

    public Booking(String bookingId, Show show, List<Seat> seats,
                   String userId, double totalAmount) {
        this.bookingId = bookingId;
        this.show = show;
        this.seats = seats;
        this.userId = userId;
        this.status = BookingStatus.PENDING;
        this.totalAmount = totalAmount;
        this.timestamp = LocalDateTime.now();
    }

    public String getBookingId() { return bookingId; }
    public Show getShow() { return show; }
    public List<Seat> getSeats() { return Collections.unmodifiableList(seats); }
    public String getUserId() { return userId; }
    public BookingStatus getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(BookingStatus status) { this.status = status; }
    public void setTotalAmount(double amount) { this.totalAmount = amount; }

    @Override
    public String toString() {
        return String.format("Booking{id='%s', movie='%s', user='%s', seats=%d, amount=%.2f, status=%s}",
                bookingId, show.getMovie().getTitle(), userId, seats.size(), totalAmount, status);
    }
}
