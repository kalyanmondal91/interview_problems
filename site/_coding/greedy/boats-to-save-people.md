---
layout: problem
title: "Boats To Save People"
category: greedy
category_display: "Greedy"
difficulty: Medium
time_complexity: "O(n log n)"
space_complexity: "O(1)"
tags: [greedy]
render_with_liquid: false
---

## Problem

Boats to Save People Given an array of people's weights and a boat weight limit, each boat can carry at most 2 people and must not exceed the weight limit. Find the minimum number of boats required to carry everyone.

## Approach

Sort the people array by weight. Use two pointers: lo starting at the lightest person and hi starting at the heaviest. If the lightest and heaviest can share a boat (sum <= limit), move both pointers inward. Otherwise, only the heaviest person boards alone and hi decrements. Increment boats count each iteration. This greedy approach is optimal because we always try to pair the heaviest person with someone.

## Solution

```java
package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Boats to Save People
 * Difficulty: Medium
 *
 * Description:
 * Given an array of people's weights and a boat weight limit, each boat can carry at most 2 people
 * and must not exceed the weight limit. Find the minimum number of boats required to carry everyone.
 *
 * Example:
 *   Input: people = [1,2], limit = 3
 *   Output: 1
 *
 * Constraints:
 *   - 1 <= people.length <= 5 * 10^4
 *   - 1 <= people[i] <= limit <= 3 * 10^4
 *
 * Approach:
 *   Sort the people array by weight. Use two pointers: lo starting at the lightest person and hi
 *   starting at the heaviest. If the lightest and heaviest can share a boat (sum <= limit), move
 *   both pointers inward. Otherwise, only the heaviest person boards alone and hi decrements.
 *   Increment boats count each iteration. This greedy approach is optimal because we always try to
 *   pair the heaviest person with someone.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2], limit=3 → Output: 1
 *   2. Input: [3,2,2,1], limit=3 → Output: 3
 *   3. Edge: [3,5,3,4], limit=5 → Output: 4
 */
public class BoatsToSavePeople {

    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int lo = 0, hi = people.length - 1;
        int boats = 0;

        while (lo <= hi) {
            if (people[lo] + people[hi] <= limit) {
                lo++;
            }
            hi--;
            boats++;
        }

        return boats;
    }

    public static void main(String[] args) {
        BoatsToSavePeople solution = new BoatsToSavePeople();

        // Test 1: two people fit in one boat
        System.out.println(solution.numRescueBoats(new int[]{1, 2}, 3)); // Expected: 1

        // Test 2: mixed pairing
        System.out.println(solution.numRescueBoats(new int[]{3, 2, 2, 1}, 3)); // Expected: 3

        // Test 3: each person barely fits alone
        System.out.println(solution.numRescueBoats(new int[]{3, 5, 3, 4}, 5)); // Expected: 4
    }
}
```

## Complexity

- **Time:** O(n log n)
- **Space:** O(1)
