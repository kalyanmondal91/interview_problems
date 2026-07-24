package org.interview.system_design.lld.notification;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Routes based on the User's channel preference map.
 * A channel is selected only if the user has enabled it (preference value = true).
 * Falls back to all channels if the user object is null or preferences are empty.
 */
public class PreferenceRoutingStrategy implements RoutingStrategy {

    private final User user;

    public PreferenceRoutingStrategy(User user) {
        this.user = user;
    }

    @Override
    public List<NotificationChannel> route(Notification notification,
                                           List<NotificationChannel> allChannels) {
        if (user == null || user.getPreferences().isEmpty()) {
            return allChannels;
        }
        return allChannels.stream()
                .filter(c -> Boolean.TRUE.equals(user.getPreferences().get(c.getType())))
                .collect(Collectors.toList());
    }
}
