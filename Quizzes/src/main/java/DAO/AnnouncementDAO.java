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
        String query;
        if(announcement.getDoneAt() == null) {
        query ="INSERT INTO Announcements (administrator_id, announcement_text) VALUES (?, ?)";
        }else{
            query = "INSERT INTO Announcements (administrator_id, announcement_text, done_at) VALUES (?, ?, ?)";
        }

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, announcement.getAdministratorId());
            ps.setString(2, announcement.getText());
            if(announcement.getDoneAt() != null){
                ps.setTimestamp(3, announcement.getDoneAt());
            }

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

    public void setAnnouncementText(int announcement_id, String text) throws SQLException{
        String update = "UPDATE Announcements SET announcement_text = ? where announcement_id = ?";
        PreparedStatement st = connection.prepareStatement(update);
        st.setString(1, text);
        st.setInt(2,announcement_id);
        st.executeUpdate();
    }

    public void deleteAnnouncement(int announcement_id) throws SQLException{
        String delete = "DELETE FROM Announcements WHERE announcement_id = ?";
        PreparedStatement st = connection.prepareStatement(delete);
        st.setInt(1, announcement_id);
        st.executeUpdate();
    }
}
