package org.interview.system_design.lld.library;

import java.time.LocalDate;

/**
 * Represents a book loan transaction between a member and a book copy.
 */
public class Loan {
    private final String loanId;
    private final BookCopy bookCopy;
    private final Member member;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public Loan(String loanId, BookCopy bookCopy, Member member, LocalDate borrowDate, LocalDate dueDate) {
        this.loanId = loanId;
        this.bookCopy = bookCopy;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
    }

    public String getLoanId() { return loanId; }
    public BookCopy getBookCopy() { return bookCopy; }
    public Member getMember() { return member; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getFine() { return fine; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setFine(double fine) { this.fine = fine; }

    /** Returns true if the loan is still active (not yet returned). */
    public boolean isActive() { return returnDate == null; }

    /** Returns true if the loan is overdue (no return date and past due). */
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format("Loan{id='%s', book='%s', member='%s', due=%s, returned=%s, fine=%.2f}",
                loanId, bookCopy.getCopyId(), member.getName(), dueDate, returnDate, fine);
    }
}
