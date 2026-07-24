# 🏗️ Low-Level Design (LLD) — 20 Problems

> **Goal:** Demonstrate clean OOP architecture, SOLID principles, design pattern fluency,
> extensibility, and concurrency awareness in every solution.

---

## 📐 Design Pattern Quick Reference

| Pattern | Category | When to Use |
|---------|----------|-------------|
| **Strategy** | Behavioural | Swap algorithms at runtime without changing the caller |
| **Factory / Factory Method** | Creational | Decouple object creation from usage |
| **Singleton** | Creational | Exactly one instance (e.g., config, registry) |
| **State** | Behavioural | Object behaviour changes based on internal state |
| **Observer** | Behavioural | Notify dependents automatically on state change |
| **Decorator** | Structural | Add responsibilities to objects without subclassing |
| **Composite** | Structural | Treat individual objects and compositions uniformly |
| **Chain of Responsibility** | Behavioural | Pass requests along a handler chain |
| **Template Method** | Behavioural | Define the skeleton; subclasses fill in the steps |
| **Builder** | Creational | Construct complex objects step-by-step |
| **Adapter** | Structural | Convert incompatible interfaces |
| **Repository** | Architectural | Abstract the data persistence layer |
| **Visitor** | Behavioural | Add operations to objects without modifying them |
| **Rules Engine** | Architectural | Evaluate ordered, prioritised business rules |

---

## 📋 Problem Index

| # | Problem | Package | Design Patterns | Key Concepts |
|---|---------|---------|-----------------|--------------|
| 1 | **Parking Lot** | `parkinglot` | Strategy · Factory · State · Singleton | Vehicle hierarchy, spot types, fee calculation |
| 2 | **Elevator System** | `elevator` | State Machine · Observer · Strategy | SCAN/FCFS scheduling, multi-elevator dispatch |
| 3 | **Chess Game** | `chess` | Strategy · Composite | Move validation, check/checkmate detection |
| 4 | **Splitwise** | `splitwise` | Observer · Strategy | Equal/Exact/% splits, min-cash-flow debt simplification |
| 5 | **Snake & Ladder** | `snakeladder` | Factory · Template Method | Board element creation, pluggable game loop |
| 6 | **Tic-Tac-Toe** | `tictactoe` | Strategy · Composite | Win strategy, Human vs AI (minimax) |
| 7 | **Library Management** | `library` | Repository · Observer · Factory | Loans, overdue fines, membership tiers |
| 8 | **Movie Booking** | `moviebooking` | State · Factory · Observer | Seat reservation FSM, surge pricing |
| 9 | **Cab Booking** | `cabbooking` | Strategy · Observer · State | Haversine geo, driver matching, ride lifecycle |
| 10 | **Vending Machine** | `vendingmachine` | State · Strategy | Coin payment, change calculation, inventory |
| 11 | **Coffee Machine** | `coffeemachine` | Decorator · Template Method | Add-on stacking, machine-type abstraction |
| 12 | **Logging Framework** | `logging` | Chain of Responsibility · Singleton | Log levels, multi-appender chain, formatter |
| 13 | **Cache Library** | `cache` | Strategy · Template · Builder | LRU/LFU/FIFO/Random eviction, TTL, stats |
| 14 | **File System** | `filesystem` | Composite · Visitor | Directory tree, disk-usage & search visitors |
| 15 | **Notification System** | `notification` | Factory · Strategy · Observer · Builder | Multi-channel routing, retry, preferences |
| 16 | **Payment Gateway** | `payment` | Adapter · Chain · Strategy | Validation chain, gateway adapters, fraud check |
| 17 | **API Rate Limiter** | `ratelimiter` | Strategy · Composite · Decorator | Token bucket, sliding window, metrics decorator |
| 18 | **Distributed Lock** | `distributedlock` | Decorator · Strategy | In-memory/DB lock strategies, auto-renew TTL |
| 19 | **Pricing Engine** | `pricingengine` | Rules Engine · Strategy · Chain | Bulk/loyalty/coupon rules, priority ordering |
| 20 | **Shopping Cart** | `shoppingcart` | Strategy · Observer · Builder | Discount strategies, tax calculation, cart merge |

---

## 🗂️ Package Structure

```
lld/
├── parkinglot/         # ParkingLot (Singleton), ParkingFloor, SpotFactory, NearestSpotStrategy
├── elevator/           # ElevatorController, SCANStrategy, FCFSStrategy, ElevatorState
├── chess/              # Board, Piece hierarchy, ChessGame, MoveStrategy per piece
├── splitwise/          # SplitwiseService, Group, SplitStrategy (Equal/Exact/Pct/Shares)
├── snakeladder/        # SnakeLadderGame, GameTemplate, BoardElementFactory
├── tictactoe/          # TicTacToeGame, StandardWinStrategy, AIPlayer (minimax)
├── library/            # LibraryService, InMemoryBookRepository, FineCalculator
├── moviebooking/       # MovieBookingService, SeatStatus FSM, StandardPricingStrategy
├── cabbooking/         # CabBookingService, NearestDriverStrategy, StandardFareCalculator
├── vendingmachine/     # VendingMachine, IdleState/HasMoneyState/..., ChangeCalculator
├── coffeemachine/      # Espresso/Latte + Decorators, EspressoMachine, DripCoffeeMachine
├── logging/            # LoggerFactory (Singleton), ConsoleAppender, FileAppender
├── cache/              # CacheBuilder, SimpleCache<K,V>, LRU/LFU/FIFO/RandomEviction
├── filesystem/         # FileSystem, DirectoryNode (Composite), SearchVisitor, DiskUsageVisitor
├── notification/       # NotificationService, ChannelFactory, PriorityRoutingStrategy
├── payment/            # PaymentService, Stripe/PayPal/RazorpayAdapter, ValidationChain
├── ratelimiter/        # TokenBucket, SlidingWindow, CompositeRateLimiter, MetricsDecorator
├── distributedlock/    # LockManager, InMemoryLockStrategy, AutoRenewLockDecorator
├── pricingengine/      # PricingEngine, AllRulesStrategy, BulkDiscountRule, CouponCodeRule
└── shoppingcart/       # CartService, Cart, CartItemBuilder, MembershipDiscountStrategy
```

---

## ✅ SOLID Principles Checklist

Before finalising any LLD solution, verify:

- **S** — Single Responsibility: each class has one reason to change
- **O** — Open/Closed: add features via new classes, not by modifying existing ones
- **L** — Liskov Substitution: subtypes are safely substitutable for base types
- **I** — Interface Segregation: clients depend only on methods they use
- **D** — Dependency Inversion: depend on abstractions, not concrete implementations

---

## 🔑 Interview Tips

1. **Clarify scope first** — ask about scale, actors, and must-have vs nice-to-have features.
2. **Draw class diagrams** — identify entities, relationships, and responsibilities before coding.
3. **Name patterns explicitly** — "I'm using Strategy here so the algorithm can be swapped."
4. **Address concurrency** — mention `synchronized`, `ReentrantLock`, or `ConcurrentHashMap` where relevant.
5. **Show extensibility** — "To add a new payment gateway, I only need to implement `PaymentGatewayAdapter`."
6. **Don't over-engineer** — start simple, then extend when asked.

---

> **See also:** [`../hld/README.md`](../hld/README.md) for high-level system design problems.
