package org.interview.design_patterns.behavioral.state;

/**
 * ============================================================
 * Design Pattern: State (Behavioral)
 * ============================================================
 *
 * INTENT:
 *   Allow an object to alter its behavior when its internal state
 *   changes. The object will appear to change its class.
 *
 * PROBLEM IT SOLVES:
 *   - An object has many states and behavior changes significantly by state.
 *   - State-specific logic is scattered across if/else or switch statements.
 *   - Adding new states requires modifying existing conditionals.
 *
 * STRUCTURE:
 *   - Context: maintains a reference to the current State object; delegates behavior
 *   - State (interface): declares methods for each state-dependent behavior
 *   - ConcreteState: implements behavior for a specific state; may transition context
 *
 * DIFFERENCE FROM STRATEGY:
 *   - Strategy: client usually picks the strategy; algorithms don't know each other
 *   - State: states know each other (they trigger transitions); context changes state
 *
 * REAL-WORLD EXAMPLES:
 *   - Order lifecycle: Pending → Processing → Shipped → Delivered
 *   - Traffic lights: Red → Green → Yellow → Red
 *   - Vending machine: Idle → HasCoin → Dispensing
 *   - TCP connection: Closed → Listen → Established → TimeWait
 *
 * PROS:
 *   + Eliminates large switch/if statements
 *   + Localizes state-specific behavior
 *   + Easy to add new states (Open/Closed)
 *   + Makes state transitions explicit
 *
 * CONS:
 *   - Many small state classes
 *   - State transitions can become complex
 *
 * SCENARIO:
 *   Traffic light state machine: RED → GREEN → YELLOW → RED.
 *   Each state handles the "next()" transition differently.
 */
public class StatePattern {

    // ================================================================
    // State Interface
    // Each concrete state handles all actions the context may receive
    // ================================================================
    interface TrafficLightState {
        void display();               // What to show to drivers
        void next(TrafficLight light); // Transition to next state
    }

    // ================================================================
    // Context: TrafficLight
    // Delegates all behavior to current state.
    // Provides setState() so states can trigger transitions.
    // ================================================================
    static class TrafficLight {
        private TrafficLightState currentState;
        private int cycleCount = 0;

        TrafficLight() {
            this.currentState = new RedState(); // initial state
        }

        /** Delegate display to current state */
        public void display() {
            currentState.display();
        }

        /** Trigger state transition — delegate to current state */
        public void next() {
            currentState.next(this);
            cycleCount++;
        }

        /** Called by states to switch the current state */
        public void setState(TrafficLightState state) {
            this.currentState = state;
        }

        public int getCycleCount() { return cycleCount; }
    }

    // ================================================================
    // ConcreteStates
    // Each knows its own behavior AND which state comes next
    // ================================================================

    static class RedState implements TrafficLightState {
        @Override
        public void display() {
            System.out.println("🔴 RED   — STOP!  (duration: 60s)");
        }

        @Override
        public void next(TrafficLight light) {
            System.out.println("  → Transitioning: RED → GREEN");
            light.setState(new GreenState()); // Red goes to Green
        }
    }

    static class GreenState implements TrafficLightState {
        @Override
        public void display() {
            System.out.println("🟢 GREEN — GO!    (duration: 45s)");
        }

        @Override
        public void next(TrafficLight light) {
            System.out.println("  → Transitioning: GREEN → YELLOW");
            light.setState(new YellowState()); // Green goes to Yellow
        }
    }

    static class YellowState implements TrafficLightState {
        @Override
        public void display() {
            System.out.println("🟡 YELLOW — SLOW DOWN! (duration: 5s)");
        }

        @Override
        public void next(TrafficLight light) {
            System.out.println("  → Transitioning: YELLOW → RED");
            light.setState(new RedState()); // Yellow goes back to Red
        }
    }

