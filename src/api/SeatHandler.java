
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
    public void handle(HttpExchange exchange)
            throws IOException {

        addCorsHeaders(exchange);

        // =====================================================
        // OPTIONS
        // =====================================================

        if ("OPTIONS".equalsIgnoreCase(
                exchange.getRequestMethod()
        )) {

            exchange.sendResponseHeaders(
                    204,
                    -1
            );

            exchange.close();

            return;
        }

        // =====================================================
        // GET ONLY
        // =====================================================

        if (!"GET".equalsIgnoreCase(
                exchange.getRequestMethod()
        )) {

            sendResponse(
                    exchange,
                    405,
                    """
                    {
                      "status":"error",
                      "message":"GET method required"
                    }
                    """
            );

            return;
        }

        try {

            String path =
                    exchange.getRequestURI()
                            .getPath();

            System.out.println();
            System.out.println(
                    "Seat API request: " + path
            );

            // =================================================
            // EXPECTED:
            //
            // /api/events/3/seats
            // =================================================

            String[] parts =
                    path.split("/");

            /*
             * parts[0] = ""
             * parts[1] = "api"
             * parts[2] = "events"
             * parts[3] = event ID
             * parts[4] = "seats"
             */

            if (parts.length != 5
                    || !"api".equals(parts[1])
                    || !"events".equals(parts[2])
                    || !"seats".equalsIgnoreCase(parts[4])) {

                sendResponse(
                        exchange,
                        400,
                        """
                        {
                          "status":"error",
                          "message":"Invalid seat API URL"
                        }
                        """
                );

                return;
            }

            // =================================================
            // EVENT ID
            // =================================================

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
                          "status":"error",
                          "message":"Invalid event ID"
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
                          "message":"Event ID must be greater than zero"
                        }
                        """
                );

                return;
            }

            // =================================================
            // LOAD SEATS
            // =================================================

            List<Seat> seats =
                    seatDAO.getSeatsByEvent(eventId);

            // =================================================
            // BUILD JSON
            // =================================================

            StringBuilder json =
                    new StringBuilder();

            json.append("[");

            for (int i = 0;
                 i < seats.size();
                 i++) {

                Seat seat =
                        seats.get(i);

                json.append("{");

                json.append("\"id\":")
                        .append(seat.getId())
                        .append(",");

                json.append("\"eventId\":")
                        .append(seat.getEventId())
                        .append(",");

                json.append("\"seatNumber\":\"")
                        .append(
                                escape(
                                        seat.getSeatNumber()
                                )
                        )
                        .append("\",");

                json.append("\"status\":\"")
                        .append(
                                escape(
                                        seat.getStatus()
                                )
                        )
                        .append("\"");

                json.append("}");

                if (i < seats.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            System.out.println(
                    "Seats returned: "
                            + seats.size()
            );

            sendResponse(
                    exchange,
                    200,
                    json.toString()
            );

        } catch (Exception e) {

            System.out.println(
                    "Seat API failed!"
            );

            e.printStackTrace();

            sendResponse(
                    exchange,
                    500,
                    """
                    {
                      "status":"error",
                      "message":"Failed to load seats from database"
                    }
                    """
            );
        }
    }

    // =========================================================
    // CORS
    // =========================================================

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

    // =========================================================
    // SEND RESPONSE
    // =========================================================

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

    // =========================================================
    // JSON ESCAPE
    // =========================================================

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