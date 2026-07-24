package org.interview.system_design.lld.moviebooking;

/**
 * Represents a movie available for booking.
 */
public class Movie {
    private final String movieId;
    private final String title;
    private final String genre;
    private final int durationMinutes;
    private final double rating;

    public Movie(String movieId, String title, String genre, int durationMinutes, double rating) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
    }

    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDurationMinutes() { return durationMinutes; }
    public double getRating() { return rating; }

    @Override
    public String toString() {
        return String.format("Movie{id='%s', title='%s', genre='%s', duration=%dmin, rating=%.1f}",
                movieId, title, genre, durationMinutes, rating);
    }
}
