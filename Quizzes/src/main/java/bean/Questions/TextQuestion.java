package bean.Questions;

import DAO.DatabaseConnection;
import DAO.QuestionsDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class TextQuestion extends Question {


    public TextQuestion(int id, int quizId, QuestionType type, String questionText, int position) {
        super(id, quizId, type, questionText, position);
    }
    public TextQuestion(int quizId, QuestionType type, String questionText, int position) {
        super(quizId, type, questionText, position);
    }
    public TextQuestion() {
        super();
    }

    public String getCorrectAnswer(Connection connection) throws SQLException {
        QuestionsDAO dao = new QuestionsDAO(connection);
        String res = dao.getCorrectAnswerTexts(getId());
        connection.close();
        return res;
    }
}
