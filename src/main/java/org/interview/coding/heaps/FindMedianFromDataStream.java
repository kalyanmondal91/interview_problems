package org.interview.coding.heaps;

import java.util.*;

/**
 * Problem: Find Median from Data Stream
 * Difficulty: Hard
 *
 * Description:
 * Design a data structure that supports adding integers from a data stream and finding the median.
 * MedianFinder() initializes the object. addNum(int num) adds an integer to the data structure.
 * findMedian() returns the median of all elements so far.
 *
 * Example:
 *   Input: addNum(1), addNum(2), findMedian(), addNum(3), findMedian()
 *   Output: 1.5, 2.0
 *
 * Constraints:
 *   - -10^5 <= num <= 10^5
 *   - There will be at least one element before findMedian is called
 *   - At most 5 * 10^4 calls to addNum and findMedian
 *
 * Approach:
 *   Maintain two heaps: a max-heap for the lower half and a min-heap for the upper half.
 *   Always add to maxHeap first, then balance by moving maxHeap top to minHeap.
 *   If minHeap becomes larger, move its top back to maxHeap to keep sizes balanced.
 *   The median is the top of maxHeap (odd total) or average of both tops (even total).
 *   Each addNum is O(log n) and findMedian is O(1).
 *
 * Time Complexity: O(log n) per addNum, O(1) per findMedian
 * Space Complexity: O(n)
 *
 * Test Cases:
 *   1. Input: addNum(1), addNum(2), findMedian() → Output: 1.5
 *   2. Input: addNum(1), addNum(2), addNum(3), findMedian() → Output: 2.0
 *   3. Edge: addNum(1), findMedian() → Output: 1.0
 */
public class FindMedianFromDataStream {

    private PriorityQueue<Integer> maxHeap; // lower half
    private PriorityQueue<Integer> minHeap; // upper half

    public FindMedianFromDataStream() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }

    public static void main(String[] args) {
        // Test 1
        FindMedianFromDataStream mf1 = new FindMedianFromDataStream();
        mf1.addNum(1);
        mf1.addNum(2);
        System.out.println("Test 1: " + mf1.findMedian()); // Expected: 1.5

        // Test 2
        FindMedianFromDataStream mf2 = new FindMedianFromDataStream();
        mf2.addNum(1);
        mf2.addNum(2);
        mf2.addNum(3);
        System.out.println("Test 2: " + mf2.findMedian()); // Expected: 2.0

        // Test 3 (edge case)
        FindMedianFromDataStream mf3 = new FindMedianFromDataStream();
        mf3.addNum(1);
        System.out.println("Test 3: " + mf3.findMedian()); // Expected: 1.0
    }
}
