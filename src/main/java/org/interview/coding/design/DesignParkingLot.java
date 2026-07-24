package org.interview.coding.design;

import java.util.*;

/**
 * Problem: Design Parking Lot
 * Difficulty: Medium
 *
 * Description: Design a parking lot with 3 levels, each having spots for motorcycles (10),
 * compact cars (40), and large vehicles (20). parkVehicle finds the first available matching spot.
 * unparkVehicle frees the spot. getAvailableCount returns count of available spots by type.
 *
 * Example:
 *   Input: parkVehicle(motorcycle), parkVehicle(compact), unparkVehicle(ticket)
 *   Output: ParkingTicket with level/spot info, spot freed
 *
 * Approach: Model spot types and vehicle types as enums. Each ParkingSpot tracks its type, level,
 * spotId, and occupied status. ParkingLot holds a list of all spots and iterates to find the first
 * available spot matching the vehicle type. Ticket holds the assigned spot for later freeing.
 *
 * Time Complexity: O(N) for parkVehicle where N=total spots, O(1) for unparkVehicle
 * Space Complexity: O(N) for spot storage
 *
 * Test Cases:
 *   1. Park motorcycle → ticket with MOTORCYCLE spot
 *   2. Park compact, unpark → spot freed, available count increases
 *   3. Edge: fill all motorcycle spots (10*3=30), next park motorcycle → null ticket
 */
public class DesignParkingLot {

    enum SpotType { MOTORCYCLE, COMPACT, LARGE }
    enum VehicleType { MOTORCYCLE, COMPACT, LARGE }

    static class ParkingSpot {
        SpotType spotType;
        int level;
        int spotId;
        boolean isOccupied;

        ParkingSpot(SpotType spotType, int level, int spotId) {
            this.spotType = spotType;
            this.level = level;
            this.spotId = spotId;
            this.isOccupied = false;
        }
    }

    static class Vehicle {
        VehicleType vehicleType;
        Vehicle(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    }

    static class ParkingTicket {
        ParkingSpot spot;
        ParkingTicket(ParkingSpot spot) { this.spot = spot; }

        @Override
        public String toString() {
            return "Ticket[level=" + spot.level + ", spotId=" + spot.spotId
                    + ", type=" + spot.spotType + "]";
        }
    }

    private final List<ParkingSpot> spots = new ArrayList<>();

    public DesignParkingLot() {
        for (int level = 0; level < 3; level++) {
            int id = 0;
            for (int i = 0; i < 10; i++) spots.add(new ParkingSpot(SpotType.MOTORCYCLE, level, id++));
            for (int i = 0; i < 40; i++) spots.add(new ParkingSpot(SpotType.COMPACT, level, id++));
            for (int i = 0; i < 20; i++) spots.add(new ParkingSpot(SpotType.LARGE, level, id++));
        }
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        SpotType needed = SpotType.valueOf(vehicle.vehicleType.name());
        for (ParkingSpot spot : spots) {
            if (!spot.isOccupied && spot.spotType == needed) {
                spot.isOccupied = true;
                return new ParkingTicket(spot);
            }
        }
        return null; // lot full for that type
    }

    public void unparkVehicle(ParkingTicket ticket) {
        if (ticket != null) ticket.spot.isOccupied = false;
    }

    public int getAvailableCount(SpotType type) {
        int count = 0;
        for (ParkingSpot spot : spots) {
            if (!spot.isOccupied && spot.spotType == type) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        DesignParkingLot lot = new DesignParkingLot();

        // Test Case 1: Park a motorcycle
        ParkingTicket t1 = lot.parkVehicle(new Vehicle(VehicleType.MOTORCYCLE));
        System.out.println("Parked motorcycle: " + t1);
        System.out.println("Available MOTORCYCLE: " + lot.getAvailableCount(SpotType.MOTORCYCLE)); // 29

        // Test Case 2: Park compact and unpark
        ParkingTicket t2 = lot.parkVehicle(new Vehicle(VehicleType.COMPACT));
        System.out.println("Parked compact: " + t2);
        lot.unparkVehicle(t2);
        System.out.println("After unpark, available COMPACT: " + lot.getAvailableCount(SpotType.COMPACT)); // 120

        // Test Case 3: Edge - exhaust all large spots
        System.out.println("Available LARGE before fill: " + lot.getAvailableCount(SpotType.LARGE)); // 60
        List<ParkingTicket> tickets = new ArrayList<>();
        for (int i = 0; i < 60; i++) tickets.add(lot.parkVehicle(new Vehicle(VehicleType.LARGE)));
        System.out.println("Available LARGE after fill: " + lot.getAvailableCount(SpotType.LARGE)); // 0
        ParkingTicket overflow = lot.parkVehicle(new Vehicle(VehicleType.LARGE));
        System.out.println("Overflow ticket: " + overflow); // null
    }
}
