package bean.Questions;

import DAO.DatabaseConnection;
import DAO.QuestionsDAO;

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

    public List<AnswerOption> getOptions(Connection connection) throws SQLException {
        QuestionsDAO dao = new QuestionsDAO(connection);
        List<AnswerOption> answerOptions =  dao.getOptions(getId());
        connection.close();
        return answerOptions;
    }
}
