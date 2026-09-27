package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.BookingDAO;
import dao.SeatDAO;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import model.Seat;

public class BookingHandler implements HttpHandler {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final SeatDAO seatDAO = new SeatDAO();

    private static final String JSON_CONTENT_TYPE =
            "application/json; charset=UTF-8";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        /*
         * IMPORTANT:
         * CORS headers are added BEFORE handling
         * any request.
         */
        addCorsHeaders(exchange);

        String method =
                exchange.getRequestMethod();

        String path =
                exchange.getRequestURI().getPath();

        System.out.println();
        System.out.println("=================================");
        System.out.println("BOOKING API REQUEST");
        System.out.println("Method: " + method);
        System.out.println("Path: " + path);
        System.out.println("Query: "
                + exchange.getRequestURI().getRawQuery());
        System.out.println("=================================");

        /*
         * =====================================================
         * OPTIONS
         * =====================================================
         */

        if ("OPTIONS".equalsIgnoreCase(method)) {

            exchange.sendResponseHeaders(
                    204,
                    -1
            );

            exchange.close();

            return;
        }

        try {

            /*
             * =================================================
             * GET /api/bookings?userId=2
             * =================================================
             */

            if (
                    "/api/bookings".equals(path)
                            && "GET".equalsIgnoreCase(method)
            ) {

                handleGetBookings(exchange);

                return;
            }

            /*
             * =================================================
             * POST /api/bookings
             * =================================================
             */

            if (
                    "/api/bookings".equals(path)
                            && "POST".equalsIgnoreCase(method)
            ) {

                handleCreateBooking(exchange);

                return;
            }

            /*
             * =================================================
             * DELETE /api/bookings/{bookingId}?userId=2
             * =================================================
             */

            if (
                    path.startsWith("/api/bookings/")
                            && "DELETE".equalsIgnoreCase(method)
            ) {

                handleCancelBooking(
                        exchange,
                        path
                );

                return;
            }

            /*
             * =================================================
             * METHOD NOT ALLOWED
             * =================================================
             */

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

        } catch (Exception e) {

            System.out.println(
                    "Booking API unexpected error!"
            );

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    """
                    {
                      "status":"error",
                      "message":"Internal server error"
                    }
                    """
            );
        }
    }

    // =========================================================
    // GET BOOKINGS
    // GET /api/bookings?userId=2
    // =========================================================

    private void handleGetBookings(
            HttpExchange exchange) throws IOException {

        try {

            String query =
                    exchange.getRequestURI()
                            .getRawQuery();

            int userId =
                    getQueryInt(
                            query,
                            "userId"
                    );

            System.out.println(
                    "Loading bookings for user: "
                            + userId
            );

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

            List<Booking> bookings =
                    bookingDAO.getBookingsByUser(
                            userId
                    );

            StringBuilder json =
                    new StringBuilder();

            json.append("{");
            json.append("\"status\":\"success\",");
            json.append("\"userId\":");
            json.append(userId);
            json.append(",");
            json.append("\"bookings\":[");

            for (
                    int i = 0;
                    i < bookings.size();
                    i++
            ) {

                Booking booking =
                        bookings.get(i);

                if (i > 0) {
                    json.append(",");
                }

                String eventName =
                        bookingDAO.getEventName(
                                booking.getEventId()
                        );

                String eventDate =
                        bookingDAO.getEventDate(
                                booking.getEventId()
                        );

                String eventVenue =
                        bookingDAO.getEventVenue(
                                booking.getEventId()
                        );

                String bookedSeats =
                        bookingDAO.getBookedSeats(
                                booking.getId()
                        );

                String ticketCode =
                        bookingDAO.getTicketCode(
                                booking.getId()
                        );

                json.append("{");

                json.append("\"id\":");
                json.append(
                        booking.getId()
                );
                json.append(",");

                json.append("\"bookingId\":");
                json.append(
                        booking.getId()
                );
                json.append(",");

                json.append("\"userId\":");
                json.append(
                        booking.getUserId()
                );
                json.append(",");

                json.append("\"eventId\":");
                json.append(
                        booking.getEventId()
                );
                json.append(",");

                json.append("\"eventName\":\"");
                json.append(
                        escape(eventName)
                );
                json.append("\",");

                json.append("\"eventDate\":\"");
                json.append(
                        escape(eventDate)
                );
                json.append("\",");

                json.append("\"venue\":\"");
                json.append(
                        escape(eventVenue)
                );
                json.append("\",");

                json.append("\"seats\":\"");
                json.append(
                        escape(bookedSeats)
                );
                json.append("\",");

                json.append("\"ticketCode\":\"");
                json.append(
                        escape(ticketCode)
                );
                json.append("\",");

                json.append("\"totalAmount\":");
                json.append(
                        booking.getTotalAmount()
                );
                json.append(",");

                json.append("\"status\":\"");
                json.append(
                        escape(
                                booking.getStatus()
                        )
                );
                json.append("\"");

                json.append("}");
            }

            json.append("]");
            json.append("}");

            System.out.println(
                    "Bookings found: "
                            + bookings.size()
            );

            sendResponse(
                    exchange,
                    200,
                    json.toString()
            );

        } catch (Exception e) {

            System.out.println(
                    "GET BOOKINGS FAILED!"
            );

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    """
                    {
                      "status":"error",
                      "message":"Unable to load bookings"
                    }
                    """
            );
        }
    }

    // =========================================================
    // CREATE BOOKING
    // POST /api/bookings
    // =========================================================

    private void handleCreateBooking(
            HttpExchange exchange) throws IOException {

        try {

            String body =
                    new String(
                            exchange
                                    .getRequestBody()
                                    .readAllBytes(),
                            StandardCharsets.UTF_8
                    );

            System.out.println();
            System.out.println(
                    "Booking request received:"
            );
            System.out.println(body);

            int userId =
                    getInt(
                            body,
                            "userId"
                    );

            int eventId =
                    getInt(
                            body,
                            "eventId"
                    );

            double totalAmount =
                    getDouble(
                            body,
                            "totalAmount"
                    );

            List<Integer> seatIds =
                    getSeatIds(body);

            System.out.println(
                    "User ID: "
                            + userId
            );

            System.out.println(
                    "Event ID: "
                            + eventId
            );

            System.out.println(
                    "Seat IDs: "
                            + seatIds
            );

            /*
             * =================================================
             * VALIDATION
             * =================================================
             */

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

            /*
             * =================================================
             * LOAD SELECTED SEATS
             * =================================================
             */

            List<Seat> selectedSeats =
                    new ArrayList<>();

            for (
                    Integer seatId :
                    seatIds
            ) {

                if (seatId == null || seatId <= 0) {

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

                Seat seat =
                        seatDAO.getSeatById(
                                seatId
                        );

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

                /*
                 * Make sure seat belongs to event.
                 */

                if (
                        seat.getEventId()
                                != eventId
                ) {

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

                /*
                 * Make sure seat is available.
                 */

                if (
                        !"AVAILABLE".equalsIgnoreCase(
                                seat.getStatus()
                        )
                ) {

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

                selectedSeats.add(
                        seat
                );
            }

            /*
             * =================================================
             * CREATE BOOKING OBJECT
             * =================================================
             */

            Booking booking =
                    new Booking();

            booking.setUserId(
                    userId
            );

            booking.setEventId(
                    eventId
            );

            booking.setTotalAmount(
                    totalAmount
            );

            booking.setStatus(
                    "CONFIRMED"
            );

            booking.setSeatIds(
                    seatIds
            );

            /*
             * =================================================
             * CREATE BOOKING
             * =================================================
             */

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

            /*
             * =================================================
             * GET TICKET
             * =================================================
             */

            String ticketCode =
                    bookingDAO.getTicketCode(
                            booking.getId()
                    );

            String bookedSeats =
                    bookingDAO.getBookedSeats(
                            booking.getId()
                    );

            /*
             * =================================================
             * SUCCESS
             * =================================================
             */

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
                            + escape(
                            bookedSeats
                    )
                            + "\","
                            + "\"ticketCode\":\""
                            + escape(
                            ticketCode
                    )
                            + "\","
                            + "\"bookingStatus\":\""
                            + escape(
                            booking.getStatus()
                    )
                            + "\""
                            + "}";

            System.out.println(
                    "Booking created successfully."
            );

            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (Exception e) {

            System.out.println(
                    "BOOKING CREATION FAILED!"
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
    // CANCEL BOOKING
    // DELETE /api/bookings/{bookingId}?userId=2
    // =========================================================

    private void handleCancelBooking(
            HttpExchange exchange,
            String path) throws IOException {

        try {

            String idPart =
                    path.substring(
                            "/api/bookings/"
                                    .length()
                    );

            if (
                    idPart == null
                            || idPart.isBlank()
            ) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Invalid booking ID"
                        }
                        """
                );

                return;
            }

            int bookingId;

            try {

                bookingId =
                        Integer.parseInt(
                                idPart
                        );

            } catch (NumberFormatException e) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Invalid booking ID"
                        }
                        """
                );

                return;
            }

            String query =
                    exchange.getRequestURI()
                            .getRawQuery();

            int userId =
                    getQueryInt(
                            query,
                            "userId"
                    );

            System.out.println(
                    "Cancelling booking: "
                            + bookingId
            );

            System.out.println(
                    "User ID: "
                            + userId
            );

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

            boolean success =
                    bookingDAO.cancelBooking(
                            bookingId,
                            userId
                    );

            if (!success) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Unable to cancel booking"
                        }
                        """
                );

                return;
            }

            sendResponse(
                    exchange,
                    200,
                    """
                    {
                      "status":"success",
                      "message":"Booking cancelled successfully"
                    }
                    """
            );

        } catch (Exception e) {

            System.out.println(
                    "CANCEL BOOKING FAILED!"
            );

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    """
                    {
                      "status":"error",
                      "message":"Unable to cancel booking"
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
                getValue(
                        json,
                        key
                );

        if (
                value == null
                        || value.isBlank()
        ) {

            return 0;
        }

        try {

            return Integer.parseInt(
                    value.trim()
            );

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
                getValue(
                        json,
                        key
                );

        if (
                value == null
                        || value.isBlank()
        ) {

            return 0;
        }

        try {

            return Double.parseDouble(
                    value.trim()
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // =========================================================
    // GET SEAT IDS
    //
    // Supports:
    //
    // "seatIds":[16,17,18]
    //
    // =========================================================

    private List<Integer> getSeatIds(
            String json) {

        List<Integer> seatIds =
                new ArrayList<>();

        int start =
                json.indexOf(
                        "\"seatIds\""
                );

        if (start == -1) {
            return seatIds;
        }

        int open =
                json.indexOf(
                        "[",
                        start
                );

        if (open == -1) {
            return seatIds;
        }

        int close =
                json.indexOf(
                        "]",
                        open
                );

        if (close == -1) {
            return seatIds;
        }

        String array =
                json.substring(
                        open + 1,
                        close
                );

        if (array.isBlank()) {
            return seatIds;
        }

        String[] values =
                array.split(",");

        for (
                String value :
                values
        ) {

            try {

                String clean =
                        value
                                .trim()
                                .replace(
                                        "\"",
                                        ""
                                );

                /*
                 * Handle accidental object format:
                 * {"id":24}
                 */

                if (
                        clean.contains(
                                "\"id\""
                        )
                ) {

                    int colon =
                            clean.indexOf(":");

                    if (colon != -1) {

                        clean =
                                clean.substring(
                                        colon + 1
                                );
                    }
                }

                if (!clean.isBlank()) {

                    int seatId =
                            Integer.parseInt(
                                    clean.trim()
                            );

                    if (seatId > 0) {

                        seatIds.add(
                                seatId
                        );
                    }
                }

            } catch (Exception ignored) {

                System.out.println(
                        "Unable to parse seat value: "
                                + value
                );
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
                json.indexOf(
                        search
                );

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
                        json.charAt(start)
                )
        ) {

            start++;
        }

        /*
         * String value
         */

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

        /*
         * Number / boolean
         */

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
    // GET INTEGER FROM QUERY STRING
    //
    // Example:
    // userId=2
    //
    // =========================================================

    private int getQueryInt(
            String query,
            String key) {

        if (
                query == null
                        || query.isBlank()
        ) {

            return 0;
        }

        String[] parameters =
                query.split("&");

        for (
                String parameter :
                parameters
        ) {

            String[] pair =
                    parameter.split(
                            "=",
                            2
                    );

            if (
                    pair.length == 2
                            && pair[0].equalsIgnoreCase(
                            key
                    )
            ) {

                try {

                    return Integer.parseInt(
                            pair[1]
                    );

                } catch (Exception e) {

                    return 0;
                }
            }
        }

        return 0;
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
                "Content-Type, Authorization"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Max-Age",
                "86400"
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                JSON_CONTENT_TYPE
        );
    }

    // =========================================================
    // SEND RESPONSE
    // =========================================================

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response) throws IOException {

        /*
         * Add CORS again before sending response.
         * This makes sure error responses also contain
         * CORS headers.
         */

        addCorsHeaders(exchange);

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try {

            exchange.getResponseBody()
                    .write(bytes);

        } finally {

            exchange.getResponseBody()
                    .close();
        }
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
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\r",
                        "\\r"
                );
    }
}