    // ================================================================
    // BONUS: Vending Machine State Machine
    // A more complex state machine with user interaction
    // ================================================================
    interface VendingMachineState {
        void insertCoin(VendingMachine machine);
        void selectProduct(VendingMachine machine, String product);
        void dispense(VendingMachine machine);
    }

    static class VendingMachine {
        private VendingMachineState state;
        private double balance = 0;
        private String selectedProduct = null;

        VendingMachine() {
            this.state = new IdleState();
        }

        public void setState(VendingMachineState state) { this.state = state; }
        public void setBalance(double b)    { this.balance = b; }
        public double getBalance()          { return balance; }
        public void setProduct(String p)    { this.selectedProduct = p; }
        public String getProduct()          { return selectedProduct; }

        public void insertCoin(double amount) {
            balance += amount;
            System.out.printf("[Vending] Inserted $%.2f (total: $%.2f)%n", amount, balance);
            state.insertCoin(this);
        }

        public void selectProduct(String product) {
            System.out.println("[Vending] Selected: " + product);
            state.selectProduct(this, product);
        }

        public void dispense() {
            state.dispense(this);
        }
    }

    static class IdleState implements VendingMachineState {
        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("  [Idle→HasMoney] Coin accepted. Please select a product.");
            m.setState(new HasMoneyState());
        }

        @Override
        public void selectProduct(VendingMachine m, String product) {
            System.out.println("  [Idle] Please insert coins first!");
        }

        @Override
        public void dispense(VendingMachine m) {
            System.out.println("  [Idle] No coin, no product!");
        }
    }

    static class HasMoneyState implements VendingMachineState {
        private static final double PRICE = 1.50;

        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("  [HasMoney] Additional coin added.");
        }

        @Override
        public void selectProduct(VendingMachine m, String product) {
            if (m.getBalance() >= PRICE) {
                m.setProduct(product);
                m.setBalance(m.getBalance() - PRICE);
                System.out.printf("  [HasMoney→Dispensing] Selected '%s', charged $%.2f, change: $%.2f%n",
                    product, PRICE, m.getBalance());
                m.setState(new DispensingState());
            } else {
                System.out.printf("  [HasMoney] Insufficient funds. Need $%.2f, have $%.2f%n",
                    PRICE, m.getBalance());
            }
        }

        @Override
        public void dispense(VendingMachine m) {
            System.out.println("  [HasMoney] Please select a product first!");
        }
    }

    static class DispensingState implements VendingMachineState {
        @Override
        public void insertCoin(VendingMachine m) {
            System.out.println("  [Dispensing] Please wait, dispensing in progress...");
        }

        @Override
        public void selectProduct(VendingMachine m, String product) {
            System.out.println("  [Dispensing] Already dispensing. Please wait.");
        }

        @Override
        public void dispense(VendingMachine m) {
            System.out.println("  [Dispensing→Idle] 🥤 Dispensing: " + m.getProduct() + ". Enjoy!");
            m.setProduct(null);
            m.setState(new IdleState());
        }
    }

    // ================================================================
    // Demo
    // ================================================================
    public static void main(String[] args) {
        System.out.println("=== Traffic Light State Machine ===");
        TrafficLight light = new TrafficLight();

        // Run through 2 complete cycles
        for (int i = 0; i < 6; i++) {
            light.display();
            light.next();
        }
        System.out.println("Total cycle transitions: " + light.getCycleCount());

        System.out.println("\n=== Vending Machine State Machine ===");
        VendingMachine vm = new VendingMachine();

        // Scenario 1: Try without coin (should fail)
        vm.selectProduct("Cola");

        // Scenario 2: Insert too little, select
        vm.insertCoin(1.00);
        vm.selectProduct("Cola"); // not enough money

        // Scenario 3: Insert more, select, dispense
        vm.insertCoin(1.00);  // now have $2.00
        vm.selectProduct("Water");
        vm.dispense();  // dispense the product

        // Scenario 4: Fresh start
        vm.insertCoin(2.00);
        vm.selectProduct("Juice");
        vm.dispense();
    }
}
