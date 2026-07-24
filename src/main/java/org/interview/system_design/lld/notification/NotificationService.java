package org.interview.system_design.lld.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Orchestrates notification delivery with routing, retry, and observer notifications.
 * Patterns: Strategy (routing), Observer, Factory (channels)
 */
public class NotificationService {

    private static final int DEFAULT_MAX_RETRIES = 3;

    private final List<NotificationChannel> channels;
    private final RoutingStrategy routingStrategy;
    private final List<NotificationObserver> observers = new ArrayList<>();
    private final int maxRetries;

    // ---- Stats ----
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    public NotificationService(RoutingStrategy routingStrategy) {
        this(routingStrategy, DEFAULT_MAX_RETRIES);
    }

    public NotificationService(RoutingStrategy routingStrategy, int maxRetries) {
        this.routingStrategy = routingStrategy;
        this.maxRetries = maxRetries;
        // Register all channel types by default
        this.channels = Arrays.asList(
                ChannelFactory.createChannel(NotificationType.EMAIL),
                ChannelFactory.createChannel(NotificationType.SMS),
                ChannelFactory.createChannel(NotificationType.PUSH),
                ChannelFactory.createChannel(NotificationType.IN_APP),
                ChannelFactory.createChannel(NotificationType.SLACK)
        );
    }

    /** Registers an observer to receive delivery lifecycle events. */
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /** Removes a previously registered observer. */
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Sends the notification to the user via channels selected by the routing strategy.
     * Retries up to maxRetries times on failure before marking FAILED.
     *
     * @param notification notification to send
     * @param user         recipient
     * @return true if at least one channel succeeded
     */
    public boolean send(Notification notification, User user) {
        List<NotificationChannel> selected = routingStrategy.route(notification, channels);

        if (selected.isEmpty()) {
            System.out.printf("[NotificationService] No channels selected for %s%n", notification.getNotificationId());
            markFailed(notification);
            return false;
        }

        boolean atLeastOneSuccess = false;

        for (NotificationChannel channel : selected) {
            boolean sent = sendWithRetry(notification, channel);
            if (sent) atLeastOneSuccess = true;
        }

        if (atLeastOneSuccess) {
            notification.setStatus(NotificationStatus.SENT);
            totalSent.incrementAndGet();
            notifyObserversSent(notification);
        } else {
            markFailed(notification);
        }

        return atLeastOneSuccess;
    }

    // ---- Private helpers ----

    private boolean sendWithRetry(Notification notification, NotificationChannel channel) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                boolean success = channel.send(notification);
                if (success) return true;
            } catch (Exception e) {
                System.out.printf("[NotificationService] Channel %s threw exception on attempt %d: %s%n",
                        channel.getType(), attempt, e.getMessage());
            }
            System.out.printf("[NotificationService] Retry %d/%d for channel %s%n",
                    attempt, maxRetries, channel.getType());
        }
        System.out.printf("[NotificationService] All retries exhausted for channel %s%n", channel.getType());
        return false;
    }

    private void markFailed(Notification notification) {
        notification.setStatus(NotificationStatus.FAILED);
        totalFailed.incrementAndGet();
        notifyObserversFailed(notification);
    }

    private void notifyObserversSent(Notification notification) {
        observers.forEach(o -> o.onNotificationSent(notification));
    }

    private void notifyObserversFailed(Notification notification) {
        observers.forEach(o -> o.onNotificationFailed(notification));
    }

    // ---- Stats ----

    public long getTotalSent() { return totalSent.get(); }
    public long getTotalFailed() { return totalFailed.get(); }

    public List<NotificationChannel> getChannels() {
        return Collections.unmodifiableList(channels);
    }
}
