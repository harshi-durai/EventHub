
package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.SeatDAO;
import model.Seat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SeatHandler implements HttpHandler {

    private final SeatDAO seatDAO = new SeatDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        // Handle browser preflight request
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        // Only GET is allowed
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {

            sendResponse(
                    exchange,
                    405,
                    """
                    {
                      "status": "error",
                      "message": "Method not allowed"
                    }
                    """
            );

            return;
        }

        try {

            String path =
                    exchange.getRequestURI().getPath();

            /*
             * Expected:
             *
             * /api/events/1/seats
             *
             * /api/events/2/seats
             */

            String[] parts =
                    path.split("/");

            /*
             * parts:
             *
             * [0] = ""
             * [1] = "api"
             * [2] = "events"
             * [3] = eventId
             * [4] = "seats"
             */

            if (parts.length < 5) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status": "error",
                          "message": "Invalid event seat URL"
                        }
                        """
                );

                return;
            }

            int eventId;

            try {

                eventId =
                        Integer.parseInt(parts[3]);

            } catch (NumberFormatException e) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status": "error",
                          "message": "Invalid event ID"
                        }
                        """
                );

                return;
            }

            List<Seat> seats =
                    seatDAO.getSeatsByEvent(eventId);

            StringBuilder json =
                    new StringBuilder();

            json.append("[");

            for (int i = 0;
                 i < seats.size();
                 i++) {

                Seat seat =
                        seats.get(i);

                json.append("{")

                        .append("\"id\":")
                        .append(seat.getId())
                        .append(",")

                        .append("\"eventId\":")
                        .append(seat.getEventId())
                        .append(",")

                        .append("\"seatNumber\":\"")
                        .append(escape(
                                seat.getSeatNumber()
                        ))
                        .append("\",")

                        .append("\"status\":\"")
                        .append(escape(
                                seat.getStatus()
                        ))
                        .append("\"")

                        .append("}");

                if (i < seats.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    """
                    {
                      "status": "error",
                      "message": "Failed to load seats"
                    }
                    """
            );
        }
    }

    private void addCorsHeaders(
            HttpExchange exchange
    ) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, OPTIONS"
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

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        exchange.getResponseBody()
                .write(bytes);

        exchange.getResponseBody()
                .close();
    }

    private String escape(
            String value
    ) {

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

