package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import dao.UserDAO;
import model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UserHandler implements HttpHandler {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        String path = exchange.getRequestURI().getPath();

        if ("/api/register".equals(path)) {

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405,
                        """
                        {
                          "status":"error",
                          "message":"POST method required"
                        }
                        """);
                return;
            }

            handleRegister(exchange);
            return;
        }

        if ("/api/login".equals(path)) {

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405,
                        """
                        {
                          "status":"error",
                          "message":"POST method required"
                        }
                        """);
                return;
            }

            handleLogin(exchange);
            return;
        }

        sendResponse(exchange, 404,
                """
                {
                  "status":"error",
                  "message":"API endpoint not found"
                }
                """);
    }

    // =========================================================
    // REGISTER
    // =========================================================

    private void handleRegister(
            HttpExchange exchange) throws IOException {

        try {

            String body = readRequestBody(exchange);

            String name =
                    getJsonValue(body, "name");

            String email =
                    getJsonValue(body, "email");

            String password =
                    getJsonValue(body, "password");

            if (isEmpty(name)
                    || isEmpty(email)
                    || isEmpty(password)) {

                sendResponse(exchange, 400,
                        """
                        {
                          "status":"error",
                          "message":"Name, email and password are required"
                        }
                        """);

                return;
            }

            User user = new User(
                    name,
                    email,
                    password,
                    "USER"
            );

            boolean registered =
                    userDAO.registerUser(user);

            if (registered) {

                sendResponse(exchange, 201,
                        """
                        {
                          "status":"success",
                          "message":"Account created successfully"
                        }
                        """);

            } else {

                sendResponse(exchange, 400,
                        """
                        {
                          "status":"error",
                          "message":"Unable to create account"
                        }
                        """);
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(exchange, 500,
                    """
                    {
                      "status":"error",
                      "message":"Registration failed"
                    }
                    """);
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void handleLogin(
            HttpExchange exchange) throws IOException {

        try {

            String body = readRequestBody(exchange);

            String email =
                    getJsonValue(body, "email");

            String password =
                    getJsonValue(body, "password");

            if (isEmpty(email)
                    || isEmpty(password)) {

                sendResponse(exchange, 400,
                        """
                        {
                          "status":"error",
                          "message":"Email and password are required"
                        }
                        """);

                return;
            }

            User user =
                    userDAO.loginUser(
                            email,
                            password
                    );

            if (user != null) {

                String response =
                        "{"
                        + "\"status\":\"success\","
                        + "\"message\":\"Login successful\","
                        + "\"user\":{"
                        + "\"id\":" + user.getId() + ","
                        + "\"name\":\"" + escape(user.getName()) + "\","
                        + "\"email\":\"" + escape(user.getEmail()) + "\","
                        + "\"role\":\"" + escape(user.getRole()) + "\""
                        + "}"
                        + "}";

                sendResponse(
                        exchange,
                        200,
                        response
                );

            } else {

                sendResponse(exchange, 401,
                        """
                        {
                          "status":"error",
                          "message":"Invalid email or password"
                        }
                        """);
            }

        } catch (Exception e) {

            e.printStackTrace();

            sendResponse(exchange, 500,
                    """
                    {
                      "status":"error",
                      "message":"Login failed"
                    }
                    """);
        }
    }

    // =========================================================
    // READ REQUEST BODY
    // =========================================================

    private String readRequestBody(
            HttpExchange exchange) throws IOException {

        return new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    // =========================================================
    // SIMPLE JSON VALUE READER
    // =========================================================

    private String getJsonValue(
            String json,
            String key) {

        String search =
                "\"" + key + "\"";

        int keyIndex =
                json.indexOf(search);

        if (keyIndex == -1) {
            return "";
        }

        int colonIndex =
                json.indexOf(
                        ":",
                        keyIndex + search.length()
                );

        if (colonIndex == -1) {
            return "";
        }

        int firstQuote =
                json.indexOf(
                        "\"",
                        colonIndex + 1
                );

        if (firstQuote == -1) {
            return "";
        }

        int secondQuote =
                json.indexOf(
                        "\"",
                        firstQuote + 1
                );

        if (secondQuote == -1) {
            return "";
        }

        return json.substring(
                firstQuote + 1,
                secondQuote
        ).trim();
    }

    private boolean isEmpty(String value) {
        return value == null
                || value.trim().isEmpty();
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
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
}