package org.interview.system_design.lld.payment;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates that the requested currency is supported by the system.
 * Pattern: Chain of Responsibility (concrete handler)
 */
public class CurrencyValidator extends PaymentValidator {

    private final Set<Currency> supportedCurrencies;

    /** Accepts all Currency enum values by default. */
    public CurrencyValidator() {
        this.supportedCurrencies = Arrays.stream(Currency.values())
                .collect(Collectors.toSet());
    }

    public CurrencyValidator(Set<Currency> supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
    }

    @Override
    protected String doValidate(PaymentRequest request) {
        if (request.getCurrency() == null) {
            return "Currency must not be null";
        }
        if (!supportedCurrencies.contains(request.getCurrency())) {
            return "Unsupported currency: " + request.getCurrency()
                   + ". Supported: " + supportedCurrencies;
        }
        return null;
    }
}
