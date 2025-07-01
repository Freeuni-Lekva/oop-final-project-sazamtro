package DAO;

import bean.User;

import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // check if user exists by its username
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String hashedPassword = rs.getString("password_hash");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    return new User(userId, username, hashedPassword, isAdmin);
                }
            }
        }

        return null;
    }

    // Returns User with the same userId
    public User getUserById(int userId) throws SQLException{
        String query = "SELECT * FROM Users WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String hashedPassword = rs.getString("password_hash");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    return new User(userId, username, hashedPassword, isAdmin);
                }
            }
        }
    }

    // add new user
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO Users (username, password_hash, is_admin) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.checkIfAdmin());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    // update user object with new ID
                    int newUserId = rs.getInt(1);
                    user.setUserId(newUserId);
                }
            }
        }
    }
}