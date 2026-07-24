package org.interview.system_design.lld.library;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entities.
 * Follows the Repository pattern to abstract data access.
 */
public interface BookRepository {
    void save(Book book);
    Optional<Book> findById(String bookId);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(Genre genre);
    Optional<Book> findByISBN(String isbn);
    List<Book> findAll();
    void delete(String bookId);
}
