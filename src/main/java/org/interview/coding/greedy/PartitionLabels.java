package org.interview.coding.greedy;

import java.util.*;

/**
 * Problem: Partition Labels
 * Difficulty: Medium
 *
 * Description:
 * Given a string s, partition the string into as many parts as possible so that each
 * letter appears in at most one part. Return a list of integers representing the size
 * of each partition.
 *
 * Example:
 *   Input: s="ababcbacadefegdehijhklij"
 *   Output: [9,7,8]
 *
 * Constraints:
 *   - 1 <= s.length <= 500
 *   - s consists of lowercase English letters
 *
 * Approach:
 *   First pass: record the last occurrence index of each character. Second pass: greedily
 *   extend the current partition's end to the max last occurrence of any character seen.
 *   When current index i equals the partition end, we found a complete partition — record
 *   its size and start a new partition. This ensures each letter stays in exactly one part.
 *
 * Time Complexity: O(n)
 * Space Complexity: O(1) — at most 26 distinct chars
 *
 * Test Cases:
 *   1. Input: "ababcbacadefegdehijhklij" → Output: [9,7,8]
 *   2. Input: "eccbbbbdec" → Output: [10]
 *   3. Edge: "abcde" → Output: [1,1,1,1,1] (all unique)
 */
public class PartitionLabels {

    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }
        List<Integer> result = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                result.add(end - start + 1);
                start = i + 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        PartitionLabels sol = new PartitionLabels();

        System.out.println("Test 1 'ababcbacadefegdehijhklij' (expect [9,7,8]): "
                + sol.partitionLabels("ababcbacadefegdehijhklij"));
        System.out.println("Test 2 'eccbbbbdec' (expect [10]): "
                + sol.partitionLabels("eccbbbbdec"));
        System.out.println("Test 3 'abcde' (expect [1,1,1,1,1]): "
                + sol.partitionLabels("abcde"));
    }
}
