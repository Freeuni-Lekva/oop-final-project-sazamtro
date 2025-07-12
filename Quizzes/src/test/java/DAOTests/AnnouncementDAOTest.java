package DAOTests;

import DAO.AnnouncementDAO;
import DAO.UserDAO;
import bean.Announcement;
import bean.Hasher;
import bean.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementDAOTest {

    private Connection connection;
    private UserDAO userDAO;
    private AnnouncementDAO announcementDAO;

    @BeforeEach
    void setUp() throws Exception {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        userDAO = new UserDAO(connection);
        announcementDAO = new AnnouncementDAO(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE Users (" +
                            "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(255) NOT NULL UNIQUE, " +
                            "password_hash VARCHAR(255) NOT NULL, " +
                            "profilePicture_url VARCHAR(255), " +
                            "is_admin BOOLEAN DEFAULT FALSE" +
                            ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE Announcements (" +
                            "announcement_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "administrator_id INT NOT NULL, " +
                            "announcement_text TEXT NOT NULL, " +
                            "done_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (administrator_id) REFERENCES Users(user_id) ON DELETE CASCADE" +
                            ")"
            );
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Announcements");
            stmt.execute("DROP TABLE IF EXISTS Users");
        }
        connection.close();
    }


    @Test
    void testAddAnnouncement() throws Exception {
        String hashedPassword = Hasher.hashPassword("pass");
        User admin = new User(0, "adminUser", hashedPassword, null, true);
        userDAO.addUser(admin);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Announcement announcement = new Announcement(0, admin.getUserId(), "adminUser", "Site will be down at 11 PM", now);
        announcementDAO.addAnnouncement(announcement);

        assertTrue(announcement.getId() > 0, "Announcement ID should be generated");
        assertEquals("adminUser", announcement.getAdministratorUsername());
        assertNotNull(announcement.getDoneAt());
        assertEquals(now.toInstant().toEpochMilli(), announcement.getDoneAt().toInstant().toEpochMilli(), "Timestamps should match exactly");
    }

    @Test
    void testGetAllAnnouncements() throws Exception {
        String hashedPassword = Hasher.hashPassword("pass");
        User admin = new User(0, "admin", hashedPassword, null, true);
        userDAO.addUser(admin);

        // explicit timestamps
        Timestamp ts1 = Timestamp.valueOf("2025-01-01 10:00:00");
        Timestamp ts2 = Timestamp.valueOf("2025-01-01 11:00:00");

        announcementDAO.addAnnouncement(new Announcement(0, admin.getUserId(), "admin", "First announcement", ts1));
        announcementDAO.addAnnouncement(new Announcement(0, admin.getUserId(), "admin", "Second announcement", ts2));

        List<Announcement> announcements = announcementDAO.getAllAnnouncements();

        assertEquals(2, announcements.size());
        assertEquals("Second announcement", announcements.get(0).getText());
        assertEquals("First announcement", announcements.get(1).getText());
    }

    @Test
    void testGetAllAnnouncements_Empty() throws Exception {
        List<Announcement> announcements = announcementDAO.getAllAnnouncements();

        assertTrue(announcements.isEmpty());
    }

    @Test
    void testSetAnnouncementText() throws Exception{
        String hashedPassword = Hasher.hashPassword("pass");
        User admin = new User(0, "admin", hashedPassword, null, true);
        userDAO.addUser(admin);

        // explicit timestamps
        Timestamp ts1 = Timestamp.valueOf("2025-01-01 10:00:00");
        Timestamp ts2 = Timestamp.valueOf("2025-01-01 11:00:00");

        announcementDAO.addAnnouncement(new Announcement(0, admin.getUserId(), "admin", "First announcement", ts1));
        announcementDAO.addAnnouncement(new Announcement(0, admin.getUserId(), "admin", "Second announcement", ts2));

        List<Announcement> announcements = announcementDAO.getAllAnnouncements();

        assertEquals(2, announcements.size());
        assertEquals("Second announcement", announcements.get(0).getText());
        assertEquals("First announcement", announcements.get(1).getText());

        announcementDAO.setAnnouncementText(announcements.get(0).getId(), "Third announcement");
        announcementDAO.setAnnouncementText(announcements.get(1).getId(), "Fourth announcement");

        announcements = announcementDAO.getAllAnnouncements();

        assertEquals("Third announcement", announcements.get(0).getText());
        assertEquals("Fourth announcement", announcements.get(1).getText());
    }

    @Test
    void testDeleteAnnouncement()throws Exception{
        String hashedPassword = Hasher.hashPassword("pass");
        User admin = new User(0, "admin", hashedPassword, null, true);
        userDAO.addUser(admin);

        // explicit timestamps
        Timestamp ts1 = Timestamp.valueOf("2025-01-01 10:00:00");
        Timestamp ts2 = Timestamp.valueOf("2025-01-01 11:00:00");

        announcementDAO.addAnnouncement(new Announcement(0, admin.getUserId(), "admin", "First announcement", ts1));
        announcementDAO.addAnnouncement(new Announcement(0, admin.getUserId(), "admin", "Second announcement", ts2));

        List<Announcement> announcements = announcementDAO.getAllAnnouncements();

        assertEquals(2, announcements.size());
        assertEquals("Second announcement", announcements.get(0).getText());
        assertEquals("First announcement", announcements.get(1).getText());

        announcementDAO.deleteAnnouncement(announcements.get(0).getId());
        announcements = announcementDAO.getAllAnnouncements();
        assertEquals(1, announcements.size());
        assertEquals("First announcement", announcements.get(0).getText());
    }
}
