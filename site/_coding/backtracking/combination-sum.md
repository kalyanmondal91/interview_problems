---
layout: problem
title: "Combination Sum"
category: backtracking
category_display: "Backtracking"
difficulty: Medium
time_complexity: "O(2^(target/min))"
space_complexity: "O(target/min)"
leetcode: 39
tags: [backtracking]
render_with_liquid: false
---

## Problem

Combination Sum Given an array of distinct integers candidates and a target integer, return all unique combinations of candidates where the chosen numbers sum to target. The same number may be chosen unlimited times. The order of combinations in the output does not matter.

## Approach

Use backtracking with a start index to avoid duplicates. At each recursive call, iterate over candidates from the start index. Add a candidate, recurse with the same start index (allowing reuse of the same element), then remove it. If remaining becomes 0, add the current combination. If remaining goes negative, prune that branch. Sorting candidates first enables early termination.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Combination Sum
 * Difficulty: Medium
 *
 * Description:
 * Given an array of distinct integers candidates and a target integer, return all unique combinations
 * of candidates where the chosen numbers sum to target. The same number may be chosen unlimited times.
 * The order of combinations in the output does not matter.
 *
 * Example:
 *   Input: candidates = [2,3,6,7], target = 7
 *   Output: [[2,2,3],[7]]
 *
 * Constraints:
 *   - 1 <= candidates.length <= 30
 *   - 2 <= candidates[i] <= 40
 *   - All elements of candidates are distinct.
 *   - 1 <= target <= 40
 *
 * Approach:
 *   Use backtracking with a start index to avoid duplicates. At each recursive call, iterate over
 *   candidates from the start index. Add a candidate, recurse with the same start index (allowing
 *   reuse of the same element), then remove it. If remaining becomes 0, add the current combination.
 *   If remaining goes negative, prune that branch. Sorting candidates first enables early termination.
 *
 * Time Complexity: O(2^(target/min))
 * Space Complexity: O(target/min)
 *
 * Test Cases:
 *   1. Input: candidates=[2,3,6,7], target=7 → Output: [[2,2,3],[7]]
 *   2. Input: candidates=[2,3,5], target=8 → Output: [[2,2,2,2],[2,3,3],[3,5]]
 *   3. Edge: candidates=[2], target=1 → Output: []
 */
public class CombinationSum {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
                           List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) break; // pruning since sorted
            current.add(candidates[i]);
            backtrack(candidates, remaining - candidates[i], i, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        CombinationSum solution = new CombinationSum();

        // Test 1: multiple combinations
        System.out.println(solution.combinationSum(new int[]{2, 3, 6, 7}, 7));
        // Expected: [[2,2,3],[7]]

        // Test 2: three combinations
        System.out.println(solution.combinationSum(new int[]{2, 3, 5}, 8));
        // Expected: [[2,2,2,2],[2,3,3],[3,5]]

        // Test 3: no valid combination
        System.out.println(solution.combinationSum(new int[]{2}, 1));
        // Expected: []
    }
}
```

## Complexity

- **Time:** O(2^(target/min))
- **Space:** O(target/min)
