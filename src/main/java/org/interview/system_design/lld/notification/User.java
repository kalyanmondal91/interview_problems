package org.interview.system_design.lld.notification;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a notification recipient with contact info and channel preferences.
 */
public class User {

    private final String userId;
    private final String name;
    private final String email;
    private final String phone;
    private final String deviceToken;
    private final Map<NotificationType, Boolean> preferences;

    public User(String userId,
                String name,
                String email,
                String phone,
                String deviceToken,
                Map<NotificationType, Boolean> preferences) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceToken = deviceToken;
        // Defensive copy using EnumMap for type safety
        Map<NotificationType, Boolean> prefs = new EnumMap<>(NotificationType.class);
        if (preferences != null) prefs.putAll(preferences);
        this.preferences = Collections.unmodifiableMap(prefs);
    }

    /** Convenience constructor: enables all channels by default. */
    public User(String userId, String name, String email) {
        this(userId, name, email, null, null, defaultPreferences());
    }

    private static Map<NotificationType, Boolean> defaultPreferences() {
        Map<NotificationType, Boolean> prefs = new EnumMap<>(NotificationType.class);
        for (NotificationType type : NotificationType.values()) {
            prefs.put(type, true);
        }
        return prefs;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDeviceToken() { return deviceToken; }
    public Map<NotificationType, Boolean> getPreferences() { return preferences; }

    @Override
    public String toString() {
        return String.format("User[%s, %s, %s]", userId, name, email);
    }
}
