package bean.Questions;

import DAO.DatabaseConnection;
import DAO.QuestionsDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class ChoiceQuestions extends Question {


    public ChoiceQuestions(int id, int quizId, QuestionType type, String questionText, int position) {
        super(id, quizId, type, questionText, position);
    }
    public ChoiceQuestions(int quizId, QuestionType type, String questionText, int position) {
        super(quizId, type, questionText, position);
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

    @Override
    public boolean hasChoices(){
        return true;
    }
}
