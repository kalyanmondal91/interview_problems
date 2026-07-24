package org.interview.system_design.lld.payment;

import java.util.List;
import java.util.Map;

/**
 * Demo runner for the Payment Gateway LLD.
 * Patterns: Adapter, Chain of Responsibility (validation), Strategy (gateway selection)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // -- Setup adapters (each wraps its own concrete gateway) --
        List<PaymentGatewayAdapter> gateways = List.of(
                new StripeAdapter(new StripeGateway()),
                new PayPalAdapter(new PayPalGateway()),
                new RazorpayAdapter(new RazorpayGateway())
        );

        // -- Scenario 1: Successful payment -- CheapestGatewayStrategy --
        banner("Scenario 1: Successful payment via CheapestGatewayStrategy");
        // PaymentService(gateways, strategy) -- 2-arg constructor; builds validation chain internally
        PaymentService cheapService = new PaymentService(gateways, new CheapestGatewayStrategy());
        cheapService.addListener(new PaymentService.PaymentEventListener() {
            @Override public void onPaymentSuccess(PaymentResponse r) {
                System.out.println("  [Event] Success: " + r.getTransactionId()
                        + " | Status: " + r.getStatus());
            }
            @Override public void onPaymentFailed(PaymentResponse r) {
                System.out.println("  [Event] Failed: " + r.getMessage());
            }
        });

        // PaymentRequest uses a fluent Builder
        PaymentRequest req1 = PaymentRequest.builder()
                .requestId("req-001")
                .userId("user1")
                .amount(150.00)
                .currency(Currency.USD)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .addMeta("description", "Order payment")
                .build();
        PaymentResponse resp1 = cheapService.processPayment(req1);
        System.out.printf("  Status: %s | TxnId: %s | Gateway: CheapestStrategy%n",
                resp1.getStatus(), resp1.getTransactionId());

        // -- Scenario 2: Amount validation failure (negative amount) --
        banner("Scenario 2: Validation failure -- negative amount");
        PaymentRequest req2 = PaymentRequest.builder()
                .requestId("req-002")
                .userId("user2")
                .amount(-50.00)
                .currency(Currency.USD)
                .paymentMethod(PaymentMethod.DEBIT_CARD)
                .build();
        PaymentResponse resp2 = cheapService.processPayment(req2);
        System.out.println("  Status: " + resp2.getStatus() + " | Reason: " + resp2.getMessage());

        // -- Scenario 3: Fraud detection rejects large amount --
        banner("Scenario 3: Fraud detection -- suspiciously large amount");
        PaymentRequest req3 = PaymentRequest.builder()
                .requestId("req-003")
                .userId("user3")
                .amount(999999.99)
                .currency(Currency.USD)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();
        PaymentResponse resp3 = cheapService.processPayment(req3);
        System.out.println("  Status: " + resp3.getStatus() + " | Reason: " + resp3.getMessage());

        // -- Scenario 4: Refund --
        banner("Scenario 4: Refund successful transaction");
        if (resp1.getTransactionId() != null && !resp1.getTransactionId().isEmpty()) {
            PaymentResponse refund = cheapService.refund(resp1.getTransactionId(), 50.00);
            System.out.println("  Refund status: " + refund.getStatus()
                    + " | Amount: $50.00");
        }

        // -- Scenario 5: FastestGatewayStrategy --
        banner("Scenario 5: FastestGatewayStrategy -- picks by latency");
        PaymentService fastService = new PaymentService(gateways, new FastestGatewayStrategy());
        PaymentRequest req4 = PaymentRequest.builder()
                .requestId("req-004")
                .userId("user4")
                .amount(200.00)
                .currency(Currency.EUR)
                .paymentMethod(PaymentMethod.UPI)
                .build();
        PaymentResponse resp4 = fastService.processPayment(req4);
        System.out.printf("  Status: %s | TxnId: %s%n", resp4.getStatus(), resp4.getTransactionId());

        // -- Scenario 6: Idempotency -- reuse same requestId --
        banner("Scenario 6: Idempotency -- duplicate requestId");
        PaymentRequest req5a = PaymentRequest.builder()
                .requestId("req-idem-001")
                .userId("user5")
                .amount(75.00)
                .currency(Currency.USD)
                .paymentMethod(PaymentMethod.WALLET)
                .build();
        PaymentRequest req5b = PaymentRequest.builder()
                .requestId("req-idem-001")
                .userId("user5")
                .amount(75.00)
                .currency(Currency.USD)
                .paymentMethod(PaymentMethod.WALLET)
                .build();
        PaymentResponse r5a = cheapService.processPayment(req5a);
        PaymentResponse r5b = cheapService.processPayment(req5b);
        System.out.println("  First  txnId: " + r5a.getTransactionId());
        System.out.println("  Second txnId: " + r5b.getTransactionId()
                + " (same = idempotent, different = processed twice)");

        banner("Payment Gateway Demo Complete");
    }
}
