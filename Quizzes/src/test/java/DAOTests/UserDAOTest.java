package DAOTests;

import DAO.UserDAO;
import bean.Hasher;
import bean.User;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private Connection connection;
    private UserDAO userDAO;


    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
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
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Users");
        }
        connection.close();
    }


    @Test
    void testAddUser() throws Exception {
        String hashedPassword = Hasher.hashPassword("pass");
        User user = new User(0, "gvantsa", hashedPassword, "", true);

        userDAO.addUser(user);

        assertTrue(user.getUserId() > 0, "user id should be generated and set");

        User dbUser = userDAO.getUserById(user.getUserId());
        assertNotNull(dbUser);
        assertEquals("gvantsa", dbUser.getUsername());
        assertEquals(hashedPassword, dbUser.getPassword());
        assertTrue(dbUser.checkIfAdmin());
    }

    @Test
    void testAddUser_duplicate() throws Exception {
        String username_duplicate = "tsotne";

        String hashPassword1 = Hasher.hashPassword("abc");
        User user1 = new User(0, username_duplicate, hashPassword1, null, false);
        userDAO.addUser(user1);

        String hashPassword2 = Hasher.hashPassword("xyz");
        User user2 = new User(0, username_duplicate, hashPassword2, null, false);

        assertThrows(SQLException.class, () ->
                userDAO.addUser(user2)
        );
    }

    @Test
    void testGetUserById() throws Exception {
        String hashedPassword = Hasher.hashPassword("pass");
        User user = new User(0, "irakli", hashedPassword, "", false);
        userDAO.addUser(user);

        int userId = user.getUserId();
        User dbUser = userDAO.getUserById(userId);

        assertNotNull(dbUser);
        assertEquals(userId, dbUser.getUserId());
        assertEquals("irakli", dbUser.getUsername());
        assertEquals(hashedPassword, dbUser.getPassword());
        assertFalse(dbUser.checkIfAdmin());
    }

    @Test
    void testGetUserById_NotFoundReturnsNull() throws SQLException {
        assertNull(userDAO.getUserById(100000));
    }

    @Test
    void testGetUserByUsername() throws Exception {
        String hashedPassword = Hasher.hashPassword("mypass");
        User user = new User(0, "taso", hashedPassword, null, false);
        userDAO.addUser(user);

        User dbUser = userDAO.getUserByUsername("taso");

        String profilePictureUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Lavandula_angustifolia_1.jpg/320px-Lavandula_angustifolia_1.jpg";
        dbUser.setProfilePictureUrl(profilePictureUrl);

        assertNotNull(dbUser);
        assertEquals("taso", dbUser.getUsername());
        assertEquals(hashedPassword, dbUser.getPassword());
        assertEquals(profilePictureUrl, dbUser.getProfilePictureUrl());
        assertFalse(dbUser.checkIfAdmin());
    }

    @Test
    void testGetUserByUsername_NotFoundReturnsNull() throws SQLException {
        assertNull(userDAO.getUserByUsername("nobody"));
    }

    @Test
    void testRemoveUser() throws Exception {
        String hashedPassword = Hasher.hashPassword("pass");
        User user = new User(0, "taso", hashedPassword, null, false);
        userDAO.addUser(user);
        int userId = user.getUserId();

        userDAO.removeUser(userId);

        assertNull(userDAO.getUserById(userId));
        assertNull(userDAO.getUserByUsername("taso"));
    }
}
