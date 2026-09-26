
package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Seat;

public class SeatDAO {

    // =========================================================
    // GET ALL SEATS FOR AN EVENT
    // =========================================================

    public List<Seat> getSeatsByEvent(int eventId) {

        List<Seat> seats = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    event_id,
                    seat_number,
                    status
                FROM seats
                WHERE event_id = ?
                ORDER BY id
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, eventId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Seat seat =
                            new Seat(
                                    resultSet.getInt("id"),
                                    resultSet.getInt("event_id"),
                                    resultSet.getString("seat_number"),
                                    resultSet.getString("status")
                            );

                    seats.add(seat);
                }
            }

            System.out.println(
                    "SeatDAO: Event "
                            + eventId
                            + " -> "
                            + seats.size()
                            + " seats loaded."
            );

        } catch (Exception e) {

            /*
             * IMPORTANT:
             *
             * Do not silently return [] without showing
             * the actual database problem.
             */
            System.out.println(
                    "SeatDAO database error for event "
                            + eventId
            );

            e.printStackTrace();
        }

        return seats;
    }

    // =========================================================
    // GET SINGLE SEAT BY ID
    // =========================================================

    public Seat getSeatById(int seatId) {

        String sql = """
                SELECT
                    id,
                    event_id,
                    seat_number,
                    status
                FROM seats
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, seatId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return new Seat(
                            resultSet.getInt("id"),
                            resultSet.getInt("event_id"),
                            resultSet.getString("seat_number"),
                            resultSet.getString("status")
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "SeatDAO database error for seat "
                            + seatId
            );

            e.printStackTrace();
        }

        return null;
    }
}