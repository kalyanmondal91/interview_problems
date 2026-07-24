---
layout: lld
render_with_liquid: false
title: "Notification System"
system: notification
description: "LLD of Notification System"
files:
  - "ChannelFactory.java"
  - "EmailChannel.java"
  - "InAppChannel.java"
  - "Main.java"
  - "Notification.java"
  - "NotificationBuilder.java"
  - "NotificationChannel.java"
  - "NotificationObserver.java"
  - "NotificationPriority.java"
  - "NotificationService.java"
  - "NotificationStatus.java"
  - "NotificationType.java"
  - "PreferenceRoutingStrategy.java"
  - "PriorityRoutingStrategy.java"
  - "PushChannel.java"
  - "RoutingStrategy.java"
  - "SMSChannel.java"
  - "SlackChannel.java"
  - "User.java"
---

## Notification System

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="ChannelFactory.java">ChannelFactory.java</button>
<button class="tab-btn" data-tab="EmailChannel.java">EmailChannel.java</button>
<button class="tab-btn" data-tab="InAppChannel.java">InAppChannel.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="Notification.java">Notification.java</button>
<button class="tab-btn" data-tab="NotificationBuilder.java">NotificationBuilder.java</button>
<button class="tab-btn" data-tab="NotificationChannel.java">NotificationChannel.java</button>
<button class="tab-btn" data-tab="NotificationObserver.java">NotificationObserver.java</button>
<button class="tab-btn" data-tab="NotificationPriority.java">NotificationPriority.java</button>
<button class="tab-btn" data-tab="NotificationService.java">NotificationService.java</button>
<button class="tab-btn" data-tab="NotificationStatus.java">NotificationStatus.java</button>
<button class="tab-btn" data-tab="NotificationType.java">NotificationType.java</button>
<button class="tab-btn" data-tab="PreferenceRoutingStrategy.java">PreferenceRoutingStrategy.java</button>
<button class="tab-btn" data-tab="PriorityRoutingStrategy.java">PriorityRoutingStrategy.java</button>
<button class="tab-btn" data-tab="PushChannel.java">PushChannel.java</button>
<button class="tab-btn" data-tab="RoutingStrategy.java">RoutingStrategy.java</button>
<button class="tab-btn" data-tab="SMSChannel.java">SMSChannel.java</button>
<button class="tab-btn" data-tab="SlackChannel.java">SlackChannel.java</button>
<button class="tab-btn" data-tab="User.java">User.java</button>
</div>
<div class="tab-content active" id="ChannelFactory-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

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
                throw new IllegalArgumentException(&quot;Unsupported notification type: &quot; + type);
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="EmailChannel-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Simulated email delivery channel.
 */
public class EmailChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf(&quot;[EMAIL] Sending to user=%s | subject=&#x27;%s&#x27; | body=&#x27;%s&#x27;%n&quot;,
                notification.getUserId(), notification.getSubject(), notification.getBody());
        return true; // Simulated success
    }

    @Override
    public NotificationType getType() { return NotificationType.EMAIL; }
}</code></pre>
</div>
<div class="tab-content" id="InAppChannel-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Simulated in-app notification channel (stores notifications in app UI).
 */
public class InAppChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf(&quot;[IN_APP] Queuing for user=%s | subject=&#x27;%s&#x27;%n&quot;,
                notification.getUserId(), notification.getSubject());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.IN_APP; }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

import java.util.EnumMap;
import java.util.Map;

