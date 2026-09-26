package model;

public class Event {

    private int id;
    private String name;
    private String description;
    private String eventDate;
    private String eventTime;
    private String venue;
    private double ticketPrice;
    private int capacity;
    private String status;

    public Event() {
    }

    public Event(
            int id,
            String name,
            String description,
            String eventDate,
            String eventTime,
            String venue,
            double ticketPrice,
            int capacity,
            String status) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.ticketPrice = ticketPrice;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getVenue() {
        return venue;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " - " + venue + " - ₹" + ticketPrice;
    }
}