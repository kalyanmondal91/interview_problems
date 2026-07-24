---
layout: problem
title: "Subsets"
category: backtracking
category_display: "Backtracking"
difficulty: Medium
time_complexity: "O(n * 2^n)"
space_complexity: "O(n)"
tags: [backtracking]
render_with_liquid: false
---

## Problem

Subsets Given an integer array nums of unique elements, return all possible subsets (the power set). The solution set must not contain duplicate subsets, and the order of elements does not matter.

## Approach

Use backtracking starting from index 0. At each call, first add the current subset to results. Then iterate from the current start index to the end of nums. For each element, add it to the current subset, recurse with start = i+1 (to avoid revisiting), then remove the last element. This generates all 2^n subsets by choosing to include or exclude each element.

## Solution

```java
package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Subsets
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums of unique elements, return all possible subsets (the power set).
 * The solution set must not contain duplicate subsets, and the order of elements does not matter.
 *
 * Example:
 *   Input: nums = [1,2,3]
 *   Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 *
 * Constraints:
 *   - 1 <= nums.length <= 10
 *   - -10 <= nums[i] <= 10
 *   - All the numbers of nums are unique.
 *
 * Approach:
 *   Use backtracking starting from index 0. At each call, first add the current subset to results.
 *   Then iterate from the current start index to the end of nums. For each element, add it to the
 *   current subset, recurse with start = i+1 (to avoid revisiting), then remove the last element.
 *   This generates all 2^n subsets by choosing to include or exclude each element.
 *
 * Time Complexity: O(n * 2^n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [1,2,3] → Output: [[],[1],[1,2],[1,2,3],[1,3],[2],[2,3],[3]]
 *   2. Input: [0] → Output: [[],[0]]
 *   3. Edge: [1,2] → Output: [[],[1],[1,2],[2]]
 */
public class Subsets {

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));
        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            backtrack(nums, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        Subsets solution = new Subsets();

        // Test 1: three elements
        System.out.println(solution.subsets(new int[]{1, 2, 3}));
        // Expected: [[], [1], [1,2], [1,2,3], [1,3], [2], [2,3], [3]]

        // Test 2: single element
        System.out.println(solution.subsets(new int[]{0}));
        // Expected: [[], [0]]

        // Test 3: two elements
        System.out.println(solution.subsets(new int[]{1, 2}));
        // Expected: [[], [1], [1,2], [2]]
    }
}
```

## Complexity

- **Time:** O(n * 2^n)
- **Space:** O(n)
