package org.interview.system_design.lld.library;

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
        if (overdueDays <= 0) return 0.0;
        return overdueDays * finePerDay;
    }
}
