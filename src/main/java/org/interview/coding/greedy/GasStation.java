package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Gas Station
 * Difficulty: Medium
 *
 * Description:
 * There are n gas stations along a circular route. You are given gas[i] (gas available)
 * and cost[i] (gas needed to travel to next station). Return the starting station index
 * for a complete circuit, or -1 if it's impossible. The answer is guaranteed unique.
 *
 * Example:
 *   Input: gas=[1,2,3,4,5], cost=[3,4,5,1,2]
 *   Output: 3
 *
 * Constraints:
 *   - n == gas.length == cost.length
 *   - 1 <= n <= 10^5
 *   - 0 <= gas[i], cost[i] <= 10^4
 *
 * Approach:
 *   If total gas >= total cost, a solution exists. Greedy: traverse stations tracking
 *   running tank. When tank drops below 0 at station i, start cannot be anywhere from
 *   current start to i — set start = i+1 and reset tank = 0. The final start value
 *   is the answer, since we know the solution exists (total gas >= total cost).
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: gas=[1,2,3,4,5], cost=[3,4,5,1,2] → Output: 3
 *   2. Input: gas=[2,3,4], cost=[3,4,3] → Output: -1
 *   3. Edge: single station gas=[5], cost=[4] → Output: 0
 */
public class GasStation {

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int totalTank = 0, currentTank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            int diff = gas[i] - cost[i];
            totalTank += diff;
            currentTank += diff;
            if (currentTank < 0) {
                start = i + 1;
                currentTank = 0;
            }
        }
        return totalTank >= 0 ? start : -1;
    }

    public static void main(String[] args) {
        GasStation sol = new GasStation();

        System.out.println("Test 1 (expect 3): "
                + sol.canCompleteCircuit(new int[]{1,2,3,4,5}, new int[]{3,4,5,1,2}));
        System.out.println("Test 2 (expect -1): "
                + sol.canCompleteCircuit(new int[]{2,3,4}, new int[]{3,4,3}));
        System.out.println("Test 3 single station (expect 0): "
                + sol.canCompleteCircuit(new int[]{5}, new int[]{4}));
    }
}
