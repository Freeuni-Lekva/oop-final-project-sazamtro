package Servlets.Quiz;

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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/quizzes/*")
public class StartQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        String path = req.getPathInfo();
        String[] pathParts = path.split("/");
        int quiz_id = Integer.parseInt(pathParts[1]);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        LocalDateTime startTime = LocalDateTime.now();
        req.setAttribute("start_time", startTime);
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            Quiz quiz = qDAO.getOneQuiz(quiz_id);
            List<Question> quizQuestions = qDAO.getQuizQuestions(quiz_id);
            Map<Question, List<AnswerOption>> questionAnswerOptionMap = new HashMap<>();
            QuestionsDAO questionsDAO = new QuestionsDAO(connection);

            if(!quiz.checkIfMultipage()){
                for(Question curr : quizQuestions){
                    List<AnswerOption> answers = new ArrayList<>();
                    if (curr.hasChoices()){
                        answers = questionsDAO.getOptions(curr.getId());
                    }
                    questionAnswerOptionMap.putIfAbsent(curr, answers);
                }
                req.setAttribute("quiz_id", quiz_id);
                req.setAttribute("question_answers", questionAnswerOptionMap);
                RequestDispatcher rd = req.getRequestDispatcher("start_quiz.jsp");
                rd.forward(req, resp);
            }

            else{
                HttpSession session = req.getSession();
                session.setAttribute("quiz_questions", quizQuestions);
                session.setAttribute("current_index", 0);
                session.setAttribute("current_score", 0);
                session.setAttribute("user_responses", new HashMap<Integer, String>());
                resp.sendRedirect("/quizzes/" + quiz_id + "/question");
            }

        } catch(SQLException e){
            throw new RuntimeException();
        }
    }
}