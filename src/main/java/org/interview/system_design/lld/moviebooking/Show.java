package org.interview.system_design.lld.moviebooking;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a scheduled showing of a movie on a specific screen.
 * Manages the seat map for this particular show instance.
 */
public class Show {
    private final String showId;
    private final Movie movie;
    private final Screen screen;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Map<String, Seat> seats;  // seatId -> Seat

    public Show(String showId, Movie movie, Screen screen,
                LocalDateTime startTime, LocalDateTime endTime) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = new HashMap<>();
    }

    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public Screen getScreen() { return screen; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Map<String, Seat> getSeats() { return Collections.unmodifiableMap(seats); }

    public void addSeat(Seat seat) {
        seats.put(seat.getSeatId(), seat);
    }

    public Seat getSeat(String seatId) {
        return seats.get(seatId);
    }

    /** Returns all currently available seats. */
    public List<Seat> getAvailableSeats() {
        return seats.values().stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.toList());
    }

    /** Returns the number of available seats. */
    public long getAvailableSeatCount() {
        return seats.values().stream().filter(Seat::isAvailable).count();
    }

    @Override
    public String toString() {
        return String.format("Show{id='%s', movie='%s', screen='%s', start=%s, available=%d}",
                showId, movie.getTitle(), screen.getName(), startTime, getAvailableSeatCount());
    }
}
