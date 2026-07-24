package org.interview.system_design.lld.library;

/**
 * Enum for membership types with their respective borrow limits.
 */
public enum MembershipType {
    BASIC(3),
    PREMIUM(10),
    STUDENT(5);

    private final int maxBorrowLimit;

    MembershipType(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }
}
