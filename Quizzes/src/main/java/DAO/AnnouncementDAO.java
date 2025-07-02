package DAO;

import bean.Announcement;
import bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {
    private Connection connection;

    public AnnouncementDAO(Connection connection) {
        this.connection = connection;
    }

    // add new announcement
    public void addAnnouncement(Announcement announcement) throws SQLException {
        String query = "INSERT INTO Announcements (administrator_id, announcement_text) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, announcement.getAdministratorId());
            ps.setString(2, announcement.getText());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    // update announcement object with new ID
                    int newAnnouncementId = rs.getInt(1);
                    announcement.setId(newAnnouncementId);
                }
            }
        }
    }

    // get all announcements based on admin's name
    public List<Announcement> getAllAnnouncements() throws SQLException {
        String query = "SELECT a.announcement_id, a.administrator_id, u.username, " +
                "a.announcement_text, a.done_at " +
                "FROM Announcements a " +
                "JOIN Users u ON a.administrator_id = u.user_id " +
                "ORDER BY a.done_at DESC";

        List<Announcement> announcements = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Announcement announcement = new Announcement(
                        rs.getInt("announcement_id"),
                        rs.getInt("administrator_id"),
                        rs.getString("username"),
                        rs.getString("announcement_text"),
                        rs.getTimestamp("done_at")
                );

                announcements.add(announcement);
            }
        }

        return announcements;
    }
}
