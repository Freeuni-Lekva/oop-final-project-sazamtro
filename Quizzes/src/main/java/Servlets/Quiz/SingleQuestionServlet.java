package Servlets.Quiz;

import DAO.AnswerDAO;
import DAO.QuestionsDAO;
import DAO.QuizDAO;
import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Quiz;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/quizzes/*/question")
public class SingleQuestionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        int currIndex = (int) req.getSession().getAttribute("current_index");

        @SuppressWarnings("unchecked")
        List<Question> quizQuestions = (List<Question>) req.getSession().getAttribute("quiz_questions");

        if (currIndex >= quizQuestions.size()) {
            resp.sendRedirect("/quizzes/" + req.getSession().getAttribute("quiz_id") + "/submit");
            return;
        }

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try {
            Question currQuestion = quizQuestions.get(currIndex);
            req.setAttribute("question", currQuestion);
            if(currQuestion.hasChoices()){
                QuestionsDAO quesDAO = new QuestionsDAO(connection);
                List<AnswerOption> answers = quesDAO.getOptions(currQuestion.getId());
                req.setAttribute("answer_options", answers);
            }
            RequestDispatcher rd = req.getRequestDispatcher("single_question.jsp");
            rd.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        int currIndex = (int) req.getSession().getAttribute("current_index");
        int currScore = (int) req.getSession().getAttribute("current_score");

        @SuppressWarnings("unchecked")
        Map<Integer, String> userResponses = (Map<Integer, String>) req.getSession().getAttribute("user_responses");
        @SuppressWarnings("unchecked")
        List<Question> quizQuestions = (List<Question>) req.getSession().getAttribute("quiz_questions");

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        AnswerDAO answerDAO = new AnswerDAO(connection);

        Question currQuestion = quizQuestions.get(currIndex);
        String currAnswer = req.getParameter("answer");
        userResponses.put(currQuestion.getId(), currAnswer);
        boolean isCorrect;
        try{
            isCorrect = answerDAO.checkAnswer(currQuestion.getId(), currAnswer);
        } catch(SQLException e){
            throw new RuntimeException();
        }
        if(isCorrect){
            currScore++;
        }
        req.getSession().setAttribute("current_score", currScore);
        req.getSession().setAttribute("user_responses", userResponses);
        req.getSession().setAttribute("is_correct", isCorrect);
        req.getSession().setAttribute("current_index", currIndex + 1);
        resp.sendRedirect("/quizzes/" + req.getSession().getAttribute("quiz_id") + "/question");
    }
}
