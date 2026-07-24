package org.interview.system_design.lld.parkinglot;

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
    private final List<ParkingFloor> floors = new ArrayList<>();
    private ParkingStrategy strategy = new NearestSpotStrategy();
    private FeeCalculator feeCalculator = new HourlyFeeCalculator();

    /** ticketId -> active ParkingTicket */
    private final Map<String, ParkingTicket> activeTickets = new ConcurrentHashMap<>();

    // ── Configuration ──────────────────────────────────────────────────────────

    public void setName(String name) { this.name = name; }
    public void setStrategy(ParkingStrategy strategy) { this.strategy = strategy; }
    public void setFeeCalculator(FeeCalculator feeCalculator) { this.feeCalculator = feeCalculator; }

    public void addFloor(ParkingFloor floor) { floors.add(floor); }
    public List<ParkingFloor> getFloors() { return Collections.unmodifiableList(floors); }

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
            throw new IllegalStateException("No available spot for vehicle: " + vehicle);
        }

        // Determine which floor this spot belongs to
        int floorNumber = findFloorOf(spot);
        spot.park(vehicle);

        ParkingTicket ticket = new ParkingTicket(vehicle, spot, floorNumber);
        activeTickets.put(ticket.getTicketId(), ticket);
        System.out.printf("[ParkingLot] Parked %s → %s (Floor %d) | Ticket: %s%n",
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
            throw new IllegalArgumentException("Invalid ticket ID: " + ticketId);
        }
        double fee = feeCalculator.calculateFee(ticket);
        ticket.getSpot().unpark();
        ticket.settle(fee);
        System.out.printf("[ParkingLot] Unparked %s from Spot %s | Fee: $%.2f%n",
                ticket.getVehicle(), ticket.getSpot().getSpotId(), fee);
        return ticket;
    }

    /** Returns a snapshot of all currently active tickets. */
    public Map<String, ParkingTicket> getActiveTickets() {
        return Collections.unmodifiableMap(activeTickets);
    }

    /** Total available spots across all floors for a given vehicle. */
    public long availableSpots(Vehicle vehicle) {
        return floors.stream().mapToLong(f -> f.availableCount(vehicle)).sum();
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
        return "ParkingLot[" + name + "] floors=" + floors.size();
    }
}
