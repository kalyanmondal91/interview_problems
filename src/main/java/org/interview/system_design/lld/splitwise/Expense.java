package org.interview.system_design.lld.splitwise;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a single shared expense.
 * Immutable after construction; splits are computed by a SplitStrategy.
 */
public class Expense {
    private final String expenseId;
    private final String description;
    private final double totalAmount;
    private final String paidBy;        // userId of the payer
    private final List<Split> splits;
    private final SplitType splitType;
    private final LocalDateTime timestamp;

    public Expense(String description, double totalAmount, String paidBy,
                   List<Split> splits, SplitType splitType) {
        this.expenseId   = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy      = paidBy;
        this.splits      = Collections.unmodifiableList(splits);
        this.splitType   = splitType;
        this.timestamp   = LocalDateTime.now();
    }

    public String getExpenseId() { return expenseId; }
    public String getDescription() { return description; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }
    public SplitType getSplitType() { return splitType; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Expense[%s] '%s' $%.2f paid by %s (%s)",
                expenseId, description, totalAmount, paidBy, splitType);
    }
}
