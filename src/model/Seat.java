package model;

public class Seat {

    private int id;
    private int eventId;
    private String seatNumber;
    private String status;

    public Seat() {
    }

    public Seat(
            int id,
            int eventId,
            String seatNumber,
            String status) {

        this.id = id;
        this.eventId = eventId;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return seatNumber + " - " + status;
    }
}