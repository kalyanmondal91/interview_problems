package org.interview.system_design.lld.notification;

/**
 * Simulated Slack message channel.
 */
public class SlackChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf("[SLACK] Posting to user=%s | body='%s'%n",
                notification.getUserId(), notification.getBody());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.SLACK; }
}
