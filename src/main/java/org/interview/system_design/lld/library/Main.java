package org.interview.system_design.lld.library;

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
                System.out.println("  [Event] Book returned: " + copy.getBook().getTitle());
            }
            @Override public void onBookOverdue(Loan loan) {
                System.out.println("  [Event] OVERDUE: " + loan.getBookCopy().getBook().getTitle()
                        + " by " + loan.getMember().getName());
            }
        });

        // ── Setup: Add books and members ──────────────────────────────────────
        System.out.println("\n=== Setup: Add books and members ===");
        Book java    = library.addBook("978-0-13-468599-1", "Effective Java",     "Joshua Bloch",  Genre.TECHNOLOGY, 2018);
        Book design  = library.addBook("978-0-20-163361-5", "Design Patterns",    "GoF",           Genre.TECHNOLOGY, 1994);
        Book dune    = library.addBook("978-0-44-100590-3", "Dune",               "Frank Herbert", Genre.FICTION,    1965);

        BookCopy javaCopy1  = library.addBookCopy(java.getBookId());
        BookCopy javaCopy2  = library.addBookCopy(java.getBookId()); // 2 copies
        BookCopy designCopy = library.addBookCopy(design.getBookId());
        BookCopy duneCopy   = library.addBookCopy(dune.getBookId());

        Member alice = new Member("m1", "Alice", "alice@lib.com", MembershipType.PREMIUM);
        Member bob   = new Member("m2", "Bob",   "bob@lib.com",   MembershipType.BASIC);
        library.registerMember(alice);
        library.registerMember(bob);
        System.out.println("  Books: Effective Java (×2), Design Patterns, Dune");
        System.out.println("  Members: Alice (PREMIUM, 10-book limit), Bob (BASIC, 3-book limit)");

        // ── Scenario 1: Borrow books ──────────────────────────────────────────
        System.out.println("\n=== Scenario 1: Members borrow books ===");
        Loan l1 = library.borrowBook("m1", java.getBookId(), 14);
        Loan l2 = library.borrowBook("m1", design.getBookId(), 14);
        Loan l3 = library.borrowBook("m2", dune.getBookId(), 7);
        System.out.printf("  Alice borrowed: %s (loan: %s)%n", l1.getBookCopy().getBook().getTitle(), l1.getLoanId());
        System.out.printf("  Alice borrowed: %s (loan: %s)%n", l2.getBookCopy().getBook().getTitle(), l2.getLoanId());
        System.out.printf("  Bob   borrowed: %s (loan: %s)%n", l3.getBookCopy().getBook().getTitle(), l3.getLoanId());
        System.out.println("  Alice active loans: " + alice.getActiveLoans().size());

        // ── Scenario 2: Return on time (no fine) ─────────────────────────────
        System.out.println("\n=== Scenario 2: Return on time -- no fine ===");
        double fine1 = library.returnBook(l1.getLoanId());
        System.out.printf("  Returned '%s' -- Fine: $%.2f%n", java.getTitle(), fine1);

        // ── Scenario 3: Search ────────────────────────────────────────────────
        System.out.println("\n=== Scenario 3: Search books ===");
        List<Book> techBooks = library.searchByGenre(Genre.TECHNOLOGY);
        System.out.println("  Technology books:");
        techBooks.forEach(b -> System.out.println("    - " + b.getTitle() + " by " + b.getAuthor()));

        List<Book> byBloch = library.searchByAuthor("Joshua Bloch");
        System.out.println("  Books by Joshua Bloch: " + byBloch.size());

        library.searchByISBN("978-0-44-100590-3")
                .ifPresent(b -> System.out.println("  Found by ISBN: " + b.getTitle()));

        // ── Scenario 4: BASIC member hits 3-book borrow limit ────────────────
        System.out.println("\n=== Scenario 4: BASIC member borrowing limit (max 3) ===");
        // Bob already has Dune; borrow 2 more (javaCopy2 is available)
        Loan l4 = library.borrowBook("m2", java.getBookId(), 7);
        System.out.println("  Bob borrowed: " + (l4 != null ? l4.getBookCopy().getBook().getTitle() : "FAILED"));
        // Return design first so another copy is available
        library.returnBook(l2.getLoanId());
        Loan l5 = library.borrowBook("m2", design.getBookId(), 7);
        System.out.println("  Bob borrowed: " + (l5 != null ? l5.getBookCopy().getBook().getTitle() : "FAILED"));
        // Bob now has 3 loans -- next should fail
        Loan l6 = library.borrowBook("m2", java.getBookId(), 7); // javaCopy2 still out
        System.out.println("  Bob borrow attempt 4: " + (l6 != null ? "SUCCESS" : "REJECTED (limit reached)"));

        // ── Scenario 5: Reserve a book ────────────────────────────────────────
        System.out.println("\n=== Scenario 5: Reserve a book ===");
        try {
            library.reserveBook("m1", duneCopy.getCopyId());
            System.out.println("  Alice reserved Dune copy -- status: " + duneCopy.getStatus());
        } catch (Exception e) {
            System.out.println("  Reserve failed: " + e.getMessage());
        }

        System.out.println("\n=== Library Management Demo Complete ===");
    }
}
