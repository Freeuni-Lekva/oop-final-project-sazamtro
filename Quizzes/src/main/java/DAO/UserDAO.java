package DAO;

import bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                    String profilePictureUrl = rs.getString("profilePicture_url");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    return new User(userId, username, hashedPassword, profilePictureUrl, isAdmin);
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
                    String profilePictureUrl = rs.getString("profilePicture_url");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    return new User(userId, username, hashedPassword, profilePictureUrl, isAdmin);
                }
            }
        }
        return null;
    }

    // add new user
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO Users (username, password_hash, profilePicture_url, is_admin) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getProfilePictureUrl());
            ps.setBoolean(4, user.checkIfAdmin());

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

    // removes user
    // using 'ON DELETE CASCADE' in the SQL to automatically remove its records from all tables."
    public void removeUser(int userId) throws SQLException {
        String query = "DELETE FROM Users WHERE user_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public void updateProfilePicture(int user_id, String url) throws SQLException{
        String update = "UPDATE Users SET profilePicture_url = ? WHERE user_id = ?";
        PreparedStatement ps = connection.prepareStatement(update);
        ps.setString(1, url);
        ps.setInt(2, user_id);
        ps.executeUpdate();
    }

    public List<User> getAllUsers() throws SQLException{
        List<User> result = new ArrayList<>();
        String query = "SELECT user_id FROM Users";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            result.add(getUserById(rs.getInt(1)));
        }
        return result;
    }

    public void promoteUser(int user_id) throws SQLException{
        String update = "UPDATE Users SET is_admin = TRUE where user_id = ?";
        PreparedStatement ps = connection.prepareStatement(update);
        ps.setInt(1, user_id);
        ps.executeUpdate();
    }
}