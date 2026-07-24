package org.interview.system_design.lld.library;

/**
 * Observer interface for library events.
 * Implement this to react to book returns and overdue notifications.
 */
public interface LibraryObserver {
    /** Called when a book copy is returned by a member. */
    void onBookReturned(BookCopy bookCopy);

    /** Called when a loan is detected as overdue. */
    void onBookOverdue(Loan loan);
}
