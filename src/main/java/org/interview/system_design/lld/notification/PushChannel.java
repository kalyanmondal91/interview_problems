package org.interview.system_design.lld.notification;

/**
 * Simulated mobile push notification channel.
 */
public class PushChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf("[PUSH] Pushing to user=%s | subject='%s'%n",
                notification.getUserId(), notification.getSubject());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.PUSH; }
}
