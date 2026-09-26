package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/eventhub"
                            + "?useSSL=false"
                            + "&allowPublicKeyRetrieval=true"
                            + "&serverTimezone=UTC"
            );

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "");

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "       EVENTHUB DATABASE TEST"
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Database connected successfully!"
            );

            System.out.println(
                    "Database: eventhub"
            );

            System.out.println(
                    "======================================"
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database connection failed!"
            );

            e.printStackTrace();
        }
    }
}