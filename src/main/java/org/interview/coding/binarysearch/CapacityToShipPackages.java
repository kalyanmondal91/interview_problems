package org.interview.coding.binarysearch;

import java.util.*;

/**
 * Problem: Capacity To Ship Packages Within D Days
 * Difficulty: Medium
 *
 * Description:
 * A conveyor belt has packages that must be shipped within d days. The ith package has weight weights[i].
 * Each day we load packages in order with total weight not exceeding the ship's capacity.
 * Return the minimum weight capacity of the ship that will result in shipping all packages within d days.
 *
 * Example:
 *   Input: weights = [1,2,3,4,5,6,7,8,9,10], d = 5
 *   Output: 15
 *
 * Constraints:
 *   - 1 <= d <= weights.length <= 5 * 10^4
 *   - 1 <= weights[i] <= 500
 *
 * Approach:
 *   Binary search on capacity in range [max(weights), sum(weights)].
 *   For each capacity, greedily pack packages day by day checking if all fit within d days.
 *   If feasible, try smaller capacity (hi=mid); otherwise increase (lo=mid+1).
 *   The minimum feasible capacity is returned.
 *
 * Time Complexity: O(n log(sum(weights)))
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: [1,2,3,4,5,6,7,8,9,10], d=5 → Output: 15
 *   2. Input: [3,2,2,4,1,4], d=3 → Output: 6
 *   3. Edge: [1,2,3,1,1], d=4 → Output: 3
 */
public class CapacityToShipPackages {

    public int shipWithinDays(int[] weights, int days) {
        int lo = 0, hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);
            hi += w;
        }

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canShip(weights, days, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    private boolean canShip(int[] weights, int days, int cap) {
        int daysNeeded = 1, current = 0;
        for (int w : weights) {
            if (current + w > cap) {
                daysNeeded++;
                current = 0;
            }
            current += w;
        }
        return daysNeeded <= days;
    }

    public static void main(String[] args) {
        CapacityToShipPackages sol = new CapacityToShipPackages();

        // Test 1
        System.out.println("Test 1: " + sol.shipWithinDays(new int[]{1,2,3,4,5,6,7,8,9,10}, 5)); // Expected: 15

        // Test 2
        System.out.println("Test 2: " + sol.shipWithinDays(new int[]{3,2,2,4,1,4}, 3)); // Expected: 6

        // Test 3 (edge case)
        System.out.println("Test 3: " + sol.shipWithinDays(new int[]{1,2,3,1,1}, 4)); // Expected: 3
    }
}
