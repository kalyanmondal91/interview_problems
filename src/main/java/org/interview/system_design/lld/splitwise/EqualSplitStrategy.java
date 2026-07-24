package org.interview.system_design.lld.splitwise;

import java.util.ArrayList;
import java.util.List;

/**
 * Splits the total equally among all participants.
 * The {@code values} parameter is ignored.
 */
public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<String> userIds, List<Double> values) {
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("At least one participant is required.");
        }
        double share = Math.round((totalAmount / userIds.size()) * 100.0) / 100.0;
        List<Split> splits = new ArrayList<>();
        for (String userId : userIds) {
            splits.add(new Split(userId, share));
        }
        return splits;
    }
}