/**
 * Demo runner for the Notification System LLD.
 * Patterns: Factory, Strategy (routing), Observer, Builder
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) {

        // -- Setup users --
        // User 6-arg constructor: (userId, name, email, phone, deviceToken, preferences)
        // Use 3-arg convenience constructor (enables all channels by default), then we demonstrate
        // that preferences are unmodifiable; we build users with the full 6-arg constructor instead.

        Map&lt;NotificationType, Boolean&gt; alicePrefs = new EnumMap&lt;&gt;(NotificationType.class);
        alicePrefs.put(NotificationType.EMAIL,  true);
        alicePrefs.put(NotificationType.SMS,    true);
        alicePrefs.put(NotificationType.PUSH,   true);
        alicePrefs.put(NotificationType.IN_APP, true);
        alicePrefs.put(NotificationType.SLACK,  false); // Alice opts out of Slack
        User alice = new User(&quot;u1&quot;, &quot;Alice&quot;, &quot;alice@example.com&quot;, &quot;+911234567890&quot;, &quot;device-token-alice&quot;, alicePrefs);

        Map&lt;NotificationType, Boolean&gt; bobPrefs = new EnumMap&lt;&gt;(NotificationType.class);
        bobPrefs.put(NotificationType.EMAIL,  true);
        bobPrefs.put(NotificationType.PUSH,   false); // Bob opts out of push
        bobPrefs.put(NotificationType.SMS,    false);
        bobPrefs.put(NotificationType.IN_APP, true);
        bobPrefs.put(NotificationType.SLACK,  false);
        User bob = new User(&quot;u2&quot;, &quot;Bob&quot;, &quot;bob@example.com&quot;, &quot;+910987654321&quot;, &quot;device-token-bob&quot;, bobPrefs);

        // Observer
        NotificationService service = new NotificationService(new PriorityRoutingStrategy(), 2);
        service.addObserver(new NotificationObserver() {
            @Override public void onNotificationSent(Notification n) {
                System.out.printf(&quot;  [OK] Sent %-8s to user %s%n&quot;, n.getType(), n.getUserId());
            }
            @Override public void onNotificationFailed(Notification n) {
                System.out.printf(&quot;  [X] Failed %-8s to user %s%n&quot;, n.getType(), n.getUserId());
            }
        });

        // -- Scenario 1: URGENT -- all channels --
        banner(&quot;Scenario 1: URGENT notification -&gt; all channels&quot;);
        Notification urgent = new NotificationBuilder()
                .userId(&quot;u1&quot;)
                .type(NotificationType.PUSH)
                .subject(&quot;Security Alert&quot;)
                .body(&quot;Suspicious login detected from a new device. Please verify.&quot;)
                .priority(NotificationPriority.URGENT)
                .build();
        service.send(urgent, alice);

        // -- Scenario 2: HIGH -- push + email only --
        banner(&quot;Scenario 2: HIGH priority -&gt; push + email&quot;);
        Notification high = new NotificationBuilder()
                .userId(&quot;u1&quot;)
                .type(NotificationType.EMAIL)
                .subject(&quot;Order Shipped&quot;)
                .body(&quot;Your order #12345 has been shipped and will arrive tomorrow.&quot;)
                .priority(NotificationPriority.HIGH)
                .build();
        service.send(high, alice);

        // -- Scenario 3: NORMAL -- email only --
        banner(&quot;Scenario 3: NORMAL priority -&gt; email only&quot;);
        Notification normal = new NotificationBuilder()
                .userId(&quot;u1&quot;)
                .type(NotificationType.EMAIL)
                .subject(&quot;Weekly Digest&quot;)
                .body(&quot;Here is your weekly activity summary.&quot;)
                .priority(NotificationPriority.NORMAL)
                .build();
        service.send(normal, alice);

        // -- Scenario 4: PreferenceRoutingStrategy -- Bob&#x27;s opt-outs --
        banner(&quot;Scenario 4: PreferenceRoutingStrategy -- respects Bob&#x27;s opt-outs&quot;);
        // PreferenceRoutingStrategy requires a User in its constructor
        NotificationService prefService = new NotificationService(new PreferenceRoutingStrategy(bob), 2);
        prefService.addObserver(new NotificationObserver() {
            @Override public void onNotificationSent(Notification n) {
                System.out.printf(&quot;  [OK] Channel used: %s%n&quot;, n.getType());
            }
            @Override public void onNotificationFailed(Notification n) {
                System.out.printf(&quot;  [X] Channel skipped (opt-out): %s%n&quot;, n.getType());
            }
        });
        Notification bobNotif = new NotificationBuilder()
                .userId(&quot;u2&quot;)
                .type(NotificationType.IN_APP)
                .subject(&quot;New Feature&quot;)
                .body(&quot;Check out our latest feature!&quot;)
                .priority(NotificationPriority.NORMAL)
                .build();
        prefService.send(bobNotif, bob); // push/SMS skipped, email + in_app used

        // -- Scenario 5: Stats after all sends --
        banner(&quot;Scenario 5: Notification service stats&quot;);
        System.out.println(&quot;  Total sent:   &quot; + service.getTotalSent());
        System.out.println(&quot;  Total failed: &quot; + service.getTotalFailed());

        banner(&quot;Notification System Demo Complete&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="Notification-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

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
    private final Map&lt;String, String&gt; metadata;

    // Package-private: only NotificationBuilder should instantiate
    Notification(String notificationId,
                 String userId,
                 NotificationType type,
                 String subject,
                 String body,
                 NotificationPriority priority,
                 NotificationStatus status,
                 LocalDateTime timestamp,
                 Map&lt;String, String&gt; metadata) {
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
    public Map&lt;String, String&gt; getMetadata() { return metadata; }

    /** Mutable status allows the service to update delivery state. */
    public void setStatus(NotificationStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format(&quot;Notification[%s] type=%s priority=%s status=%s subject=&#x27;%s&#x27;&quot;,
                notificationId, type, priority, status, subject);
    }
}</code></pre>
</div>
<div class="tab-content" id="NotificationBuilder-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Fluent builder for Notification.
 * Pattern: Builder
 *
 * &lt;pre&gt;
 * Notification n = new NotificationBuilder()
 *     .userId(&quot;u1&quot;)
 *     .type(NotificationType.EMAIL)
 *     .subject(&quot;Welcome!&quot;)
 *     .body(&quot;Hello, world.&quot;)
 *     .priority(NotificationPriority.NORMAL)
 *     .build();
 * &lt;/pre&gt;
 */
