package org.interview.system_design.lld.splitwise;

import java.util.List;

/**
 * Strategy Pattern interface for expense splitting algorithms.
 * Each implementation encodes a different splitting rule.
 */
public interface SplitStrategy {
    /**
     * Calculates how much each participant owes.
     *
     * @param totalAmount total expense amount
     * @param userIds     participant user IDs
     * @param values      strategy-specific values (percentages, exact amounts,
     *                    share counts, or empty for EQUAL)
     * @return list of Split objects, one per participant
     */
    List<Split> calculateSplits(double totalAmount, List<String> userIds, List<Double> values);
}
