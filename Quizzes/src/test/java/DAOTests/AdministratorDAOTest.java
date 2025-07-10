package DAOTests;

import DAO.AdministratorDAO;
import DAO.UserDAO;
import bean.Hasher;
import bean.User;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorDAOTest {

    private Connection connection;
    private AdministratorDAO administratorDAO;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() throws Exception {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        administratorDAO = new AdministratorDAO(connection);
        userDAO = new UserDAO(connection);

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
                    "CREATE TABLE QuizAttempts (" +
                            "attempt_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "quiz_id INT NOT NULL, " +
                            "user_id INT NOT NULL, " +
                            "score INT" +
                            ")"
            );
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS QuizAttempts");
            stmt.execute("DROP TABLE IF EXISTS Users");
        }
        connection.close();
    }

    @Test
    void testAllGettersAndSettersOfUser() {
        User user = new User(0, "taso", "no", null, false);

        // set and get user ID
        user.setUserId(42);
        assertEquals(42, user.getUserId());

        // set and get profile picture
        assertNull(user.getProfilePictureUrl());
        user.setProfilePictureUrl("https://example.com/photo.jpg");
        assertEquals("https://example.com/photo.jpg", user.getProfilePictureUrl());

        // get username and password
        assertEquals("taso", user.getUsername());
        assertEquals("no", user.getPassword());

        // check admin status
        assertFalse(user.checkIfAdmin());
    }

    @Test
    void testPromoteUserToAdmin() throws Exception {
        String hashedPassword = Hasher.hashPassword("sazamtro");
        User user = new User(0, "gvanca", hashedPassword, null, false);
        userDAO.addUser(user);

        assertFalse(userDAO.getUserById(user.getUserId()).checkIfAdmin());

        administratorDAO.promoteUserToAdmin(user.getUserId());

        User updatedUser = userDAO.getUserById(user.getUserId());
        assertTrue(updatedUser.checkIfAdmin(), "User should be promoted to admin");
    }

    @Test
    void testGetNumUsers() throws Exception {
        assertEquals(0, administratorDAO.getNumUsers());

        userDAO.addUser(new User(0, "one", Hasher.hashPassword("a"), null, false));
        userDAO.addUser(new User(0, "two", Hasher.hashPassword("b"), null, true));

        assertEquals(2, administratorDAO.getNumUsers());
    }

    @Test
    void testGetNumAttempts() throws Exception {
        assertEquals(0, administratorDAO.getNumAttempts());

        // insert fake attempts
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO QuizAttempts (quiz_id, user_id, score) VALUES (?, ?, ?)")) {

            ps.setInt(1, 1);
            ps.setInt(2, 10); // dummy user
            ps.setInt(3, 90);
            ps.executeUpdate();

            ps.setInt(1, 2);
            ps.setInt(2, 20);
            ps.setInt(3, 80);
            ps.executeUpdate();
        }

        assertEquals(2, administratorDAO.getNumAttempts());
    }

    @Test
    void testClearQuizHistory() throws Exception {
        // Add quiz attempts for quizId = 1 and quizId = 2
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO QuizAttempts (quiz_id, user_id, score) VALUES (?, ?, ?)")) {
            ps.setInt(1, 1);
            ps.setInt(2, 10);
            ps.setInt(3, 95);
            ps.executeUpdate();

            ps.setInt(1, 1);
            ps.setInt(2, 11);
            ps.setInt(3, 85);
            ps.executeUpdate();

            ps.setInt(1, 2);
            ps.setInt(2, 12);
            ps.setInt(3, 75);
            ps.executeUpdate();
        }

        assertEquals(3, administratorDAO.getNumAttempts());

        administratorDAO.clearQuizHistory(1);
        assertEquals(1, administratorDAO.getNumAttempts(), "Only quiz_id=2 should remain");

        administratorDAO.clearQuizHistory(2);
        assertEquals(0, administratorDAO.getNumAttempts(), "Both of quizzes should be cleared");
    }
}