public class NotificationBuilder {

    private String notificationId = UUID.randomUUID().toString();
    private String userId;
    private NotificationType type = NotificationType.IN_APP;
    private String subject = &quot;&quot;;
    private String body = &quot;&quot;;
    private NotificationPriority priority = NotificationPriority.NORMAL;
    private NotificationStatus status = NotificationStatus.PENDING;
    private LocalDateTime timestamp = LocalDateTime.now();
    private final Map&lt;String, String&gt; metadata = new HashMap&lt;&gt;();

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

    public NotificationBuilder metadataAll(Map&lt;String, String&gt; metadata) {
        this.metadata.putAll(metadata);
        return this;
    }

    public Notification build() {
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException(&quot;userId is required&quot;);
        }
        return new Notification(notificationId, userId, type, subject, body,
                priority, status, timestamp, new HashMap&lt;&gt;(metadata));
    }
}</code></pre>
</div>
<div class="tab-content" id="NotificationChannel-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Strategy interface for delivering notifications through a specific channel.
 * Pattern: Strategy
 */
public interface NotificationChannel {
    /**
     * Attempts to send the notification.
     *
     * @param notification the notification to send
     * @return true if sent successfully, false on failure
     */
    boolean send(Notification notification);

    /** Returns the channel type this implementation handles. */
    NotificationType getType();
}</code></pre>
</div>
<div class="tab-content" id="NotificationObserver-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Observer interface for notification lifecycle events.
 * Pattern: Observer
 */
public interface NotificationObserver {
    /** Called when a notification is successfully sent. */
    void onNotificationSent(Notification notification);

    /** Called when all delivery attempts for a notification have failed. */
    void onNotificationFailed(Notification notification);
}</code></pre>
</div>
<div class="tab-content" id="NotificationPriority-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/** Priority of a notification, affecting routing and retry behaviour. */
public enum NotificationPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}</code></pre>
</div>
<div class="tab-content" id="NotificationService-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Orchestrates notification delivery with routing, retry, and observer notifications.
 * Patterns: Strategy (routing), Observer, Factory (channels)
 */
public class NotificationService {

    private static final int DEFAULT_MAX_RETRIES = 3;

    private final List&lt;NotificationChannel&gt; channels;
    private final RoutingStrategy routingStrategy;
    private final List&lt;NotificationObserver&gt; observers = new ArrayList&lt;&gt;();
    private final int maxRetries;

    // ---- Stats ----
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    public NotificationService(RoutingStrategy routingStrategy) {
        this(routingStrategy, DEFAULT_MAX_RETRIES);
    }

