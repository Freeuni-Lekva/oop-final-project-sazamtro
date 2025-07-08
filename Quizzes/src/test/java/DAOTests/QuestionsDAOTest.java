package DAOTests;

import DAO.QuestionsDAO;
import DAO.QuizDAO;
import DAO.UserDAO;
import bean.Questions.*;
import bean.User;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionsDAOTest {

    private Connection connection;
    private UserDAO userDAO;
    private QuestionsDAO questionsDAO;
    private QuizDAO quizDAO;


    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException, FileNotFoundException {
        Class.forName("org.h2.Driver");

        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        userDAO = new UserDAO(connection);
        questionsDAO = new QuestionsDAO(connection);
        quizDAO = new QuizDAO(connection);

        createTables();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS CorrectAnswers CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS AnswerOptions CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS Questions CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS Quizzes CASCADE");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users CASCADE");
        }
        connection.close();
    }
    @Test
    void testInsertQuestion() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizId = quizDAO.insertNewQuiz("Title", "Desc", userID, false, false, false);
        Question q = new QuestionResponse(quizId, "prompt text",  2);
        int questionId = questionsDAO.insertQuestion(q);
        assertTrue(questionId > 0);
    }

    @Test
    void testInsertOptions() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new MultipleChoice(quizID, "text", -1);
        int questionId = questionsDAO.insertQuestion(q);

        // Insert options
        questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "3", false));
        questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "4", true));

        List<AnswerOption> options = questionsDAO.getOptions(questionId);
        assertEquals(2, options.size());
    }

    @Test
    void testInsertTextAnswers() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new PictureResponse(quizID, "text", -1, "url");
        int questionId = questionsDAO.insertQuestion(q);

        // Insert options
        questionsDAO.insertCorrectAnswerText(questionId, "answer");

        assertTrue("answer".equals(questionsDAO.getCorrectAnswerText(questionId)));
    }

    @Test
    void testUpdatingQuestion() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new FillInQuestion(quizID, "text1", -1);
        int questionId = questionsDAO.insertQuestion(q);
        assertTrue(questionId > 0);
        questionsDAO.updateQuestion(new FillInQuestion(questionId, quizID, "text2", -1));
        Question updated = questionsDAO.getQuestionById(questionId);
        assertEquals("text2", updated.getQuestionText());
    }

    @Test
    void deleteAllOptions() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new MultipleChoice(quizID, "text1", -1);
        int questionId = questionsDAO.insertQuestion(q);
        questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "3", false));
        questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "4", true));
        questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "5", false));
        questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "6", false));

        List<AnswerOption> options = questionsDAO.getOptions(questionId);
        assertEquals(4, options.size());
        questionsDAO.deleteAllOptions(questionId);
        List<AnswerOption> updatedOptions = questionsDAO.getOptions(questionId);
        assertEquals(0, updatedOptions.size());
    }

    @Test
    void deleteOption() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new MultiSelect(quizID, "text1", -1);
        int questionId = questionsDAO.insertQuestion(q);
        int id1 = questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "hoh", false));
        int id2 = questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "lala", true));
        AnswerOption ans = questionsDAO.getOptionById(id1);
        assertEquals("hoh", ans.getAnswerText());
        questionsDAO.deleteOption(id1);
        assertNull(questionsDAO.getOptionById(id1));
    }

    @Test
    void updateAnswerOption() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new MultiSelect(quizID, "text1", -1);
        int questionId = questionsDAO.insertQuestion(q);
        int id1 = questionsDAO.insertAnswerOptions(new AnswerOption(questionId, "lalala", false));
        assertEquals(1, questionsDAO.getOptions(questionId).size());
        questionsDAO.updateAnswerOption(new AnswerOption(id1, questionId, "okokok", true));
        List<AnswerOption> ans = questionsDAO.getOptions(questionId);
        assertEquals(1, ans.size());
        assertEquals("okokok", ans.getFirst().getAnswerText());
    }

    @Test
    void addTextAnswer() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", "url", false));
        int userID = userDAO.getUserByUsername("name").getUserId();
        int quizID = quizDAO.insertNewQuiz("title", "desc", userID, false,
                false, false);
        Question q = new QuestionResponse(quizID, "text1", -1);
        int questionId = questionsDAO.insertQuestion(q);
        questionsDAO.insertCorrectAnswerText(questionId, "text2");
        assertEquals("text2", questionsDAO.getCorrectAnswerText(questionId));
        questionsDAO.updateCorrectAnswerText(questionId, "text3");
        assertEquals("text3", questionsDAO.getCorrectAnswerText(questionId));
        questionsDAO.deleteCorrectAnswerTextByQuestionId(questionId);
        assertNull(questionsDAO.getCorrectAnswerText(questionId));
    }

    @Test
    void testQuestionBean(){
        Question res = QuestionFactory.createQuestion(1, QuestionType.QUESTION_RESPONSE, "hehe", 1, null);
        Question pic = QuestionFactory.createQuestion(1, QuestionType.PICTURE_RESPONSE, "hehe", 1, "url");
        Question mul = QuestionFactory.createQuestion(1, QuestionType.MULTIPLE_CHOICE, "hehe", 1, null);
        Question sel = QuestionFactory.createQuestion(1, QuestionType.MULTI_SELECT, "hehe", 1, null);
        Question fill = QuestionFactory.createQuestion(1, QuestionType.FILL_IN_THE_BLANK, "hehe", 1, null);

        assertTrue(mul.hasChoices() && sel.hasChoices());
        assertTrue(!pic.hasChoices() && !fill.hasChoices() && !res.hasChoices());
    }

    private void createTables() throws SQLException {
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
                    "CREATE TABLE Quizzes (\n" +
                            "                         quiz_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "                         title VARCHAR(255) NOT NULL,\n" +
                            "                         description TEXT,\n" +
                            "                         creator_id INT NOT NULL,\n" +
                            "                         is_random BOOLEAN DEFAULT FALSE,\n" +
                            "                         is_multipage BOOLEAN DEFAULT FALSE,\n" +
                            "                         immediate_correction BOOLEAN DEFAULT FALSE,\n" +
                            "                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                            "\n" +
                            "                         FOREIGN KEY (creator_id) REFERENCES Users(user_id) ON DELETE CASCADE\n" +
                            ");"
            );
            stmt.executeUpdate(
                    "CREATE TABLE Questions (\n" +
                            "                           question_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "                           quiz_id INT NOT NULL,\n" +
                            "                           type VARCHAR(20) NOT NULL,\n" +
                            "                           prompt TEXT NOT NULL,\n" +
                            "                           image_url TEXT,\n" +
                            "                           position INT,\n" +
                            "\n" +
                            "                           FOREIGN KEY (quiz_id) REFERENCES Quizzes (quiz_id) ON DELETE CASCADE\n" +
                            ");\n"
            );
            stmt.executeUpdate(
                    "CREATE TABLE AnswerOptions (\n" +
                            "                               option_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "                               question_id INT NOT NULL,\n" +
                            "                               text TEXT NOT NULL,\n" +
                            "                               is_correct BOOLEAN DEFAULT FALSE,\n" +
                            "\n" +
                            "                               FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE\n" +
                            ");"
            );
            stmt.executeUpdate(
                    "CREATE TABLE CorrectAnswers (\n" +
                            "                                answer_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "                                question_id INT NOT NULL,\n" +
                            "                                text TEXT NOT NULL,\n" +
                            "\n" +
                            "                                FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE\n" +
                            ");"
            );
        }
    }
}





