package org.interview.system_design.lld.notification;

import java.util.EnumMap;
import java.util.Map;

/**
 * Demo runner for the Notification System LLD.
 * Patterns: Factory, Strategy (routing), Observer, Builder
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }

    public static void main(String[] args) {

        // -- Setup users --
        // User 6-arg constructor: (userId, name, email, phone, deviceToken, preferences)
        // Use 3-arg convenience constructor (enables all channels by default), then we demonstrate
        // that preferences are unmodifiable; we build users with the full 6-arg constructor instead.

        Map<NotificationType, Boolean> alicePrefs = new EnumMap<>(NotificationType.class);
        alicePrefs.put(NotificationType.EMAIL,  true);
        alicePrefs.put(NotificationType.SMS,    true);
        alicePrefs.put(NotificationType.PUSH,   true);
        alicePrefs.put(NotificationType.IN_APP, true);
        alicePrefs.put(NotificationType.SLACK,  false); // Alice opts out of Slack
        User alice = new User("u1", "Alice", "alice@example.com", "+911234567890", "device-token-alice", alicePrefs);

        Map<NotificationType, Boolean> bobPrefs = new EnumMap<>(NotificationType.class);
        bobPrefs.put(NotificationType.EMAIL,  true);
        bobPrefs.put(NotificationType.PUSH,   false); // Bob opts out of push
        bobPrefs.put(NotificationType.SMS,    false);
        bobPrefs.put(NotificationType.IN_APP, true);
        bobPrefs.put(NotificationType.SLACK,  false);
        User bob = new User("u2", "Bob", "bob@example.com", "+910987654321", "device-token-bob", bobPrefs);

        // Observer
        NotificationService service = new NotificationService(new PriorityRoutingStrategy(), 2);
        service.addObserver(new NotificationObserver() {
            @Override public void onNotificationSent(Notification n) {
                System.out.printf("  [OK] Sent %-8s to user %s%n", n.getType(), n.getUserId());
            }
            @Override public void onNotificationFailed(Notification n) {
                System.out.printf("  [X] Failed %-8s to user %s%n", n.getType(), n.getUserId());
            }
        });

        // -- Scenario 1: URGENT -- all channels --
        banner("Scenario 1: URGENT notification -> all channels");
        Notification urgent = new NotificationBuilder()
                .userId("u1")
                .type(NotificationType.PUSH)
                .subject("Security Alert")
                .body("Suspicious login detected from a new device. Please verify.")
                .priority(NotificationPriority.URGENT)
                .build();
        service.send(urgent, alice);

        // -- Scenario 2: HIGH -- push + email only --
        banner("Scenario 2: HIGH priority -> push + email");
        Notification high = new NotificationBuilder()
                .userId("u1")
                .type(NotificationType.EMAIL)
                .subject("Order Shipped")
                .body("Your order #12345 has been shipped and will arrive tomorrow.")
                .priority(NotificationPriority.HIGH)
                .build();
        service.send(high, alice);

        // -- Scenario 3: NORMAL -- email only --
        banner("Scenario 3: NORMAL priority -> email only");
        Notification normal = new NotificationBuilder()
                .userId("u1")
                .type(NotificationType.EMAIL)
                .subject("Weekly Digest")
                .body("Here is your weekly activity summary.")
                .priority(NotificationPriority.NORMAL)
                .build();
        service.send(normal, alice);

        // -- Scenario 4: PreferenceRoutingStrategy -- Bob's opt-outs --
        banner("Scenario 4: PreferenceRoutingStrategy -- respects Bob's opt-outs");
        // PreferenceRoutingStrategy requires a User in its constructor
        NotificationService prefService = new NotificationService(new PreferenceRoutingStrategy(bob), 2);
        prefService.addObserver(new NotificationObserver() {
            @Override public void onNotificationSent(Notification n) {
                System.out.printf("  [OK] Channel used: %s%n", n.getType());
            }
            @Override public void onNotificationFailed(Notification n) {
                System.out.printf("  [X] Channel skipped (opt-out): %s%n", n.getType());
            }
        });
        Notification bobNotif = new NotificationBuilder()
                .userId("u2")
                .type(NotificationType.IN_APP)
                .subject("New Feature")
                .body("Check out our latest feature!")
                .priority(NotificationPriority.NORMAL)
                .build();
        prefService.send(bobNotif, bob); // push/SMS skipped, email + in_app used

        // -- Scenario 5: Stats after all sends --
        banner("Scenario 5: Notification service stats");
        System.out.println("  Total sent:   " + service.getTotalSent());
        System.out.println("  Total failed: " + service.getTotalFailed());

        banner("Notification System Demo Complete");
    }
}