    public NotificationService(RoutingStrategy routingStrategy, int maxRetries) {
        this.routingStrategy = routingStrategy;
        this.maxRetries = maxRetries;
        // Register all channel types by default
        this.channels = Arrays.asList(
                ChannelFactory.createChannel(NotificationType.EMAIL),
                ChannelFactory.createChannel(NotificationType.SMS),
                ChannelFactory.createChannel(NotificationType.PUSH),
                ChannelFactory.createChannel(NotificationType.IN_APP),
                ChannelFactory.createChannel(NotificationType.SLACK)
        );
    }

    /** Registers an observer to receive delivery lifecycle events. */
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /** Removes a previously registered observer. */
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Sends the notification to the user via channels selected by the routing strategy.
     * Retries up to maxRetries times on failure before marking FAILED.
     *
     * @param notification notification to send
     * @param user         recipient
     * @return true if at least one channel succeeded
     */
    public boolean send(Notification notification, User user) {
        List&lt;NotificationChannel&gt; selected = routingStrategy.route(notification, channels);

        if (selected.isEmpty()) {
            System.out.printf(&quot;[NotificationService] No channels selected for %s%n&quot;, notification.getNotificationId());
            markFailed(notification);
            return false;
        }

        boolean atLeastOneSuccess = false;

        for (NotificationChannel channel : selected) {
            boolean sent = sendWithRetry(notification, channel);
            if (sent) atLeastOneSuccess = true;
        }

        if (atLeastOneSuccess) {
            notification.setStatus(NotificationStatus.SENT);
            totalSent.incrementAndGet();
            notifyObserversSent(notification);
        } else {
            markFailed(notification);
        }

        return atLeastOneSuccess;
    }

    // ---- Private helpers ----

    private boolean sendWithRetry(Notification notification, NotificationChannel channel) {
        for (int attempt = 1; attempt &lt;= maxRetries; attempt++) {
            try {
                boolean success = channel.send(notification);
                if (success) return true;
            } catch (Exception e) {
                System.out.printf(&quot;[NotificationService] Channel %s threw exception on attempt %d: %s%n&quot;,
                        channel.getType(), attempt, e.getMessage());
            }
            System.out.printf(&quot;[NotificationService] Retry %d/%d for channel %s%n&quot;,
                    attempt, maxRetries, channel.getType());
        }
        System.out.printf(&quot;[NotificationService] All retries exhausted for channel %s%n&quot;, channel.getType());
        return false;
    }

    private void markFailed(Notification notification) {
        notification.setStatus(NotificationStatus.FAILED);
        totalFailed.incrementAndGet();
        notifyObserversFailed(notification);
    }

    private void notifyObserversSent(Notification notification) {
        observers.forEach(o -&gt; o.onNotificationSent(notification));
    }

    private void notifyObserversFailed(Notification notification) {
        observers.forEach(o -&gt; o.onNotificationFailed(notification));
    }

    // ---- Stats ----

    public long getTotalSent() { return totalSent.get(); }
    public long getTotalFailed() { return totalFailed.get(); }

    public List&lt;NotificationChannel&gt; getChannels() {
        return Collections.unmodifiableList(channels);
    }
}</code></pre>
</div>
<div class="tab-content" id="NotificationStatus-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/** Lifecycle status of a notification. */
public enum NotificationStatus {
    PENDING,
    SENT,
    DELIVERED,
    FAILED,
    READ
}</code></pre>
</div>
<div class="tab-content" id="NotificationType-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/** Supported notification delivery channels. */
public enum NotificationType {
    EMAIL,
    SMS,
    PUSH,
    IN_APP,
    SLACK
}</code></pre>
</div>
<div class="tab-content" id="PreferenceRoutingStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Routes based on the User&#x27;s channel preference map.
 * A channel is selected only if the user has enabled it (preference value = true).
 * Falls back to all channels if the user object is null or preferences are empty.
 */
public class PreferenceRoutingStrategy implements RoutingStrategy {

    private final User user;

    public PreferenceRoutingStrategy(User user) {
        this.user = user;
    }

