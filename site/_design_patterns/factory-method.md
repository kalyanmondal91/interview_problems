---
layout: design_pattern
title: "Factory Method"
category: "creational"
difficulty: "Medium"
also_known_as: "Virtual Constructor"
intent: "Define an interface for creating an object, but let subclasses decide which class to instantiate."
tags: [creational, factory]
render_with_liquid: false
---

## Intent

Define an interface for creating an object, but let subclasses decide which class to instantiate.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Product (interface)** | Defines objects the factory creates |
| **ConcreteProduct** | Implements Product |
| **Creator (abstract)** | Declares the factory method |
| **ConcreteCreator** | Overrides factory method |

## Pros

- Avoids tight coupling between creator and product
- Single Responsibility
- Open/Closed: add new products without changing existing code

## Cons

- Code may become complex due to subclassing
- Requires a separate class per product

## Real-World Examples

- `Calendar.getInstance()`
- `DocumentBuilderFactory.newInstance()`
- `SLF4J LoggerFactory`
- `Spring BeanFactory`

## Variants

- Simple Factory (static helper, not GoF)
- Parameterized Factory Method

## Full Java Implementation

```java
package org.interview.design_patterns.creational.factory_method;

/**
 * ============================================================
 * Design Pattern: Factory Method (Creational)
 * ============================================================
 *
 * INTENT:
 *   Define an interface for creating an object, but let subclasses
 *   decide which class to instantiate. Factory Method lets a class
 *   defer instantiation to subclasses.
 *
 * PROBLEM IT SOLVES:
 *   When a framework/library needs to create objects but doesn't know
 *   exactly what subclass to instantiate — it delegates that decision
 *   to the client subclass.
 *
 * STRUCTURE:
 *   - Product (interface/abstract): defines the interface of objects the factory creates
 *   - ConcreteProduct: implements the Product interface
 *   - Creator (abstract class): declares the factory method; may also define a default implementation
 *   - ConcreteCreator: overrides the factory method to return a ConcreteProduct
 *
 * REAL-WORLD EXAMPLES:
 *   - java.util.Calendar#getInstance()
 *   - javax.xml.parsers.DocumentBuilderFactory#newInstance()
 *   - Loggers (SLF4J LoggerFactory)
 *   - Spring BeanFactory
 *
 * PROS:
 *   + Avoids tight coupling between creator and product
 *   + Single Responsibility: product creation in one place
 *   + Open/Closed: add new products without changing existing code
 *
 * CONS:
 *   - Code may become complex due to subclassing
 *
 * Time Complexity:  O(1) per creation
 * Space Complexity: O(1)
 *
 * SCENARIO:
 *   A notification system that sends notifications via different channels
 *   (Email, SMS, Push). Each channel is a product; the factory decides
 *   which one to create.
 */
public class FactoryMethodPattern {

    // ----------------------------------------------------------------
    // Product Interface
    // All concrete products must implement this contract.
    // ----------------------------------------------------------------
    interface Notification {
        void send(String message);
    }

    // ----------------------------------------------------------------
    // Concrete Products
    // ----------------------------------------------------------------
    static class EmailNotification implements Notification {
        private final String email;

        EmailNotification(String email) {
            this.email = email;
        }

        @Override
        public void send(String message) {
            // In production: call SMTP/SES/Sendgrid API
            System.out.println("[Email → " + email + "] " + message);
        }
    }

    static class SmsNotification implements Notification {
        private final String phoneNumber;

        SmsNotification(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public void send(String message) {
            // In production: call Twilio/SNS API
            System.out.println("[SMS → " + phoneNumber + "] " + message);
        }
    }

    static class PushNotification implements Notification {
        private final String deviceToken;

        PushNotification(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        @Override
        public void send(String message) {
            // In production: call FCM/APNs API
            System.out.println("[Push → " + deviceToken + "] " + message);
        }
    }

    // ----------------------------------------------------------------
    // Creator (Abstract)
    // Declares the factory method. The 'notifyUser' method uses the
    // product — it doesn't know which concrete product will be used.
    // ----------------------------------------------------------------
    static abstract class NotificationCreator {
        // THE factory method — subclasses override this
        public abstract Notification createNotification(String target);

        // Template method: uses the product created by factory method
        public void notifyUser(String target, String message) {
            Notification n = createNotification(target);
            n.send(message);
        }
    }

    // ----------------------------------------------------------------
    // Concrete Creators
    // Each subclass decides which product class to instantiate.
    // ----------------------------------------------------------------
    static class EmailNotificationCreator extends NotificationCreator {
        @Override
        public Notification createNotification(String target) {
            return new EmailNotification(target);  // target = email address
        }
    }

    static class SmsNotificationCreator extends NotificationCreator {
        @Override
        public Notification createNotification(String target) {
            return new SmsNotification(target);    // target = phone number
        }
    }

    static class PushNotificationCreator extends NotificationCreator {
        @Override
        public Notification createNotification(String target) {
            return new PushNotification(target);   // target = device token
        }
    }

    // ----------------------------------------------------------------
    // Simple Factory Helper (bonus — NOT GoF, but commonly used)
    // Centralizes creation logic with a static method.
    // ----------------------------------------------------------------
    static class NotificationFactory {
        public static Notification create(String type, String target) {
            return switch (type.toLowerCase()) {
                case "email" -> new EmailNotification(target);
                case "sms"   -> new SmsNotification(target);
                case "push"  -> new PushNotification(target);
                default      -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        }
    }

    // ----------------------------------------------------------------
    // Demo
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        // GoF Factory Method: client works with creator abstraction
        NotificationCreator emailCreator = new EmailNotificationCreator();
        emailCreator.notifyUser("user@example.com", "Your order has shipped!");

        NotificationCreator smsCreator = new SmsNotificationCreator();
        smsCreator.notifyUser("+14155552671", "Your OTP is 482910");

        NotificationCreator pushCreator = new PushNotificationCreator();
        pushCreator.notifyUser("device-token-xyz", "You have a new message");

        System.out.println("--- Simple Factory ---");
        // Simple Factory: single static method dispatches on type string
        Notification n = NotificationFactory.create("email", "admin@co.com");
        n.send("Server is down!");
    }
}

```
