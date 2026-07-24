package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Top K Frequent Words
 * Difficulty: Medium
 *
 * Description:
 * Given an array of strings words and an integer k, return the k most frequent strings.
 * Return the answer sorted by frequency from highest to lowest.
 * Sort words with the same frequency by their lexicographical order.
 *
 * Example:
 *   Input: words = ["i","love","leetcode","i","love","coding"], k = 2
 *   Output: ["i","love"]
 *
 * Constraints:
 *   - 1 <= words.length <= 500
 *   - 1 <= words[i].length <= 10
 *   - k is in the range [1, the number of unique words[i]]
 *
 * Approach:
 *   First build a frequency map using a HashMap.
 *   Use a min-heap of size k ordered by (frequency ascending, word descending) so that
 *   the least relevant word is always at the top and gets polled first.
 *   After processing all words, collect heap elements and reverse the list.
 *   This gives O(n log k) time complexity.
 *
 * Time Complexity: O(n log k)
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: ["i","love","leetcode","i","love","coding"], k=2 → Output: ["i","love"]
 *   2. Input: ["the","day","is","sunny","the","the","the","sunny","is","is"], k=4 → Output: ["the","is","sunny","day"]
 *   3. Edge: ["a"], k=1 → Output: ["a"]
 */
public class TopKFrequentWords {

    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) freq.merge(w, 1, Integer::sum);

        // Min-heap: least relevant at top (lowest freq, highest lex order)
        PriorityQueue<String> heap = new PriorityQueue<>(
            (a, b) -> freq.get(a).equals(freq.get(b))
                ? b.compareTo(a)   // reverse lex: higher lex is "worse"
                : freq.get(a) - freq.get(b) // lower freq is "worse"
        );

        for (String w : freq.keySet()) {
            heap.offer(w);
            if (heap.size() > k) heap.poll();
        }

        List<String> result = new ArrayList<>(heap);
        result.sort((a, b) -> freq.get(a).equals(freq.get(b))
            ? a.compareTo(b)
            : freq.get(b) - freq.get(a));
        return result;
    }

    public static void main(String[] args) {
        TopKFrequentWords sol = new TopKFrequentWords();

        // Test 1
        String[] w1 = {"i", "love", "leetcode", "i", "love", "coding"};
        System.out.println("Test 1: " + sol.topKFrequent(w1, 2)); // [i, love]

        // Test 2
        String[] w2 = {"the","day","is","sunny","the","the","the","sunny","is","is"};
        System.out.println("Test 2: " + sol.topKFrequent(w2, 4)); // [the, is, sunny, day]

        // Test 3 (edge case)
        String[] w3 = {"a"};
        System.out.println("Test 3: " + sol.topKFrequent(w3, 1)); // [a]
    }
}
