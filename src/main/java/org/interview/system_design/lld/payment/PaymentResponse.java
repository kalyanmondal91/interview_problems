package org.interview.system_design.lld.payment;

import java.time.LocalDateTime;

/**
 * Result of a payment processing attempt.
 * Immutable value object.
 */
public class PaymentResponse {

    private final String requestId;
    private final PaymentStatus status;
    private final String transactionId;
    private final String message;
    private final LocalDateTime timestamp;

    public PaymentResponse(String requestId, PaymentStatus status,
                           String transactionId, String message) {
        this.requestId     = requestId;
        this.status        = status;
        this.transactionId = transactionId;
        this.message       = message;
        this.timestamp     = LocalDateTime.now();
    }

    public String getRequestId()        { return requestId; }
    public PaymentStatus getStatus()    { return status; }
    public String getTransactionId()    { return transactionId; }
    public String getMessage()          { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public boolean isSuccess() {
        return status == PaymentStatus.SUCCESS;
    }

    @Override
    public String toString() {
        return String.format("PaymentResponse[req=%s, txn=%s, status=%s, msg=%s]",
                requestId, transactionId, status, message);
    }
}
