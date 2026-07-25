package org.interview.design_patterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * Design Pattern: Mediator (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Define an object that encapsulates how a set of objects interact.
 *   Mediator promotes loose coupling by keeping objects from referring
 *   to each other explicitly, letting you vary their interaction independently.
 *
 * PROBLEM IT SOLVES:
 *   - Many objects communicate with many others → spaghetti dependencies.
 *   - Adding a new object forces you to update all its collaborators.
 *   - You want to centralize complex coordination logic.
 *
 * STRUCTURE:
 *   - Mediator (interface): declares methods for colleagues to communicate
 *   - ConcreteMediator: implements coordination logic; knows all colleagues
 *   - Colleague: knows only the Mediator, not other colleagues
 *
 * DIFFERENCE FROM OBSERVER:
 *   - Observer: one-to-many broadcast; subject doesn't know who is listening
 *   - Mediator: many-to-many via a central hub; mediator orchestrates all interactions
 *
 * REAL-WORLD EXAMPLES:
 *   - Air traffic control (pilots ↔ ATC ↔ pilots)
 *   - Chat room (users ↔ chat server ↔ users)
 *   - GUI: form components communicate through a dialog mediator
 *   - Spring ApplicationContext event bus
 *   - Message broker (Kafka, RabbitMQ)
 *
 * PROS:
 *   + Reduces coupling between components
 *   + Centralizes communication logic
 *   + Easier to change component interactions
 *
 * CONS:
 *   - Mediator can become a "God object" if it grows too complex
 *
 * SCENARIO:
 *   Chat room: users send messages to the mediator (chat room),
 *   which broadcasts them to other users.
 */
public class MediatorPattern {

    // ================================================================
    // Mediator Interface
    // ================================================================
    interface ChatMediator {
        void sendMessage(String message, User sender);
        void addUser(User user);
    }

    // ================================================================
    // Colleague: User
    // Knows only the Mediator — never directly references other users
    // ================================================================
    static abstract class User {
        protected final ChatMediator mediator;
        protected final String       name;

        User(ChatMediator mediator, String name) {
            this.mediator = mediator;
            this.name     = name;
        }

        /** Send a message through the mediator */
        public void send(String message) {
            System.out.printf("[%s] → Sending: %s%n", name, message);
            mediator.sendMessage(message, this);
        }

        /** Receive a message (called by the mediator) */
        public abstract void receive(String message, String fromUser);

        public String getName() { return name; }
    }

    // ================================================================
    // ConcreteColleague: BasicUser (receives all messages)
    // ================================================================
    static class BasicUser extends User {
        BasicUser(ChatMediator mediator, String name) {
            super(mediator, name);
        }

        @Override
        public void receive(String message, String fromUser) {
            System.out.printf("  [%s received from %s]: %s%n", name, fromUser, message);
        }
    }

    // ================================================================
    // ConcreteColleague: AdminUser (receives + can broadcast alerts)
    // ================================================================
    static class AdminUser extends User {
        AdminUser(ChatMediator mediator, String name) {
            super(mediator, name);
        }

        @Override
        public void receive(String message, String fromUser) {
            System.out.printf("  [ADMIN %s received from %s]: %s%n", name, fromUser, message);
        }

        /** Special method: broadcast a system alert to all users */
        public void broadcastAlert(String alert) {
            String alertMsg = "🚨 SYSTEM ALERT: " + alert;
            System.out.printf("[%s (Admin)] Broadcasting: %s%n", name, alertMsg);
            mediator.sendMessage(alertMsg, this);
        }
    }

    // ================================================================
    // ConcreteMediator: ChatRoom
    // Central hub: knows all users; routes messages between them
    // ================================================================
    static class ChatRoom implements ChatMediator {
        private final List<User> users = new ArrayList<>();
        private final String     name;

        ChatRoom(String name) {
            this.name = name;
            System.out.println("[ChatRoom] '" + name + "' created.");
        }

        @Override
        public void addUser(User user) {
            users.add(user);
            System.out.println("[ChatRoom] " + user.getName() + " joined '" + name + "'");
        }

        /**
         * Route message from sender to all OTHER users.
         * This is the core mediation logic — centralized here, not in each User.
         */
        @Override
        public void sendMessage(String message, User sender) {
            for (User user : users) {
                // Don't send the message back to the sender
                if (!user.getName().equals(sender.getName())) {
                    user.receive(message, sender.getName());
                }
            }
        }
    }

    // ================================================================
    // BONUS: Air Traffic Control Mediator
    // Planes don't communicate with each other — only with ATC
    // ================================================================
    interface ATCMediator {
        void requestLanding(String aircraft, String runway);
        void reportPosition(String aircraft, int altitude, int heading);
    }

    static class AirTrafficControl implements ATCMediator {
        private final java.util.Set<String> occupiedRunways = new java.util.HashSet<>();

        @Override
        public void requestLanding(String aircraft, String runway) {
            if (occupiedRunways.contains(runway)) {
                System.out.printf("  [ATC → %s] Runway %s occupied. Hold pattern at 3000ft.%n",
                    aircraft, runway);
            } else {
                occupiedRunways.add(runway);
                System.out.printf("  [ATC → %s] Cleared to land on runway %s. Wind 270@10kt.%n",
                    aircraft, runway);
                // Simulate: runway freed after landing
                new Thread(() -> {
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    occupiedRunways.remove(runway);
                    System.out.printf("  [ATC] Runway %s now clear.%n", runway);
                }).start();
            }
        }

        @Override
        public void reportPosition(String aircraft, int altitude, int heading) {
            System.out.printf("  [ATC] Traffic: %s at %,dft heading %d°%n",
                aircraft, altitude, heading);
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Chat Room Mediator ===");
        ChatRoom room = new ChatRoom("General");

        User alice = new BasicUser(room, "Alice");
        User bob   = new BasicUser(room, "Bob");
        User carol = new BasicUser(room, "Carol");
        AdminUser admin = new AdminUser(room, "Admin");

        room.addUser(alice);
        room.addUser(bob);
        room.addUser(carol);
        room.addUser(admin);

        System.out.println();
        alice.send("Hey everyone!");
        System.out.println();
        bob.send("Hi Alice!");
        System.out.println();
        admin.broadcastAlert("Server maintenance in 30 minutes!");

        System.out.println("\n=== Air Traffic Control Mediator ===");
        AirTrafficControl atc = new AirTrafficControl();

        // Planes communicate ONLY with ATC, not with each other
        atc.reportPosition("BA-123", 10_000, 270);
        atc.reportPosition("UA-456",  5_000, 180);

        atc.requestLanding("BA-123", "27L");   // granted
        atc.requestLanding("UA-456", "27L");   // held (runway busy)
        atc.requestLanding("UA-456", "09R");   // different runway — granted

        Thread.sleep(200); // wait for runway to free
        atc.requestLanding("QA-789", "27L");   // now free again
    }
}
