package dao;

import database.DBConnection;
import model.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public List<Seat> getSeatsByEvent(int eventId) {

        List<Seat> seats = new ArrayList<>();

        String sql = """
                SELECT id, event_id, seat_number, status
                FROM seats
                WHERE event_id = ?
                ORDER BY seat_number
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, eventId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Seat seat = new Seat(
                            resultSet.getInt("id"),
                            resultSet.getInt("event_id"),
                            resultSet.getString("seat_number"),
                            resultSet.getString("status")
                    );

                    seats.add(seat);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load seats!"
            );

            e.printStackTrace();
        }

        return seats;
    }
}