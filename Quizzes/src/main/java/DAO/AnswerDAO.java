package DAO;

import java.sql.*;

public class AnswerDAO {

    private Connection connection;

    public AnswerDAO(Connection connection){
        this.connection = connection;
    }

    public int insertUserAnswer(String text, int attempt_id, int question_id, boolean is_correct) throws SQLException {
        String sqlCommand = "INSERT INTO UserAnswers (attempt_id, question_id, response_text, is_correct)" +
                "VALUES (?, ?, ?, ?)";

        try(PreparedStatement st = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)){

            st.setInt(1, attempt_id);
            st.setInt(2, question_id);
            st.setString(3, text);
            st.setBoolean(4, is_correct);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Inserting answer failed");
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

    public boolean checkAnswer(int question_id, String answer) throws SQLException {
        String sqlCommand = "SELECT COUNT(*) FROM CorrectAnswers WHERE question_id = ? AND LOWER(text) = LOWER(?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, question_id);
            st.setString(2, answer);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int num = rs.getInt(1);
                return num > 0;
            }
        }
        return false;
    }
}