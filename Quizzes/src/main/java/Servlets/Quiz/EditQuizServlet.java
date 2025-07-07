package Servlets.Quiz;

import DAO.QuestionsDAO;
import DAO.QuizDAO;
import bean.Questions.AnswerOption;
import bean.Questions.ChoiceQuestions;
import bean.Questions.Question;
import bean.Questions.QuestionType;

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

@WebServlet("/quizzes/*")
public class EditQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        String path = req.getPathInfo();
        String[] pathParts = path.split("/");
        int quiz_id = Integer.parseInt(pathParts[1]);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            List<Question> quizQuestions = qDAO.getQuizQuestions(quiz_id);
            Map<Question, List<AnswerOption>> questionAnswerOptionMap = new HashMap<>();
            QuestionsDAO questionsDAO = new QuestionsDAO(connection);
            for(Question curr : quizQuestions){
                List<AnswerOption> answers = new ArrayList<>();
                if (curr.getQuestionType() == QuestionType.MULTIPLE_CHOICE ||
                        curr.getQuestionType() == QuestionType.MULTI_SELECT){
                    answers = questionsDAO.getOptions(curr.getId());
                }
                questionAnswerOptionMap.putIfAbsent(curr, answers);
            }
            req.setAttribute("quiz_id", quiz_id);
            req.setAttribute("question_answers", questionAnswerOptionMap);
            RequestDispatcher rd = req.getRequestDispatcher("edit_quiz.jsp");
            rd.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
