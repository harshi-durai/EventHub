
package dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import model.Seat;

public class BookingDAO {

    // =========================================================
    // CREATE BOOKING
    // =========================================================

    public boolean createBooking(
            Booking booking,
            List<Seat> selectedSeats) {

        String bookingSQL = """
                INSERT INTO bookings
                (user_id, event_id, total_amount, status)
                VALUES (?, ?, ?, 'CONFIRMED')
                """;

        String seatSQL = """
                INSERT INTO booking_seats
                (booking_id, seat_id)
                VALUES (?, ?)
                """;

        String updateSeatSQL = """
                UPDATE seats
                SET status = 'BOOKED'
                WHERE id = ?
                AND status = 'AVAILABLE'
                """;

        String ticketSQL = """
                INSERT INTO tickets
                (booking_id, ticket_code)
                VALUES (?, ?)
                """;

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            int bookingId;

            // -------------------------------------------------
            // 1. Create booking
            // -------------------------------------------------

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 bookingSQL,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                statement.setInt(
                        1,
                        booking.getUserId()
                );

                statement.setInt(
                        2,
                        booking.getEventId()
                );

                statement.setDouble(
                        3,
                        booking.getTotalAmount()
                );

                statement.executeUpdate();

                try (ResultSet keys =
                             statement.getGeneratedKeys()) {

                    if (!keys.next()) {

                        throw new Exception(
                                "Unable to create booking ID."
                        );
                    }

                    bookingId = keys.getInt(1);
                }
            }

            // -------------------------------------------------
            // 2. Book selected seats
            // -------------------------------------------------

