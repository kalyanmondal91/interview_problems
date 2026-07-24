package org.interview.system_design.lld.payment;

import java.util.Comparator;
import java.util.List;

/**
 * Selects the gateway with the lowest estimated processing latency.
 * Pattern: Strategy (concrete)
 */
public class FastestGatewayStrategy implements GatewaySelectionStrategy {

    @Override
    public PaymentGatewayAdapter selectGateway(PaymentRequest request,
                                               List<PaymentGatewayAdapter> gateways) {
        PaymentGatewayAdapter selected = gateways.stream()
                .min(Comparator.comparingLong(PaymentGatewayAdapter::getEstimatedLatencyMs))
                .orElseThrow(() -> new IllegalStateException("No gateways available"));
        System.out.printf("[FastestGatewayStrategy] Selected %s (latency=%dms)%n",
                selected.getGatewayName(), selected.getEstimatedLatencyMs());
        return selected;
    }
}
