package org.interview.system_design.lld.library;

import java.time.LocalDate;

/**
 * Represents a physical copy of a book held by the library.
 * One Book can have many BookCopies.
 */
public class BookCopy {
    private final String copyId;
    private final Book book;
    private BookStatus status;
    private String borrowedBy;   // memberId of current borrower
    private LocalDate dueDate;

    public BookCopy(String copyId, Book book) {
        this.copyId = copyId;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }

    public String getCopyId() { return copyId; }
    public Book getBook() { return book; }
    public BookStatus getStatus() { return status; }
    public String getBorrowedBy() { return borrowedBy; }
    public LocalDate getDueDate() { return dueDate; }

    public void setStatus(BookStatus status) { this.status = status; }
    public void setBorrowedBy(String memberId) { this.borrowedBy = memberId; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isAvailable() { return status == BookStatus.AVAILABLE; }

    @Override
    public String toString() {
        return String.format("BookCopy{id='%s', book='%s', status=%s, borrowedBy='%s'}",
                copyId, book.getTitle(), status, borrowedBy);
    }
}
