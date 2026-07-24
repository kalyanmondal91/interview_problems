package org.interview.system_design.lld.splitwise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Splitwise group — contains members and shared expenses.
 * Implements the Observer pattern: registered ExpenseObservers are notified
 * each time an expense is added.
 *
 * Balance calculation:
 *   For each expense, the payer is credited the total; every participant
 *   (including the payer) is debited their split amount.
 *   Net result gives the amount each person owes or is owed.
 */
public class Group {
    private final String groupId;
    private final String name;
    private final List<User> members;
    private final List<Expense> expenses;
    private final List<ExpenseObserver> observers;

    public Group(String groupId, String name) {
        this.groupId   = groupId;
        this.name      = name;
        this.members   = new ArrayList<>();
        this.expenses  = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    // ── Membership ─────────────────────────────────────────────────────────────

    public void addMember(User user) { members.add(user); }

    // ── Observer management ────────────────────────────────────────────────────

    public void addObserver(ExpenseObserver observer) { observers.add(observer); }
    public void removeObserver(ExpenseObserver observer) { observers.remove(observer); }

    // ── Expense management ─────────────────────────────────────────────────────

    /**
     * Adds an expense to the group and notifies all observers.
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
        observers.forEach(o -> o.onExpenseAdded(expense));
    }

    // ── Balance computation ────────────────────────────────────────────────────

    /**
     * Computes the net balance for every member.
     * A positive balance means the user is owed money;
     * a negative balance means the user owes money.
     *
     * @return map from userId to net balance
     */
    public Map<String, Double> getNetBalances() {
        Map<String, Double> balances = new HashMap<>();
        for (User member : members) balances.put(member.getUserId(), 0.0);

        for (Expense expense : expenses) {
            // Payer receives credit for the full amount
            String payer = expense.getPaidBy();
            balances.merge(payer, expense.getTotalAmount(), Double::sum);

            // Each participant is debited their share
            for (Split split : expense.getSplits()) {
                balances.merge(split.getUserId(), -split.getAmount(), Double::sum);
            }
        }
        return balances;
    }

    /**
     * Returns a list of directed Balance objects (from→to with amount > 0).
     * Only non-zero balances are returned.
     */
    public List<Balance> getBalances() {
        Map<String, Double> netBalances = getNetBalances();
        List<Balance> result = new ArrayList<>();

        for (Map.Entry<String, Double> debtor : netBalances.entrySet()) {
            if (debtor.getValue() >= -0.01) continue; // debtor owes money (negative balance)
            for (Map.Entry<String, Double> creditor : netBalances.entrySet()) {
                if (creditor.getValue() <= 0.01) continue;
                double owed = Math.min(-debtor.getValue(), creditor.getValue());
                if (owed > 0.01) {
                    result.add(new Balance(debtor.getKey(), creditor.getKey(),
                            Math.round(owed * 100.0) / 100.0));
                }
            }
        }
        return result;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getGroupId() { return groupId; }
    public String getName() { return name; }
    public List<User> getMembers() { return Collections.unmodifiableList(members); }
    public List<Expense> getExpenses() { return Collections.unmodifiableList(expenses); }

    @Override
    public String toString() { return "Group[" + groupId + "] " + name; }
}
