package DAOTests;

import DAO.AnswerDAO;
import DAO.QuestionsDAO;
import DAO.QuizAttemptDAO;
import bean.Questions.*;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import DAO.QuizDAO;
import bean.Questions.QuestionFactory;
import bean.Quiz;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerDAOTest {

    private Connection connection;
    private QuizDAO quizDAO;
    private QuestionsDAO questionsDAO;
    private AnswerDAO answerDAO;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement st = connection.createStatement()){
            st.executeUpdate("CREATE TABLE UserAnswers (\n" +
                    "    answer_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    attempt_id INT NOT NULL,\n" +
                    "    question_id INT NOT NULL,\n" +
                    "    response_text TEXT,\n" +
                    "    is_correct BOOLEAN);");

            st.executeUpdate("CREATE TABLE AnswerOptions (\n" +
                    "    option_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    question_id INT NOT NULL,\n" +
                    "    text TEXT NOT NULL,\n" +
                    "    is_correct BOOLEAN DEFAULT FALSE);");

            st.executeUpdate("CREATE TABLE CorrectAnswers (\n" +
                    "    answer_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    question_id INT NOT NULL,\n" +
                    "    text TEXT NOT NULL);");

            st.executeUpdate("CREATE TABLE Quizzes (\n" +
                    "    quiz_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    title VARCHAR(255) NOT NULL,\n" +
                    "    description TEXT,\n" +
                    "    creator_id INT NOT NULL,\n" +
                    "    is_random BOOLEAN DEFAULT FALSE,\n" +
                    "    is_multipage BOOLEAN DEFAULT FALSE,\n" +
                    "    immediate_correction BOOLEAN DEFAULT FALSE,\n" +
                    "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
                    ");");

            st.executeUpdate("CREATE TABLE QuizAttempts (" +
                    "attempt_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "quiz_id INT NOT NULL, " +
                    "score INT, " +
                    "time_taken_min DOUBLE, " +
                    "is_practice BOOLEAN, " +
                    "taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")"
            );

            st.executeUpdate(
                    "CREATE TABLE Questions (\n" +
                            "    question_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "    quiz_id INT NOT NULL,\n" +
                            "    type VARCHAR(20) NOT NULL,\n" +
                            "    prompt TEXT NOT NULL,\n" +
                            "    image_url TEXT,\n" +
                            "    position INT\n" +
                            ");\n"
            );

            st.executeUpdate("INSERT INTO Quizzes (title, description, creator_id, is_random, is_multipage, immediate_correction) VALUES\n" +
                    "            ('Math Basics', 'A quiz about basic math.', 1, FALSE, FALSE, TRUE),\n" +
                    "            ('Geography Fun', 'Test your world knowledge!', 2, TRUE, TRUE, FALSE),\n" +
                    "            ('Science Quiz', 'Random science questions.', 1, TRUE, FALSE, FALSE);\n" +
                    "\n" +
                    "-- QUESTIONS\n" +
                    "INSERT INTO Questions (quiz_id, type, prompt, image_url, position) VALUES\n" +
                    "            (1, 'QUESTION_RESPONSE', 'What is 2 + 2?', NULL, 1),\n" +
                    "            (1, 'MULTIPLE_CHOICE', 'Select the prime number:', NULL, 2),\n" +
                    "            (2, 'QUESTION_RESPONSE', 'Capital of France?', NULL, 1),\n" +
                    "            (3, 'PICTURE_RESPONSE', 'Name this constellation.', 'https://www.thoughtco.com/thmb/N0DqcVSob63BmrpXu_mDhKWsM3M=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/orion-constellation-944405126-b2b06de820fd4458af81e0f864dcd413.jpg', 1);\n" +
                    "\n" +
                    "-- ANSWER OPTIONS (for question 2)\n" +
                    "INSERT INTO AnswerOptions (question_id, text, is_correct) VALUES\n" +
                    "            (2, '4', FALSE),\n" +
                    "            (2, '5', TRUE),\n" +
                    "            (2, '6', FALSE);\n" +
                    "\n" +
                    "-- CORRECT ANSWERS\n" +
                    "INSERT INTO CorrectAnswers (question_id, text) VALUES\n" +
                    "            (1, '4'),\n" +
                    "            (3, 'Paris');\n" +
                    "\n" +
                    "-- QUIZ ATTEMPTS\n" +
                    "INSERT INTO QuizAttempts (user_id, quiz_id, score, time_taken_min, is_practice) VALUES\n" +
                    "            (1, 1, 2, 1.5, FALSE),\n" +
                    "            (2, 1, 1, 2.0, TRUE),\n" +
                    "            (3, 2, 1, 3.5, FALSE);\n" +
                    "\n" +
                    "-- USER ANSWERS\n" +
                    "INSERT INTO UserAnswers (attempt_id, question_id, response_text, is_correct) VALUES\n" +
                    "            (1, 1, '4', TRUE),\n" +
                    "            (1, 2, '5', TRUE),\n" +
                    "            (2, 1, '5', FALSE),\n" +
                    "            (3, 3, 'Paris', TRUE);");

        }

        quizDAO = new QuizDAO(connection);
        questionsDAO = new QuestionsDAO(connection);
        answerDAO = new AnswerDAO(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS AnswerOptions");
            stmt.execute("DROP TABLE IF EXISTS UserAnswers");
            stmt.execute("DROP TABLE IF EXISTS CorrectAnswers");
            stmt.execute("DROP TABLE IF EXISTS Quizzes");
            stmt.execute("DROP TABLE IF EXISTS QuizAttempts");
            stmt.execute("DROP TABLE IF EXISTS Questions");
        }
        connection.close();
    }

    @Test
    void testInsertAnswer() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        Question q = new QuestionResponse(quiz_id, "prompt text",  2);
        AnswerOption answerOption = new AnswerOption(3, q.getId(), "answer", true);
        int a = questionsDAO.insertAnswerOptions(answerOption);
        questionsDAO.insertCorrectAnswerText(q.getId(), "text");
        int attempt_id = quizDAO.insertAttempt(2, quiz_id, 1, 2.0, true);
        int answer_id = answerDAO.insertUserAnswer("answer", attempt_id, q.getId(), true);
        assertNotNull(answer_id);
        assertTrue(answer_id > 0);
        assertTrue(answerDAO.checkAnswer(q.getId(), "answer"));
        assertTrue(answerDAO.checkAnswer(q.getId(), "text"));
    }

    @Test
    void testGetCorrectAnswers() throws SQLException {
        Question q = questionsDAO.getQuestionById(1);
        assertFalse(answerDAO.getCorrectAnswers(2).isEmpty());
        assertFalse(answerDAO.getCorrectAnswers(1).isEmpty());
    }

    @Test
    void testGetUserAnswers() throws SQLException {
        assertEquals(1, answerDAO.getUserAnswers(1, 1).size());
    }

}
