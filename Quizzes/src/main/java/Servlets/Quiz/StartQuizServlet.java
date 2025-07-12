package Servlets.Quiz;

import DAO.AnswerDAO;
import DAO.QuestionsDAO;
import DAO.QuizDAO;
import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Questions.QuestionType;
import bean.Quiz;
import bean.User;

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

@WebServlet("/quizzes/start")
public class StartQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }
        int user_id = user.getUserId();
        int quiz_id = Integer.parseInt(req.getParameter("id"));
        boolean isPractice = Boolean.parseBoolean(req.getParameter("practice"));
        session.setAttribute("practice", isPractice);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        LocalDateTime startTime = LocalDateTime.now();
        session.setAttribute("start_time", startTime);
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            AnswerDAO answerDAO = new AnswerDAO(connection);
            Quiz quiz = qDAO.getOneQuiz(quiz_id);
            req.setAttribute("quiz", quiz);
            List<Question> quizQuestions = qDAO.getQuizQuestions(quiz_id);
            if(isPractice){
                quizQuestions = qDAO.getQuestionsForPractice(quiz_id, user_id, answerDAO);
                if(quizQuestions.isEmpty() && qDAO.getUserAttempts(user_id, quiz_id).size() >= 3){
                    req.setAttribute("quiz", quiz);
                    RequestDispatcher rd = req.getRequestDispatcher("/finished.jsp");
                    rd.forward(req, resp);
                    return;
                }
            }
            Map<Question, List<AnswerOption>> questionAnswerOptionMap = new HashMap<>();
            QuestionsDAO questionsDAO = new QuestionsDAO(connection);

            if(!quiz.checkIfMultipage()){
                List<String> pictures = new ArrayList<>(quizQuestions.size());
                int i = 0;
                for(Question curr : quizQuestions){
                    if(curr.getQuestionType() == QuestionType.PICTURE_RESPONSE){
                        pictures.add(i, curr.getImageUrl());
                    }
                    i++;
                    List<AnswerOption> answers = new ArrayList<>();
                    answers = questionsDAO.getOptions(curr.getId());
                    questionAnswerOptionMap.putIfAbsent(curr, answers);
                }
                req.setAttribute("pictures", pictures);
                req.setAttribute("quiz_id", quiz_id);
                session.setAttribute("quiz_questions", quizQuestions);
                req.setAttribute("question_answers", questionAnswerOptionMap);
                RequestDispatcher rd = req.getRequestDispatcher("/start_quiz.jsp");
                rd.forward(req, resp);
            }

            else{
                req.setAttribute("quiz_id", quiz_id);
                session.setAttribute("quiz_id", quiz_id);
                session.setAttribute("quiz", quiz);
                session.setAttribute("start_time", startTime);
                session.setAttribute("quiz_questions", quizQuestions);
                session.setAttribute("current_index", 0);
                session.setAttribute("current_score", 0);
                session.setAttribute("user_responses", new HashMap<Integer, String[]>());
                resp.sendRedirect("/quizzes/question");
            }

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}