package org.interview.system_design.lld.notification;

/**
 * Simulated SMS delivery channel.
 */
public class SMSChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf("[SMS] Sending to user=%s | body='%s'%n",
                notification.getUserId(), notification.getBody());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.SMS; }
}
