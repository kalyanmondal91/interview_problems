package org.interview.system_design.lld.parkinglot;

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
        return String.format("Ticket[%s] %s @ Floor%d Spot%s | Entry:%s",
                ticketId, vehicle, floorNumber, spot.getSpotId(), entryTime);
    }
}
