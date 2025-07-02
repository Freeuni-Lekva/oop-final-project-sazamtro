package DAO;

import bean.Questions.Question;
import bean.Questions.QuestionType;
import bean.Quiz;

import java.sql.*;
import java.util.*;

public class QuizDAO {

    private Connection connection;

    public QuizDAO(Connection connection){
        this.connection = connection;
    }

    public int insertNewQuiz(String title, String description, int creator_id,
                                    boolean is_random, boolean is_multipage, boolean immediate_correction) throws SQLException {
        String sqlCommand = "INSERT INTO Quizzes (title, description, creator_id, is_random, is_multipage, immediate_correction, created_at) " +
                " VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setString(1, title);
            st.setString(2, description);
            st.setInt(3, creator_id);
            st.setBoolean(4, is_random);
            st.setBoolean(5, is_multipage);
            st.setBoolean(6, immediate_correction);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating quiz failed");
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


    public Quiz getOneQuiz(int id) throws SQLException {
        String sqlCommand = "SELECT * FROM Quizzes WHERE quiz_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return new Quiz(rs.getInt("quiz_id"), rs.getString("title"),
                        rs.getString("description"), rs.getInt("creator_id"),
                        rs.getBoolean("is_random"), rs.getBoolean("is_multipage"),
                        rs.getBoolean("immediate_correction"),
                        rs.getTimestamp("created_at").toLocalDateTime());
            }
            return null;
        }
    }

    public List<Quiz> getAllQuizzes() {
        List<Quiz> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM Quizzes";
        try(Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery(sqlCommand);
            while(rs.next()){
                Quiz curr = new Quiz(rs.getInt("quiz_id"), rs.getString("title"),
                                     rs.getString("description"), rs.getInt("creator_id"),
                                     rs.getBoolean("is_random"), rs.getBoolean("is_multipage"),
                                     rs.getBoolean("immediate_correction"),
                                     rs.getTimestamp("created_at").toLocalDateTime());
                result.add(curr);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void deleteQuiz(int quiz_id) throws SQLException {
        String sqlCommand = "DELETE FROM Quizzes WHERE quiz_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, quiz_id);
            st.executeUpdate();
        }
    }

    public List<Question> getQuizQuestions(int quiz_id) throws SQLException {
        List<Question> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM Questions WHERE quiz_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, quiz_id);
            ResultSet rs = st.executeQuery();
            Question curr = new Question(rs.getInt("question_id"),
                                         rs.getInt("quiz_id"),
                                         QuestionType.valueOf(rs.getString("type").toUpperCase()),
                                         rs.getString("prompt"),
                                         rs.getInt("position"));
            result.add(curr);
        }
        return result;
    }
}
