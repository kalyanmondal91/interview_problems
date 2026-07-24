package org.interview.system_design.lld.moviebooking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a cinema theatre containing multiple screens.
 */
public class Theatre {
    private final String theatreId;
    private final String name;
    private final String city;
    private final List<Screen> screens;

    public Theatre(String theatreId, String name, String city) {
        this.theatreId = theatreId;
        this.name = name;
        this.city = city;
        this.screens = new ArrayList<>();
    }

    public String getTheatreId() { return theatreId; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public List<Screen> getScreens() { return Collections.unmodifiableList(screens); }

    public void addScreen(Screen screen) { screens.add(screen); }

    @Override
    public String toString() {
        return String.format("Theatre{id='%s', name='%s', city='%s', screens=%d}",
                theatreId, name, city, screens.size());
    }
}
