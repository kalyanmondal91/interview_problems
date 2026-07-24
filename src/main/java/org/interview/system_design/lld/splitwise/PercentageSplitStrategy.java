package org.interview.system_design.lld.splitwise;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits the total based on percentage contributions.
 * {@code values[i]} is the percentage for {@code userIds[i]}.
 * All percentages must sum to 100.
 */
public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<String> userIds, List<Double> values) {
        if (userIds.size() != values.size()) {
            throw new IllegalArgumentException("Number of users must match number of percentages.");
        }
        double totalPercent = values.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalPercent - 100.0) > 0.01) {
            throw new IllegalArgumentException(
                    String.format("Percentages must sum to 100 but got %.2f.", totalPercent));
        }
        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            double amount = Math.round((values.get(i) / 100.0 * totalAmount) * 100.0) / 100.0;
            splits.add(new Split(userIds.get(i), amount));
        }
        return splits;
    }
}
