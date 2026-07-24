package org.interview.system_design.lld.payment;

import java.util.Comparator;
import java.util.List;

/**
 * Selects the gateway with the lowest transaction fee.
 * Pattern: Strategy (concrete)
 */
public class CheapestGatewayStrategy implements GatewaySelectionStrategy {

    @Override
    public PaymentGatewayAdapter selectGateway(PaymentRequest request,
                                               List<PaymentGatewayAdapter> gateways) {
        PaymentGatewayAdapter selected = gateways.stream()
                .min(Comparator.comparingDouble(PaymentGatewayAdapter::getFeePercent))
                .orElseThrow(() -> new IllegalStateException("No gateways available"));
        System.out.printf("[CheapestGatewayStrategy] Selected %s (fee=%.1f%%)%n",
                selected.getGatewayName(), selected.getFeePercent() * 100);
        return selected;
    }
}
