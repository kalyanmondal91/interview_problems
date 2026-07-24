package org.interview.coding.math;

import java.util.*;

/**
 * Problem: Excel Sheet Column Number (and Title)
 * Difficulty: Easy
 *
 * Description: Convert an Excel column title (like "A", "AB", "ZY") to its corresponding
 * column number. A→1, B→2, ..., Z→26, AA→27, AB→28, etc. Also implement the reverse:
 * convert a column number to its title.
 *
 * Example:
 *   Input: "A" → 1, "AB" → 28, "ZY" → 701
 *   Input: 1 → "A", 28 → "AB", 701 → "ZY"
 *
 * Approach: titleToNumber: treat the title as a base-26 number where A=1, ..., Z=26.
 * Process left to right: result = result * 26 + (c - 'A' + 1). For columnToTitle (reverse):
 * like converting to base 26 but 1-indexed. While n > 0, compute remainder as (n-1) % 26
 * (0-indexed), prepend char ('A' + remainder), and n = (n-1) / 26.
 *
 * Time Complexity: O(L) for titleToNumber, O(log_26 n) for columnToTitle
 * Space Complexity: O(L) for output string
 *
 * Test Cases:
 *   1. titleToNumber("A") → 1, columnToTitle(1) → "A"
 *   2. titleToNumber("AB") → 28, columnToTitle(28) → "AB"
 *   3. Edge: titleToNumber("ZY") → 701, columnToTitle(701) → "ZY"
 */
public class ExcelSheetColumnNumber {

    public static int titleToNumber(String columnTitle) {
        int result = 0;
        for (char c : columnTitle.toCharArray()) {
            result = result * 26 + (c - 'A' + 1);
        }
        return result;
    }

    public static String columnToTitle(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--; // 1-indexed to 0-indexed
            sb.insert(0, (char) ('A' + n % 26));
            n /= 26;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Test Case 1: Single letter columns
        System.out.println("titleToNumber(A)=" + titleToNumber("A"));       // 1
        System.out.println("columnToTitle(1)=" + columnToTitle(1));         // A

        // Test Case 2: Two-letter column
        System.out.println("titleToNumber(AB)=" + titleToNumber("AB"));     // 28
        System.out.println("columnToTitle(28)=" + columnToTitle(28));       // AB

        // Test Case 3: Edge - larger column
        System.out.println("titleToNumber(ZY)=" + titleToNumber("ZY"));     // 701
        System.out.println("columnToTitle(701)=" + columnToTitle(701));     // ZY

        // Round-trip verification
        for (String col : new String[]{"A", "Z", "AA", "AZ", "BA", "ZZ"}) {
            int num = titleToNumber(col);
            String back = columnToTitle(num);
            System.out.println(col + " → " + num + " → " + back + " (match=" + col.equals(back) + ")");
        }
    }
}
