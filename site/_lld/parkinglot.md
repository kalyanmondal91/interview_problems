---
layout: lld
render_with_liquid: false
title: "Parking Lot System"
system: parkinglot
description: "LLD of Parking Lot System"
files:
  - "Car.java"
  - "CompactSpot.java"
  - "FeeCalculator.java"
  - "HandicappedSpot.java"
  - "HourlyFeeCalculator.java"
  - "LargeSpot.java"
  - "Main.java"
  - "Motorcycle.java"
  - "NearestSpotStrategy.java"
  - "ParkingFloor.java"
  - "ParkingLot.java"
  - "ParkingLotSystem.java"
  - "ParkingSpot.java"
  - "ParkingStrategy.java"
  - "ParkingTicket.java"
  - "RegularSpot.java"
  - "SpotFactory.java"
  - "SpotState.java"
  - "SpotType.java"
  - "Truck.java"
  - "Vehicle.java"
  - "VehicleType.java"
---

## Parking Lot System

Complete Java LLD implementation.

## Source Files

<div class="lld-tabs">
<div class="tab-buttons">
<button class="tab-btn active" data-tab="Car.java">Car.java</button>
<button class="tab-btn" data-tab="CompactSpot.java">CompactSpot.java</button>
<button class="tab-btn" data-tab="FeeCalculator.java">FeeCalculator.java</button>
<button class="tab-btn" data-tab="HandicappedSpot.java">HandicappedSpot.java</button>
<button class="tab-btn" data-tab="HourlyFeeCalculator.java">HourlyFeeCalculator.java</button>
<button class="tab-btn" data-tab="LargeSpot.java">LargeSpot.java</button>
<button class="tab-btn" data-tab="Main.java">Main.java</button>
<button class="tab-btn" data-tab="Motorcycle.java">Motorcycle.java</button>
<button class="tab-btn" data-tab="NearestSpotStrategy.java">NearestSpotStrategy.java</button>
<button class="tab-btn" data-tab="ParkingFloor.java">ParkingFloor.java</button>
<button class="tab-btn" data-tab="ParkingLot.java">ParkingLot.java</button>
<button class="tab-btn" data-tab="ParkingLotSystem.java">ParkingLotSystem.java</button>
<button class="tab-btn" data-tab="ParkingSpot.java">ParkingSpot.java</button>
<button class="tab-btn" data-tab="ParkingStrategy.java">ParkingStrategy.java</button>
<button class="tab-btn" data-tab="ParkingTicket.java">ParkingTicket.java</button>
<button class="tab-btn" data-tab="RegularSpot.java">RegularSpot.java</button>
<button class="tab-btn" data-tab="SpotFactory.java">SpotFactory.java</button>
<button class="tab-btn" data-tab="SpotState.java">SpotState.java</button>
<button class="tab-btn" data-tab="SpotType.java">SpotType.java</button>
<button class="tab-btn" data-tab="Truck.java">Truck.java</button>
<button class="tab-btn" data-tab="Vehicle.java">Vehicle.java</button>
<button class="tab-btn" data-tab="VehicleType.java">VehicleType.java</button>
</div>
<div class="tab-content active" id="Car-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Concrete vehicle: Car.
 * Can be parked in COMPACT, REGULAR, or HANDICAPPED spots.
 */
public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}</code></pre>
</div>
<div class="tab-content" id="CompactSpot-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Compact spot — fits only motorcycles and cars.
 */
public class CompactSpot extends ParkingSpot {
    public CompactSpot(String spotId) {
        super(spotId, SpotType.COMPACT);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        VehicleType t = vehicle.getVehicleType();
        return t == VehicleType.MOTORCYCLE || t == VehicleType.CAR;
    }
}</code></pre>
</div>
<div class="tab-content" id="FeeCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Strategy Pattern interface for computing parking fees.
 * Implementations can apply flat rates, hourly rates, surge pricing, etc.
 */
