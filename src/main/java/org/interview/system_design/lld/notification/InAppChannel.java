package org.interview.system_design.lld.notification;

/**
 * Simulated in-app notification channel (stores notifications in app UI).
 */
public class InAppChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf("[IN_APP] Queuing for user=%s | subject='%s'%n",
                notification.getUserId(), notification.getSubject());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.IN_APP; }
}
