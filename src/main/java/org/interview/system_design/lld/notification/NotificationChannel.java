package org.interview.system_design.lld.notification;

/**
 * Strategy interface for delivering notifications through a specific channel.
 * Pattern: Strategy
 */
public interface NotificationChannel {
    /**
     * Attempts to send the notification.
     *
     * @param notification the notification to send
     * @return true if sent successfully, false on failure
     */
    boolean send(Notification notification);

    /** Returns the channel type this implementation handles. */
    NotificationType getType();
}
