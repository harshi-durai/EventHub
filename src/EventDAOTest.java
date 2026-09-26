import dao.EventDAO;
import model.Event;

import java.util.List;

public class EventDAOTest {

    public static void main(String[] args) {

        EventDAO eventDAO = new EventDAO();

        List<Event> events = eventDAO.getAllEvents();

        System.out.println("======================================");
        System.out.println("       EVENTHUB EVENT TEST");
        System.out.println("======================================");

        for (Event event : events) {

            System.out.println("Event   : " + event.getName());
            System.out.println("Date    : " + event.getEventDate());
            System.out.println("Time    : " + event.getEventTime());
            System.out.println("Venue   : " + event.getVenue());
            System.out.println("Price   : ₹" + event.getTicketPrice());
            System.out.println("--------------------------------------");
        }

        System.out.println("Total Events: " + events.size());
        System.out.println("======================================");
    }
}