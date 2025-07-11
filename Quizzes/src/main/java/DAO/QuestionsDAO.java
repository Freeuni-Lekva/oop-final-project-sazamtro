package DAO;

import bean.Questions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDAO {
    private Connection connection;
    public QuestionsDAO(Connection connection) {
        this.connection = connection;
    }

    public int insertQuestion(Question question) throws SQLException {
        String sqlCommand = "INSERT INTO Questions (quiz_id, type, prompt, image_url, position) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)){
            st.setInt(1, question.getQuizId());
            st.setString(2, question.getQuestionType().toString());
            st.setString(3, question.getQuestionText());
            st.setString(4, question.getImageUrl());
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

    public void deleteQuestion(int questionId) throws SQLException {
        String sqlCommand = "DELETE FROM Questions WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
            st.setInt(1, questionId);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Deleting question failed");
            }
        }
    }

    //returns number of rows updated
    public int updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE Questions SET quiz_id = ?, type = ?, prompt = ?, image_url = ?, position = ? WHERE question_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, question.getQuizId());
            st.setString(2, question.getQuestionType().toString());
            st.setString(3, question.getQuestionText());
            st.setString(4, question.getImageUrl());  // Can be null
            st.setInt(5, question.getPosition());
            st.setInt(6, question.getId());
            return st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error updating question data", e);
        }
    }


    public int insertAnswerOptions(AnswerOption option) throws SQLException {
        String sqlCommand = "INSERT INTO AnswerOptions (question_id, text, is_correct) VALUES (?, ?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)){
            st.setInt(1, option.getQuestionId());
            st.setString(2, option.getAnswerText());
            st.setBoolean(3, option.isCorrect());

            int rows = st.executeUpdate();
            if (rows == 0) throw new SQLException("Creating Answer failed");
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Error, no ID");
                }
            }
        }
    }
    public void deleteAllOptions(int questionId) throws SQLException {
        String sql = "DELETE FROM AnswerOptions WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, questionId);
            int rows = st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting question answers", e);
        }
    }
    public boolean deleteOption(int optionId) throws SQLException {
        String sql = "DELETE FROM AnswerOptions WHERE option_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, optionId);
            int rows = st.executeUpdate();
            return rows > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting option answers", e);
        }
    }


    public void updateAnswerOption(AnswerOption option) throws SQLException {
        String sql = "UPDATE AnswerOptions SET text = ?, is_correct = ? WHERE option_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setString(1, option.getAnswerText());
            st.setBoolean(2, option.isCorrect());
            st.setInt(3, option.getId());
            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error updating answer option", e);
        }
    }
    public AnswerOption getOptionById(int optionId) throws SQLException {
        String sql = "SELECT * FROM AnswerOptions WHERE option_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, optionId);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return new AnswerOption(rs.getInt("question_id"),
                        rs.getString("text"), rs.getBoolean("is_correct"));
            }
            return null;
        }
    }
    public Question getQuestionById(int questionId) throws SQLException {
        String sql = "SELECT * FROM Questions WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, questionId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String strType = rs.getString("type").toUpperCase();
                QuestionType type = QuestionType.valueOf(strType);
                int id = rs.getInt("question_id");
                int quizId = rs.getInt("quiz_id");
                String prompt = rs.getString("prompt");
                int position = rs.getInt("position");
                String picUrl = rs.getString("image_url");
                Question res = QuestionFactory.createQuestion(quizId, type, prompt, position, picUrl);
                assert res != null;
                res.setId(id);
                return res;
            }
            return null;
        }
    }


    public void insertCorrectAnswerText(int question_id, String text) throws SQLException {
        String sqlCommand = "INSERT INTO CorrectAnswers (question_id, text) VALUES (?, ?)";
        try(PreparedStatement st = connection.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS)){
            st.setInt(1, question_id);
            st.setString(2, text);
            int rows = st.executeUpdate();
            if (rows == 0) throw new SQLException("Creating CorrectAnswerText failed");
        }
    }
    public void updateCorrectAnswerText(int question_id, String text) throws SQLException {
        String sql = "UPDATE CorrectAnswers SET text = ? WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setString(1, text);
            st.setInt(2, question_id);
            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error updating CorrectAnswerText failed", e);
        }
    }
    public void deleteCorrectAnswerTextByQuestionId(int question_id) throws SQLException {
        String sql = "DELETE FROM CorrectAnswers WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, question_id);
            int rows = st.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting CorrectAnswerText failed", e);
        }
    }

    //if question has options (multiple choice/answer questions)
    public List<AnswerOption> getOptions(int question_id) throws SQLException {
        String query = "SELECT * FROM AnswerOptions WHERE question_id = ?";
        try(PreparedStatement st = connection.prepareStatement(query)){
            st.setInt(1, question_id);
            ResultSet rs = st.executeQuery();
            List<AnswerOption> options = new ArrayList<>();
            while (rs.next()) {
                AnswerOption ans = new AnswerOption(rs.getInt("option_id"), rs.getInt("question_id"),
                        rs.getString("text"), rs.getBoolean("is_correct"));
                options.add(ans);
            }
            return options;
        }
    }


    public String getCorrectAnswerText(int question_id) throws SQLException {
        String query = "SELECT * FROM CorrectAnswers WHERE question_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, question_id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("text");
            } else {return null;}
        }
    }

//    public void deactivateQuestion(int questionID) throws SQLException {
//        String query = "UPDATE Questions SET is_active = ? WHERE question_id = ?";
//        try (PreparedStatement st = connection.prepareStatement(query)) {
//            st.setBoolean(1, false);
//            st.setInt(2, questionID);
//            st.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException("Error deactivating question", e);
//        }
//    }

    public void updateQuestionText(int questionId, String text) {
        String query = "UPDATE Questions SET prompt = ? WHERE question_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, text);
            st.setInt(2, questionId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating question text", e);
        }
    }

    public void updateImageUrl(int questionId, String imageUrl) {
        String query = "UPDATE Questions SET image_url = ? WHERE question_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, imageUrl);
            st.setInt(2, questionId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating imageURL", e);
        }
    }
    public List<Integer> getOrderedQuestionIds(int quizId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT question_id FROM Questions WHERE quiz_id = ? AND is_active = true ORDER BY question_id";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, quizId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("question_id"));
            }
        }
        return ids;
    }
    public void updateQuestionPosition(int questionId, int position) throws SQLException {
        String query = "UPDATE Questions SET position = ? WHERE question_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, position);
            st.setInt(2, questionId);
            st.executeUpdate();
        }
    }


}
