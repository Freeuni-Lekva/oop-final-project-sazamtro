package Servlets.Quiz;

import DAO.AnswerDAO;
import DAO.QuestionsDAO;
import DAO.QuizDAO;
import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Questions.QuestionType;
import bean.Quiz;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/quizzes/question")
public class SingleQuestionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        int currIndex = (int) req.getSession().getAttribute("current_index");

        @SuppressWarnings("unchecked")
        List<Question> quizQuestions = (List<Question>) req.getSession().getAttribute("quiz_questions");

        if (currIndex >= quizQuestions.size()) {
            //resp.sendRedirect("/quizzes/submit"); //" + req.getSession().getAttribute("quiz_id") + "/
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<form id='submitForm' method='POST' action='/quizzes/submit'>");
            out.println("<input type='hidden' name='id' value='" + req.getSession().getAttribute("quiz_id") + "' />");
            out.println("</form>");
            out.println("<script>document.getElementById('submitForm').submit();</script>");
            out.println("</body></html>");
            return;
        }

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try {
            QuizDAO qDAO = new QuizDAO(connection);
            AnswerDAO answerDAO = new AnswerDAO(connection);

            int quiz_id = (int) req.getSession().getAttribute("quiz_id"); // Assuming you store it in session
            Quiz quiz = qDAO.getOneQuiz(quiz_id);
            req.setAttribute("quiz", quiz);
            Question currQuestion = quizQuestions.get(currIndex);
            String picture_url = "";
            if(currQuestion.getQuestionType() == QuestionType.PICTURE_RESPONSE){
                picture_url = currQuestion.getImageUrl();
                req.setAttribute("picture", picture_url);
            }
            List<AnswerOption> correctAnswers = answerDAO.getCorrectAnswers(currQuestion.getId());
            req.setAttribute("question", currQuestion);
            req.setAttribute("correct_answers", correctAnswers);
            List<AnswerOption> answers = new ArrayList<>();
            if(currQuestion.hasChoices()){
                QuestionsDAO quesDAO = new QuestionsDAO(connection);
                answers = quesDAO.getOptions(currQuestion.getId());
            }
            req.setAttribute("answer_options", answers);
            RequestDispatcher rd = req.getRequestDispatcher("/single_question.jsp");
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
        int quizId = (int) req.getSession().getAttribute("quiz_id");
        if ("true".equals(req.getParameter("advance"))) {
            currIndex++;
            req.getSession().setAttribute("current_index", currIndex);
            resp.sendRedirect("/quizzes/question");
            return;
        }


        @SuppressWarnings("unchecked")
        Map<Integer, String[]> userResponses = (Map<Integer, String[]>) req.getSession().getAttribute("user_responses");
        @SuppressWarnings("unchecked")
        List<Question> quizQuestions = (List<Question>) req.getSession().getAttribute("quiz_questions");

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        AnswerDAO answerDAO = new AnswerDAO(connection);
        QuestionsDAO questionsDAO = new QuestionsDAO(connection);
        QuizDAO quizDAO = new QuizDAO(connection);
        Quiz quiz;
        try {
            quiz = quizDAO.getOneQuiz(quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Question currQuestion = quizQuestions.get(currIndex);
        //String[] currAnswers = req.getParameterValues("answer");
        String paramName = "q_" + currQuestion.getId();
        String[] currAnswers = req.getParameterValues(paramName);
        userResponses.put(currQuestion.getId(), currAnswers);
        int singleQuesScore = 0;

        if (currAnswers != null) {
            try {
                for (String curr : currAnswers) {
                    if (answerDAO.checkAnswer(currQuestion.getId(), curr)) singleQuesScore++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(currQuestion.getQuestionType() == QuestionType.MULTI_SELECT){
            try {
                int numPossAnswers = questionsDAO.getOptions(currQuestion.getId()).size();
                if(currAnswers != null && numPossAnswers == currAnswers.length) singleQuesScore = 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        currScore = currScore + singleQuesScore;

        if(quiz.checkIfImmediate_correction()){
            req.getSession().setAttribute("current_score", currScore);
            req.getSession().setAttribute("quiz", quiz);
            req.getSession().setAttribute("user_responses", userResponses);
            req.getSession().setAttribute("single_ques_score", singleQuesScore);
            req.getSession().setAttribute("current_index", currIndex);
            RequestDispatcher rd = req.getRequestDispatcher("/single_question_score.jsp");
            rd.forward(req, resp);
        } else{
            req.getSession().setAttribute("current_score", currScore);
            req.getSession().setAttribute("user_responses", userResponses);
            req.getSession().setAttribute("is_correct", singleQuesScore);
            req.getSession().setAttribute("current_index", currIndex + 1);
            resp.sendRedirect("/quizzes/question");
        }
    }
}