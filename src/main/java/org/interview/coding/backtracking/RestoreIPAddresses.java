package org.interview.coding.backtracking;

import java.util.*;

/**
 * Problem: Restore IP Addresses
 * Difficulty: Medium
 *
 * Description:
 * Given a string s containing only digits, return all possible valid IPv4 addresses that can be
 * formed by inserting dots into s. A valid IPv4 address consists of exactly four integers separated
 * by dots, where each integer is between 0 and 255 (inclusive) with no leading zeros.
 *
 * Example:
 *   Input: s = "25525511135"
 *   Output: ["255.255.11.135","255.255.111.35"]
 *
 * Constraints:
 *   - 1 <= s.length <= 20
 *   - s consists of digits only.
 *
 * Approach:
 *   Use backtracking with the current position and a list of segments collected so far. At each
 *   step, try taking 1, 2, or 3 digits as the next segment. Validate each segment: no leading
 *   zeros (unless segment is exactly "0"), and value must be 0-255. When exactly 4 segments have
 *   been formed and the entire string is consumed, join with dots and add to results.
 *
 * Time Complexity: O(1) - bounded by at most 3^4 = 81 combinations
 * Space Complexity: O(1)
 *
 * Test Cases:
 *   1. Input: "25525511135" → Output: ["255.255.11.135","255.255.111.35"]
 *   2. Input: "0000" → Output: ["0.0.0.0"]
 *   3. Edge: "1111111111111111" → Output: [] (too long to be a valid IP)
 */
public class RestoreIPAddresses {

    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(String s, int start, List<String> segments, List<String> result) {
        if (segments.size() == 4 && start == s.length()) {
            result.add(String.join(".", segments));
            return;
        }
        if (segments.size() == 4 || start == s.length()) return;

        for (int len = 1; len <= 3; len++) {
            if (start + len > s.length()) break;
            String segment = s.substring(start, start + len);
            // No leading zeros for multi-digit segments
            if (segment.length() > 1 && segment.charAt(0) == '0') break;
            // Value must be <= 255
            if (Integer.parseInt(segment) > 255) break;

            segments.add(segment);
            backtrack(s, start + len, segments, result);
            segments.remove(segments.size() - 1);
        }
    }

    public static void main(String[] args) {
        RestoreIPAddresses solution = new RestoreIPAddresses();

        // Test 1: two valid IPs
        System.out.println(solution.restoreIpAddresses("25525511135"));
        // Expected: [255.255.11.135, 255.255.111.35]

        // Test 2: only zeros
        System.out.println(solution.restoreIpAddresses("0000"));
        // Expected: [0.0.0.0]

        // Test 3: string too long for valid IP
        System.out.println(solution.restoreIpAddresses("1111111111111111"));
        // Expected: []
    }
}
