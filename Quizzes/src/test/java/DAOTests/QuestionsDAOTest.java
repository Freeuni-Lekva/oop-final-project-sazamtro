package DAOTests;

import DAO.QuestionsDAO;
import DAO.QuizDAO;
import DAO.UserDAO;
import bean.Questions.AnswerOption;
import bean.Questions.MultipleChoice;
import bean.Questions.Question;
import bean.User;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;


import static org.testng.AssertJUnit.assertEquals;

public class QuestionsDAOTest extends TestCase {

    private static Connection connection;
    private static QuestionsDAO questionsDAO;
    private static QuizDAO quizDAO;
    private static UserDAO userDAO;

    protected void setUp() throws Exception {
        String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String USER = "sa";
        String PASSWORD = "";
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.println("H2 Driver not found");
            throw new RuntimeException("Failed to initialize H2 database connection", classNotFoundException);
        } catch (SQLException exception) {
            System.out.println("Connection to H2 failed");
            throw new RuntimeException("Failed to initialize H2 database connection", exception);
        }

        // Run schema.sql to create tables
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        QuestionsDAOTest.class.getClassLoader().getResourceAsStream("schema.sql")))) {

            String sql = reader.lines().collect(Collectors.joining("\n"));
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        }

        questionsDAO = new QuestionsDAO(connection);
        quizDAO = new QuizDAO(connection);
        userDAO = new UserDAO(connection);
    }


    public void testInsertAndRetrieveQuestion() throws SQLException {
        userDAO.addUser(new User(-1, "name", "pass", false));
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
}