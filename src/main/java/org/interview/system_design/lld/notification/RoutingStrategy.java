package org.interview.system_design.lld.notification;

import java.util.List;

/**
 * Strategy interface that selects which channels to use for a notification.
 * Pattern: Strategy
 */
public interface RoutingStrategy {
    /**
     * Returns the subset of available channels that should be used to deliver
     * the given notification.
     *
     * @param notification  the notification to route
     * @param allChannels   all registered channels
     * @return list of channels to send through (may be empty)
     */
    List<NotificationChannel> route(Notification notification, List<NotificationChannel> allChannels);
}
