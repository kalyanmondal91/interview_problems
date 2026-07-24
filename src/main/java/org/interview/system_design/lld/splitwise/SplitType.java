package org.interview.system_design.lld.splitwise;

/**
 * Enum representing the method used to split an expense.
 */
public enum SplitType {
    EQUAL,       // Divide total equally among all participants
    EXACT,       // Each participant pays a specified exact amount
    PERCENTAGE,  // Each participant pays a percentage of the total
    SHARES       // Split proportionally based on share counts
}
