package org.interview.system_design.lld.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a library member who can borrow books.
 */
public class Member {
    private final String memberId;
    private final String name;
    private final String email;
    private final MembershipType membershipType;
    private final List<Loan> activeLoans;

    public Member(String memberId, String name, String email, MembershipType membershipType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
        this.activeLoans = new ArrayList<>();
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public MembershipType getMembershipType() { return membershipType; }
    public List<Loan> getActiveLoans() { return Collections.unmodifiableList(activeLoans); }

    /** Adds a loan to this member's active loans. */
    public void addLoan(Loan loan) {
        activeLoans.add(loan);
    }

    /** Removes a loan from this member's active loans upon return. */
    public void removeLoan(Loan loan) {
        activeLoans.remove(loan);
    }

    /** Checks whether this member can borrow more books. */
    public boolean canBorrow() {
        return activeLoans.size() < membershipType.getMaxBorrowLimit();
    }

    public int getRemainingBorrowLimit() {
        return membershipType.getMaxBorrowLimit() - activeLoans.size();
    }

    @Override
    public String toString() {
        return String.format("Member{id='%s', name='%s', type=%s, activeLoans=%d}",
                memberId, name, membershipType, activeLoans.size());
    }
}
