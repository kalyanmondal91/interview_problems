---
layout: problem
title: "Daily Temperatures"
category: stacks
category_display: "Stacks"
difficulty: Medium
time_complexity: "O(n)"
space_complexity: "O(n)"
leetcode: 739
tags: [stacks]
render_with_liquid: false
---

## Problem

Daily Temperatures Given an array of integers temperatures representing daily temperatures, return an array answer such that answer[i] is the number of days you have to wait after the i-th day to get a warmer temperature. If there is no future day with a warmer temperature, answer[i] = 0.

## Approach

Use a monotonic decreasing stack that stores indices. For each temperature, while the stack is non-empty and the current temperature is greater than the temperature at the stack's top index, pop that index and set its answer to (current index - popped index). Push the current index. Unprocessed stack entries keep answer 0 (default).

## Solution

```java
package org.interview.coding.stacks;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Problem: Daily Temperatures
 * Difficulty: Medium
 *
 * Description:
 * Given an array of integers temperatures representing daily temperatures, return an array
 * answer such that answer[i] is the number of days you have to wait after the i-th day to
 * get a warmer temperature. If there is no future day with a warmer temperature, answer[i] = 0.
 *
 * Example:
 *   Input: temperatures = [73,74,75,71,69,72,76,73]
 *   Output: [1,1,4,2,1,1,0,0]
 *
 * Constraints:
 *   - 1 <= temperatures.length <= 10^5
 *   - 30 <= temperatures[i] <= 100
 *
 * Approach:
 *   Use a monotonic decreasing stack that stores indices. For each temperature, while the
 *   stack is non-empty and the current temperature is greater than the temperature at the
 *   stack's top index, pop that index and set its answer to (current index - popped index).
 *   Push the current index. Unprocessed stack entries keep answer 0 (default).
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: [73,74,75,71,69,72,76,73] → Output: [1,1,4,2,1,1,0,0]
 *   2. Input: [30,40,50,60] → Output: [1,1,1,0]
 *   3. Edge case: [30,60,90] → Output: [1,1,0]
 */
public class DailyTemperatures {

    public int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int idx = stack.pop();
                result[idx] = i - idx;
            }
            stack.push(i);
        }
        return result;
    }

    public static void main(String[] args) {
        DailyTemperatures sol = new DailyTemperatures();
        // Test 1
        System.out.println(Arrays.toString(sol.dailyTemperatures(new int[]{73,74,75,71,69,72,76,73}))); // [1,1,4,2,1,1,0,0]
        // Test 2
        System.out.println(Arrays.toString(sol.dailyTemperatures(new int[]{30,40,50,60})));              // [1,1,1,0]
        // Test 3 (edge case)
        System.out.println(Arrays.toString(sol.dailyTemperatures(new int[]{30,60,90})));                 // [1,1,0]
    }
}
```

## Complexity

- **Time:** O(n)
- **Space:** O(n)
