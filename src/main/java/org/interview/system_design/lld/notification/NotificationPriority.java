package org.interview.system_design.lld.notification;

/** Priority of a notification, affecting routing and retry behaviour. */
public enum NotificationPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}
