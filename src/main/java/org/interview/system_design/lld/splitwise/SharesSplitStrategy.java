package org.interview.system_design.lld.splitwise;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits the total proportionally based on share counts.
 * {@code values[i]} is the number of shares for {@code userIds[i]}.
 * Amount = (shares / totalShares) * totalAmount.
 */
public class SharesSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<String> userIds, List<Double> values) {
        if (userIds.size() != values.size()) {
            throw new IllegalArgumentException("Number of users must match number of share values.");
        }
        double totalShares = values.stream().mapToDouble(Double::doubleValue).sum();
        if (totalShares <= 0) {
            throw new IllegalArgumentException("Total shares must be positive.");
        }
        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            double amount = Math.round((values.get(i) / totalShares * totalAmount) * 100.0) / 100.0;
            splits.add(new Split(userIds.get(i), amount));
        }
        return splits;
    }
}
