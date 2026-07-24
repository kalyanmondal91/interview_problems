---
layout: problem
title: "Merge Triplets To Form Target Triplet"
category: greedy
category_display: "Greedy"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(1)"
tags: [greedy]
render_with_liquid: false
---

## Problem

Merge Triplets to Form Target Triplet Given a list of triplets [a,b,c] and a target triplet [x,y,z], you can select any subset of triplets and merge them by taking the element-wise maximum. Determine if it's possible to obtain the target triplet from the given triplets.

## Approach

First filter out triplets where any element exceeds the corresponding target element, since including such a triplet would overshoot the target. Among the remaining valid triplets, take the element-wise maximum. If the resulting triplet equals the target, return true; otherwise false. This works because we only need to achieve exactly the target values.

## Solution

```java
package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Merge Triplets to Form Target Triplet
 * Difficulty: Medium
 *
 * Description:
 * Given a list of triplets [a,b,c] and a target triplet [x,y,z], you can select any subset of
 * triplets and merge them by taking the element-wise maximum. Determine if it's possible to obtain
 * the target triplet from the given triplets.
 *
 * Example:
 *   Input: triplets = [[2,5,3],[1,8,4],[1,7,5]], target = [2,7,5]
 *   Output: true
 *
 * Constraints:
 *   - 1 <= triplets.length <= 10^5
 *   - triplets[i].length == target.length == 3
 *   - 1 <= ai, bi, ci, x, y, z <= 1000
 *
 * Approach:
 *   First filter out triplets where any element exceeds the corresponding target element, since
 *   including such a triplet would overshoot the target. Among the remaining valid triplets, take
 *   the element-wise maximum. If the resulting triplet equals the target, return true; otherwise
 *   false. This works because we only need to achieve exactly the target values.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [[2,5,3],[1,8,4],[1,7,5]], target=[2,7,5] → Output: true
 *   2. Input: [[3,4,5],[4,5,6]], target=[3,2,5] → Output: false
 *   3. Edge: [[1,1,1]], target=[1,1,1] → Output: true
 */
public class MergeTripletsToFormTargetTriplet {

    public boolean mergeTriplets(int[][] triplets, int[] target) {
        int[] result = new int[3];

        for (int[] triplet : triplets) {
            // Skip triplets that exceed target in any position
            if (triplet[0] > target[0] || triplet[1] > target[1] || triplet[2] > target[2]) {
                continue;
            }
            result[0] = Math.max(result[0], triplet[0]);
            result[1] = Math.max(result[1], triplet[1]);
            result[2] = Math.max(result[2], triplet[2]);
        }

        return result[0] == target[0] && result[1] == target[1] && result[2] == target[2];
    }

    public static void main(String[] args) {
        MergeTripletsToFormTargetTriplet solution = new MergeTripletsToFormTargetTriplet();

        // Test 1: achievable target
        System.out.println(solution.mergeTriplets(
            new int[][]{{2,5,3},{1,8,4},{1,7,5}}, new int[]{2,7,5})); // Expected: true

        // Test 2: target element too small to achieve
        System.out.println(solution.mergeTriplets(
            new int[][]{{3,4,5},{4,5,6}}, new int[]{3,2,5})); // Expected: false

        // Test 3: edge case single triplet equals target
        System.out.println(solution.mergeTriplets(
            new int[][]{{1,1,1}}, new int[]{1,1,1})); // Expected: true
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(1)
