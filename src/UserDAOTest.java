package dao;

import database.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // ==========================================
    // LOGIN
    // ==========================================

    public User login(
            String email,
            String password
    ) {

        String sql =
                "SELECT id, name, email, password, role " +
                "FROM users " +
                "WHERE email = ? AND password = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    email
            );

            statement.setString(
                    2,
                    password
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    User user =
                            new User();

                    user.setId(
                            resultSet.getInt("id")
                    );

                    user.setName(
                            resultSet.getString("name")
                    );

                    user.setEmail(
                            resultSet.getString("email")
                    );

                    user.setPassword(
                            resultSet.getString("password")
                    );

                    user.setRole(
                            resultSet.getString("role")
                    );

                    return user;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Login database error:"
            );

            e.printStackTrace();
        }

        return null;
    }
}