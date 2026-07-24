package org.interview.system_design.lld.payment;

/**
 * Supported currencies with their exchange rates relative to USD.
 * Pattern: Value Object (Enum)
 */
public enum Currency {
    USD(1.0),
    EUR(1.08),
    GBP(1.27),
    INR(0.012),
    JPY(0.0067);

    /** Exchange rate: 1 unit of this currency = exchangeRateToUsd USD */
    private final double exchangeRateToUsd;

    Currency(double exchangeRateToUsd) {
        this.exchangeRateToUsd = exchangeRateToUsd;
    }

    public double getExchangeRateToUsd() {
        return exchangeRateToUsd;
    }

    /** Converts an amount in this currency to USD. */
    public double toUsd(double amount) {
        return amount * exchangeRateToUsd;
    }

    /** Converts a USD amount to this currency. */
    public double fromUsd(double usdAmount) {
        return usdAmount / exchangeRateToUsd;
    }
}
