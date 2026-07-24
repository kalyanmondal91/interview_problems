package org.interview.system_design.lld.splitwise;

import java.util.ArrayList;
import java.util.List;

/**
 * Assigns an exact amount to each participant.
 * {@code values[i]} is the exact amount owed by {@code userIds[i]}.
 * Validates that the sum of values equals the total amount.
 */
public class ExactSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<String> userIds, List<Double> values) {
        if (userIds.size() != values.size()) {
            throw new IllegalArgumentException("Number of users must match number of exact amounts.");
        }
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new IllegalArgumentException(
                    String.format("Exact amounts (%.2f) do not add up to total (%.2f).", sum, totalAmount));
        }
        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            splits.add(new Split(userIds.get(i), values.get(i)));
        }
        return splits;
    }
}
