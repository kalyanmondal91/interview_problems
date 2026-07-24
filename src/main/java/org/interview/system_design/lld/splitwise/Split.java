package org.interview.system_design.lld.splitwise;

/**
 * Represents one person's share of an expense.
 * Immutable once created by a SplitStrategy.
 */
public class Split {
    private final String userId;
    private double amount;

    public Split(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return String.format("Split[user=%s, owes=%.2f]", userId, amount);
    }
}
