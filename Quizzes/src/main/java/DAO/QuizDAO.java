package DAO;

import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Questions.QuestionFactory;
import bean.Questions.QuestionType;
import bean.Quiz;
import bean.QuizAttempt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class QuizDAO {

    private Connection connection;

    public QuizDAO(Connection connection) {
        this.connection = connection;
    }

    public int insertNewQuiz(String title, String description, int creator_id,
                             boolean is_random, boolean is_multipage, boolean immediate_correction) throws SQLException {
        String sqlCommand = "INSERT INTO Quizzes (title, description, creator_id, is_random, is_multipage, immediate_correction, created_at) " +
                " VALUES (?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement st = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)) {
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
        try (PreparedStatement st = connection.prepareStatement(sqlCommand)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
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
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(sqlCommand);
            while (rs.next()) {
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

    public void removeQuiz(int quiz_id) throws SQLException {
        String sqlCommand = "DELETE FROM Quizzes WHERE quiz_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sqlCommand)) {
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
            while (rs.next()) {
                String typeStr = rs.getString("type").toUpperCase();
                QuestionType type = QuestionType.valueOf(typeStr);
                int id = rs.getInt("question_id");
                int quizId = rs.getInt("quiz_id");
                String prompt = rs.getString("prompt");
                int position = rs.getInt("position");
                String picUrl = rs.getString("image_url");
                Question curr = QuestionFactory.createQuestion(quizId, type, prompt, position, picUrl);
                assert curr != null;
                curr.setId(id);
                result.add(curr);
            }
        }
        Quiz q = getOneQuiz(quiz_id);
        if (!q.checkIfRandom()) {
            result.sort(Comparator.comparingInt(Question::getPosition));
        } else {
            Collections.shuffle(result);
        }
        return result;
    }

    public int insertAttempt(int user_id, int quiz_id, int score, double time_taken,
                             boolean isPractice) throws SQLException {

        String sqlCommand = "INSERT INTO QuizAttempts (user_id, quiz_id, score, time_taken_min, is_practice, taken_at)" +
                " VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement st = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, user_id);
            st.setInt(2, quiz_id);
            st.setInt(3, score);
            st.setDouble(4, time_taken);
            st.setBoolean(5, isPractice);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating attempt failed, no rows affected.");
            }
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating attempt failed, no ID obtained.");
                }
            }
        }
    }


    public List<Quiz> getUserQuizzes(int user_id) throws SQLException {
        List<Quiz> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM Quizzes WHERE creator_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sqlCommand)) {
            st.setInt(1, user_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
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

    public void updateQuizMeta(int quizId, String title, String description, boolean random, boolean multiPage, boolean immediateCorrection) {
        String sql = "UPDATE Quizzes SET title = ?, description = ?, is_random = ?, is_multipage = ?, immediate_correction = ? WHERE quiz_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setBoolean(3, random);
            stmt.setBoolean(4, multiPage);
            stmt.setBoolean(5, immediateCorrection);
            stmt.setInt(6, quizId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update quiz metadata for quiz ID: " + quizId, e);
        }
    }


    //Added for practice
    public List<QuizAttempt> getUserAttempts(int user_id, int quiz_id) throws SQLException {
        List<QuizAttempt> result = new ArrayList<>();
        String sqlCommand = "SELECT * FROM QuizAttempts WHERE user_id = ? AND quiz_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sqlCommand)) {
            st.setInt(1, user_id);
            st.setInt(2, quiz_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int attempt_id = rs.getInt("attempt_id");
                int userId = rs.getInt("user_id");
                int quizId = rs.getInt("quiz_id");
                int score = rs.getInt("score");
                double time_taken_min = rs.getInt("time_taken_min");
                boolean is_practice = rs.getBoolean("is_practice");
                Timestamp taken_at = rs.getTimestamp("taken_at");
                QuizAttempt quizAttempt = new QuizAttempt(attempt_id, userId, quizId, score, time_taken_min, is_practice, taken_at);
                result.add(quizAttempt);
            }
        }
        return result;
    }

    //Added for practice
    public List<Question> getQuestionsForPractice(int quiz_id, int user_id, AnswerDAO answerDAO) throws SQLException {
        List<QuizAttempt> quizAttempts = getUserAttempts(user_id, quiz_id);
        if (quizAttempts.isEmpty()) return getQuizQuestions(quiz_id);
        List<Question> result = new ArrayList<>();
        List<Question> allQuestions = getQuizQuestions(quiz_id);
        for (Question q : allQuestions) {
            int numCorrectAttempts = 0;
            Set<String> correctAnswers = answerDAO.getCorrectAnswers(q.getId()).stream().map(AnswerOption::getAnswerText).collect(Collectors.toSet());
            for (QuizAttempt attempt : quizAttempts) {
                List<AnswerOption> userAttemptAnswers = answerDAO.getUserAnswers(attempt.getAttemptId(), q.getId());
                if (userAttemptAnswers.isEmpty()) continue;
                Set<String> userAnswers = userAttemptAnswers.stream().map(AnswerOption::getAnswerText).collect(Collectors.toSet());
                if (userAnswers.equals(correctAnswers)) numCorrectAttempts++;
            }
            if (numCorrectAttempts < 3) result.add(q);
        }
        return result;
    }

    public Quiz getQuizByTitle(String title) throws SQLException{
        String query = "Select quiz_id WHERE title like ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, title);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return getOneQuiz(rs.getInt(1));
        }
        return null;
    }

}