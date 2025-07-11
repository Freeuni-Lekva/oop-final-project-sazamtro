package DAOTests;

import DAO.AnswerDAO;
import DAO.QuestionsDAO;
import DAO.QuizAttemptDAO;
import bean.Questions.Question;
import bean.Questions.QuestionFactory;
import bean.Questions.QuestionResponse;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import DAO.QuizDAO;
import bean.Quiz;

import static org.junit.jupiter.api.Assertions.*;

public class QuizDAOTest {

    private Connection connection;
    private QuizDAO quizDAO;
    private QuestionsDAO questionsDAO;
    private QuizAttemptDAO quizAttemptDAO;
    private AnswerDAO answerDAO;

    @BeforeEach
    void setup() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement st = connection.createStatement()){
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
                            "question_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "quiz_id INT NOT NULL,\n" +
                            "type VARCHAR(20) NOT NULL,\n" +
                            "prompt TEXT NOT NULL,\n" +
                            "image_url TEXT,\n" +
                            "position INT,\n" +
                            "\n" +
                            "FOREIGN KEY (quiz_id) REFERENCES Quizzes (quiz_id) ON DELETE CASCADE\n" +
                            ");\n"
            );

            st.executeUpdate(
                    "CREATE TABLE UserAnswers (" +
                            "answer_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "attempt_id INT NOT NULL, " +
                            "question_id INT NOT NULL, " +
                            "response_text TEXT, " +
                            "is_correct BOOLEAN" +
                            ")"
            );

            st.executeUpdate("CREATE TABLE CorrectAnswers (" +
                    "answer_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "question_id INT NOT NULL, " +
                    "text TEXT NOT NULL" +
                    ")");

        }
        quizDAO = new QuizDAO(connection);
        questionsDAO = new QuestionsDAO(connection);
        quizAttemptDAO = new QuizAttemptDAO(connection);
        answerDAO = new AnswerDAO(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Quizzes CASCADE");
            stmt.execute("DROP TABLE IF EXISTS QuizAttempts");
            stmt.execute("DROP TABLE IF EXISTS Questions");
            stmt.execute("DROP TABLE IF EXISTS UserAnswers");
            stmt.execute("DROP TABLE IF EXISTS CorrectAnswers");
        }
        connection.close();
    }

    @Test
    void testInsertNewQuiz() throws SQLException {  //And getOneQuiz
        //Quiz q = new Quiz(0, "my_quiz", "description_1", 1, false, false, false, LocalDateTime.now());
        int id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        //quizDAO.
        assertTrue(id > 0);
        Quiz quiz = quizDAO.getOneQuiz(id);
        assertNotNull(quiz);
        assertEquals("my_quiz", quiz.getQuizTitle());
        assertEquals("description_1", quiz.getQuizDescription());
        assertEquals(1, quiz.getCreator_id());
    }

    @Test
    void testGetAllQuizzes() throws SQLException {
        assertEquals(0, quizDAO.getAllQuizzes().size());
        int id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        assertTrue(id > 0);
        //Quiz quiz =
        assertEquals(1, quizDAO.getAllQuizzes().size());
        quizDAO.removeQuiz(id);
        assertEquals(0, quizDAO.getAllQuizzes().size());
    }

    @Test
    void testGetQuizQuestions() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        assertEquals(0, quizDAO.getQuizQuestions(quiz_id).size());
        Question q = new QuestionResponse(quiz_id, "prompt text",  2);
        int question_id = questionsDAO.insertQuestion(q);
        assertEquals(1, quizDAO.getQuizQuestions(quiz_id).size());
        int quiz_id1 = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, true, false, false);
        Question q1 = new QuestionResponse(quiz_id1, "prompt text",  -1);
        int qustion_id1 = questionsDAO.insertQuestion(q1);
        assertEquals(1, quizDAO.getQuizQuestions(quiz_id1).size());
    }

    @Test
    void testInsertAttempt() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        int attempt_id = quizDAO.insertAttempt(1, quiz_id, 3, 3.0, true);
        assertNotNull(quizAttemptDAO.getPracticeAttemptsByQuiz(quiz_id));
    }

    @Test
    void testGetUserQuizzes() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        assertEquals(1, quizDAO.getUserQuizzes(1).size());
    }

    @Test
    void testUpdateQuizMeta() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        Quiz quiz = quizDAO.getOneQuiz(quiz_id);
        assertEquals("my_quiz", quiz.getQuizTitle());
        assertFalse(quiz.checkIfRandom());
        quizDAO.updateQuizMeta(quiz_id, "new_title", "description_2", true, false, false);
        quiz = quizDAO.getOneQuiz(quiz_id);
        assertEquals("new_title", quiz.getQuizTitle());
        assertTrue(quiz.checkIfRandom());
    }

    @Test
    void testGetUserAttempts() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        int quiz_id1 = quizDAO.insertNewQuiz("her_quiz", "description_2", 2, false, false, false);
        int attempt_id = quizDAO.insertAttempt(3, quiz_id, 3, 3.0, true);
        int attempt_id1 = quizDAO.insertAttempt(3, quiz_id1, 4, 2.0, true);
        int attempt_id2 = quizDAO.insertAttempt(3, quiz_id1, 4, 3.0, true);
        assertEquals(1, quizDAO.getUserAttempts(3, quiz_id).size());
        assertEquals(2, quizDAO.getUserAttempts(3, quiz_id1).size());
    }

    @Test
    void testGetQuestionsForPractice() throws SQLException {
        int quiz_id = quizDAO.insertNewQuiz("my_quiz", "description_1", 1, false, false, false);
        Question q = new QuestionResponse(quiz_id, "prompt text",  2);
        int question_id = questionsDAO.insertQuestion(q);
        assertEquals(1, quizDAO.getQuestionsForPractice(quiz_id, 3, answerDAO).size());
        int attempt_id = quizDAO.insertAttempt(3, quiz_id, 3, 3.0, true);
        int attempt_id1 = quizDAO.insertAttempt(3, quiz_id, 4, 2.0, true);
        int answer_id = answerDAO.insertUserAnswer("answer", attempt_id, question_id, true);
        int answer_id1 = answerDAO.insertUserAnswer("answer1", attempt_id1, question_id, false);
        assertEquals(1, quizDAO.getQuestionsForPractice(quiz_id, 3, answerDAO).size());
    }
}
