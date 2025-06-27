package bean.Questions;

import DAO.DatabaseConnection;
import DAO.Quiz.QuestionsDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class TextQuestion extends Question {

    public TextQuestion(QuestionType type, int id, String questionText, int position, int questionScore) {
        super(type, id, questionText, position, questionScore);
    }
    public String getCorrectAnswer() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String res = QuestionsDAO.getCorrectAnswerTexts(connection, getId());
        connection.close();
        return res;
    }
}