    @Override
    public List&lt;NotificationChannel&gt; route(Notification notification,
                                           List&lt;NotificationChannel&gt; allChannels) {
        if (user == null || user.getPreferences().isEmpty()) {
            return allChannels;
        }
        return allChannels.stream()
                .filter(c -&gt; Boolean.TRUE.equals(user.getPreferences().get(c.getType())))
                .collect(Collectors.toList());
    }
}</code></pre>
</div>
<div class="tab-content" id="PriorityRoutingStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Routes notifications based on priority:
 * - URGENT  → all channels
 * - HIGH    → PUSH + EMAIL
 * - NORMAL  → EMAIL only
 * - LOW     → IN_APP only
 */
public class PriorityRoutingStrategy implements RoutingStrategy {

    @Override
    public List&lt;NotificationChannel&gt; route(Notification notification,
                                           List&lt;NotificationChannel&gt; allChannels) {
        Set&lt;NotificationType&gt; allowed = resolveTypes(notification.getPriority());
        return allChannels.stream()
                .filter(c -&gt; allowed.contains(c.getType()))
                .collect(Collectors.toList());
    }

    private Set&lt;NotificationType&gt; resolveTypes(NotificationPriority priority) {
        switch (priority) {
            case URGENT:
                return EnumSet.allOf(NotificationType.class);
            case HIGH:
                return EnumSet.of(NotificationType.PUSH, NotificationType.EMAIL);
            case NORMAL:
                return EnumSet.of(NotificationType.EMAIL);
            case LOW:
            default:
                return EnumSet.of(NotificationType.IN_APP);
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="PushChannel-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Simulated mobile push notification channel.
 */
public class PushChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf(&quot;[PUSH] Pushing to user=%s | subject=&#x27;%s&#x27;%n&quot;,
                notification.getUserId(), notification.getSubject());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.PUSH; }
}</code></pre>
</div>
<div class="tab-content" id="RoutingStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

import java.util.List;

/**
 * Strategy interface that selects which channels to use for a notification.
 * Pattern: Strategy
 */
public interface RoutingStrategy {
    /**
     * Returns the subset of available channels that should be used to deliver
     * the given notification.
     *
     * @param notification  the notification to route
     * @param allChannels   all registered channels
     * @return list of channels to send through (may be empty)
     */
    List&lt;NotificationChannel&gt; route(Notification notification, List&lt;NotificationChannel&gt; allChannels);
}</code></pre>
</div>
<div class="tab-content" id="SMSChannel-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Simulated SMS delivery channel.
 */
public class SMSChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf(&quot;[SMS] Sending to user=%s | body=&#x27;%s&#x27;%n&quot;,
                notification.getUserId(), notification.getBody());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.SMS; }
}</code></pre>
</div>
<div class="tab-content" id="SlackChannel-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

/**
 * Simulated Slack message channel.
 */
public class SlackChannel implements NotificationChannel {

    @Override
    public boolean send(Notification notification) {
        System.out.printf(&quot;[SLACK] Posting to user=%s | body=&#x27;%s&#x27;%n&quot;,
                notification.getUserId(), notification.getBody());
        return true;
    }

    @Override
    public NotificationType getType() { return NotificationType.SLACK; }
}</code></pre>
</div>
<div class="tab-content" id="User-java">
<pre><code class="language-java">package org.interview.system_design.lld.notification;

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
    private final Map&lt;NotificationType, Boolean&gt; preferences;

    public User(String userId,
                String name,
                String email,
                String phone,
                String deviceToken,
                Map&lt;NotificationType, Boolean&gt; preferences) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceToken = deviceToken;
        // Defensive copy using EnumMap for type safety
        Map&lt;NotificationType, Boolean&gt; prefs = new EnumMap&lt;&gt;(NotificationType.class);
        if (preferences != null) prefs.putAll(preferences);
        this.preferences = Collections.unmodifiableMap(prefs);
    }

    /** Convenience constructor: enables all channels by default. */
    public User(String userId, String name, String email) {
        this(userId, name, email, null, null, defaultPreferences());
    }

    private static Map&lt;NotificationType, Boolean&gt; defaultPreferences() {
        Map&lt;NotificationType, Boolean&gt; prefs = new EnumMap&lt;&gt;(NotificationType.class);
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
    public Map&lt;NotificationType, Boolean&gt; getPreferences() { return preferences; }

    @Override
    public String toString() {
        return String.format(&quot;User[%s, %s, %s]&quot;, userId, name, email);
    }
}</code></pre>
</div>
</div>
