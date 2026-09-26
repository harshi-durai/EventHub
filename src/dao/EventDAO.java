package dao;

import database.DBConnection;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    // ==============================
    // GET ALL UPCOMING EVENTS
    // ==============================

    public List<Event> getAllEvents() {

        List<Event> events = new ArrayList<>();

        String sql = """
                SELECT id, name, description, event_date, event_time,
                       venue, ticket_price, capacity, status
                FROM events
                WHERE status = 'UPCOMING'
                ORDER BY event_date ASC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Event event = new Event(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("event_date"),
                        resultSet.getString("event_time"),
                        resultSet.getString("venue"),
                        resultSet.getDouble("ticket_price"),
                        resultSet.getInt("capacity"),
                        resultSet.getString("status")
                );

                events.add(event);
            }

        } catch (Exception e) {

            System.out.println("Unable to load events!");
            e.printStackTrace();
        }

        return events;
    }

    // ==============================
    // GET EVENT BY ID
    // ==============================

    public Event getEventById(int eventId) {

        String sql = """
                SELECT id, name, description, event_date, event_time,
                       venue, ticket_price, capacity, status
                FROM events
                WHERE id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Event(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("event_date"),
                            resultSet.getString("event_time"),
                            resultSet.getString("venue"),
                            resultSet.getDouble("ticket_price"),
                            resultSet.getInt("capacity"),
                            resultSet.getString("status")
                    );
                }
            }

        } catch (Exception e) {

            System.out.println("Unable to find event!");
            e.printStackTrace();
        }

        return null;
    }
}