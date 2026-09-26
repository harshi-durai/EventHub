package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.EventDAO;
import model.Event;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EventHandler implements HttpHandler {

    private final EventDAO eventDAO = new EventDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

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

            List<Event> events = eventDAO.getAllEvents();

            StringBuilder json = new StringBuilder();

            json.append("[");

            for (int i = 0; i < events.size(); i++) {

                Event event = events.get(i);

                json.append("{")
                        .append("\"id\":")
                        .append(event.getId())
                        .append(",")

                        .append("\"name\":\"")
                        .append(escape(event.getName()))
                        .append("\",")

                        .append("\"description\":\"")
                        .append(escape(event.getDescription()))
                        .append("\",")

                        .append("\"eventDate\":\"")
                        .append(escape(event.getEventDate()))
                        .append("\",")

                        .append("\"eventTime\":\"")
                        .append(escape(event.getEventTime()))
                        .append("\",")

                        .append("\"venue\":\"")
                        .append(escape(event.getVenue()))
                        .append("\",")

                        .append("\"ticketPrice\":")
                        .append(event.getTicketPrice())
                        .append(",")

                        .append("\"capacity\":")
                        .append(event.getCapacity())
                        .append(",")

                        .append("\"status\":\"")
                        .append(escape(event.getStatus()))
                        .append("\"")

                        .append("}");

                if (i < events.size() - 1) {
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
                      "message": "Failed to load events"
                    }
                    """
            );
        }
    }

    private void addCorsHeaders(HttpExchange exchange) {

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

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        exchange.getResponseBody().write(bytes);

        exchange.getResponseBody().close();
    }

    private String escape(String value) {

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