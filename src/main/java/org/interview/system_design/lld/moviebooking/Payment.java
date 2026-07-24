package org.interview.system_design.lld.moviebooking;

import java.time.LocalDateTime;

/**
 * Represents a payment transaction for a booking.
 */
public class Payment {
    private final String paymentId;
    private final Booking booking;
    private final double amount;
    private PaymentStatus status;
    private final LocalDateTime timestamp;

    public Payment(String paymentId, Booking booking, double amount) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.timestamp = LocalDateTime.now();
    }

    public String getPaymentId() { return paymentId; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Payment{id='%s', bookingId='%s', amount=%.2f, status=%s}",
                paymentId, booking.getBookingId(), amount, status);
    }
}
