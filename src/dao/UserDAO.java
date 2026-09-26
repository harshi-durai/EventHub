package dao;

import database.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // ==============================
    // REGISTER USER
    // ==============================

    public boolean registerUser(User user) {

        String sql = """
                INSERT INTO users (name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Registration failed!");
            e.printStackTrace();

            return false;
        }
    }

    // ==============================
    // LOGIN USER
    // ==============================

    public User loginUser(String email, String password) {

        String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = ? AND password = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                );
            }

        } catch (SQLException e) {

            System.out.println("Login failed!");
            e.printStackTrace();
        }

        return null;
    }
}