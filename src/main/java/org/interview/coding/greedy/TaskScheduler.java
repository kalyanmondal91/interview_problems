package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Task Scheduler
 * Difficulty: Medium
 *
 * Description:
 * Given a list of CPU tasks and a cooldown n (must wait n intervals between the same
 * task), return the minimum number of intervals (including idle time) to finish all tasks.
 *
 * Example:
 *   Input: tasks=["A","A","A","B","B","B"], n=2
 *   Output: 8 (A→B→idle→A→B→idle→A→B)
 *
 * Constraints:
 *   - 1 <= tasks.length <= 10^4
 *   - tasks[i] is uppercase English letter
 *   - 0 <= n <= 100
 *
 * Approach:
 *   Count task frequencies. The most frequent task determines the frame structure.
 *   With maxFreq occurrences of the most frequent task, we have (maxFreq-1) "frames"
 *   each of size (n+1). slots = (maxFreq-1)*(n+1). Add countMax (tasks with maxFreq)
 *   to fill the last partial frame. Result = max(tasks.length, slots + countMax).
 *   The max handles the case where tasks are so varied that no idle time is needed.
 *
 * Time Complexity: O(n) where n = tasks.length
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: ["A","A","A","B","B","B"], n=2 → Output: 8
 *   2. Input: ["A","A","A","B","B","B"], n=0 → Output: 6 (no cooldown)
 *   3. Edge: ["A","A","A","A","B","C"], n=3 → Output: 10
 */
public class TaskScheduler {

    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;
        int maxFreq = 0;
        for (int f : freq) maxFreq = Math.max(maxFreq, f);
        int countMax = 0;
        for (int f : freq) if (f == maxFreq) countMax++;
        int slots = (maxFreq - 1) * (n + 1) + countMax;
        return Math.max(tasks.length, slots);
    }

    public static void main(String[] args) {
        TaskScheduler sol = new TaskScheduler();

        System.out.println("Test 1 AAABBB n=2 (expect 8): "
                + sol.leastInterval(new char[]{'A','A','A','B','B','B'}, 2));
        System.out.println("Test 2 AAABBB n=0 (expect 6): "
                + sol.leastInterval(new char[]{'A','A','A','B','B','B'}, 0));
        System.out.println("Test 3 AAAABBC n=3 (expect 10): "
                + sol.leastInterval(new char[]{'A','A','A','A','B','B','C'}, 3));
    }
}
