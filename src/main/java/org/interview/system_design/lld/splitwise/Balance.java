package org.interview.system_design.lld.splitwise;

/**
 * Represents a net balance: {@code from} owes {@code amount} to {@code to}.
 * Negative amounts indicate the relationship is reversed.
 */
public class Balance {
    private final String from;  // debtor
    private final String to;    // creditor
    private final double amount;

    public Balance(String from, String to, double amount) {
        this.from   = from;
        this.to     = to;
        this.amount = amount;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("%s owes %s $%.2f", from, to, amount);
    }
}
