---
layout: lld
render_with_liquid: false
title: "Library Management System"
system: library
description: "LLD of Library Management System"
files:
  - "Book.java"
  - "BookCopy.java"
  - "BookFactory.java"
  - "BookRepository.java"
  - "BookStatus.java"
  - "FineCalculator.java"
  - "Genre.java"
  - "InMemoryBookRepository.java"
  - "InMemoryMemberRepository.java"
  - "LibraryObserver.java"
  - "LibraryService.java"
  - "Loan.java"
  - "Main.java"
  - "Member.java"
  - "MemberRepository.java"
  - "MembershipType.java"
---

## Library Management System

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="Book.java">Book.java</button>
<button class="tab-btn" data-tab="BookCopy.java">BookCopy.java</button>
<button class="tab-btn" data-tab="BookFactory.java">BookFactory.java</button>
<button class="tab-btn" data-tab="BookRepository.java">BookRepository.java</button>
<button class="tab-btn" data-tab="BookStatus.java">BookStatus.java</button>
<button class="tab-btn" data-tab="FineCalculator.java">FineCalculator.java</button>
<button class="tab-btn" data-tab="Genre.java">Genre.java</button>
<button class="tab-btn" data-tab="InMemoryBookRepository.java">InMemoryBookRepository.java</button>
<button class="tab-btn" data-tab="InMemoryMemberRepository.java">InMemoryMemberRepository.java</button>
<button class="tab-btn" data-tab="LibraryObserver.java">LibraryObserver.java</button>
<button class="tab-btn" data-tab="LibraryService.java">LibraryService.java</button>
<button class="tab-btn" data-tab="Loan.java">Loan.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="Member.java">Member.java</button>
<button class="tab-btn" data-tab="MemberRepository.java">MemberRepository.java</button>
<button class="tab-btn" data-tab="MembershipType.java">MembershipType.java</button>
</div>
<div class="tab-content active" id="Book-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
        return String.format(&quot;Book{id=&#x27;%s&#x27;, title=&#x27;%s&#x27;, author=&#x27;%s&#x27;, genre=%s, year=%d}&quot;,
                bookId, title, author, genre, publishYear);
    }
}</code></pre>
</div>
<div class="tab-content" id="BookCopy-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
        return String.format(&quot;BookCopy{id=&#x27;%s&#x27;, book=&#x27;%s&#x27;, status=%s, borrowedBy=&#x27;%s&#x27;}&quot;,
                copyId, book.getTitle(), status, borrowedBy);
    }
}</code></pre>
</div>
<div class="tab-content" id="BookFactory-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
        String bookId = &quot;BK-&quot; + bookCounter.getAndIncrement();
        return new Book(bookId, isbn, title, author, genre, publishYear);
    }

    /** Creates a new Book with an explicit bookId. */
    public static Book createBook(String bookId, String isbn, String title,
                                  String author, Genre genre, int publishYear) {
        return new Book(bookId, isbn, title, author, genre, publishYear);
    }

    /** Creates a new BookCopy for an existing Book with an auto-generated copyId. */
    public static BookCopy createBookCopy(Book book) {
        String copyId = &quot;CP-&quot; + copyCounter.getAndIncrement();
        return new BookCopy(copyId, book);
    }

    /** Creates a new BookCopy with an explicit copyId. */
    public static BookCopy createBookCopy(String copyId, Book book) {
        return new BookCopy(copyId, book);
    }
}</code></pre>
</div>
<div class="tab-content" id="BookRepository-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entities.
 * Follows the Repository pattern to abstract data access.
 */
public interface BookRepository {
    void save(Book book);
    Optional&lt;Book&gt; findById(String bookId);
    List&lt;Book&gt; findByTitle(String title);
    List&lt;Book&gt; findByAuthor(String author);
    List&lt;Book&gt; findByGenre(Genre genre);
    Optional&lt;Book&gt; findByISBN(String isbn);
    List&lt;Book&gt; findAll();
    void delete(String bookId);
}</code></pre>
</div>
<div class="tab-content" id="BookStatus-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

/**
 * Enum representing the status of a book copy.
 */
public enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    LOST
}</code></pre>
</div>
<div class="tab-content" id="FineCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates the overdue fine for a loan.
 * Default rate: $1.00 per day overdue.
 */
public class FineCalculator {
    private final double finePerDay;

    public FineCalculator() {
        this(1.0);
    }

    public FineCalculator(double finePerDay) {
        this.finePerDay = finePerDay;
    }

    /**
     * Calculates the fine for a loan as of today (or return date if already returned).
     *
     * @param loan the loan to evaluate
     * @return fine amount in currency units (0.0 if no overdue days)
     */
    public double calculateFine(Loan loan) {
        LocalDate effectiveReturnDate = loan.getReturnDate() != null
                ? loan.getReturnDate()
                : LocalDate.now();

        long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), effectiveReturnDate);
        if (overdueDays &lt;= 0) return 0.0;
        return overdueDays * finePerDay;
    }
}</code></pre>
</div>
<div class="tab-content" id="Genre-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

/**
 * Enum representing book genres.
 */
public enum Genre {
    FICTION,
    NON_FICTION,
    SCIENCE,
    HISTORY,
    BIOGRAPHY,
    TECHNOLOGY
}</code></pre>
</div>
<div class="tab-content" id="InMemoryBookRepository-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
    private final Map&lt;String, Book&gt; booksById = new HashMap&lt;&gt;();
    private final Map&lt;String, BookCopy&gt; copiesById = new HashMap&lt;&gt;();

    @Override
    public void save(Book book) {
        booksById.put(book.getBookId(), book);
    }

    @Override
    public Optional&lt;Book&gt; findById(String bookId) {
        return Optional.ofNullable(booksById.get(bookId));
    }

    @Override
    public List&lt;Book&gt; findByTitle(String title) {
        String lower = title.toLowerCase();
        return booksById.values().stream()
                .filter(b -&gt; b.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List&lt;Book&gt; findByAuthor(String author) {
        String lower = author.toLowerCase();
        return booksById.values().stream()
                .filter(b -&gt; b.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List&lt;Book&gt; findByGenre(Genre genre) {
        return booksById.values().stream()
                .filter(b -&gt; b.getGenre() == genre)
                .collect(Collectors.toList());
    }

    @Override
    public Optional&lt;Book&gt; findByISBN(String isbn) {
        return booksById.values().stream()
                .filter(b -&gt; b.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public List&lt;Book&gt; findAll() {
        return new ArrayList&lt;&gt;(booksById.values());
    }

    @Override
    public void delete(String bookId) {
        booksById.remove(bookId);
    }

    // --- BookCopy management ---

    public void saveBookCopy(BookCopy copy) {
        copiesById.put(copy.getCopyId(), copy);
    }

    public Optional&lt;BookCopy&gt; findCopyById(String copyId) {
        return Optional.ofNullable(copiesById.get(copyId));
    }

    /** Finds the first available copy of a given book. */
    public Optional&lt;BookCopy&gt; findAvailableCopy(String bookId) {
        return copiesById.values().stream()
                .filter(c -&gt; c.getBook().getBookId().equals(bookId) &amp;&amp; c.isAvailable())
                .findFirst();
    }

    public List&lt;BookCopy&gt; findAllCopiesByBook(String bookId) {
        return copiesById.values().stream()
                .filter(c -&gt; c.getBook().getBookId().equals(bookId))
                .collect(Collectors.toList());
    }
}</code></pre>
</div>
<div class="tab-content" id="InMemoryMemberRepository-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HashMap-backed in-memory implementation of MemberRepository.
 */
public class InMemoryMemberRepository implements MemberRepository {
    private final Map&lt;String, Member&gt; membersById = new HashMap&lt;&gt;();

    @Override
    public void save(Member member) {
        membersById.put(member.getMemberId(), member);
    }

    @Override
    public Optional&lt;Member&gt; findById(String memberId) {
        return Optional.ofNullable(membersById.get(memberId));
    }

    @Override
    public List&lt;Member&gt; findByName(String name) {
        String lower = name.toLowerCase();
        return membersById.values().stream()
                .filter(m -&gt; m.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List&lt;Member&gt; findAll() {
        return new ArrayList&lt;&gt;(membersById.values());
    }

    @Override
    public void delete(String memberId) {
        membersById.remove(memberId);
    }
}</code></pre>
</div>
<div class="tab-content" id="LibraryObserver-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

/**
 * Observer interface for library events.
 * Implement this to react to book returns and overdue notifications.
 */
public interface LibraryObserver {
    /** Called when a book copy is returned by a member. */
    void onBookReturned(BookCopy bookCopy);

    /** Called when a loan is detected as overdue. */
    void onBookOverdue(Loan loan);
}</code></pre>
</div>
<div class="tab-content" id="LibraryService-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Core library service orchestrating borrowing, returning, reserving, and searching.
 *
 * Design patterns:
 *  - Repository: BookRepository / MemberRepository abstract data access
 *  - Observer: LibraryObserver notified on returns and overdue events
 *  - Factory: BookFactory used for creating book/copy instances
 */
public class LibraryService {
    private final InMemoryBookRepository bookRepository;
    private final InMemoryMemberRepository memberRepository;
    private final FineCalculator fineCalculator;
    private final List&lt;LibraryObserver&gt; observers;
    // All active loans (indexed by loanId)
    private final java.util.Map&lt;String, Loan&gt; loansById;

    public LibraryService() {
        this.bookRepository = new InMemoryBookRepository();
        this.memberRepository = new InMemoryMemberRepository();
        this.fineCalculator = new FineCalculator();
        this.observers = new ArrayList&lt;&gt;();
        this.loansById = new java.util.HashMap&lt;&gt;();
    }

    // ---- Observer management ----

    public void addObserver(LibraryObserver observer) { observers.add(observer); }
    public void removeObserver(LibraryObserver observer) { observers.remove(observer); }

    private void notifyBookReturned(BookCopy copy) {
        observers.forEach(o -&gt; o.onBookReturned(copy));
    }
    private void notifyBookOverdue(Loan loan) {
        observers.forEach(o -&gt; o.onBookOverdue(loan));
    }

    // ---- Book / Copy registration ----

    public Book addBook(String isbn, String title, String author, Genre genre, int year) {
        Book book = BookFactory.createBook(isbn, title, author, genre, year);
        bookRepository.save(book);
        return book;
    }

    public BookCopy addBookCopy(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -&gt; new IllegalArgumentException(&quot;Book not found: &quot; + bookId));
        BookCopy copy = BookFactory.createBookCopy(book);
        bookRepository.saveBookCopy(copy);
        return copy;
    }

    public void registerMember(Member member) {
        memberRepository.save(member);
    }

    // ---- Borrowing ----

    /**
     * Borrows the first available copy of a book for a member.
     *
     * @param memberId  the borrowing member&#x27;s ID
     * @param bookId    the book to borrow
     * @param daysLoan  loan period in days
     * @return the created Loan
     */
    public Loan borrowBook(String memberId, String bookId, int daysLoan) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -&gt; new IllegalArgumentException(&quot;Member not found: &quot; + memberId));

        if (!member.canBorrow()) {
            throw new IllegalStateException(&quot;Member has reached borrow limit: &quot; + member.getMembershipType());
        }

        BookCopy copy = bookRepository.findAvailableCopy(bookId)
                .orElseThrow(() -&gt; new IllegalStateException(&quot;No available copy for book: &quot; + bookId));

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(daysLoan);
        String loanId = &quot;LN-&quot; + UUID.randomUUID().toString().substring(0, 8);

        Loan loan = new Loan(loanId, copy, member, borrowDate, dueDate);

        copy.setStatus(BookStatus.BORROWED);
        copy.setBorrowedBy(memberId);
        copy.setDueDate(dueDate);
        member.addLoan(loan);
        loansById.put(loanId, loan);

        System.out.printf(&quot;Borrowed: &#x27;%s&#x27; by %s, due %s%n&quot;,
                copy.getBook().getTitle(), member.getName(), dueDate);
        return loan;
    }

    // ---- Returning ----

    /**
     * Returns a borrowed book copy.
     *
     * @param loanId the loan ID
     * @return the fine charged (0.0 if on time)
     */
    public double returnBook(String loanId) {
        Loan loan = Optional.ofNullable(loansById.get(loanId))
                .orElseThrow(() -&gt; new IllegalArgumentException(&quot;Loan not found: &quot; + loanId));

        if (!loan.isActive()) {
            throw new IllegalStateException(&quot;Loan already returned: &quot; + loanId);
        }

        loan.setReturnDate(LocalDate.now());
        double fine = fineCalculator.calculateFine(loan);
        loan.setFine(fine);

        BookCopy copy = loan.getBookCopy();
        copy.setStatus(BookStatus.AVAILABLE);
        copy.setBorrowedBy(null);
        copy.setDueDate(null);
        loan.getMember().removeLoan(loan);

        notifyBookReturned(copy);
        if (fine &gt; 0) {
            System.out.printf(&quot;Returned: &#x27;%s&#x27; — Fine: $%.2f%n&quot;, copy.getBook().getTitle(), fine);
        } else {
            System.out.printf(&quot;Returned: &#x27;%s&#x27; on time.%n&quot;, copy.getBook().getTitle());
        }
        return fine;
    }

    // ---- Reservation ----

    /** Reserves a book copy (marks as RESERVED). */
    public void reserveBook(String memberId, String copyId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -&gt; new IllegalArgumentException(&quot;Member not found: &quot; + memberId));
        BookCopy copy = bookRepository.findCopyById(copyId)
                .orElseThrow(() -&gt; new IllegalArgumentException(&quot;Copy not found: &quot; + copyId));
        if (!copy.isAvailable()) {
            throw new IllegalStateException(&quot;Copy not available for reservation: &quot; + copyId);
        }
        copy.setStatus(BookStatus.RESERVED);
        copy.setBorrowedBy(memberId);
        System.out.printf(&quot;Reserved copy &#x27;%s&#x27; for member &#x27;%s&#x27;%n&quot;, copyId, memberId);
    }

    // ---- Search ----

    public List&lt;Book&gt; searchByTitle(String title) { return bookRepository.findByTitle(title); }
    public List&lt;Book&gt; searchByAuthor(String author) { return bookRepository.findByAuthor(author); }
    public List&lt;Book&gt; searchByGenre(Genre genre) { return bookRepository.findByGenre(genre); }
    public Optional&lt;Book&gt; searchByISBN(String isbn) { return bookRepository.findByISBN(isbn); }

    // ---- Overdue check ----

    /** Scans all active loans and notifies observers of any overdue ones. */
    public void checkOverdueLoans() {
        loansById.values().stream()
                .filter(Loan::isOverdue)
                .forEach(this::notifyBookOverdue);
    }

    // ---- Accessors ----

    public InMemoryBookRepository getBookRepository() { return bookRepository; }
    public InMemoryMemberRepository getMemberRepository() { return memberRepository; }

    /**
     * Demo main method.
     */
    public static void main(String[] args) {
        LibraryService service = new LibraryService();

        // Observer: print to console
        service.addObserver(new LibraryObserver() {
            @Override
            public void onBookReturned(BookCopy copy) {
                System.out.println(&quot;[Observer] Book returned: &quot; + copy.getBook().getTitle());
            }
            @Override
            public void onBookOverdue(Loan loan) {
                System.out.println(&quot;[Observer] OVERDUE: &quot; + loan);
            }
        });

        Book book = service.addBook(&quot;978-0-00-1&quot;, &quot;Clean Code&quot;, &quot;Robert Martin&quot;,
                Genre.TECHNOLOGY, 2008);
        service.addBookCopy(book.getBookId());
        service.addBookCopy(book.getBookId());

        Member alice = new Member(&quot;M1&quot;, &quot;Alice&quot;, &quot;alice@example.com&quot;, MembershipType.PREMIUM);
        service.registerMember(alice);

        Loan loan = service.borrowBook(&quot;M1&quot;, book.getBookId(), 14);
        service.returnBook(loan.getLoanId());

        List&lt;Book&gt; results = service.searchByTitle(&quot;clean&quot;);
        System.out.println(&quot;Search results: &quot; + results);
    }
}</code></pre>
</div>
<div class="tab-content" id="Loan-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
        return returnDate == null &amp;&amp; LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return String.format(&quot;Loan{id=&#x27;%s&#x27;, book=&#x27;%s&#x27;, member=&#x27;%s&#x27;, due=%s, returned=%s, fine=%.2f}&quot;,
                loanId, bookCopy.getCopyId(), member.getName(), dueDate, returnDate, fine);
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

import java.util.List;

/**
 * Demo runner for the Library Management LLD.
 * Patterns: Repository, Observer, Factory
 */
public class Main {

    public static void main(String[] args) {

        LibraryService library = new LibraryService();

        // Observer: print borrow/return events
        library.addObserver(new LibraryObserver() {
            @Override public void onBookReturned(BookCopy copy) {
                System.out.println(&quot;  [Event] Book returned: &quot; + copy.getBook().getTitle());
            }
            @Override public void onBookOverdue(Loan loan) {
                System.out.println(&quot;  [Event] OVERDUE: &quot; + loan.getBookCopy().getBook().getTitle()
                        + &quot; by &quot; + loan.getMember().getName());
            }
        });

        // ── Setup: Add books and members ──────────────────────────────────────
        System.out.println(&quot;\n=== Setup: Add books and members ===&quot;);
        Book java    = library.addBook(&quot;978-0-13-468599-1&quot;, &quot;Effective Java&quot;,     &quot;Joshua Bloch&quot;,  Genre.TECHNOLOGY, 2018);
        Book design  = library.addBook(&quot;978-0-20-163361-5&quot;, &quot;Design Patterns&quot;,    &quot;GoF&quot;,           Genre.TECHNOLOGY, 1994);
        Book dune    = library.addBook(&quot;978-0-44-100590-3&quot;, &quot;Dune&quot;,               &quot;Frank Herbert&quot;, Genre.FICTION,    1965);

        BookCopy javaCopy1  = library.addBookCopy(java.getBookId());
        BookCopy javaCopy2  = library.addBookCopy(java.getBookId()); // 2 copies
        BookCopy designCopy = library.addBookCopy(design.getBookId());
        BookCopy duneCopy   = library.addBookCopy(dune.getBookId());

        Member alice = new Member(&quot;m1&quot;, &quot;Alice&quot;, &quot;alice@lib.com&quot;, MembershipType.PREMIUM);
        Member bob   = new Member(&quot;m2&quot;, &quot;Bob&quot;,   &quot;bob@lib.com&quot;,   MembershipType.BASIC);
        library.registerMember(alice);
        library.registerMember(bob);
        System.out.println(&quot;  Books: Effective Java (×2), Design Patterns, Dune&quot;);
        System.out.println(&quot;  Members: Alice (PREMIUM, 10-book limit), Bob (BASIC, 3-book limit)&quot;);

        // ── Scenario 1: Borrow books ──────────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 1: Members borrow books ===&quot;);
        Loan l1 = library.borrowBook(&quot;m1&quot;, java.getBookId(), 14);
        Loan l2 = library.borrowBook(&quot;m1&quot;, design.getBookId(), 14);
        Loan l3 = library.borrowBook(&quot;m2&quot;, dune.getBookId(), 7);
        System.out.printf(&quot;  Alice borrowed: %s (loan: %s)%n&quot;, l1.getBookCopy().getBook().getTitle(), l1.getLoanId());
        System.out.printf(&quot;  Alice borrowed: %s (loan: %s)%n&quot;, l2.getBookCopy().getBook().getTitle(), l2.getLoanId());
        System.out.printf(&quot;  Bob   borrowed: %s (loan: %s)%n&quot;, l3.getBookCopy().getBook().getTitle(), l3.getLoanId());
        System.out.println(&quot;  Alice active loans: &quot; + alice.getActiveLoans().size());

        // ── Scenario 2: Return on time (no fine) ─────────────────────────────
        System.out.println(&quot;\n=== Scenario 2: Return on time -- no fine ===&quot;);
        double fine1 = library.returnBook(l1.getLoanId());
        System.out.printf(&quot;  Returned &#x27;%s&#x27; -- Fine: $%.2f%n&quot;, java.getTitle(), fine1);

        // ── Scenario 3: Search ────────────────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 3: Search books ===&quot;);
        List&lt;Book&gt; techBooks = library.searchByGenre(Genre.TECHNOLOGY);
        System.out.println(&quot;  Technology books:&quot;);
        techBooks.forEach(b -&gt; System.out.println(&quot;    - &quot; + b.getTitle() + &quot; by &quot; + b.getAuthor()));

        List&lt;Book&gt; byBloch = library.searchByAuthor(&quot;Joshua Bloch&quot;);
        System.out.println(&quot;  Books by Joshua Bloch: &quot; + byBloch.size());

        library.searchByISBN(&quot;978-0-44-100590-3&quot;)
                .ifPresent(b -&gt; System.out.println(&quot;  Found by ISBN: &quot; + b.getTitle()));

        // ── Scenario 4: BASIC member hits 3-book borrow limit ────────────────
        System.out.println(&quot;\n=== Scenario 4: BASIC member borrowing limit (max 3) ===&quot;);
        // Bob already has Dune; borrow 2 more (javaCopy2 is available)
        Loan l4 = library.borrowBook(&quot;m2&quot;, java.getBookId(), 7);
        System.out.println(&quot;  Bob borrowed: &quot; + (l4 != null ? l4.getBookCopy().getBook().getTitle() : &quot;FAILED&quot;));
        // Return design first so another copy is available
        library.returnBook(l2.getLoanId());
        Loan l5 = library.borrowBook(&quot;m2&quot;, design.getBookId(), 7);
        System.out.println(&quot;  Bob borrowed: &quot; + (l5 != null ? l5.getBookCopy().getBook().getTitle() : &quot;FAILED&quot;));
        // Bob now has 3 loans -- next should fail
        Loan l6 = library.borrowBook(&quot;m2&quot;, java.getBookId(), 7); // javaCopy2 still out
        System.out.println(&quot;  Bob borrow attempt 4: &quot; + (l6 != null ? &quot;SUCCESS&quot; : &quot;REJECTED (limit reached)&quot;));

        // ── Scenario 5: Reserve a book ────────────────────────────────────────
        System.out.println(&quot;\n=== Scenario 5: Reserve a book ===&quot;);
        try {
            library.reserveBook(&quot;m1&quot;, duneCopy.getCopyId());
            System.out.println(&quot;  Alice reserved Dune copy -- status: &quot; + duneCopy.getStatus());
        } catch (Exception e) {
            System.out.println(&quot;  Reserve failed: &quot; + e.getMessage());
        }

        System.out.println(&quot;\n=== Library Management Demo Complete ===&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="Member-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
    private final List&lt;Loan&gt; activeLoans;

    public Member(String memberId, String name, String email, MembershipType membershipType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
        this.activeLoans = new ArrayList&lt;&gt;();
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public MembershipType getMembershipType() { return membershipType; }
    public List&lt;Loan&gt; getActiveLoans() { return Collections.unmodifiableList(activeLoans); }

    /** Adds a loan to this member&#x27;s active loans. */
    public void addLoan(Loan loan) {
        activeLoans.add(loan);
    }

    /** Removes a loan from this member&#x27;s active loans upon return. */
    public void removeLoan(Loan loan) {
        activeLoans.remove(loan);
    }

    /** Checks whether this member can borrow more books. */
    public boolean canBorrow() {
        return activeLoans.size() &lt; membershipType.getMaxBorrowLimit();
    }

    public int getRemainingBorrowLimit() {
        return membershipType.getMaxBorrowLimit() - activeLoans.size();
    }

    @Override
    public String toString() {
        return String.format(&quot;Member{id=&#x27;%s&#x27;, name=&#x27;%s&#x27;, type=%s, activeLoans=%d}&quot;,
                memberId, name, membershipType, activeLoans.size());
    }
}</code></pre>
</div>
<div class="tab-content" id="MemberRepository-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Member entities.
 */
public interface MemberRepository {
    void save(Member member);
    Optional&lt;Member&gt; findById(String memberId);
    List&lt;Member&gt; findByName(String name);
    List&lt;Member&gt; findAll();
    void delete(String memberId);
}</code></pre>
</div>
<div class="tab-content" id="MembershipType-java">
<pre><code class="language-java">package org.interview.system_design.lld.library;

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
}</code></pre>
</div>
</div>
