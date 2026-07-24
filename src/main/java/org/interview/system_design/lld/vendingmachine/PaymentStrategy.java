package org.interview.system_design.lld.vendingmachine;

import java.util.Map;

/**
 * Strategy interface for payment processing in the vending machine.
 * Allows different payment methods (coin, card, etc.) to be supported.
 */
public interface PaymentStrategy {
    /**
     * Processes a payment attempt.
     *
     * @param amount   the total amount required
     * @param coinSlot the coins currently inserted, keyed by coin type
     * @return true if payment is successful (sufficient amount inserted)
     */
    boolean pay(double amount, Map<Coin, Integer> coinSlot);
}
