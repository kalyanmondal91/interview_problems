package org.interview.system_design.lld.payment;

import java.util.List;

/**
 * Strategy interface for choosing which payment gateway to use for a request.
 * Pattern: Strategy
 */
public interface GatewaySelectionStrategy {

    /**
     * Selects the most appropriate gateway from the available list.
     *
     * @param request  the payment to be processed
     * @param gateways available gateway adapters (non-empty)
     * @return the selected adapter
     */
    PaymentGatewayAdapter selectGateway(PaymentRequest request,
                                        List<PaymentGatewayAdapter> gateways);
}
