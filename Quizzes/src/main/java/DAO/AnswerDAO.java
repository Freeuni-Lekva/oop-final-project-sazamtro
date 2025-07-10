package DAO;

import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Questions.QuestionType;

import java.sql.*;
import java.util.*;

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
        String sqlCommand1 = "SELECT COUNT(*) FROM CorrectAnswers WHERE question_id = ? AND LOWER(text) = LOWER(?)";
        String sqlCommand2 = "SELECT COUNT(*) FROM AnswerOptions WHERE question_id = ? AND LOWER(Text) = LOWER(?) AND is_correct = true";
        int num1 = 0;
        int num2 = 0;
        try(PreparedStatement st = connection.prepareStatement(sqlCommand1)){
            st.setInt(1, question_id);
            st.setString(2, answer);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                num1 = rs.getInt(1);
            }
        }
        try(PreparedStatement st = connection.prepareStatement(sqlCommand2)){
            st.setInt(1, question_id);
            st.setString(2, answer);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                num2 = rs.getInt(1);
            }
        }
        return num1 > 0 || num2 > 0;
    }

    public List<AnswerOption> getCorrectAnswers(int question_id) throws SQLException {
        List<AnswerOption> result = new ArrayList<>();
        QuestionsDAO questionsDAO = new QuestionsDAO(connection);
        Question q = questionsDAO.getQuestionById(question_id);
        String sqlCommand = "";
        if(q.getQuestionType() == QuestionType.MULTI_SELECT || q.getQuestionType() == QuestionType.MULTIPLE_CHOICE){
            sqlCommand = "SELECT * FROM AnswerOptions WHERE question_id = ? AND is_correct = TRUE";
            try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
                st.setInt(1, question_id);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    int option_id = rs.getInt("option_id");
                    int questionId = rs.getInt("question_id");
                    String answerText = rs.getString("text");
                    boolean isCorrect = rs.getBoolean("is_correct");
                    AnswerOption answ = new AnswerOption(option_id, questionId, answerText, isCorrect);
                }
            }

        } else{
            sqlCommand = "SELECT * FROM CorrectAnswers WHERE question_id = ?";
            try(PreparedStatement st = connection.prepareStatement(sqlCommand)){
                st.setInt(1, question_id);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    int answer_id = rs.getInt("answer_id");
                    int questionId = rs.getInt("question_id");
                    String answerText = rs.getString("text");
                    AnswerOption answ = new AnswerOption(answer_id, questionId, answerText, true);
                    result.add(answ);
                }
            }
        }
        return result;
    }
}