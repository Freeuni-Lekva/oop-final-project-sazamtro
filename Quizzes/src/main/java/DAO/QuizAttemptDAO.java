package DAO;


import bean.QuizAttempt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizAttemptDAO {
    private Connection connection;

    public QuizAttemptDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new quiz attempt (real or practice)
    public void addAttempt(QuizAttempt attempt) throws SQLException {
        String query = "INSERT INTO QuizAttempts (user_id, quiz_id, score, time_taken_min, is_practice) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, attempt.getUserId());
            ps.setInt(2, attempt.getQuizId());
            ps.setInt(3, attempt.getScore());
            ps.setDouble(4, attempt.getTimeTakenMin());
            ps.setBoolean(5, attempt.isPractice());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int newAttemptId = rs.getInt(1);
                    attempt.setAttemptId(newAttemptId);
                }
            }
        }
    }


    // Get all attempts (real or practice) by user
    public List<QuizAttempt> getAttemptsByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM QuizAttempts WHERE user_id = ? ORDER BY taken_at DESC";

        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attempts.add(mapRow(rs));
            }
        }

        return attempts;
    }

    // Get only real attempts by user
    public List<QuizAttempt> getRealAttemptsByUser(int userId) throws SQLException {
        String query = "SELECT * FROM QuizAttempts WHERE user_id = ? AND is_practice = FALSE ORDER BY taken_at DESC";

        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attempts.add(mapRow(rs));
            }
        }

        return attempts;
    }

    // Get only practice attempts by user
    public List<QuizAttempt> getPracticeAttemptsByUser(int userId) throws SQLException {
        String query = "SELECT * FROM QuizAttempts WHERE user_id = ? AND is_practice = TRUE ORDER BY taken_at DESC";

        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attempts.add(mapRow(rs));
            }
        }

        return attempts;
    }

    // Get all attempts for a quiz (real + practice)
    public List<QuizAttempt> getAttemptsByQuiz(int quizId) throws SQLException {
        String query = "SELECT * FROM QuizAttempts WHERE quiz_id = ? ORDER BY score DESC, time_taken_min ASC";

        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attempts.add(mapRow(rs));
            }
        }
        return attempts;
    }

    // Get top real attempts for a quiz
    public List<QuizAttempt> getRealAttemptsByQuiz(int quizId) throws SQLException {
        String query = "SELECT * FROM QuizAttempts WHERE quiz_id = ? AND is_practice = FALSE ORDER BY score DESC, time_taken_min ASC";

        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attempts.add(mapRow(rs));
            }
        }

        return attempts;
    }

    // Get practice attempts for a quiz
    public List<QuizAttempt> getPracticeAttemptsByQuiz(int quizId) throws SQLException {
        String query = "SELECT * FROM QuizAttempts WHERE quiz_id = ? AND is_practice = TRUE ORDER BY taken_at DESC";

        List<QuizAttempt> attempts = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attempts.add(mapRow(rs));
            }
        }

        return attempts;
    }

    // Delete all attempts for a quiz
    public void deleteAttemptsByQuiz(int quizId) throws SQLException {
        String query = "DELETE FROM QuizAttempts WHERE quiz_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.executeUpdate();
        }
    }

    // Utility to map DB row to Java bean
    private QuizAttempt mapRow(ResultSet rs) throws SQLException {
        QuizAttempt attempt = new QuizAttempt();

        attempt.setAttemptId(rs.getInt("attempt_id"));
        attempt.setUserId(rs.getInt("user_id"));
        attempt.setQuizId(rs.getInt("quiz_id"));
        attempt.setScore(rs.getInt("score"));
        attempt.setTimeTakenMin(rs.getDouble("time_taken_min"));
        attempt.setPractice(rs.getBoolean("is_practice"));
        attempt.setTakenAt(rs.getTimestamp("taken_at"));

        return attempt;
    }
    public QuizAttempt getAttempt(int attemptId) throws SQLException {
        String query = "SELECT * FROM QuizAttempts WHERE attempt_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, attemptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve attempt with id " + attemptId, e);
        }
        return null;
    }
    public Set<Integer> getAttemptQuestions(int attemptId){
        Set<Integer> result = new HashSet<>();
        String query = "SELECT * FROM UserAnswers WHERE attempt_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, attemptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result.add(rs.getInt("question_id"));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve attempt with id " + attemptId, e);
        }
        return result;
    }

    public List<String> getResponsesByAttemptAndQuestion(int attemptId, int qid) {
        String query = "SELECT * FROM UserAnswers WHERE attempt_id = ? AND question_id = ?";
        List<String> responses = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, attemptId);
            ps.setInt(2, qid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                responses.add(rs.getString("response_text"));
            }
            return responses;
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve attempt with id " + attemptId, e);
        }
    }
    public void updateScore(int attemptId, int score) throws SQLException {
        String sql = "UPDATE QuizAttempts SET score = ? WHERE attempt_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, score);
            st.setInt(2, attemptId);
            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error updating score failed", e);
        }
    }
}