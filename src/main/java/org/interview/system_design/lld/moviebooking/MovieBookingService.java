package org.interview.system_design.lld.moviebooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Core service for the movie booking system.
 *
 * Design patterns:
 *  - State: Booking transitions through PENDING → CONFIRMED → CANCELLED/REFUNDED
 *  - Factory: Payment instances created internally
 *  - Observer: BookingObserver notified on confirm/cancel
 *  - Strategy: PricingStrategy injectable for pricing
 */
public class MovieBookingService {
    private final Map<String, Movie> movies = new HashMap<>();
    private final Map<String, Theatre> theatres = new HashMap<>();
    private final Map<String, Show> shows = new HashMap<>();
    private final Map<String, Booking> bookings = new HashMap<>();
    private final Map<String, Payment> payments = new HashMap<>();
    private final List<BookingObserver> observers = new ArrayList<>();
    private PricingStrategy pricingStrategy;

    public MovieBookingService() {
        this.pricingStrategy = new StandardPricingStrategy();
    }

    public MovieBookingService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    // ---- Observer management ----

    public void addObserver(BookingObserver observer) { observers.add(observer); }
    public void removeObserver(BookingObserver observer) { observers.remove(observer); }

    private void notifyConfirmed(Booking booking) {
        observers.forEach(o -> o.onBookingConfirmed(booking));
    }
    private void notifyCancelled(Booking booking) {
        observers.forEach(o -> o.onBookingCancelled(booking));
    }

    // ---- Setup / Registration ----

    public void registerMovie(Movie movie) { movies.put(movie.getMovieId(), movie); }
    public void registerTheatre(Theatre theatre) { theatres.put(theatre.getTheatreId(), theatre); }

    public void registerShow(Show show) {
        shows.put(show.getShowId(), show);
        show.getScreen().addShow(show);
    }

    // ---- Search ----

    /** Returns all movies whose title (case-insensitive) contains the query string. */
    public List<Movie> searchMovies(String titleQuery) {
        String lower = titleQuery.toLowerCase();
        return movies.values().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /** Returns all shows for a given movie on or after a given time. */
    public List<Show> searchShows(String movieId, LocalDateTime from) {
        return shows.values().stream()
                .filter(s -> s.getMovie().getMovieId().equals(movieId)
                        && !s.getStartTime().isBefore(from))
                .collect(Collectors.toList());
    }

    // ---- Seat reservation (PENDING booking) ----

    /**
     * Reserves the requested seats and creates a PENDING booking.
     * Seats are marked RESERVED to prevent double-booking during payment.
     *
     * @param showId   the show ID
     * @param seatIds  the seat IDs to reserve
     * @param userId   the user making the booking
     * @return the created Booking in PENDING state
     */
    public Booking selectSeats(String showId, List<String> seatIds, String userId) {
        Show show = Optional.ofNullable(shows.get(showId))
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));

        List<Seat> selectedSeats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = show.getSeat(seatId);
            if (seat == null) throw new IllegalArgumentException("Seat not found: " + seatId);
            if (!seat.isAvailable()) throw new IllegalStateException("Seat not available: " + seatId);
            selectedSeats.add(seat);
        }

        // Calculate total price using the injected strategy
        double totalAmount = selectedSeats.stream()
                .mapToDouble(s -> pricingStrategy.calculatePrice(s, show))
                .sum();

        // Reserve seats atomically
        selectedSeats.forEach(s -> s.setStatus(SeatStatus.RESERVED));

        String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking booking = new Booking(bookingId, show, selectedSeats, userId, totalAmount);
        bookings.put(bookingId, booking);

        System.out.printf("Seats reserved for '%s': %s, total $%.2f%n",
                show.getMovie().getTitle(), seatIds, totalAmount);
        return booking;
    }

    // ---- Booking confirmation (payment) ----

    /**
     * Confirms a booking by processing payment (simulated as always succeeding).
     * Transitions booking to CONFIRMED and seats to BOOKED.
     *
     * @param bookingId the booking to confirm
     * @return the Payment record
     */
    public Payment confirmBooking(String bookingId) {
        Booking booking = getBooking(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in PENDING state: " + bookingId);
        }

        // Simulate payment processing (always succeeds in this demo)
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = new Payment(paymentId, booking, booking.getTotalAmount());
        payment.setStatus(PaymentStatus.SUCCESS);
        payments.put(paymentId, payment);

        // Confirm booking and mark seats as BOOKED
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.getSeats().forEach(s -> s.setStatus(SeatStatus.BOOKED));

        notifyConfirmed(booking);
        System.out.println("Booking confirmed: " + booking);
        return payment;
    }

    // ---- Cancellation ----

    /**
     * Cancels a booking and releases the seats back to AVAILABLE.
     *
     * @param bookingId the booking to cancel
     */
    public void cancelBooking(String bookingId) {
        Booking booking = getBooking(bookingId);
        if (booking.getStatus() == BookingStatus.CANCELLED
                || booking.getStatus() == BookingStatus.REFUNDED) {
            throw new IllegalStateException("Booking is already cancelled/refunded: " + bookingId);
        }

        // Release seats
        booking.getSeats().forEach(s -> s.setStatus(SeatStatus.AVAILABLE));
        booking.setStatus(BookingStatus.CANCELLED);

        notifyCancelled(booking);
        System.out.println("Booking cancelled: " + bookingId);
    }

    // ---- Helpers ----

    private Booking getBooking(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
    }

    public void setPricingStrategy(PricingStrategy strategy) { this.pricingStrategy = strategy; }

    public Optional<Booking> findBooking(String bookingId) {
        return Optional.ofNullable(bookings.get(bookingId));
    }

    /**
     * Demo main method.
     */
    public static void main(String[] args) {
        MovieBookingService service = new MovieBookingService();

        service.addObserver(new BookingObserver() {
            @Override
            public void onBookingConfirmed(Booking b) {
                System.out.println("[Observer] Confirmed: " + b.getBookingId());
            }
            @Override
            public void onBookingCancelled(Booking b) {
                System.out.println("[Observer] Cancelled: " + b.getBookingId());
            }
        });

        Movie movie = new Movie("M1", "Inception", "Sci-Fi", 148, 8.8);
        service.registerMovie(movie);

        Screen screen = new Screen("SC1", "Screen 1", 100);
        Theatre theatre = new Theatre("TH1", "CineMax", "Mumbai");
        theatre.addScreen(screen);
        service.registerTheatre(theatre);

        Show show = new Show("SH1", movie, screen,
                LocalDateTime.of(2024, 7, 1, 20, 0),
                LocalDateTime.of(2024, 7, 1, 22, 30));

        // Add seats
        show.addSeat(new Seat("A1", 0, 0, SeatType.STANDARD));
        show.addSeat(new Seat("A2", 0, 1, SeatType.STANDARD));
        show.addSeat(new Seat("B1", 1, 0, SeatType.VIP));
        service.registerShow(show);

        Booking booking = service.selectSeats("SH1", List.of("A1", "B1"), "user42");
        Payment payment = service.confirmBooking(booking.getBookingId());
        System.out.println("Payment: " + payment);
    }
}
