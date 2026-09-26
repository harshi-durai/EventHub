package model;

import java.util.List;

public class Booking {

    private int id;
    private int userId;
    private int eventId;
    private double totalAmount;
    private String status;
    private List<Integer> seatIds;

    public Booking() {
    }

    public Booking(
            int id,
            int userId,
            int eventId,
            double totalAmount,
            String status) {

        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
}