package bean.Questions;

import DAO.DatabaseConnection;
import DAO.Quiz.QuestionsDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChoiceQuestions extends Question {


    public ChoiceQuestions(int id, int quiz_id, QuestionType type, String questionText, int position) {
        super(id, quiz_id, type, questionText, position);
    }
    public ChoiceQuestions() {
        super();
    }

    public List<AnswerOption> getOptions() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        List<AnswerOption> answerOptions =  QuestionsDAO.getOptions(connection, getId());
        connection.close();
        return answerOptions;
    }
}