public interface FeeCalculator {
    /**
     * Calculates the parking fee for the given ticket.
     *
     * @param ticket the parking ticket (must have exitTime set)
     * @return total fee in currency units (e.g. dollars)
     */
    double calculateFee(ParkingTicket ticket);
}</code></pre>
</div>
<div class="tab-content" id="HandicappedSpot-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Handicapped spot — reserved for motorcycles and cars with handicap permits.
 * For simplicity, any motorcycle or car can use this spot.
 */
public class HandicappedSpot extends ParkingSpot {
    public HandicappedSpot(String spotId) {
        super(spotId, SpotType.HANDICAPPED);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        VehicleType t = vehicle.getVehicleType();
        return t == VehicleType.MOTORCYCLE || t == VehicleType.CAR;
    }
}</code></pre>
</div>
<div class="tab-content" id="HourlyFeeCalculator-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Hourly fee calculator.
 * Rates (per hour):
 *   MOTORCYCLE → $1.00
 *   CAR        → $2.00
 *   TRUCK      → $3.50
 * Minimum charge: 1 hour. Partial hours are rounded up.
 */
public class HourlyFeeCalculator implements FeeCalculator {

    private static final double MOTORCYCLE_RATE = 1.00;
    private static final double CAR_RATE        = 2.00;
    private static final double TRUCK_RATE      = 3.50;

