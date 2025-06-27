package DAO.Quiz;

import bean.Questions.AnswerOptions;
import bean.Questions.QuestionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDAO {
    public static int insertQuestion(Connection connection, int quiz_id, QuestionType type, String prompt,
                                     String image_url, int position) throws SQLException {
        String sqlCommand = "INSERT INTO Questions (quiz_id, type, prompt, image_url, position) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, quiz_id);
            st.setString(2, type.toString());
            st.setString(3, prompt);
            st.setString(4, image_url);
            st.setInt(5, position);

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

    public static void insertAnswerOptions(Connection connection, int question_id, String text, boolean is_correct) throws SQLException {
        String sqlCommand = "INSERT INTO AnswerOptions (question_id, text, is_correct) VALUES (?, ?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, question_id);
            st.setString(2, text);
            st.setBoolean(3, is_correct);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating Answer failed");
            }
        }
    }
    public static void insertCorrectAnswerText(Connection connection, int question_id, String text) throws SQLException {
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
    public static List<AnswerOptions> getOptions(Connection connection, int question_id) throws SQLException {
        String query = "SELECT * FROM AnswerOptions WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, question_id);
            ResultSet rs = st.executeQuery();
            List<AnswerOptions> options = new ArrayList<>();
            while (rs.next()) {
                options.add(new AnswerOptions(rs.getInt("question_id"),
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
