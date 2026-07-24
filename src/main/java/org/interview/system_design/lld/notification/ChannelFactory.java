package org.interview.system_design.lld.notification;

/**
 * Factory that creates the appropriate NotificationChannel for a given type.
 * Pattern: Factory Method
 */
public class ChannelFactory {

    private ChannelFactory() {}

    /**
     * Creates and returns the channel implementation for the given type.
     *
     * @param type notification type
     * @return corresponding channel
     * @throws IllegalArgumentException if the type is unsupported
     */
    public static NotificationChannel createChannel(NotificationType type) {
        switch (type) {
            case EMAIL:  return new EmailChannel();
            case SMS:    return new SMSChannel();
            case PUSH:   return new PushChannel();
            case IN_APP: return new InAppChannel();
            case SLACK:  return new SlackChannel();
            default:
                throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
    }
}