            for (Seat seat : selectedSeats) {

                int updatedRows;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     updateSeatSQL
                             )) {

                    statement.setInt(
                            1,
                            seat.getId()
                    );

                    updatedRows =
                            statement.executeUpdate();
                }

                if (updatedRows == 0) {

                    throw new Exception(
                            "Seat "
                                    + seat.getSeatNumber()
                                    + " is no longer available."
                    );
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     seatSQL
                             )) {

                    statement.setInt(
                            1,
                            bookingId
                    );

                    statement.setInt(
                            2,
                            seat.getId()
                    );

                    statement.executeUpdate();
                }
            }

            // -------------------------------------------------
            // 3. Generate digital ticket
            // -------------------------------------------------

            String ticketCode =
                    "EVH-"
                            + System.currentTimeMillis();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 ticketSQL
                         )) {

                statement.setInt(
                        1,
                        bookingId
                );

                statement.setString(
                        2,
                        ticketCode
                );

                statement.executeUpdate();
            }

            // -------------------------------------------------
            // 4. Commit transaction
            // -------------------------------------------------

            connection.commit();

            booking.setId(
                    bookingId
            );

            System.out.println(
                    "Booking created successfully!"
            );

            System.out.println(
                    "Booking ID: "
                            + bookingId
            );

            System.out.println(
                    "Ticket: "
                            + ticketCode
            );

            return true;

        } catch (Exception e) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackError) {

                rollbackError.printStackTrace();
            }

            System.out.println(
                    "Booking failed!"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (connection != null) {

                    connection.setAutoCommit(
                            true
                    );

                    connection.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    public boolean cancelBooking(
            int bookingId,
            int userId) {

        String checkBookingSQL = """
                SELECT status
                FROM bookings
                WHERE id = ?
                AND user_id = ?
                """;

        String getSeatsSQL = """
                SELECT seat_id
                FROM booking_seats
                WHERE booking_id = ?
                """;

        String releaseSeatSQL = """
                UPDATE seats
                SET status = 'AVAILABLE'
                WHERE id = ?
                AND status = 'BOOKED'
                """;

        String cancelBookingSQL = """
                UPDATE bookings
                SET status = 'CANCELLED'
                WHERE id = ?
                AND user_id = ?
                AND status = 'CONFIRMED'
                """;

        Connection connection = null;

        try {

            connection =
                    DBConnection.getConnection();

            connection.setAutoCommit(
                    false
            );

            // -------------------------------------------------
            // 1. Verify booking belongs to this user
            // -------------------------------------------------

            String currentStatus = null;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 checkBookingSQL
                         )) {

                statement.setInt(
                        1,
                        bookingId
                );

                statement.setInt(
                        2,
                        userId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println(
                                "Booking not found."
                        );

                        connection.rollback();

                        return false;
                    }

                    currentStatus =
                            resultSet.getString(
                                    "status"
                            );
                }
            }

            // -------------------------------------------------
            // 2. Check booking status
            // -------------------------------------------------

            if (
                    currentStatus == null
                            || !currentStatus.equalsIgnoreCase(
                            "CONFIRMED"
                    )
            ) {

                System.out.println(
                        "Booking cannot be cancelled. "
                                + "Current status: "
                                + currentStatus
                );

                connection.rollback();

                return false;
            }

            // -------------------------------------------------
            // 3. Release seats
            // -------------------------------------------------

            List<Integer> seatIds =
                    new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 getSeatsSQL
                         )) {

                statement.setInt(
                        1,
                        bookingId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    while (
                            resultSet.next()
                    ) {

                        seatIds.add(
                                resultSet.getInt(
                                        "seat_id"
                                )
                        );
                    }
                }
            }

            for (
                    Integer seatId :
                    seatIds
            ) {

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     releaseSeatSQL
                             )) {

                    statement.setInt(
                            1,
                            seatId
                    );

                    statement.executeUpdate();
                }
            }

            // -------------------------------------------------
            // 4. Mark booking as CANCELLED
            // -------------------------------------------------

            int updatedRows;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 cancelBookingSQL
                         )) {

                statement.setInt(
                        1,
                        bookingId
                );

                statement.setInt(
                        2,
                        userId
                );

                updatedRows =
                        statement.executeUpdate();
            }

            if (
                    updatedRows == 0
            ) {

                throw new Exception(
                        "Unable to cancel booking."
                );
            }

            // -------------------------------------------------
            // 5. Commit
            // -------------------------------------------------

            connection.commit();

            System.out.println(
                    "Booking cancelled successfully!"
            );

            System.out.println(
                    "Booking ID: "
                            + bookingId
            );

            System.out.println(
                    "Released seats: "
                            + seatIds.size()
            );

            return true;

        } catch (Exception e) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackError) {

                rollbackError.printStackTrace();
            }

            System.out.println(
                    "Booking cancellation failed!"
            );

            e.printStackTrace();

            return false;

        } finally {

            try {

                if (connection != null) {

                    connection.setAutoCommit(
                            true
                    );

                    connection.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    // =========================================================
    // GET BOOKINGS OF A USER
    // =========================================================

    public List<Booking> getBookingsByUser(
            int userId) {

        List<Booking> bookings =
                new ArrayList<>();

        String sql = """
                SELECT
                    b.id,
                    b.user_id,
                    b.event_id,
                    b.total_amount,
                    b.status
                FROM bookings b
                WHERE b.user_id = ?
                ORDER BY b.id DESC
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    userId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Booking booking =
                            new Booking(

                                    resultSet.getInt(
                                            "id"
                                    ),

                                    resultSet.getInt(
                                            "user_id"
                                    ),

                                    resultSet.getInt(
                                            "event_id"
                                    ),

                                    resultSet.getDouble(
                                            "total_amount"
                                    ),

                                    resultSet.getString(
                                            "status"
                                    )
                            );

                    bookings.add(
                            booking
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load bookings!"
            );

            e.printStackTrace();
        }

        return bookings;
    }


    // =========================================================
    // GET SEAT NAMES FOR A BOOKING
    // =========================================================

    public String getBookedSeats(
            int bookingId) {

        String sql = """
                SELECT s.seat_number
                FROM booking_seats bs
                JOIN seats s
                    ON bs.seat_id = s.id
                WHERE bs.booking_id = ?
                ORDER BY s.seat_number
                """;

        StringBuilder seats =
                new StringBuilder();

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    if (seats.length() > 0) {

                        seats.append(
                                ", "
                        );
                    }

                    seats.append(
                            resultSet.getString(
                                    "seat_number"
                            )
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load booked seats!"
            );

            e.printStackTrace();
        }

        return seats.toString();
    }


    // =========================================================
    // GET EVENT NAME
    // =========================================================

    public String getEventName(
            int eventId) {

        String sql = """
                SELECT name
                FROM events
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    eventId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString(
                            "name"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load event name!"
            );

            e.printStackTrace();
        }

        return "Unknown Event";
    }


    // =========================================================
    // GET EVENT DATE
    // =========================================================

    public String getEventDate(
            int eventId) {

        String sql = """
                SELECT event_date
                FROM events
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    eventId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString(
                            "event_date"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load event date!"
            );

            e.printStackTrace();
        }

        return "";
    }


    // =========================================================
    // GET EVENT VENUE
    // =========================================================

    public String getEventVenue(
            int eventId) {

        String sql = """
                SELECT venue
                FROM events
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    eventId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString(
                            "venue"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load event venue!"
            );

            e.printStackTrace();
        }

        return "";
    }


    // =========================================================
    // GET TICKET CODE
    // =========================================================

    public String getTicketCode(
            int bookingId) {

        String sql = """
                SELECT ticket_code
                FROM tickets
                WHERE booking_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    bookingId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString(
                            "ticket_code"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load ticket code!"
            );

            e.printStackTrace();
        }

        return "N/A";
    }
}

