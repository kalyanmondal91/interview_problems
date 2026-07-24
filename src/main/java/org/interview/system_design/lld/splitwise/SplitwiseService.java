package org.interview.system_design.lld.splitwise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Top-level service managing users and groups.
 *
 * Key feature: simplifyDebts() uses the min-cash-flow algorithm
 * to reduce the total number of transactions needed to settle all debts.
 *
 * Design Patterns:
 *   • Observer  — Group notifies ExpenseObservers on addExpense
 *   • Strategy  — SplitStrategy encapsulates the splitting logic
 */
public class SplitwiseService {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();

    // ── User management ────────────────────────────────────────────────────────

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    // ── Group management ───────────────────────────────────────────────────────

    public void addGroup(Group group) {
        groups.put(group.getGroupId(), group);
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    // ── Expense creation helper ────────────────────────────────────────────────

    /**
     * Convenience method: creates an Expense using a strategy and adds it to a group.
     *
     * @param groupId     target group
     * @param description expense description
     * @param totalAmount total bill amount
     * @param paidBy      userId of the person who paid
     * @param userIds     participant user IDs
     * @param strategy    splitting strategy to apply
     * @param values      strategy values (may be empty for EQUAL)
     * @param splitType   enum label for record-keeping
     * @return the created Expense
     */
    public Expense addExpense(String groupId, String description, double totalAmount,
                              String paidBy, List<String> userIds, SplitStrategy strategy,
                              List<Double> values, SplitType splitType) {
        Group group = groups.get(groupId);
        if (group == null) throw new IllegalArgumentException("Group not found: " + groupId);

        List<Split> splits = strategy.calculateSplits(totalAmount, userIds, values);
        Expense expense = new Expense(description, totalAmount, paidBy, splits, splitType);
        group.addExpense(expense);
        return expense;
    }

    // ── Debt simplification (min-cash-flow algorithm) ──────────────────────────

    /**
     * Returns a minimal set of transactions that settle all debts across ALL groups.
     *
     * Algorithm:
     *   1. Aggregate net balance per user across all groups.
     *   2. Separate into creditors (positive) and debtors (negative).
     *   3. Greedily pair the largest creditor with the largest debtor,
     *      producing at most (n-1) transactions for n people.
     *
     * @return list of simplified Balance transactions
     */
    public List<Balance> simplifyDebts() {
        // Aggregate net balances across all groups
        Map<String, Double> netBalances = new HashMap<>();
        for (User user : users.values()) netBalances.put(user.getUserId(), 0.0);

        for (Group group : groups.values()) {
            Map<String, Double> groupBalances = group.getNetBalances();
            groupBalances.forEach((uid, amt) ->
                    netBalances.merge(uid, amt, Double::sum));
        }

        // Max-heap for creditors (most owed first)
        PriorityQueue<double[]> creditors = new PriorityQueue<>(
                (a, b) -> Double.compare(b[0], a[0]));
        // Max-heap for debtors (most owing first, stored as positive)
        PriorityQueue<double[]> debtors = new PriorityQueue<>(
                (a, b) -> Double.compare(b[0], a[0]));

        // Map index to userId for reconstruction
        List<String> userIdList = new ArrayList<>(netBalances.keySet());
        for (int i = 0; i < userIdList.size(); i++) {
            double balance = netBalances.get(userIdList.get(i));
            if (balance > 0.01) creditors.offer(new double[]{balance, i});
            else if (balance < -0.01) debtors.offer(new double[]{-balance, i});
        }

        List<Balance> transactions = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            double[] creditor = creditors.poll();
            double[] debtor   = debtors.poll();

            double settled = Math.min(creditor[0], debtor[0]);
            settled = Math.round(settled * 100.0) / 100.0;

            String creditorId = userIdList.get((int) creditor[1]);
            String debtorId   = userIdList.get((int) debtor[1]);
            transactions.add(new Balance(debtorId, creditorId, settled));

            double remaining = creditor[0] - settled;
            double leftover  = debtor[0]   - settled;

            if (remaining > 0.01) creditors.offer(new double[]{remaining, creditor[1]});
            if (leftover  > 0.01) debtors.offer(new double[]{leftover,  debtor[1]});
        }

        return Collections.unmodifiableList(transactions);
    }

    // ── Demo ──────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SplitwiseService service = new SplitwiseService();

        // Create users
        User alice = new User("u1", "Alice", "alice@example.com");
        User bob   = new User("u2", "Bob",   "bob@example.com");
        User carol = new User("u3", "Carol", "carol@example.com");
        service.addUser(alice);
        service.addUser(bob);
        service.addUser(carol);

        // Create group
        Group trip = new Group("g1", "Weekend Trip");
        trip.addMember(alice);
        trip.addMember(bob);
        trip.addMember(carol);

        // Attach a logging observer
        trip.addObserver(e -> System.out.println("[Observer] New expense: " + e));

        service.addGroup(trip);

        List<String> all = List.of("u1", "u2", "u3");

        // Alice pays $90 for dinner — split equally
        service.addExpense("g1", "Dinner", 90.0, "u1", all,
                new EqualSplitStrategy(), Collections.emptyList(), SplitType.EQUAL);

        // Bob pays $120 for hotel — split by shares (1:2:3)
        service.addExpense("g1", "Hotel", 120.0, "u2", all,
                new SharesSplitStrategy(), List.of(1.0, 2.0, 3.0), SplitType.SHARES);

        // Print group balances
        System.out.println("\n--- Group Balances ---");
        trip.getBalances().forEach(System.out::println);

        // Simplify all debts
        System.out.println("\n--- Simplified Debts ---");
        service.simplifyDebts().forEach(System.out::println);
    }
}
