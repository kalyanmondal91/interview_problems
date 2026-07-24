package org.interview.system_design.lld.moviebooking;

/**
 * Represents a seat in a cinema screen.
 */
public class Seat {
    private final String seatId;
    private final int row;
    private final int col;
    private final SeatType type;
    private SeatStatus status;

    public Seat(String seatId, int row, int col, SeatType type) {
        this.seatId = seatId;
        this.row = row;
        this.col = col;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getSeatId() { return seatId; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public SeatType getType() { return type; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    public boolean isAvailable() { return status == SeatStatus.AVAILABLE; }

    @Override
    public String toString() {
        return String.format("Seat{id='%s', row=%d, col=%d, type=%s, status=%s}",
                seatId, row, col, type, status);
    }
}
