---
layout: design_pattern
title: "Decorator"
category: "structural"
difficulty: "Medium"
also_known_as: "Wrapper"
intent: "Attach additional responsibilities to an object dynamically as a flexible alternative to subclassing."
tags: [structural, decorator]
render_with_liquid: false
---

## Intent

Attach additional responsibilities to an object dynamically as a flexible alternative to subclassing.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Component** | Defines the base interface |
| **ConcreteComponent** | The core implementation |
| **Decorator (abstract)** | Implements Component AND holds a Component reference |
| **ConcreteDecorator** | Adds behavior before/after delegating |

## Pros

- Extend behavior without modifying existing code
- Combine behaviors by stacking decorators
- Adheres to Single Responsibility and Open/Closed

## Cons

- Stack ordering matters; bugs can be hard to trace
- Many small wrapper objects can be confusing

## Real-World Examples

- `java.io.BufferedInputStream`
- `Spring Security filter chain`
- `HTTP middleware`
- `Coffee ordering systems`

## Full Java Implementation

```java
package org.interview.design_patterns.structural.decorator;

/**
 * ============================================================
 * Design Pattern: Decorator (Structural)
 * ============================================================
 *
 * INTENT:
 *   Attach additional responsibilities to an object DYNAMICALLY.
 *   Decorators provide a flexible alternative to subclassing
 *   for extending functionality.
 *
 * ALSO KNOWN AS: Wrapper
 *
 * PROBLEM IT SOLVES:
 *   - Subclassing for every combination of features causes class explosion.
 *   - You need to add behavior to individual objects, not entire classes.
 *   - Behavior should be composable and stackable at runtime.
 *
 * STRUCTURE:
 *   - Component (interface): defines the base interface
 *   - ConcreteComponent: the core implementation (the "real" object)
 *   - Decorator (abstract): implements Component AND holds a Component reference
 *   - ConcreteDecorator: adds behavior before/after delegating to wrapped component
 *
 * KEY INSIGHT:
 *   Each decorator wraps another component. Calling the top-level decorator
 *   triggers a chain of calls: Decorator3 → Decorator2 → Decorator1 → Core.
 *
 * DIFFERENCE FROM PROXY:
 *   - Decorator: focuses on adding features; often stacked
 *   - Proxy: focuses on controlling access; usually single layer
 *
 * REAL-WORLD EXAMPLES:
 *   - java.io: BufferedInputStream(new GZIPInputStream(new FileInputStream(f)))
 *   - Spring Security filter chain
 *   - HTTP middleware (logging, auth, compression)
 *   - Coffee ordering systems
 *
 * PROS:
 *   + Extend behavior without modifying existing code
 *   + Combine multiple behaviors by stacking decorators
 *   + Adheres to Single Responsibility and Open/Closed principles
 *
 * CONS:
 *   - Stack ordering matters; bugs can be hard to trace
 *   - Many small wrapper objects can be confusing
 *
 * SCENARIO:
 *   A text message pipeline. Core sends a plain message.
 *   Decorators add: encryption, compression, base64 encoding.
 *   Each can be independently toggled and stacked in any order.
 */
public class DecoratorPattern {

    // ================================================================
    // Component — the interface all message handlers implement
    // ================================================================
    interface MessageSender {
        void send(String message);
    }

    // ================================================================
    // ConcreteComponent — core implementation (no extra behavior)
    // ================================================================
    static class PlainMessageSender implements MessageSender {
        private final String destination;

        PlainMessageSender(String destination) {
            this.destination = destination;
        }

        @Override
        public void send(String message) {
            System.out.println("[Send → " + destination + "] \" + message + \"");
        }
    }

    // ================================================================
    // Base Decorator (abstract)
    // Holds a reference to the wrapped component.
    // Subclasses can call wrappee.send() and add behavior around it.
    // ================================================================
    static abstract class MessageDecorator implements MessageSender {
        // The wrapped component — could be core OR another decorator
        protected final MessageSender wrappee;

        MessageDecorator(MessageSender wrappee) {
            this.wrappee = wrappee;
        }

        // Default: just delegate (subclasses override to add behavior)
        @Override
        public void send(String message) {
            wrappee.send(message);
        }
    }

    // ================================================================
    // ConcreteDecorator 1: Logging
    // Adds logging before and after the send operation
    // ================================================================
    static class LoggingDecorator extends MessageDecorator {
        LoggingDecorator(MessageSender wrappee) {
            super(wrappee);
        }

        @Override
        public void send(String message) {
            System.out.println("[LOG] Sending message (length=" + message.length() + ")");
            wrappee.send(message);       // delegate to next in chain
            System.out.println("[LOG] Message sent successfully");
        }
    }

    // ================================================================
    // ConcreteDecorator 2: Compression
    // Simulates compressing the message before passing it along
    // ================================================================
    static class CompressionDecorator extends MessageDecorator {
        CompressionDecorator(MessageSender wrappee) {
            super(wrappee);
        }

        @Override
        public void send(String message) {
            String compressed = compress(message);
            System.out.println("[Compress] " + message.length() + " chars → " + compressed.length() + " chars");
            wrappee.send(compressed);    // pass compressed message down the chain
        }

        private String compress(String message) {
            // Simplified simulation: reverse + abbreviate (real impl would use GZIP)
            return "COMPRESSED[" + message.substring(0, Math.min(message.length(), 10)) + "...]";
        }
    }

    // ================================================================
    // ConcreteDecorator 3: Encryption
    // Simulates encrypting the message before passing it along
    // ================================================================
    static class EncryptionDecorator extends MessageDecorator {
        EncryptionDecorator(MessageSender wrappee) {
            super(wrappee);
        }

        @Override
        public void send(String message) {
            String encrypted = encrypt(message);
            System.out.println("[Encrypt] Message encrypted");
            wrappee.send(encrypted);     // pass encrypted message down the chain
        }

        private String encrypt(String message) {
            // Simplified Caesar cipher simulation (real impl would use AES)
            StringBuilder sb = new StringBuilder();
            for (char c : message.toCharArray()) {
                sb.append((char)(c + 1));  // shift each char by 1
            }
            return "ENC{" + sb + "}";
        }
    }

    // ================================================================
    // ConcreteDecorator 4: Rate Limiting
    // Throttles sends; rejects if too frequent
    // ================================================================
    static class RateLimitDecorator extends MessageDecorator {
        private long lastSendTime = 0;
        private final long minIntervalMs;

        RateLimitDecorator(MessageSender wrappee, long minIntervalMs) {
            super(wrappee);
            this.minIntervalMs = minIntervalMs;
        }

        @Override
        public void send(String message) {
            long now = System.currentTimeMillis();
            if (now - lastSendTime < minIntervalMs) {
                System.out.println("[RateLimit] Rejected: too many requests. Try again later.");
                return;
            }
            lastSendTime = now;
            System.out.println("[RateLimit] Request allowed");
            wrappee.send(message);
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Plain sender (no decorators) ===");
        MessageSender plain = new PlainMessageSender("user@example.com");
        plain.send("Hello, World!");

        System.out.println("\n=== Sender with Logging decorator ===");
        MessageSender logged = new LoggingDecorator(
            new PlainMessageSender("user@example.com")
        );
        logged.send("Hello, World!");

        System.out.println("\n=== Sender with Encryption + Logging (stacked) ===");
        // Chain: EncryptionDecorator → LoggingDecorator → PlainSender
        // Call order: Encrypt first, then Log, then Send
        MessageSender encryptedLogged = new EncryptionDecorator(
            new LoggingDecorator(
                new PlainMessageSender("secure@example.com")
            )
        );
        encryptedLogged.send("Secret message");

        System.out.println("\n=== Full pipeline: RateLimit → Encrypt → Compress → Log → Send ===");
        MessageSender fullPipeline =
            new RateLimitDecorator(
                new EncryptionDecorator(
                    new CompressionDecorator(
                        new LoggingDecorator(
                            new PlainMessageSender("receiver@example.com")
                        )
                    )
                ),
                100  // 100ms minimum between sends
            );

        fullPipeline.send("Important data payload");

        System.out.println("\n--- Sending again immediately (should be rate-limited) ---");
        fullPipeline.send("Another message"); // should be rejected
    }
}

```
