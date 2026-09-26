
package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.BookingDAO;
import dao.SeatDAO;
import model.Booking;
import model.Seat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BookingHandler implements HttpHandler {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final SeatDAO seatDAO = new SeatDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        String path = exchange.getRequestURI().getPath();

        // =====================================================
        // POST /api/bookings
        // =====================================================

        if (
                "/api/bookings".equals(path)
                        && "POST".equalsIgnoreCase(
                        exchange.getRequestMethod())
        ) {

            handleCreateBooking(exchange);
            return;
        }

        // =====================================================
        // METHOD NOT ALLOWED
        // =====================================================

        sendResponse(
                exchange,
                405,
                """
                {
                  "status":"error",
                  "message":"Method not allowed"
                }
                """
        );
    }

    // =========================================================
    // CREATE BOOKING
    // =========================================================

    private void handleCreateBooking(
            HttpExchange exchange) throws IOException {

        try {

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            System.out.println();
            System.out.println("Booking request received:");
            System.out.println(body);

            int userId = getInt(body, "userId");
            int eventId = getInt(body, "eventId");
            double totalAmount = getDouble(body, "totalAmount");

            List<Integer> seatIds =
                    getSeatIds(body);

            // -------------------------------------------------
            // Basic validation
            // -------------------------------------------------

            if (userId <= 0) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Invalid user ID"
                        }
                        """
                );

                return;
            }

            if (eventId <= 0) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Invalid event ID"
                        }
                        """
                );

                return;
            }

            if (seatIds.isEmpty()) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Please select at least one seat"
                        }
                        """
                );

                return;
            }

            // -------------------------------------------------
            // Load selected seats from database
            // -------------------------------------------------

            List<Seat> selectedSeats =
                    new ArrayList<>();

            for (Integer seatId : seatIds) {

                Seat seat =
                        seatDAO.getSeatById(seatId);

                if (seat == null) {

                    sendResponse(
                            exchange,
                            400,
                            """
                            {
                              "status":"error",
                              "message":"Invalid seat ID"
                            }
                            """
                    );

                    return;
                }

                // Make sure seat belongs to requested event
                if (seat.getEventId() != eventId) {

                    sendResponse(
                            exchange,
                            400,
                            """
                            {
                              "status":"error",
                              "message":"Seat does not belong to this event"
                            }
                            """
                    );

                    return;
                }

                // Make sure seat is available
                if (!"AVAILABLE".equalsIgnoreCase(
                        seat.getStatus())) {

                    sendResponse(
                            exchange,
                            409,
                            """
                            {
                              "status":"error",
                              "message":"One or more selected seats are already booked"
                            }
                            """
                    );

                    return;
                }

                selectedSeats.add(seat);
            }

            // -------------------------------------------------
            // Create Booking object
            // -------------------------------------------------

            Booking booking =
                    new Booking();

            booking.setUserId(userId);
            booking.setEventId(eventId);
            booking.setTotalAmount(totalAmount);
            booking.setStatus("CONFIRMED");
            booking.setSeatIds(seatIds);

            // -------------------------------------------------
            // Create booking using existing DAO
            // -------------------------------------------------

            boolean success =
                    bookingDAO.createBooking(
                            booking,
                            selectedSeats
                    );

            if (!success) {

                sendResponse(
                        exchange,
                        500,
                        """
                        {
                          "status":"error",
                          "message":"Unable to create booking"
                        }
                        """
                );

                return;
            }

            // -------------------------------------------------
            // Get generated ticket
            // -------------------------------------------------

            String ticketCode =
                    bookingDAO.getTicketCode(
                            booking.getId()
                    );

            String bookedSeats =
                    bookingDAO.getBookedSeats(
                            booking.getId()
                    );

            // -------------------------------------------------
            // Success response
            // -------------------------------------------------

            String response =
                    "{"
                            + "\"status\":\"success\","
                            + "\"message\":\"Booking confirmed successfully\","
                            + "\"bookingId\":"
                            + booking.getId()
                            + ","
                            + "\"userId\":"
                            + booking.getUserId()
                            + ","
                            + "\"eventId\":"
                            + booking.getEventId()
                            + ","
                            + "\"totalAmount\":"
                            + booking.getTotalAmount()
                            + ","
                            + "\"seats\":\""
                            + escape(bookedSeats)
                            + "\","
                            + "\"ticketCode\":\""
                            + escape(ticketCode)
                            + "\","
                            + "\"bookingStatus\":\""
                            + escape(booking.getStatus())
                            + "\""
                            + "}";

            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (Exception e) {

            System.out.println(
                    "Booking API failed!"
            );

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    """
                    {
                      "status":"error",
                      "message":"Unable to create booking"
                    }
                    """
            );
        }
    }

    // =========================================================
    // GET INTEGER FROM JSON
    // =========================================================

    private int getInt(
            String json,
            String key) {

        String value =
                getValue(json, key);

        if (
                value == null
                        || value.isBlank()
        ) {
            return 0;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // =========================================================
    // GET DOUBLE FROM JSON
    // =========================================================

    private double getDouble(
            String json,
            String key) {

        String value =
                getValue(json, key);

        if (
                value == null
                        || value.isBlank()
        ) {
            return 0;
        }

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // =========================================================
    // GET SEAT IDS
    // =========================================================

    private List<Integer> getSeatIds(
            String json) {

        List<Integer> seatIds =
                new ArrayList<>();

        int start =
                json.indexOf("\"seatIds\"");

        if (start == -1) {
            return seatIds;
        }

        int open =
                json.indexOf(
                        "[",
                        start
                );

        int close =
                json.indexOf(
                        "]",
                        open
                );

        if (
                open == -1
                        || close == -1
        ) {
            return seatIds;
        }

        String array =
                json.substring(
                        open + 1,
                        close
                );

        String[] values =
                array.split(",");

        for (String value : values) {

            try {

                String clean =
                        value
                                .trim()
                                .replace(
                                        "\"",
                                        ""
                                );

                if (!clean.isBlank()) {

                    seatIds.add(
                            Integer.parseInt(clean)
                    );
                }

            } catch (Exception ignored) {
            }
        }

        return seatIds;
    }

    // =========================================================
    // GET SIMPLE JSON VALUE
    // =========================================================

    private String getValue(
            String json,
            String key) {

        String search =
                "\"" + key + "\"";

        int keyPosition =
                json.indexOf(search);

        if (keyPosition == -1) {
            return null;
        }

        int colon =
                json.indexOf(
                        ":",
                        keyPosition
                );

        if (colon == -1) {
            return null;
        }

        int start =
                colon + 1;

        while (
                start < json.length()
                        && Character.isWhitespace(
                        json.charAt(start))
        ) {
            start++;
        }

        if (
                start < json.length()
                        && json.charAt(start) == '"'
        ) {

            start++;

            int end =
                    json.indexOf(
                            "\"",
                            start
                    );

            if (end == -1) {
                return null;
            }

            return json.substring(
                    start,
                    end
            );
        }

        int end =
                start;

        while (
                end < json.length()
                        && json.charAt(end) != ','
                        && json.charAt(end) != '}'
        ) {
            end++;
        }

        return json.substring(
                start,
                end
        ).trim();
    }

    // =========================================================
    // CORS
    // =========================================================

    private void addCorsHeaders(
            HttpExchange exchange) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );
    }

    // =========================================================
    // SEND RESPONSE
    // =========================================================

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response) throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        exchange.getResponseBody().write(bytes);

        exchange.getResponseBody().close();
    }

    // =========================================================
    // ESCAPE JSON
    // =========================================================

    private String escape(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
