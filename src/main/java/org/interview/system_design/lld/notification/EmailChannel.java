package org.interview.system_design.lld.notification;

/**
 * Simulated email delivery channel.
 */
public class EmailChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf("[EMAIL] Sending to user=%s | subject='%s' | body='%s'%n",
                notification.getUserId(), notification.getSubject(), notification.getBody());
        return true; // Simulated success
    }

    @Override
    public NotificationType getType() { return NotificationType.EMAIL; }
}
