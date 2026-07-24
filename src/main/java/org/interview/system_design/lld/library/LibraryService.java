package org.interview.system_design.lld.library;

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
    private final List<LibraryObserver> observers;
    // All active loans (indexed by loanId)
    private final java.util.Map<String, Loan> loansById;

    public LibraryService() {
        this.bookRepository = new InMemoryBookRepository();
        this.memberRepository = new InMemoryMemberRepository();
        this.fineCalculator = new FineCalculator();
        this.observers = new ArrayList<>();
        this.loansById = new java.util.HashMap<>();
    }

    // ---- Observer management ----

    public void addObserver(LibraryObserver observer) { observers.add(observer); }
    public void removeObserver(LibraryObserver observer) { observers.remove(observer); }

    private void notifyBookReturned(BookCopy copy) {
        observers.forEach(o -> o.onBookReturned(copy));
    }
    private void notifyBookOverdue(Loan loan) {
        observers.forEach(o -> o.onBookOverdue(loan));
    }

    // ---- Book / Copy registration ----

    public Book addBook(String isbn, String title, String author, Genre genre, int year) {
        Book book = BookFactory.createBook(isbn, title, author, genre, year);
        bookRepository.save(book);
        return book;
    }

    public BookCopy addBookCopy(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
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
     * @param memberId  the borrowing member's ID
     * @param bookId    the book to borrow
     * @param daysLoan  loan period in days
     * @return the created Loan
     */
    public Loan borrowBook(String memberId, String bookId, int daysLoan) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        if (!member.canBorrow()) {
            throw new IllegalStateException("Member has reached borrow limit: " + member.getMembershipType());
        }

        BookCopy copy = bookRepository.findAvailableCopy(bookId)
                .orElseThrow(() -> new IllegalStateException("No available copy for book: " + bookId));

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(daysLoan);
        String loanId = "LN-" + UUID.randomUUID().toString().substring(0, 8);

        Loan loan = new Loan(loanId, copy, member, borrowDate, dueDate);

        copy.setStatus(BookStatus.BORROWED);
        copy.setBorrowedBy(memberId);
        copy.setDueDate(dueDate);
        member.addLoan(loan);
        loansById.put(loanId, loan);

        System.out.printf("Borrowed: '%s' by %s, due %s%n",
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
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

        if (!loan.isActive()) {
            throw new IllegalStateException("Loan already returned: " + loanId);
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
        if (fine > 0) {
            System.out.printf("Returned: '%s' — Fine: $%.2f%n", copy.getBook().getTitle(), fine);
        } else {
            System.out.printf("Returned: '%s' on time.%n", copy.getBook().getTitle());
        }
        return fine;
    }

    // ---- Reservation ----

    /** Reserves a book copy (marks as RESERVED). */
    public void reserveBook(String memberId, String copyId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        BookCopy copy = bookRepository.findCopyById(copyId)
                .orElseThrow(() -> new IllegalArgumentException("Copy not found: " + copyId));
        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy not available for reservation: " + copyId);
        }
        copy.setStatus(BookStatus.RESERVED);
        copy.setBorrowedBy(memberId);
        System.out.printf("Reserved copy '%s' for member '%s'%n", copyId, memberId);
    }

    // ---- Search ----

    public List<Book> searchByTitle(String title) { return bookRepository.findByTitle(title); }
    public List<Book> searchByAuthor(String author) { return bookRepository.findByAuthor(author); }
    public List<Book> searchByGenre(Genre genre) { return bookRepository.findByGenre(genre); }
    public Optional<Book> searchByISBN(String isbn) { return bookRepository.findByISBN(isbn); }

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
                System.out.println("[Observer] Book returned: " + copy.getBook().getTitle());
            }
            @Override
            public void onBookOverdue(Loan loan) {
                System.out.println("[Observer] OVERDUE: " + loan);
            }
        });

        Book book = service.addBook("978-0-00-1", "Clean Code", "Robert Martin",
                Genre.TECHNOLOGY, 2008);
        service.addBookCopy(book.getBookId());
        service.addBookCopy(book.getBookId());

        Member alice = new Member("M1", "Alice", "alice@example.com", MembershipType.PREMIUM);
        service.registerMember(alice);

        Loan loan = service.borrowBook("M1", book.getBookId(), 14);
        service.returnBook(loan.getLoanId());

        List<Book> results = service.searchByTitle("clean");
        System.out.println("Search results: " + results);
    }
}
