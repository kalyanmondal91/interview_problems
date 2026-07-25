---
layout: design_pattern
title: "Observer"
category: "behavioral"
difficulty: "Medium"
also_known_as: "Publish-Subscribe"
intent: "Define a one-to-many dependency so that when one object changes state, all dependents are notified and updated automatically."
tags: [behavioral, observer, events]
render_with_liquid: false
---

## Intent

Define a one-to-many dependency so that when one object changes state, all dependents are notified and updated automatically.

## Problem It Solves

See the full Javadoc header and inline comments in the source code below.

## Structure

| Participant | Role |
|-------------|------|
| **Subject (Observable)** | Maintains list of observers; notifies on state change |
| **Observer** | Declares the update/notification method |
| **ConcreteSubject** | Stores state; triggers notification on change |
| **ConcreteObserver** | Implements update; reacts to subject state change |

## Pros

- Open/Closed: add observers without changing subject
- Loose coupling
- Supports broadcast communication

## Cons

- Unexpected updates and ordering issues
- Memory leaks if observers are not unregistered
- Cascade of updates can be hard to debug

## Real-World Examples

- `java.util.EventListener (Swing)`
- `Spring ApplicationEvent`
- `Kafka/RabbitMQ topic subscriptions`
- `Stock market price feeds`

## Variants

- Push model
- Pull model
- Event bus / message bus

## Full Java Implementation

