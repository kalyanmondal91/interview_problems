package org.interview.system_design.lld.splitwise;

/**
 * Observer Pattern interface for expense events.
 * Implement to receive notifications when new expenses are added to a group.
 */
public interface ExpenseObserver {
    /**
     * Called whenever a new expense is added to a group.
     *
     * @param expense the newly added expense
     */
    void onExpenseAdded(Expense expense);
}
