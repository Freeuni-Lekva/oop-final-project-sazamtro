package DAOTests;

import DAO.AchievementsDAO;
import bean.Achievement;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AchievementsDAOTest {

    private Connection connection;
    private AchievementsDAO achievementsDAO;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Achievements (" +
                    "achievement_id INT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "description VARCHAR(255), " +
                    "icon_url VARCHAR(255))");

            stmt.execute("CREATE TABLE UserAchievements (" +
                    "user_id INT, " +
                    "achievement_id INT)");

            stmt.execute("CREATE TABLE Users (" +
                    "user_id INT PRIMARY KEY, username VARCHAR(255))");

            stmt.execute("CREATE TABLE FriendRequests (" +
                    "from_user_id INT, to_user_id INT, status VARCHAR(20))");

            stmt.execute("CREATE TABLE Quizzes (" +
                    "quiz_id INT PRIMARY KEY AUTO_INCREMENT, creator_id INT)");

            stmt.execute("CREATE TABLE QuizAttempts (" +
                    "attempt_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT, quiz_id INT, score INT, is_practice BOOLEAN, taken_at TIMESTAMP)");

            stmt.execute("CREATE TABLE Questions (" +
                    "question_id INT PRIMARY KEY AUTO_INCREMENT, quiz_id INT)");
        }

        achievementsDAO = new AchievementsDAO(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO Achievements VALUES (1, 'Amateur Author', 'Create a quiz', 'icon1.png')");
            stmt.execute("INSERT INTO Achievements VALUES (2, 'Quiz Machine', 'Take 10 quizzes', 'icon2.png')");
            stmt.execute("INSERT INTO Achievements VALUES (3, 'Practice Makes Perfect', 'Practice once', 'icon3.png')");
            stmt.execute("INSERT INTO Achievements VALUES (4, 'Perfectionist', 'Get full score', 'icon4.png')");
            stmt.execute("INSERT INTO Achievements VALUES (5, 'Social Butterfly', 'Get 5 friends', 'icon5.png')");
            stmt.execute("INSERT INTO Achievements VALUES (6, 'Night Owl', 'Take a quiz at 3 AM', 'icon6.png')");

            stmt.execute("INSERT INTO Users VALUES (1, 'testuser')");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP ALL OBJECTS");
        }
        connection.close();
    }

    @Test
    void testGetAllAchievements() throws SQLException {
        List<Achievement> all = achievementsDAO.getAllAchievements();
        assertEquals(6, all.size());
        assertEquals("Amateur Author", all.get(0).getAchievement_name());
    }

    @Test
    void testGetUserAchievements_empty() throws SQLException {
        List<Achievement> achievements = achievementsDAO.getUserAchievements(1);
        assertTrue(achievements.isEmpty());
    }

    @Test
    void testGetUserAchievements_one() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO UserAchievements VALUES (1, 1)");
        }
        List<Achievement> achievements = achievementsDAO.getUserAchievements(1);
        assertEquals(1, achievements.size());
        assertEquals("Amateur Author", achievements.get(0).getAchievement_name());
    }

    @Test
    void testCheckAmateurAuthor_awardsAchievement() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO Quizzes (creator_id) VALUES (1)");
        }
        achievementsDAO.checkAmateurAuthor(1);

        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM UserAchievements WHERE user_id = 1 AND achievement_id = 1")) {
            assertTrue(rs.next());
        }
    }

    @Test
    void testCheckQuizMachine_awardsAchievement() throws SQLException {
        for (int i = 0; i < 10; i++) {
            connection.createStatement().execute(
                    "INSERT INTO QuizAttempts (user_id, quiz_id, score, is_practice, taken_at) " +
                            "VALUES (1, 1, 0, FALSE, CURRENT_TIMESTAMP)");
        }

        achievementsDAO.checkQuizMachine(1);

        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM UserAchievements WHERE user_id = 1 AND achievement_id = 2")) {
            assertTrue(rs.next());
        }
    }

    @Test
    void testCheckPracticeMode_awardsAchievement() throws SQLException {
        connection.createStatement().execute(
                "INSERT INTO QuizAttempts (user_id, quiz_id, score, is_practice, taken_at) " +
                        "VALUES (1, 1, 5, TRUE, CURRENT_TIMESTAMP)");

        achievementsDAO.checkPracticeMode(1);

        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM UserAchievements WHERE user_id = 1 AND achievement_id = 3")) {
            assertTrue(rs.next());
        }
    }

    @Test
    void testCheckPerfectionist_awardsAchievement() throws SQLException {
        // 3 questions in quiz 1
        for (int i = 0; i < 3; i++) {
            connection.createStatement().execute("INSERT INTO Questions (quiz_id) VALUES (1)");
        }
        // matching score attempt
        connection.createStatement().execute(
                "INSERT INTO QuizAttempts (user_id, quiz_id, score, is_practice, taken_at) " +
                        "VALUES (1, 1, 3, FALSE, CURRENT_TIMESTAMP)");

        achievementsDAO.checkPerfectionist(1, 1);

        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM UserAchievements WHERE user_id = 1 AND achievement_id = 4")) {
            assertTrue(rs.next());
        }
    }

    @Test
    void testCheckSocialButterfly_awardsAchievement() throws SQLException {
        for (int i = 2; i <= 6; i++) {
            connection.createStatement().execute("INSERT INTO Users VALUES (" + i + ", 'friend" + i + "')");
            connection.createStatement().execute("INSERT INTO FriendRequests VALUES (1, " + i + ", 'ACCEPTED')");
        }

        achievementsDAO.checkSocialButterfly(1);

        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM UserAchievements WHERE user_id = 1 AND achievement_id = 5")) {
            assertTrue(rs.next());
        }
    }

    @Test
    void testCheckNightOwl_awardsAchievement() throws SQLException {
        // insert quiz attempt at 3:00 AM
        connection.createStatement().execute(
                "INSERT INTO QuizAttempts (attempt_id, user_id, quiz_id, score, is_practice, taken_at) " +
                        "VALUES (101, 1, 1, 5, FALSE, '2024-01-01 03:00:00')");

        achievementsDAO.checkNightOwl(1, 101);

        try (ResultSet rs = connection.createStatement().executeQuery(
                "SELECT * FROM UserAchievements WHERE user_id = 1 AND achievement_id = 6")) {
            assertTrue(rs.next());
        }
    }

    @Test
    void testGetFriendsAchievements() throws SQLException {
        connection.createStatement().execute("INSERT INTO Users VALUES (2, 'friend')");
        connection.createStatement().execute("INSERT INTO FriendRequests VALUES (1, 2, 'ACCEPTED')");
        connection.createStatement().execute("INSERT INTO UserAchievements VALUES (2, 1)");

        List<Achievement> friendsAchievements = achievementsDAO.getFriendsAchievements(1);
        assertEquals(1, friendsAchievements.size());
        assertEquals("friend", friendsAchievements.get(0).getUsername());
    }


    @Test
    void testAchievementBeanMethods() {
        Achievement a = new Achievement(10, "Achiever", "Earned it", "icon.png");

        // Test getters
        assertEquals(10, a.getAchievement_id());
        assertEquals("Achiever", a.getAchievement_name());
        assertEquals("Earned it", a.getAchievement_descr());
        assertEquals("icon.png", a.getIcon_url());
        assertNull(a.getUsername());

        // Test setters
        a.setAchievement_id(20);
        a.setAchievement_name("Champion");
        a.setAchievement_descr("Won everything");
        a.setIcon_url("newicon.png");
        a.setUsername("bob");

        assertEquals(20, a.getAchievement_id());
        assertEquals("Champion", a.getAchievement_name());
        assertEquals("Won everything", a.getAchievement_descr());
        assertEquals("newicon.png", a.getIcon_url());
        assertEquals("bob", a.getUsername());

        // Also test 5-arg constructor
        Achievement b = new Achievement(5, "Fast Learner", "Complete 3 quizzes", "fast.png", "alice");
        assertEquals("alice", b.getUsername());
    }
}