```java
package org.interview.design_patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * Design Pattern: Observer (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Define a one-to-many dependency between objects so that when
 *   one object changes state, all its dependents are notified
 *   and updated automatically.
 *
 * ALSO KNOWN AS: Publish-Subscribe, Event Listener
 *
 * PROBLEM IT SOLVES:
 *   - Object A needs to notify multiple objects B, C, D when it changes,
 *     without knowing who they are or how many there are.
 *   - You want loose coupling between the notifier and its dependents.
 *
 * STRUCTURE:
 *   - Subject (Observable): maintains list of observers; notifies on state change
 *   - Observer: defines the update interface
 *   - ConcreteSubject: stores state; triggers notification on change
 *   - ConcreteObserver: implements update; reacts to subject's state change
 *
 * REAL-WORLD EXAMPLES:
 *   - java.util.EventListener (Swing)
 *   - java.util.Observer (deprecated in Java 9, but classic example)
 *   - Spring ApplicationEvent / ApplicationListener
 *   - Kafka/RabbitMQ topic subscriptions
 *   - Stock market price feeds
 *   - GUI: model updates → multiple view components refresh
 *
 * PROS:
 *   + Open/Closed: add observers without changing subject
 *   + Loose coupling: subject doesn't know observer types
 *   + Supports broadcast communication
 *
 * CONS:
 *   - Unexpected updates and ordering issues
 *   - Memory leaks if observers are not unregistered (lapsed listener problem)
 *   - Cascade of updates can be hard to debug
 *
 * SCENARIO:
 *   Stock market: StockMarket (subject) has many price subscribers.
 *   When Apple's price changes, Dashboard, MobileApp, and EmailAlert
 *   are all notified automatically.
 */
public class ObserverPattern {

    // ================================================================
    // Observer Interface
    // ================================================================
    interface StockObserver {
        /**
         * Called by the subject when stock prices change.
         * @param stockSymbol e.g., "AAPL"
         * @param newPrice    new stock price
         */
        void onPriceChange(String stockSymbol, double newPrice);
    }

    // ================================================================
    // Subject Interface
    // ================================================================
    interface StockMarketSubject {
        void subscribe(StockObserver observer);
        void unsubscribe(StockObserver observer);
        void notifyObservers(String stockSymbol, double newPrice);
    }

    // ================================================================
    // ConcreteSubject: StockMarket
    // Maintains price state; notifies all observers on changes
    // ================================================================
    static class StockMarket implements StockMarketSubject {
        // List of registered observers (loose coupling: we don't know their types)
        private final List<StockObserver> observers = new ArrayList<>();

        @Override
        public void subscribe(StockObserver observer) {
            observers.add(observer);
            System.out.println("[Market] New subscriber: " + observer.getClass().getSimpleName());
        }

        @Override
        public void unsubscribe(StockObserver observer) {
            observers.remove(observer);
            System.out.println("[Market] Unsubscribed: " + observer.getClass().getSimpleName());
        }

        /**
         * Called when a stock price changes.
         * Fans out to ALL registered observers.
         */
        @Override
        public void notifyObservers(String stockSymbol, double newPrice) {
            System.out.printf("%n[Market] %s price changed to $%.2f — notifying %d observers%n",
                stockSymbol, newPrice, observers.size());
            for (StockObserver observer : observers) {
                observer.onPriceChange(stockSymbol, newPrice); // each observer reacts differently
            }
        }

        /** Called by trading engine when price updates come in */
        public void priceUpdated(String stockSymbol, double newPrice) {
            notifyObservers(stockSymbol, newPrice);
        }
    }

    // ================================================================
    // ConcreteObserver 1: Dashboard UI
    // Displays current prices in a web dashboard
    // ================================================================
    static class DashboardObserver implements StockObserver {
        @Override
        public void onPriceChange(String stockSymbol, double newPrice) {
            System.out.printf("  [Dashboard] Refreshing chart for %s → $%.2f%n",
                stockSymbol, newPrice);
        }
    }

    // ================================================================
    // ConcreteObserver 2: Mobile App Notification
    // Sends push notification to mobile users
    // ================================================================
    static class MobileAppObserver implements StockObserver {
        private final double alertThreshold;

        MobileAppObserver(double alertThreshold) {
            this.alertThreshold = alertThreshold;
        }

        @Override
        public void onPriceChange(String stockSymbol, double newPrice) {
            if (newPrice >= alertThreshold) {
                System.out.printf("  [MobileApp] 🔔 Push: %s hit $%.2f (threshold: $%.2f)!%n",
                    stockSymbol, newPrice, alertThreshold);
            } else {
                System.out.printf("  [MobileApp] %s at $%.2f — below alert threshold%n",
                    stockSymbol, newPrice);
            }
        }
    }

    // ================================================================
    // ConcreteObserver 3: Email Alert
    // Sends email when price crosses certain threshold
    // ================================================================
    static class EmailAlertObserver implements StockObserver {
        private final String email;
        private double lastKnownPrice = 0;

        EmailAlertObserver(String email) {
            this.email = email;
        }

        @Override
        public void onPriceChange(String stockSymbol, double newPrice) {
            if (lastKnownPrice > 0) {
                double change = ((newPrice - lastKnownPrice) / lastKnownPrice) * 100;
                if (Math.abs(change) > 2.0) { // alert on >2% change
                    System.out.printf("  [Email → %s] %s changed by %.1f%% (from $%.2f to $%.2f)%n",
                        email, stockSymbol, change, lastKnownPrice, newPrice);
                }
            }
            lastKnownPrice = newPrice;
        }
    }

    // ================================================================
    // ConcreteObserver 4: Trading Bot
    // Automatically places orders based on price movements
    // ================================================================
    static class TradingBotObserver implements StockObserver {
        private final double buyBelow;
        private final double sellAbove;

        TradingBotObserver(double buyBelow, double sellAbove) {
            this.buyBelow  = buyBelow;
            this.sellAbove = sellAbove;
        }

        @Override
        public void onPriceChange(String stockSymbol, double newPrice) {
            if (newPrice < buyBelow) {
                System.out.printf("  [TradingBot] 🟢 BUY ORDER: %s @ $%.2f (below $%.2f)%n",
                    stockSymbol, newPrice, buyBelow);
            } else if (newPrice > sellAbove) {
                System.out.printf("  [TradingBot] 🔴 SELL ORDER: %s @ $%.2f (above $%.2f)%n",
                    stockSymbol, newPrice, sellAbove);
            } else {
                System.out.printf("  [TradingBot] %s @ $%.2f — HOLD%n", stockSymbol, newPrice);
            }
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        StockMarket market = new StockMarket();

        // Register observers
        StockObserver dashboard = new DashboardObserver();
        StockObserver mobile    = new MobileAppObserver(185.0);    // alert above $185
        StockObserver email     = new EmailAlertObserver("investor@example.com");
        StockObserver bot       = new TradingBotObserver(175.0, 190.0); // buy <$175, sell >$190

        market.subscribe(dashboard);
        market.subscribe(mobile);
        market.subscribe(email);
        market.subscribe(bot);

        // Simulate price changes — all observers auto-notified
        market.priceUpdated("AAPL", 178.50);
        market.priceUpdated("AAPL", 182.00);
        market.priceUpdated("AAPL", 186.30); // crosses mobile alert threshold
        market.priceUpdated("AAPL", 191.00); // bot should sell

        // Unsubscribe the dashboard
        System.out.println("\n--- Unsubscribing Dashboard ---");
        market.unsubscribe(dashboard);
        market.priceUpdated("AAPL", 174.50); // bot should buy; dashboard no longer notified
    }
}

```
