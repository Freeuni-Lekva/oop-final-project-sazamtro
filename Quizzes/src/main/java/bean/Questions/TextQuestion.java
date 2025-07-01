package bean.Questions;

import DAO.DatabaseConnection;
import DAO.Quiz.QuestionsDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class TextQuestion extends Question {


    public TextQuestion(int id, int quiz_id, QuestionType type, String questionText, int position) {
        super(id, quiz_id, type, questionText, position);
    }
    public TextQuestion() {
        super();
    }

    public String getCorrectAnswer() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String res = QuestionsDAO.getCorrectAnswerTexts(connection, getId());
        connection.close();
        return res;
    }
}
