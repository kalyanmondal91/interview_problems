package org.interview.system_design.lld.moviebooking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a cinema screen within a theatre.
 */
public class Screen {
    private final String screenId;
    private final String name;
    private final int capacity;
    private final List<Show> shows;

    public Screen(String screenId, String name, int capacity) {
        this.screenId = screenId;
        this.name = name;
        this.capacity = capacity;
        this.shows = new ArrayList<>();
    }

    public String getScreenId() { return screenId; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public List<Show> getShows() { return Collections.unmodifiableList(shows); }

    public void addShow(Show show) { shows.add(show); }

    @Override
    public String toString() {
        return String.format("Screen{id='%s', name='%s', capacity=%d}", screenId, name, capacity);
    }
}
