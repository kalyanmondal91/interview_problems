package org.interview.coding.hashing;

import java.util.*;

/**
 * Problem: Top K Frequent Elements
 * Difficulty: Medium
 *
 * Description:
 * Given an integer array nums and an integer k, return the k most frequent elements.
 * The answer may be returned in any order, and it is guaranteed that the answer is unique.
 *
 * Example:
 *   Input: nums = [1,1,1,2,2,3], k = 2
 *   Output: [1,2]
 *
 * Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - -10^4 <= nums[i] <= 10^4
 *   - k is in the range [1, number of unique elements]
 *
 * Approach:
 *   First build a frequency map using a HashMap. Then use bucket sort: create an array of
 *   lists indexed by frequency (max frequency is n). Place each element in the bucket
 *   corresponding to its frequency. Finally, iterate buckets from high to low frequency,
 *   collecting elements until we have k elements. This achieves O(n) overall.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: nums=[1,1,1,2,2,3], k=2 → Output: [1,2]
 *   2. Input: nums=[1], k=1 → Output: [1]
 *   3. Edge case: nums=[4,1,-1,2,-1,2,3], k=2 → Output: [-1,2]
 */
public class TopKFrequentElements {

    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        // Bucket sort by frequency
        List<Integer>[] buckets = new List[nums.length + 1];
        for (int key : freq.keySet()) {
            int f = freq.get(key);
            if (buckets[f] == null) buckets[f] = new ArrayList<>();
            buckets[f].add(key);
        }

        int[] result = new int[k];
        int idx = 0;
        for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
            if (buckets[i] != null) {
                for (int val : buckets[i]) {
                    if (idx < k) result[idx++] = val;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        TopKFrequentElements sol = new TopKFrequentElements();
        // Test 1
        System.out.println(Arrays.toString(sol.topKFrequent(new int[]{1,1,1,2,2,3}, 2)));    // [1,2]
        // Test 2
        System.out.println(Arrays.toString(sol.topKFrequent(new int[]{1}, 1)));              // [1]
        // Test 3 (edge case: negatives)
        System.out.println(Arrays.toString(sol.topKFrequent(new int[]{4,1,-1,2,-1,2,3}, 2))); // [-1,2]
    }
}
