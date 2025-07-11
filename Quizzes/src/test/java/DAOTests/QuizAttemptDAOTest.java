package DAOTests;

import DAO.QuizAttemptDAO;
import bean.QuizAttempt;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizAttemptDAOTest {

    private Connection connection;
    private QuizAttemptDAO dao;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE QuizAttempts (" +
                            "attempt_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "quiz_id INT NOT NULL, " +
                            "score INT, " +
                            "time_taken_min DOUBLE, " +
                            "is_practice BOOLEAN, " +
                            "taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE UserAnswers (" +
                            "attempt_id INT NOT NULL, " +
                            "question_id INT NOT NULL, " +
                            "response_text TEXT" +
                            ")"
            );

        }

        dao = new QuizAttemptDAO(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS UserAnswers");
            stmt.execute("DROP TABLE IF EXISTS QuizAttempts");
        }
        connection.close();
    }

    @Test
    void testAddAttempt_setsId() throws SQLException {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(1);
        attempt.setQuizId(2);
        attempt.setScore(95);
        attempt.setTimeTakenMin(10.0);
        attempt.setPractice(false);

        dao.addAttempt(attempt);

        assertTrue(attempt.getAttemptId() > 0);

        List<QuizAttempt> attempts = dao.getAttemptsByUser(1);
        assertEquals(1, attempts.size());
        assertEquals(95, attempts.get(0).getScore());
    }

    @Test
    void testGetAttemptsByUser_realAndPractice() throws SQLException {
        QuizAttempt real = new QuizAttempt();
        real.setUserId(1);
        real.setQuizId(2);
        real.setScore(100);
        real.setTimeTakenMin(5);
        real.setPractice(false);
        dao.addAttempt(real);

        QuizAttempt practice = new QuizAttempt();
        practice.setUserId(1);
        practice.setQuizId(2);
        practice.setScore(50);
        practice.setTimeTakenMin(15);
        practice.setPractice(true);
        dao.addAttempt(practice);

        List<QuizAttempt> all = dao.getAttemptsByUser(1);
        assertEquals(2, all.size());

        List<QuizAttempt> realOnly = dao.getRealAttemptsByUser(1);
        assertEquals(1, realOnly.size());
        assertFalse(realOnly.get(0).isPractice());

        List<QuizAttempt> practiceOnly = dao.getPracticeAttemptsByUser(1);
        assertEquals(1, practiceOnly.size());
        assertTrue(practiceOnly.get(0).isPractice());
    }

    @Test
    void testGetAttemptsByQuiz_filtersAndOrder() throws SQLException {
        QuizAttempt a1 = new QuizAttempt();
        a1.setUserId(1);
        a1.setQuizId(42);
        a1.setScore(80);
        a1.setTimeTakenMin(6);
        a1.setPractice(false);
        dao.addAttempt(a1);

        QuizAttempt a2 = new QuizAttempt();
        a2.setUserId(2);
        a2.setQuizId(42);
        a2.setScore(90);
        a2.setTimeTakenMin(5);
        a2.setPractice(false);
        dao.addAttempt(a2);

        QuizAttempt a3 = new QuizAttempt();
        a3.setUserId(3);
        a3.setQuizId(42);
        a3.setScore(85);
        a3.setTimeTakenMin(7);
        a3.setPractice(true);
        dao.addAttempt(a3);

        List<QuizAttempt> all = dao.getAttemptsByQuiz(42);
        assertEquals(3, all.size());
        assertEquals(90, all.get(0).getScore());

        List<QuizAttempt> realOnly = dao.getRealAttemptsByQuiz(42);
        assertEquals(2, realOnly.size());
        assertFalse(realOnly.get(0).isPractice());

        List<QuizAttempt> practiceOnly = dao.getPracticeAttemptsByQuiz(42);
        assertEquals(1, practiceOnly.size());
        assertTrue(practiceOnly.get(0).isPractice());
    }

    @Test
    void testDeleteAttemptsByQuiz() throws SQLException {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(1);
        attempt.setQuizId(88);
        attempt.setScore(70);
        attempt.setTimeTakenMin(8);
        attempt.setPractice(false);
        dao.addAttempt(attempt);

        assertFalse(dao.getAttemptsByQuiz(88).isEmpty());

        dao.deleteAttemptsByQuiz(88);

        assertTrue(dao.getAttemptsByQuiz(88).isEmpty());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        int attemptId = 5;
        int userId = 10;
        int quizId = 20;
        int score = 99;
        double timeTakenMin = 7.5;
        boolean isPractice = true;
        Timestamp takenAt = new Timestamp(System.currentTimeMillis());

        QuizAttempt attempt = new QuizAttempt(attemptId, userId, quizId, score, timeTakenMin, isPractice, takenAt);

        assertEquals(attemptId, attempt.getAttemptId());
        assertEquals(userId, attempt.getUserId());
        assertEquals(quizId, attempt.getQuizId());
        assertEquals(score, attempt.getScore());
        assertEquals(timeTakenMin, attempt.getTimeTakenMin());
        assertTrue(attempt.isPractice());
        assertEquals(takenAt, attempt.getTakenAt());
    }

    @Test
    void testGetAttemptById() throws SQLException {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(1);
        attempt.setQuizId(2);
        attempt.setScore(77);
        attempt.setTimeTakenMin(12.0);
        attempt.setPractice(false);
        dao.addAttempt(attempt);

        QuizAttempt loaded = dao.getAttempt(attempt.getAttemptId());
        assertNotNull(loaded);
        assertEquals(77, loaded.getScore());
        assertEquals(1, loaded.getUserId());
    }

    @Test
    void testGetResponsesByAttemptAndQuestion() throws SQLException {
        // Manually insert attempt + responses
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(1);
        attempt.setQuizId(3);
        attempt.setScore(50);
        attempt.setTimeTakenMin(9.0);
        attempt.setPractice(true);
        dao.addAttempt(attempt);
        int attemptId = attempt.getAttemptId();

        try (PreparedStatement st = connection.prepareStatement(
                "INSERT INTO UserAnswers (attempt_id, question_id, response_text) VALUES (?, ?, ?)")) {
            st.setInt(1, attemptId);
            st.setInt(2, 42);
            st.setString(3, "Answer A");
            st.executeUpdate();

            st.setInt(1, attemptId);
            st.setInt(2, 42);
            st.setString(3, "Answer B");
            st.executeUpdate();
        }

        List<String> responses = dao.getResponsesByAttemptAndQuestion(attemptId, 42);
        assertEquals(2, responses.size());
        assertTrue(responses.contains("Answer A"));
        assertTrue(responses.contains("Answer B"));
    }

    @Test
    void testUpdateScore() throws SQLException {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(1);
        attempt.setQuizId(2);
        attempt.setScore(60);
        attempt.setTimeTakenMin(10.0);
        attempt.setPractice(false);
        dao.addAttempt(attempt);

        dao.updateScore(attempt.getAttemptId(), 99);
        QuizAttempt updated = dao.getAttempt(attempt.getAttemptId());

        assertEquals(99, updated.getScore());
    }

}