    @Override
    public double calculateFee(ParkingTicket ticket) {
        LocalDateTime exit = LocalDateTime.now(); // Use current time if not yet set
        Duration duration = Duration.between(ticket.getEntryTime(), exit);

        // Minimum 1 hour, round up partial hours
        long hours = Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));

        double rate = rateFor(ticket.getVehicle().getVehicleType());
        return Math.round(hours * rate * 100.0) / 100.0;
    }

    private double rateFor(VehicleType type) {
        switch (type) {
            case MOTORCYCLE: return MOTORCYCLE_RATE;
            case CAR:        return CAR_RATE;
            case TRUCK:      return TRUCK_RATE;
            default:         return CAR_RATE;
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="LargeSpot-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Large spot — fits all vehicle types including trucks.
 */
public class LargeSpot extends ParkingSpot {
    public LargeSpot(String spotId) {
        super(spotId, SpotType.LARGE);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        return true; // All vehicles fit
    }
}</code></pre>
</div>
<div class="tab-content" id="Main-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.util.List;

/**
 * Demo runner for the Parking Lot LLD.
 * Patterns: Strategy, Factory, State, Singleton
 */
public class Main {

    private static void banner(String text) {
        System.out.println();
        System.out.println(&quot;=== &quot; + text + &quot; ===&quot;);
    }

    public static void main(String[] args) throws InterruptedException {

        // -- Setup --
        ParkingLot lot = ParkingLot.getInstance();
        lot.setName(&quot;CityCenter Parking&quot;);
        lot.setStrategy(new NearestSpotStrategy());
        lot.setFeeCalculator(new HourlyFeeCalculator());
        lot.reset();

        ParkingFloor ground = new ParkingFloor(0);
        ground.addSpot(SpotFactory.createSpot(SpotType.COMPACT,    &quot;G-C1&quot;));
        ground.addSpot(SpotFactory.createSpot(SpotType.COMPACT,    &quot;G-C2&quot;));
        ground.addSpot(SpotFactory.createSpot(SpotType.REGULAR,    &quot;G-R1&quot;));
        ground.addSpot(SpotFactory.createSpot(SpotType.REGULAR,    &quot;G-R2&quot;));
        ground.addSpot(SpotFactory.createSpot(SpotType.LARGE,      &quot;G-L1&quot;));
        ground.addSpot(SpotFactory.createSpot(SpotType.HANDICAPPED,&quot;G-H1&quot;));

        ParkingFloor upper = new ParkingFloor(1);
        upper.addSpot(SpotFactory.createSpot(SpotType.COMPACT, &quot;U-C1&quot;));
        upper.addSpot(SpotFactory.createSpot(SpotType.REGULAR, &quot;U-R1&quot;));

        lot.addFloor(ground);
        lot.addFloor(upper);

        // -- Scenario 1: Park multiple vehicle types --
        banner(&quot;Scenario 1: Park a car, motorcycle, and truck&quot;);
        Vehicle car1   = new Car(&quot;KA-01-AB-1234&quot;);
        Vehicle car2   = new Car(&quot;KA-02-CD-5678&quot;);
        Vehicle moto   = new Motorcycle(&quot;MH-12-EF-9999&quot;);
        Vehicle truck  = new Truck(&quot;DL-01-TRK-001&quot;);

        ParkingTicket t1 = lot.park(car1);
        ParkingTicket t2 = lot.park(car2);
        ParkingTicket t3 = lot.park(moto);
        ParkingTicket t4 = lot.park(truck);

        for (ParkingTicket t : List.of(t1, t2, t3, t4)) {
            System.out.printf(&quot;  Parked %-20s | Ticket: %-10s | Spot: %s%n&quot;,
                    t.getVehicle().getLicensePlate(), t.getTicketId(), t.getSpot().getSpotId());
        }

        // -- Scenario 2: Check available spots --
        banner(&quot;Scenario 2: Available spots by vehicle type&quot;);
        System.out.println(&quot;  Available for Car:        &quot; + lot.availableSpots(new Car(&quot;X&quot;)));
        System.out.println(&quot;  Available for Motorcycle: &quot; + lot.availableSpots(new Motorcycle(&quot;X&quot;)));
        System.out.println(&quot;  Available for Truck:      &quot; + lot.availableSpots(new Truck(&quot;X&quot;)));

        // -- Scenario 3: Unpark and calculate fee --
        banner(&quot;Scenario 3: Unpark car1 after brief time&quot;);
        Thread.sleep(1500); // simulate 1.5 seconds parked
        ParkingTicket unparkResult = lot.unpark(t1.getTicketId());
        if (unparkResult != null) {
            System.out.printf(&quot;  Unparked: %s | Fee: $%.2f | Duration: %d ms%n&quot;,
                    unparkResult.getVehicle().getLicensePlate(),
                    unparkResult.getFee(),
                    java.time.temporal.ChronoUnit.MILLIS.between(unparkResult.getEntryTime(), unparkResult.getExitTime()));
        }

        // -- Scenario 4: Fill lot and attempt overflow --
        banner(&quot;Scenario 4: Fill lot and try to park when full&quot;);
        // Park remaining spots
        Vehicle car3 = new Car(&quot;TN-01-XX-0001&quot;);
        Vehicle car4 = new Car(&quot;TN-01-XX-0002&quot;);
        Vehicle car5 = new Car(&quot;TN-01-XX-0003&quot;);
        lot.park(car3); lot.park(car4); lot.park(car5);

        Vehicle overflow = new Car(&quot;OVERFLOW-001&quot;);
        try {
            ParkingTicket overflowTicket = lot.park(overflow);
            System.out.println(&quot;  Unexpected: got spot &quot; + overflowTicket.getSpot().getSpotId());
        } catch (IllegalStateException e) {
            System.out.println(&quot;  No spot available for &quot; + overflow.getLicensePlate() + &quot; -- lot is full!&quot;);
        }

        // -- Scenario 5: Handicapped spot usage --
        banner(&quot;Scenario 5: Park in handicapped spot (any vehicle)&quot;);
        lot.reset();
        ParkingFloor hFloor = new ParkingFloor(0);
        hFloor.addSpot(SpotFactory.createSpot(SpotType.HANDICAPPED, &quot;H-01&quot;));
        lot.addFloor(hFloor);
        try {
            ParkingTicket hTicket = lot.park(new Car(&quot;HANDI-CAR&quot;));
            System.out.println(&quot;  Parked in: &quot; + hTicket.getSpot().getSpotId()
                    + &quot; (type=&quot; + hTicket.getSpot().getSpotType() + &quot;)&quot;);
        } catch (IllegalStateException e) {
            System.out.println(&quot;  No handicapped spot matched for car.&quot;);
        }

        banner(&quot;Parking Lot Demo Complete&quot;);
    }
}</code></pre>
</div>
<div class="tab-content" id="Motorcycle-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Concrete vehicle: Motorcycle.
 * Fits in any spot type (COMPACT, REGULAR, LARGE, HANDICAPPED).
 */
public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}</code></pre>
</div>
<div class="tab-content" id="NearestSpotStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.util.List;

/**
 * Strategy: finds the nearest available spot.
 * Iterates floors in order (lower floor = nearer entrance) and returns
 * the first spot within that floor that can accommodate the vehicle.
 * Within a floor, spots are ordered by their position index (index 0 = nearest).
 */
public class NearestSpotStrategy implements ParkingStrategy {

    @Override
    public ParkingSpot findSpot(Vehicle vehicle, List&lt;ParkingFloor&gt; floors) {
        for (ParkingFloor floor : floors) {
            List&lt;ParkingSpot&gt; spots = floor.getSpots();
            for (ParkingSpot spot : spots) {
                if (spot.isAvailable() &amp;&amp; spot.canFit(vehicle)) {
                    return spot;
                }
            }
        }
        return null; // No suitable spot found
    }
}</code></pre>
</div>
<div class="tab-content" id="ParkingFloor-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single floor in the parking lot.
 * Holds an ordered list of spots; their index within the list
 * is used by strategies as a proximity proxy.
 */
public class ParkingFloor {
    private final int floorNumber;
    private final List&lt;ParkingSpot&gt; spots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList&lt;&gt;();
    }

    /** Adds a spot to this floor. */
    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    /** Returns an unmodifiable view of all spots on this floor. */
    public List&lt;ParkingSpot&gt; getSpots() {
        return Collections.unmodifiableList(spots);
    }

    /** Returns all available spots that can accommodate the given vehicle. */
    public List&lt;ParkingSpot&gt; getAvailableSpots(Vehicle vehicle) {
        List&lt;ParkingSpot&gt; available = new ArrayList&lt;&gt;();
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() &amp;&amp; spot.canFit(vehicle)) {
                available.add(spot);
            }
        }
        return available;
    }

    public int getFloorNumber() { return floorNumber; }

    /** Total count of spots on this floor. */
    public int totalSpots() { return spots.size(); }

    /** Count of available spots that fit the vehicle. */
    public long availableCount(Vehicle vehicle) {
        return spots.stream().filter(s -&gt; s.isAvailable() &amp;&amp; s.canFit(vehicle)).count();
    }

    @Override
    public String toString() {
        return &quot;Floor &quot; + floorNumber + &quot; [&quot; + spots.size() + &quot; spots]&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="ParkingLot-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton — the core parking lot managing floors, strategy, and active tickets.
 * Thread-safe via synchronized park/unpark methods.
 */
public class ParkingLot {

    // ── Singleton ──────────────────────────────────────────────────────────────
    private static volatile ParkingLot instance;

    private ParkingLot() {}

    public static ParkingLot getInstance() {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }

    // ── State ──────────────────────────────────────────────────────────────────
    private String name;
    private final List&lt;ParkingFloor&gt; floors = new ArrayList&lt;&gt;();
    private ParkingStrategy strategy = new NearestSpotStrategy();
    private FeeCalculator feeCalculator = new HourlyFeeCalculator();

    /** ticketId -&gt; active ParkingTicket */
    private final Map&lt;String, ParkingTicket&gt; activeTickets = new ConcurrentHashMap&lt;&gt;();

    // ── Configuration ──────────────────────────────────────────────────────────

    public void setName(String name) { this.name = name; }
    public void setStrategy(ParkingStrategy strategy) { this.strategy = strategy; }
    public void setFeeCalculator(FeeCalculator feeCalculator) { this.feeCalculator = feeCalculator; }

    public void addFloor(ParkingFloor floor) { floors.add(floor); }
    public List&lt;ParkingFloor&gt; getFloors() { return Collections.unmodifiableList(floors); }

    // ── Operations ─────────────────────────────────────────────────────────────

    /**
     * Parks a vehicle using the configured strategy.
     *
     * @param vehicle vehicle to park
     * @return issued ParkingTicket
     * @throws IllegalStateException if no suitable spot is available
     */
    public synchronized ParkingTicket park(Vehicle vehicle) {
        ParkingSpot spot = strategy.findSpot(vehicle, floors);
        if (spot == null) {
            throw new IllegalStateException(&quot;No available spot for vehicle: &quot; + vehicle);
        }

        // Determine which floor this spot belongs to
        int floorNumber = findFloorOf(spot);
        spot.park(vehicle);

        ParkingTicket ticket = new ParkingTicket(vehicle, spot, floorNumber);
        activeTickets.put(ticket.getTicketId(), ticket);
        System.out.printf(&quot;[ParkingLot] Parked %s → %s (Floor %d) | Ticket: %s%n&quot;,
                vehicle, spot, floorNumber, ticket.getTicketId());
        return ticket;
    }

    /**
     * Unparks a vehicle using the ticket, calculates fee, and frees the spot.
     *
     * @param ticketId the ticket ID received at entry
     * @return settled ParkingTicket with fee
     */
    public synchronized ParkingTicket unpark(String ticketId) {
        ParkingTicket ticket = activeTickets.remove(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException(&quot;Invalid ticket ID: &quot; + ticketId);
        }
        double fee = feeCalculator.calculateFee(ticket);
        ticket.getSpot().unpark();
        ticket.settle(fee);
        System.out.printf(&quot;[ParkingLot] Unparked %s from Spot %s | Fee: $%.2f%n&quot;,
                ticket.getVehicle(), ticket.getSpot().getSpotId(), fee);
        return ticket;
    }

    /** Returns a snapshot of all currently active tickets. */
    public Map&lt;String, ParkingTicket&gt; getActiveTickets() {
        return Collections.unmodifiableMap(activeTickets);
    }

    /** Total available spots across all floors for a given vehicle. */
    public long availableSpots(Vehicle vehicle) {
        return floors.stream().mapToLong(f -&gt; f.availableCount(vehicle)).sum();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private int findFloorOf(ParkingSpot target) {
        for (ParkingFloor floor : floors) {
            if (floor.getSpots().contains(target)) {
                return floor.getFloorNumber();
            }
        }
        return -1;
    }

    /** Resets singleton state (useful for testing). */
    public synchronized void reset() {
        floors.clear();
        activeTickets.clear();
        strategy = new NearestSpotStrategy();
        feeCalculator = new HourlyFeeCalculator();
    }

    @Override
    public String toString() {
        return &quot;ParkingLot[&quot; + name + &quot;] floors=&quot; + floors.size();
    }
}</code></pre>
</div>
<div class="tab-content" id="ParkingLotSystem-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Entry point / demo for the Parking Lot LLD.
 *
 * Design Patterns used:
 *   • Singleton  — ParkingLot has one global instance
 *   • Factory    — SpotFactory creates concrete ParkingSpot objects
 *   • Strategy   — ParkingStrategy (NearestSpotStrategy) for spot selection;
 *                  FeeCalculator (HourlyFeeCalculator) for pricing
 *   • State      — SpotState tracks each spot&#x27;s lifecycle
 */
public class ParkingLotSystem {

    public static void main(String[] args) {
        // 1. Get singleton instance and configure
        ParkingLot lot = ParkingLot.getInstance();
        lot.setName(&quot;Downtown Garage&quot;);
        lot.setStrategy(new NearestSpotStrategy());
        lot.setFeeCalculator(new HourlyFeeCalculator());

        // 2. Build floors using SpotFactory
        ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(SpotFactory.createSpot(SpotType.COMPACT,     &quot;1-C1&quot;));
        floor1.addSpot(SpotFactory.createSpot(SpotType.COMPACT,     &quot;1-C2&quot;));
        floor1.addSpot(SpotFactory.createSpot(SpotType.REGULAR,     &quot;1-R1&quot;));
        floor1.addSpot(SpotFactory.createSpot(SpotType.HANDICAPPED, &quot;1-H1&quot;));
        floor1.addSpot(SpotFactory.createSpot(SpotType.LARGE,       &quot;1-L1&quot;));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(SpotFactory.createSpot(SpotType.COMPACT, &quot;2-C1&quot;));
        floor2.addSpot(SpotFactory.createSpot(SpotType.REGULAR, &quot;2-R1&quot;));
        floor2.addSpot(SpotFactory.createSpot(SpotType.LARGE,   &quot;2-L1&quot;));

        lot.addFloor(floor1);
        lot.addFloor(floor2);

        // 3. Park vehicles
        Vehicle bike  = new Motorcycle(&quot;MOTO-001&quot;);
        Vehicle car1  = new Car(&quot;CAR-001&quot;);
        Vehicle truck = new Truck(&quot;TRUCK-001&quot;);
        Vehicle car2  = new Car(&quot;CAR-002&quot;);

        ParkingTicket t1 = lot.park(bike);
        ParkingTicket t2 = lot.park(car1);
        ParkingTicket t3 = lot.park(truck);
        ParkingTicket t4 = lot.park(car2);

        System.out.println(&quot;\nActive tickets: &quot; + lot.getActiveTickets().size());

        // 4. Unpark and print fee
        lot.unpark(t1.getTicketId());
        lot.unpark(t2.getTicketId());
        lot.unpark(t3.getTicketId());
        lot.unpark(t4.getTicketId());

        System.out.println(&quot;\nAll vehicles unparked. Active tickets: &quot; + lot.getActiveTickets().size());
    }
}</code></pre>
</div>
<div class="tab-content" id="ParkingSpot-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Abstract base class for all parking spots.
 * Implements the State Pattern via SpotState, and defines
 * the canFit() contract for each concrete spot type.
 */
public abstract class ParkingSpot {
    protected final String spotId;
    protected final SpotType spotType;
    protected SpotState state;
    protected Vehicle parkedVehicle;

    protected ParkingSpot(String spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.state = SpotState.AVAILABLE;
    }

    /**
     * Determines whether the given vehicle can fit in this spot.
     * Each subclass implements vehicle-type compatibility rules.
     */
    public abstract boolean canFit(Vehicle vehicle);

    public boolean isAvailable() {
        return state == SpotState.AVAILABLE;
    }

    /**
     * Parks a vehicle in this spot, transitioning state to OCCUPIED.
     * @throws IllegalStateException if spot is not available
     */
    public void park(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException(&quot;Spot &quot; + spotId + &quot; is not available. Current state: &quot; + state);
        }
        if (!canFit(vehicle)) {
            throw new IllegalArgumentException(&quot;Vehicle &quot; + vehicle + &quot; cannot fit in spot &quot; + spotId);
        }
        this.parkedVehicle = vehicle;
        this.state = SpotState.OCCUPIED;
    }

    /**
     * Removes the parked vehicle, transitioning state back to AVAILABLE.
     * @throws IllegalStateException if spot is not occupied
     */
    public Vehicle unpark() {
        if (state != SpotState.OCCUPIED) {
            throw new IllegalStateException(&quot;Spot &quot; + spotId + &quot; is not occupied.&quot;);
        }
        Vehicle removed = this.parkedVehicle;
        this.parkedVehicle = null;
        this.state = SpotState.AVAILABLE;
        return removed;
    }

    public void reserve() {
        if (!isAvailable()) throw new IllegalStateException(&quot;Cannot reserve: spot &quot; + spotId + &quot; is &quot; + state);
        this.state = SpotState.RESERVED;
    }

    public void setOutOfService() { this.state = SpotState.OUT_OF_SERVICE; }

    public String getSpotId() { return spotId; }
    public SpotType getSpotType() { return spotType; }
    public SpotState getState() { return state; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }

    @Override
    public String toString() {
        return spotType + &quot;Spot[&quot; + spotId + &quot;](&quot; + state + &quot;)&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="ParkingStrategy-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.util.List;

/**
 * Strategy Pattern interface for selecting a parking spot.
 * Different implementations can offer nearest-spot, cheapest-spot, or
 * load-balanced strategies without changing the ParkingLot core.
 */
public interface ParkingStrategy {
    /**
     * Finds a suitable available spot for the given vehicle across all floors.
     *
     * @param vehicle the vehicle that needs parking
     * @param floors  all floors in the parking lot
     * @return a suitable ParkingSpot, or null if none is available
     */
    ParkingSpot findSpot(Vehicle vehicle, List&lt;ParkingFloor&gt; floors);
}</code></pre>
</div>
<div class="tab-content" id="ParkingTicket-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a parking ticket issued when a vehicle parks.
 * Contains all information needed for fee calculation upon exit.
 */
public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final int floorNumber;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;
    private boolean paid;

    public ParkingTicket(Vehicle vehicle, ParkingSpot spot, int floorNumber) {
        this.ticketId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle = vehicle;
        this.spot = spot;
        this.floorNumber = floorNumber;
        this.entryTime = LocalDateTime.now();
        this.paid = false;
    }

    /** Records exit time and fee, marking the ticket as settled. */
    public void settle(double fee) {
        this.exitTime = LocalDateTime.now();
        this.fee = fee;
        this.paid = true;
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public int getFloorNumber() { return floorNumber; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getFee() { return fee; }
    public boolean isPaid() { return paid; }

    @Override
    public String toString() {
        return String.format(&quot;Ticket[%s] %s @ Floor%d Spot%s | Entry:%s&quot;,
                ticketId, vehicle, floorNumber, spot.getSpotId(), entryTime);
    }
}</code></pre>
</div>
<div class="tab-content" id="RegularSpot-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Regular spot — fits motorcycles and cars (not trucks).
 */
public class RegularSpot extends ParkingSpot {
    public RegularSpot(String spotId) {
        super(spotId, SpotType.REGULAR);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        VehicleType t = vehicle.getVehicleType();
        return t == VehicleType.MOTORCYCLE || t == VehicleType.CAR;
    }
}</code></pre>
</div>
<div class="tab-content" id="SpotFactory-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Factory Pattern — creates ParkingSpot instances based on SpotType.
 * Centralizes spot construction logic so callers don&#x27;t depend on concrete classes.
 */
public class SpotFactory {

    /**
     * Creates a parking spot of the specified type with the given ID.
     *
     * @param type   the spot type
     * @param spotId unique identifier for the spot
     * @return a new ParkingSpot instance
     */
    public static ParkingSpot createSpot(SpotType type, String spotId) {
        switch (type) {
            case COMPACT:     return new CompactSpot(spotId);
            case REGULAR:     return new RegularSpot(spotId);
            case LARGE:       return new LargeSpot(spotId);
            case HANDICAPPED: return new HandicappedSpot(spotId);
            default: throw new IllegalArgumentException(&quot;Unknown spot type: &quot; + type);
        }
    }
}</code></pre>
</div>
<div class="tab-content" id="SpotState-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Enum representing the state of a parking spot (State Pattern).
 * Transitions: AVAILABLE &lt;-&gt; OCCUPIED, AVAILABLE -&gt; RESERVED, any -&gt; OUT_OF_SERVICE
 */
public enum SpotState {
    AVAILABLE,
    OCCUPIED,
    RESERVED,
    OUT_OF_SERVICE
}</code></pre>
</div>
<div class="tab-content" id="SpotType-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Enum representing different types of parking spots.
 * Used by Factory and Strategy patterns to match vehicles to appropriate spots.
 */
public enum SpotType {
    COMPACT,
    REGULAR,
    LARGE,
    HANDICAPPED
}</code></pre>
</div>
<div class="tab-content" id="Truck-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Concrete vehicle: Truck.
 * Requires a LARGE spot.
 */
public class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}</code></pre>
</div>
<div class="tab-content" id="Vehicle-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Abstract base class for all vehicles.
 * Each vehicle has a license plate and a type that determines compatible spots.
 */
public abstract class Vehicle {
    protected final String licensePlate;
    protected final VehicleType vehicleType;

    protected Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() { return licensePlate; }
    public VehicleType getVehicleType() { return vehicleType; }

    @Override
    public String toString() {
        return vehicleType + &quot;[&quot; + licensePlate + &quot;]&quot;;
    }
}</code></pre>
</div>
<div class="tab-content" id="VehicleType-java">
<pre><code class="language-java">package org.interview.system_design.lld.parkinglot;

/**
 * Enum representing vehicle categories.
 * Determines which spot types are compatible for parking.
 */
public enum VehicleType {
    MOTORCYCLE,
    CAR,
    TRUCK
}</code></pre>
</div>
</div>
