
package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class ApiServer {

    /*
     * Local:
     *   http://localhost:8080
     *
     * Deployment:
     *   Hosting platform provides PORT automatically.
     */
    private static final int PORT =
            Integer.parseInt(
                    System.getenv().getOrDefault(
                            "PORT",
                            "8080"
                    )
            );

    public static void main(String[] args) {

        try {

            HttpServer server =
                    HttpServer.create(
                            new InetSocketAddress(
                                    "0.0.0.0",
                                    PORT
                            ),
                            0
                    );

            // =================================================
            // HEALTH CHECK
            // =================================================

            server.createContext(
                    "/",
                    ApiServer::handleHealth
            );

            // =================================================
            // EVENTS
            // =================================================

            server.createContext(
                    "/api/events",
                    new EventHandler()
            );

            // =================================================
            // SEATS
            // =================================================

            /*
             * Example:
             *
             * GET /api/events/1/seats
             * GET /api/events/2/seats
             * GET /api/events/3/seats
             */
            server.createContext(
                    "/api/events/",
                    new SeatHandler()
            );

            // =================================================
            // REGISTER
            // =================================================

            server.createContext(
                    "/api/register",
                    new UserHandler()
            );

            // =================================================
            // LOGIN
            // =================================================

            server.createContext(
                    "/api/login",
                    new UserHandler()
            );

            // =================================================
            // BOOKINGS
            // =================================================

            /*
             * POST /api/bookings
             *
             * This was missing from your ApiServer.
             */
            server.createContext(
                    "/api/bookings",
                    new BookingHandler()
            );

            // =================================================
            // EXECUTOR
            // =================================================

            server.setExecutor(null);

            // =================================================
            // START SERVER
            // =================================================

            server.start();

            System.out.println("--------------------------------");
            System.out.println(" EVENTHUB JAVA BACKEND");
            System.out.println("--------------------------------");

            System.out.println(
                    "Server running on port: " + PORT
            );

            System.out.println();

            System.out.println("Available APIs:");

            System.out.println("GET  /");
            System.out.println("GET  /api/events");
            System.out.println(
                    "GET  /api/events/{id}/seats"
            );

            System.out.println(
                    "POST /api/register"
            );

            System.out.println(
                    "POST /api/login"
            );

            System.out.println(
                    "POST /api/bookings"
            );

            System.out.println("--------------------------------");
            System.out.println(
                    "Backend is running."
            );

            System.out.println(
                    "Press Ctrl+C to stop."
            );

            // Keep server alive
            CountDownLatch latch =
                    new CountDownLatch(1);

            latch.await();

        } catch (IOException e) {

            System.out.println(
                    "Failed to start EventHub backend."
            );

            e.printStackTrace();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Backend server stopped."
            );
        }
    }

    // =========================================================
    // HEALTH CHECK
    // =========================================================

    private static void handleHealth(
            HttpExchange exchange
    ) throws IOException {

        addCorsHeaders(exchange);

        // OPTIONS
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

        // GET only
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

        String response =
                """
                {
                  "status": "online",
                  "message": "EventHub Java Backend is running"
                }
                """;

        sendResponse(
                exchange,
                200,
                response
        );
    }

    // =========================================================
    // CORS
    // =========================================================

    private static void addCorsHeaders(
            HttpExchange exchange
    ) {

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

    private static void sendResponse(
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
}