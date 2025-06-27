package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.Quiz.QuestionsDAO;
import DAO.Quiz.QuizDAO;
import bean.Questions.QuestionType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AddQuestion extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int quiz_id = Integer.parseInt(req.getParameter("quiz_id"));
        QuestionType type = QuestionType.valueOf(req.getParameter("type"));
        String prompt = req.getParameter("prompt");

        //tu images monishnavs types gavxdit picture_response JSP shi
        String image_url = null;
        if(type.equals(QuestionType.PICTURE_RESPONSE)) image_url = req.getParameter("image_url");

        int position = -1;
        int question_id;
        Connection con = DatabaseConnection.getConnection();
        try {
            boolean isRandom = QuizDAO.getOneQuiz(con, quiz_id).checkIfRandom();
            if(!isRandom) position = Integer.parseInt(req.getParameter("position"));
            question_id = QuestionsDAO.insertQuestion(con, quiz_id, type, prompt, image_url, position);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(type.equals(QuestionType.MULTIPLE_CHOICE) || type.equals(QuestionType.MULTI_SELECT)) {
            addOptions(con, req, question_id);
        }
        else if(!req.getParameter("correctAnswer").isEmpty()) { //an ragac sxvit vamowmebdet ro text mowmdeba
            try {
                QuestionsDAO.insertCorrectAnswerText(con, question_id, req.getParameter("correctAnswer"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.sendRedirect("/add-question?quiz_id=" + quiz_id + "&position=" + (position + 1));

    }
    private void addOptions(Connection con, HttpServletRequest req, int question_id){
        int numOptions = Integer.parseInt(req.getParameter("numOptions"));
        String[] correctOptions = req.getParameterValues("correct_options");
        Set<Integer> correctOptionSet = getCorrectOptionsSet(correctOptions);
        Connection conn = DatabaseConnection.getConnection();
        for(int i = 0; i < numOptions; i++) {
            String optionText = req.getParameter("option" + i);
            try {
                QuestionsDAO.insertAnswerOptions(conn, question_id, optionText, isCorrect(i, correctOptionSet));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Boolean isCorrect(int i, Set<Integer> correctOptionSet) {
        return correctOptionSet.contains(i);
    }

    private Set<Integer> getCorrectOptionsSet(String[] correctOptions) {
        Set<Integer> res = new HashSet<>();
        for(String correctOption : correctOptions) {
            res.add(Integer.parseInt(correctOption));
        }
        return res;
    }

}
