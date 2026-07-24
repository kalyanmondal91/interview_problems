package org.interview.system_design.lld.moviebooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demo runner for the Movie Booking LLD (BookMyShow style).
 * Patterns: State, Factory, Observer
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        MovieBookingService service = new MovieBookingService(new StandardPricingStrategy());

        // Observer
        service.addObserver(new BookingObserver() {
            @Override public void onBookingConfirmed(Booking b) {
                System.out.printf("  [Event] Booking CONFIRMED: %s | Amount: $%.2f%n",
                        b.getBookingId(), b.getTotalAmount());
            }
            @Override public void onBookingCancelled(Booking b) {
                System.out.printf("  [Event] Booking CANCELLED: %s%n", b.getBookingId());
            }
        });

        // -- Setup --
        // Movie rating is a double (e.g. 8.5), not a String like "PG-13"
        Movie inception = new Movie("mv1", "Inception",  "Sci-Fi",  148, 8.5);
        Movie avatar    = new Movie("mv2", "Avatar",     "Fantasy", 162, 7.8);
        service.registerMovie(inception);
        service.registerMovie(avatar);

        Screen screen1 = new Screen("sc1", "Audi 1", 10); // 10 seats for demo
        Theatre pvr = new Theatre("th1", "PVR Cinemas", "Bangalore");
        pvr.addScreen(screen1);
        service.registerTheatre(pvr);

        LocalDateTime showTime = LocalDateTime.now().plusHours(2);
        Show show1 = new Show("sh1", inception, screen1, showTime, showTime.plusMinutes(148));
        Show show2 = new Show("sh2", avatar,    screen1, showTime.plusHours(4), showTime.plusHours(4).plusMinutes(162));

        // Add seats to shows so the demo has something to book
        for (int i = 0; i < 10; i++) {
            show1.addSeat(new Seat("S1-" + i, i / 5, i % 5, SeatType.STANDARD));
            show2.addSeat(new Seat("S2-" + i, i / 5, i % 5, SeatType.STANDARD));
        }

        service.registerShow(show1);
        service.registerShow(show2);

        // -- Scenario 1: Search movies and shows --
        banner("Scenario 1: Search movies and shows");
        List<Movie> results = service.searchMovies("Inc");
        results.forEach(m -> System.out.println("  Found: " + m.getTitle() + " (" + m.getDurationMinutes() + " min)"));

        List<Show> shows = service.searchShows("mv1", LocalDateTime.now());
        System.out.println("  Shows for Inception: " + shows.size());

        // -- Scenario 2: Book 3 seats and confirm --
        banner("Scenario 2: Book 3 seats and confirm payment");
        List<String> seats = show1.getAvailableSeats().stream()
                .limit(3)
                .map(Seat::getSeatId)
                .collect(Collectors.toList());
        System.out.println("  Reserving seats: " + seats);
        Booking booking1 = service.selectSeats("sh1", seats, "user1");
        if (booking1 != null) {
            System.out.println("  Booking created: " + booking1.getBookingId()
                    + " | Status: " + booking1.getStatus());
            Payment payment = service.confirmBooking(booking1.getBookingId());
            System.out.println("  Payment status: " + payment.getStatus()
                    + " | Total: $" + String.format("%.2f", payment.getAmount()));
        }

        // -- Scenario 3: Try to double-book same seats --
        banner("Scenario 3: Double-book attempt on same seats");
        try {
            Booking booking2 = service.selectSeats("sh1", seats, "user2");
            if (booking2 == null) {
                System.out.println("  Double-booking rejected -- seats already taken!");
            } else {
                System.out.println("  Unexpected: double-booking succeeded: " + booking2.getBookingId());
            }
        } catch (IllegalStateException e) {
            System.out.println("  Double-booking rejected -- seats already taken! (" + e.getMessage() + ")");
        }

        // -- Scenario 4: Book and cancel --
        banner("Scenario 4: Book and cancel");
        List<String> moreSeats = show1.getAvailableSeats().stream()
                .limit(2)
                .map(Seat::getSeatId)
                .collect(Collectors.toList());
        if (!moreSeats.isEmpty()) {
            Booking booking3 = service.selectSeats("sh1", moreSeats, "user3");
            if (booking3 != null) {
                System.out.println("  Booking created: " + booking3.getBookingId());
                service.confirmBooking(booking3.getBookingId());
                service.cancelBooking(booking3.getBookingId());
                System.out.println("  Status after cancel: " + booking3.getStatus());
            }
        }

        // -- Scenario 5: Show availability after bookings --
        banner("Scenario 5: Remaining available seats");
        System.out.println("  Available seats for show1: " + show1.getAvailableSeatCount());
        System.out.println("  Available seats for show2: " + show2.getAvailableSeatCount());

        banner("Movie Booking Demo Complete");
    }
}
