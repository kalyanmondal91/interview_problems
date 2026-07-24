package org.interview.system_design.lld.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HashMap-backed in-memory implementation of BookRepository.
 * Provides case-insensitive partial matching for title and author searches.
 */
public class InMemoryBookRepository implements BookRepository {
    private final Map<String, Book> booksById = new HashMap<>();
    private final Map<String, BookCopy> copiesById = new HashMap<>();

    @Override
    public void save(Book book) {
        booksById.put(book.getBookId(), book);
    }

    @Override
    public Optional<Book> findById(String bookId) {
        return Optional.ofNullable(booksById.get(bookId));
    }

    @Override
    public List<Book> findByTitle(String title) {
        String lower = title.toLowerCase();
        return booksById.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String lower = author.toLowerCase();
        return booksById.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        return booksById.values().stream()
                .filter(b -> b.getGenre() == genre)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {
        return booksById.values().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(booksById.values());
    }

    @Override
    public void delete(String bookId) {
        booksById.remove(bookId);
    }

    // --- BookCopy management ---

    public void saveBookCopy(BookCopy copy) {
        copiesById.put(copy.getCopyId(), copy);
    }

    public Optional<BookCopy> findCopyById(String copyId) {
        return Optional.ofNullable(copiesById.get(copyId));
    }

    /** Finds the first available copy of a given book. */
    public Optional<BookCopy> findAvailableCopy(String bookId) {
        return copiesById.values().stream()
                .filter(c -> c.getBook().getBookId().equals(bookId) && c.isAvailable())
                .findFirst();
    }

    public List<BookCopy> findAllCopiesByBook(String bookId) {
        return copiesById.values().stream()
                .filter(c -> c.getBook().getBookId().equals(bookId))
                .collect(Collectors.toList());
    }
}
