package org.interview.system_design.lld.splitwise;

import java.util.Collections;
import java.util.List;

/**
 * Demo runner for the Splitwise LLD.
 * Patterns: Observer, Strategy (split types)
 *
 * addExpense signature:
 *   (groupId, description, totalAmount, paidBy,
 *    List<userIds>, SplitStrategy, List<Double> values, SplitType)
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        SplitwiseService service = new SplitwiseService();

        User alice   = new User("u1", "Alice",   "alice@example.com");
        User bob     = new User("u2", "Bob",     "bob@example.com");
        User charlie = new User("u3", "Charlie", "charlie@example.com");
        User diana   = new User("u4", "Diana",   "diana@example.com");

        service.addUser(alice);
        service.addUser(bob);
        service.addUser(charlie);
        service.addUser(diana);

        // ── Scenario 1: Equal split — Alice pays $120 dinner ─────────────────
        banner("Scenario 1: Equal Split -- Alice pays $120 dinner");
        Group tripGroup = new Group("g1", "Weekend Trip");
        tripGroup.addMember(alice);
        tripGroup.addMember(bob);
        tripGroup.addMember(charlie);
        service.addGroup(tripGroup);

        tripGroup.addObserver(new ExpenseObserver() {
            @Override
            public void onExpenseAdded(Expense expense) {
                System.out.printf("  [Event] Expense added: '%s' $%.2f by %s%n",
                        expense.getDescription(), expense.getTotalAmount(), expense.getPaidBy());
            }
        });

        service.addExpense("g1", "Dinner", 120.0, "u1",
                List.of("u1", "u2", "u3"),
                new EqualSplitStrategy(),
                Collections.emptyList(),
                SplitType.EQUAL);

        // ── Scenario 2: Bob pays $60 taxi (equal) ────────────────────────────
        banner("Scenario 2: Equal Split -- Bob pays $60 taxi");
        service.addExpense("g1", "Taxi", 60.0, "u2",
                List.of("u1", "u2", "u3"),
                new EqualSplitStrategy(),
                Collections.emptyList(),
                SplitType.EQUAL);

        // ── Scenario 3: Exact split — Charlie pays $90 hotel ─────────────────
        banner("Scenario 3: Exact Split -- Charlie pays $90 hotel (Alice $40, Bob $20, Charlie $30)");
        service.addExpense("g1", "Hotel", 90.0, "u3",
                List.of("u1", "u2", "u3"),
                new ExactSplitStrategy(),
                List.of(40.0, 20.0, 30.0),
                SplitType.EXACT);

        // ── Raw balances ──────────────────────────────────────────────────────
        banner("Raw Balances (before simplification)");
        tripGroup.getBalances().forEach(b ->
                System.out.printf("  %s owes %s: $%.2f%n", b.getFrom(), b.getTo(), b.getAmount()));

        // ── Scenario 4: Simplify debts (min cash flow) ───────────────────────
        banner("Scenario 4: Simplified Debts (min-cash-flow)");
        List<Balance> simplified = service.simplifyDebts();
        if (simplified.isEmpty()) {
            System.out.println("  All debts settled!");
        } else {
            simplified.forEach(b ->
                    System.out.printf("  %s -> %s: $%.2f%n", b.getFrom(), b.getTo(), b.getAmount()));
        }

        // ── Scenario 5: Percentage split ─────────────────────────────────────
        banner("Scenario 5: Percentage Split -- $200 bill (40/30/20/10%)");
        Group officeGroup = new Group("g2", "Office Lunch");
        officeGroup.addMember(alice);
        officeGroup.addMember(bob);
        officeGroup.addMember(charlie);
        officeGroup.addMember(diana);
        service.addGroup(officeGroup);

        service.addExpense("g2", "Team Lunch", 200.0, "u1",
                List.of("u1", "u2", "u3", "u4"),
                new PercentageSplitStrategy(),
                List.of(40.0, 30.0, 20.0, 10.0),
                SplitType.PERCENTAGE);

        officeGroup.getBalances().forEach(b ->
                System.out.printf("  %s owes %s: $%.2f%n", b.getFrom(), b.getTo(), b.getAmount()));

        // ── Scenario 6: Shares split ──────────────────────────────────────────
        banner("Scenario 6: Shares Split -- $100 bill (3:2:1 shares)");
        service.addExpense("g2", "Conference Dinner", 100.0, "u2",
                List.of("u1", "u2", "u3"),
                new SharesSplitStrategy(),
                List.of(3.0, 2.0, 1.0),
                SplitType.SHARES);

        officeGroup.getBalances().forEach(b ->
                System.out.printf("  %s owes %s: $%.2f%n", b.getFrom(), b.getTo(), b.getAmount()));

        System.out.println();
        System.out.println("=== Splitwise Demo Complete ===");
    }
}
