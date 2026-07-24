package org.interview.system_design.lld.notification;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Routes notifications based on priority:
 * - URGENT  → all channels
 * - HIGH    → PUSH + EMAIL
 * - NORMAL  → EMAIL only
 * - LOW     → IN_APP only
 */
public class PriorityRoutingStrategy implements RoutingStrategy {

    @Override
    public List<NotificationChannel> route(Notification notification,
                                           List<NotificationChannel> allChannels) {
        Set<NotificationType> allowed = resolveTypes(notification.getPriority());
        return allChannels.stream()
                .filter(c -> allowed.contains(c.getType()))
                .collect(Collectors.toList());
    }

    private Set<NotificationType> resolveTypes(NotificationPriority priority) {
        switch (priority) {
            case URGENT:
                return EnumSet.allOf(NotificationType.class);
            case HIGH:
                return EnumSet.of(NotificationType.PUSH, NotificationType.EMAIL);
            case NORMAL:
                return EnumSet.of(NotificationType.EMAIL);
            case LOW:
            default:
                return EnumSet.of(NotificationType.IN_APP);
        }
    }
}
