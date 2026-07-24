package org.interview.system_design.lld.notification;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Fluent builder for Notification.
 * Pattern: Builder
 *
 * <pre>
 * Notification n = new NotificationBuilder()
 *     .userId("u1")
 *     .type(NotificationType.EMAIL)
 *     .subject("Welcome!")
 *     .body("Hello, world.")
 *     .priority(NotificationPriority.NORMAL)
 *     .build();
 * </pre>
 */
public class NotificationBuilder {

    private String notificationId = UUID.randomUUID().toString();
    private String userId;
    private NotificationType type = NotificationType.IN_APP;
    private String subject = "";
    private String body = "";
    private NotificationPriority priority = NotificationPriority.NORMAL;
    private NotificationStatus status = NotificationStatus.PENDING;
    private LocalDateTime timestamp = LocalDateTime.now();
    private final Map<String, String> metadata = new HashMap<>();

    public NotificationBuilder notificationId(String notificationId) {
        this.notificationId = notificationId;
        return this;
    }

    public NotificationBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public NotificationBuilder type(NotificationType type) {
        this.type = type;
        return this;
    }

    public NotificationBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public NotificationBuilder body(String body) {
        this.body = body;
        return this;
    }

    public NotificationBuilder priority(NotificationPriority priority) {
        this.priority = priority;
        return this;
    }

    public NotificationBuilder status(NotificationStatus status) {
        this.status = status;
        return this;
    }

    public NotificationBuilder timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public NotificationBuilder metadata(String key, String value) {
        this.metadata.put(key, value);
        return this;
    }

    public NotificationBuilder metadataAll(Map<String, String> metadata) {
        this.metadata.putAll(metadata);
        return this;
    }

    public Notification build() {
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("userId is required");
        }
        return new Notification(notificationId, userId, type, subject, body,
                priority, status, timestamp, new HashMap<>(metadata));
    }
}
