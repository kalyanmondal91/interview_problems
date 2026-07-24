package org.interview.system_design.lld.library;

/**
 * Represents a book (the abstract concept, not a physical copy).
 * A Book can have multiple BookCopy instances in the library.
 */
public class Book {
    private final String bookId;
    private final String isbn;
    private final String title;
    private final String author;
    private final Genre genre;
    private final int publishYear;

    public Book(String bookId, String isbn, String title, String author, Genre genre, int publishYear) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishYear = publishYear;
    }

    public String getBookId() { return bookId; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Genre getGenre() { return genre; }
    public int getPublishYear() { return publishYear; }

    @Override
    public String toString() {
        return String.format("Book{id='%s', title='%s', author='%s', genre=%s, year=%d}",
                bookId, title, author, genre, publishYear);
    }
}
