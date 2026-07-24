package org.interview.system_design.lld.payment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents an incoming payment request from a user.
 * Constructed with a fluent builder for readability.
 */
public class PaymentRequest {

    private final String requestId;
    private final String userId;
    private final double amount;
    private final Currency currency;
    private final PaymentMethod paymentMethod;
    private final Map<String, String> metadata;

    private PaymentRequest(Builder builder) {
        this.requestId     = builder.requestId;
        this.userId        = builder.userId;
        this.amount        = builder.amount;
        this.currency      = builder.currency;
        this.paymentMethod = builder.paymentMethod;
        this.metadata      = Collections.unmodifiableMap(new HashMap<>(builder.metadata));
    }

    public String getRequestId()          { return requestId; }
    public String getUserId()             { return userId; }
    public double getAmount()             { return amount; }
    public Currency getCurrency()         { return currency; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Map<String, String> getMetadata() { return metadata; }

    @Override
    public String toString() {
        return String.format("PaymentRequest[%s] user=%s amount=%.2f %s via %s",
                requestId, userId, amount, currency, paymentMethod);
    }

    // ---- Builder ----

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String requestId = UUID.randomUUID().toString();
        private String userId;
        private double amount;
        private Currency currency = Currency.USD;
        private PaymentMethod paymentMethod;
        private Map<String, String> metadata = new HashMap<>();

        public Builder requestId(String id)              { this.requestId = id;        return this; }
        public Builder userId(String userId)             { this.userId = userId;        return this; }
        public Builder amount(double amount)             { this.amount = amount;        return this; }
        public Builder currency(Currency currency)       { this.currency = currency;    return this; }
        public Builder paymentMethod(PaymentMethod pm)   { this.paymentMethod = pm;     return this; }
        public Builder metadata(Map<String, String> m)   { this.metadata = m;           return this; }
        public Builder addMeta(String key, String value) { this.metadata.put(key, value); return this; }

        public PaymentRequest build() {
            if (userId == null || paymentMethod == null) {
                throw new IllegalStateException("userId and paymentMethod are required");
            }
            return new PaymentRequest(this);
        }
    }
}
