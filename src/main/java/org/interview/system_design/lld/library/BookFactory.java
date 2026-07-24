package org.interview.system_design.lld.library;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory class for creating Book and BookCopy instances.
 * Encapsulates ID generation and object initialisation logic.
 */
public class BookFactory {
    private static final AtomicInteger bookCounter = new AtomicInteger(1000);
    private static final AtomicInteger copyCounter = new AtomicInteger(1);

    /** Creates a new Book with an auto-generated bookId. */
    public static Book createBook(String isbn, String title, String author,
                                  Genre genre, int publishYear) {
        String bookId = "BK-" + bookCounter.getAndIncrement();
        return new Book(bookId, isbn, title, author, genre, publishYear);
    }

    /** Creates a new Book with an explicit bookId. */
    public static Book createBook(String bookId, String isbn, String title,
                                  String author, Genre genre, int publishYear) {
        return new Book(bookId, isbn, title, author, genre, publishYear);
    }

    /** Creates a new BookCopy for an existing Book with an auto-generated copyId. */
    public static BookCopy createBookCopy(Book book) {
        String copyId = "CP-" + copyCounter.getAndIncrement();
        return new BookCopy(copyId, book);
    }

    /** Creates a new BookCopy with an explicit copyId. */
    public static BookCopy createBookCopy(String copyId, Book book) {
        return new BookCopy(copyId, book);
    }
}
