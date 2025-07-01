package DAO;

import bean.Questions.AnswerOption;
import bean.Questions.PictureResponse;
import bean.Questions.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDAO {
    private Connection connection;
    public QuestionsDAO(Connection connection) {
        this.connection = connection;
    }

    public int insertQuestion(Question question) throws SQLException {
        String sqlCommand = "INSERT INTO Questions (quiz_id, type, prompt, image_url, position) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, question.getQuizId());
            st.setString(2, question.getQuestionType().toString());
            st.setString(3, question.getQuestionText());
            st.setString(4, (question instanceof PictureResponse)
                    ? ((PictureResponse) question).getImage_url()
                    : null);
            st.setInt(5, question.getPosition());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating question failed");
            }
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Error, no ID");
                }
            }
        }
    }

    public void insertAnswerOptions(AnswerOption option) throws SQLException {
        String sqlCommand = "INSERT INTO AnswerOptions (question_id, text, is_correct) VALUES (?, ?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, option.getQuestionId());
            st.setString(2, option.getAnswerText());
            st.setBoolean(3, option.isCorrect());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating Answer failed");
            }
        }
    }
    public void insertCorrectAnswerText(int question_id, String text) throws SQLException {
        String sqlCommand = "INSERT INTO CorrectAnswer (question_id, text) VALUES (?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, question_id);
            st.setString(2, text);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating CorrectAnswerText failed");
            }
        }
    }

    //if question has options (multiple choice/answer questions)
    public static List<AnswerOption> getOptions(Connection connection, int question_id) throws SQLException {
        String query = "SELECT * FROM AnswerOptions WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, question_id);
            ResultSet rs = st.executeQuery();
            List<AnswerOption> options = new ArrayList<>();
            while (rs.next()) {
                options.add(new AnswerOption(rs.getInt("question_id"),
                        rs.getString("text"), rs.getBoolean("is_correct")));
            }
            return options;
        }
    }

    public static String getCorrectAnswerTexts(Connection connection, int question_id) throws SQLException {
        String query = "SELECT * FROM CorrectAnswers WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, question_id);
            ResultSet rs = st.executeQuery();
            return rs.getString("text");
        }
    }

}
