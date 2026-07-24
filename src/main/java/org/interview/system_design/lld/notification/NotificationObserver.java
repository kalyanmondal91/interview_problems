package org.interview.system_design.lld.notification;

/**
 * Observer interface for notification lifecycle events.
 * Pattern: Observer
 */
public interface NotificationObserver {
    /** Called when a notification is successfully sent. */
    void onNotificationSent(Notification notification);

    /** Called when all delivery attempts for a notification have failed. */
    void onNotificationFailed(Notification notification);
}
