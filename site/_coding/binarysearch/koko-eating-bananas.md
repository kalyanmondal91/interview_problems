---
layout: problem
title: "Koko Eating Bananas"
category: binarysearch
category_display: "Binary Search"
difficulty: Medium
time_complexity: "O(n log(max(piles)))"
space_complexity: "O(1)"
tags: [binarysearch]
render_with_liquid: false
---

## Problem

Koko Eating Bananas Koko loves to eat bananas. There are n piles of bananas, piles[i] is the number in the ith pile. Koko can decide her bananas-per-hour eating speed k. Each hour she eats up to k bananas from a pile. Given h hours, find the minimum k such that Koko can eat all bananas within h hours.

## Approach

Binary search on speed k in range [1, max(piles)]. For each candidate speed k, compute total hours as sum of ceil(pile/k) for each pile. If total hours <= h, the speed is sufficient; try smaller (hi=mid). Otherwise try larger speed (lo=mid+1). Return lo when lo == hi.

## Solution

```java
package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Koko Eating Bananas
 * Difficulty: Medium
 *
 * Description:
 * Koko loves to eat bananas. There are n piles of bananas, piles[i] is the number in the ith pile.
 * Koko can decide her bananas-per-hour eating speed k. Each hour she eats up to k bananas from a pile.
 * Given h hours, find the minimum k such that Koko can eat all bananas within h hours.
 *
 * Example:
 *   Input: piles = [3,6,7,11], h = 8
 *   Output: 4
 *
 * Constraints:
 *   - 1 <= piles.length <= 10^4
 *   - piles.length <= h <= 10^9
 *   - 1 <= piles[i] <= 10^9
 *
 * Approach:
 *   Binary search on speed k in range [1, max(piles)].
 *   For each candidate speed k, compute total hours as sum of ceil(pile/k) for each pile.
 *   If total hours <= h, the speed is sufficient; try smaller (hi=mid).
 *   Otherwise try larger speed (lo=mid+1). Return lo when lo == hi.
 *
 * Time Complexity: O(n log(max(piles)))
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [3,6,7,11], h=8 → Output: 4
 *   2. Input: [30,11,23,4,20], h=5 → Output: 30
 *   3. Edge: [1000000000], h=2 → Output: 500000000
 */
public class KokoEatingBananas {

    public int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) hi = Math.max(hi, p);

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canFinish(piles, mid, h)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    private boolean canFinish(int[] piles, int speed, int h) {
        long hours = 0;
        for (int p : piles) hours += (p + speed - 1) / speed;
        return hours <= h;
    }

    public static void main(String[] args) {
        KokoEatingBananas sol = new KokoEatingBananas();

        // Test 1
        System.out.println("Test 1: " + sol.minEatingSpeed(new int[]{3, 6, 7, 11}, 8)); // Expected: 4

        // Test 2
        System.out.println("Test 2: " + sol.minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5)); // Expected: 30

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.minEatingSpeed(new int[]{1000000000}, 2)); // Expected: 500000000
    }
}
```

## Complexity

- **Time:** O(n log(max(piles)))
- **Space:** O(1)
