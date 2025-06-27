package bean.Questions;

import DAO.DatabaseConnection;
import DAO.Quiz.QuestionsDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChoiceQuestions extends Question {

    public ChoiceQuestions(QuestionType type, int id, String questionText, int position, int questionScore) {
        super(type, id, questionText, position, questionScore);
    }
    public List<AnswerOptions> getOptions() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        List<AnswerOptions> answerOptions =  QuestionsDAO.getOptions(connection, getId());
        connection.close();
        return answerOptions;
    }
}
