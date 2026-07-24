package org.interview.system_design.lld.notification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Immutable notification value object.
 * Constructed exclusively via NotificationBuilder.
 * Pattern: Builder
 */
public class Notification {

    private final String notificationId;
    private final String userId;
    private final NotificationType type;
    private final String subject;
    private final String body;
    private final NotificationPriority priority;
    private NotificationStatus status;
    private final LocalDateTime timestamp;
    private final Map<String, String> metadata;

    // Package-private: only NotificationBuilder should instantiate
    Notification(String notificationId,
                 String userId,
                 NotificationType type,
                 String subject,
                 String body,
                 NotificationPriority priority,
                 NotificationStatus status,
                 LocalDateTime timestamp,
                 Map<String, String> metadata) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.body = body;
        this.priority = priority;
        this.status = status;
        this.timestamp = timestamp;
        this.metadata = Collections.unmodifiableMap(metadata);
    }

    public String getNotificationId() { return notificationId; }
    public String getUserId() { return userId; }
    public NotificationType getType() { return type; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public NotificationPriority getPriority() { return priority; }
    public NotificationStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, String> getMetadata() { return metadata; }

    /** Mutable status allows the service to update delivery state. */
    public void setStatus(NotificationStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Notification[%s] type=%s priority=%s status=%s subject='%s'",
                notificationId, type, priority, status, subject);
    }
}
