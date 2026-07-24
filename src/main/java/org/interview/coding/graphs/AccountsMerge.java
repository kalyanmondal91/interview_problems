package org.interview.coding.graphs;

import java.util.*;

/**
 * Problem: Accounts Merge
 * Difficulty: Medium
 *
 * Description:
 * Given a list of accounts where each account is a list of strings (first element is
 * the name, rest are emails), merge accounts that share at least one common email.
 * Return the merged accounts with sorted emails, each preceded by the account name.
 *
 * Example:
 *   Input: [["John","johnsmith@mail.com","john_newyork@mail.com"],["John","johnsmith@mail.com","john00@mail.com"]]
 *   Output: [["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"]]
 *
 * Constraints:
 *   - 1 <= accounts.length <= 1000
 *   - 2 <= accounts[i].length <= 10
 *   - 1 <= accounts[i][j].length <= 30
 *
 * Approach:
 *   Use Union-Find on emails. For each account, union all its emails under the first
 *   email (as representative). Maintain a map from each email to its owner name.
 *   After processing, group emails by their root representative. Sort each group and
 *   prepend the account name. This correctly handles transitive merges across accounts.
 *
 * Time Complexity: O(N * K * alpha(N*K)) where N=accounts, K=avg emails
 * Space Complexity: O(N * K)
 *
 * Test Cases:
 *   1. Input: two accounts with shared email → Output: merged into one account
 *   2. Input: three accounts, first and third share email via second → Output: all merged
 *   3. Edge: single account with one email → Output: same account unchanged
 */
public class AccountsMerge {

    private Map<String, String> parent = new HashMap<>();
    private Map<String, String> emailToName = new HashMap<>();

    private String find(String x) {
        parent.putIfAbsent(x, x);
        if (!parent.get(x).equals(x)) {
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }

    private void union(String x, String y) {
        String px = find(x), py = find(y);
        if (!px.equals(py)) {
            parent.put(px, py);
        }
    }

    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        // Build union-find structure
        for (List<String> account : accounts) {
            String name = account.get(0);
            String firstEmail = account.get(1);
            emailToName.put(firstEmail, name);
            for (int i = 2; i < account.size(); i++) {
                String email = account.get(i);
                emailToName.put(email, name);
                union(firstEmail, email);
            }
        }

        // Group emails by root
        Map<String, TreeSet<String>> groups = new HashMap<>();
        for (String email : emailToName.keySet()) {
            String root = find(email);
            groups.computeIfAbsent(root, k -> new TreeSet<>()).add(email);
        }

        // Build result
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<String, TreeSet<String>> entry : groups.entrySet()) {
            List<String> merged = new ArrayList<>();
            merged.add(emailToName.get(entry.getKey()));
            merged.addAll(entry.getValue());
            result.add(merged);
        }
        return result;
    }

    public static void main(String[] args) {
        AccountsMerge sol = new AccountsMerge();

        // Test 1: two accounts sharing an email
        List<List<String>> accounts1 = new ArrayList<>();
        accounts1.add(Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"));
        accounts1.add(Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"));
        accounts1.add(Arrays.asList("Mary", "mary@mail.com"));
        System.out.println("Test 1: " + sol.accountsMerge(accounts1));

        // Test 2: chain merge (reset sol state)
        sol = new AccountsMerge();
        List<List<String>> accounts2 = new ArrayList<>();
        accounts2.add(Arrays.asList("Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe1@m.co"));
        accounts2.add(Arrays.asList("Kevin", "Kevin3@m.co", "Kevin5@m.co"));
        accounts2.add(Arrays.asList("Ethan", "Ethan5@m.co", "Ethan4@m.co"));
        System.out.println("Test 2: " + sol.accountsMerge(accounts2));

        // Test 3: single account
        sol = new AccountsMerge();
        List<List<String>> accounts3 = new ArrayList<>();
        accounts3.add(Arrays.asList("Alice", "alice@example.com"));
        System.out.println("Test 3: " + sol.accountsMerge(accounts3));
    }
}
