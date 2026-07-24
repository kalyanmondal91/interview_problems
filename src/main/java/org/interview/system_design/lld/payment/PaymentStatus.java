package org.interview.system_design.lld.payment;

/**
 * Lifecycle states of a payment transaction.
 */
public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    REFUNDED,
    CANCELLED
}
