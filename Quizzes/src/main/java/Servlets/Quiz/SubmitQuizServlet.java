package Servlets.Quiz;

import DAO.*;
import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Quiz;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/quizzes/*/submit")
public class SubmitQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }
        int user_id = user.getUserId();

        String path = req.getPathInfo();
        String[] pathParts = path.split("/");
        int quiz_id = Integer.parseInt(pathParts[1]);

        LocalDateTime startTime = (LocalDateTime) session.getAttribute("start_time");
        LocalDateTime endTime = LocalDateTime.now();
        long seconds = Duration.between(startTime, endTime).getSeconds();

        boolean isPractice = (Boolean) session.getAttribute("is_practice");

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        QuizDAO quizDAO = new QuizDAO(connection);
        AnswerDAO answerDAO = new AnswerDAO(connection);
        AchievementsDAO achievementsDAO = new AchievementsDAO(connection);

        try {

            Quiz q = quizDAO.getOneQuiz(quiz_id);
            int score = 0;
            int attempt_id = -1;

            if(!q.checkIfMultipage()){

                List<Question> questions = quizDAO.getQuizQuestions(quiz_id);
                Map<Integer, String[]> userAnswers = new HashMap<>();

                for(Question curr : questions){
                    String[] userAnswer = req.getParameterValues("q_" + curr.getId());
                    for (String currAns : userAnswer) {
                        if (answerDAO.checkAnswer(curr.getId(), currAns)) {
                            score = score + 1;
                        }
                    }
                    userAnswers.put(curr.getId(), userAnswer);
                }

                attempt_id = quizDAO.insertAttempt(user.getUserId(), quiz_id, score, seconds, isPractice);
                insertAnswers(userAnswers, answerDAO, attempt_id);
            }
            else{
                @SuppressWarnings("unchecked")
                Map<Integer, String[]> userAnswers = (Map<Integer, String[]>)session.getAttribute("user_responses");
                score = (int) req.getSession().getAttribute("current_score");
                attempt_id = quizDAO.insertAttempt(user.getUserId(), quiz_id, score, seconds, isPractice);
                insertAnswers(userAnswers, answerDAO, attempt_id);
            }

             achievementsDAO.checkQuizMachine(user_id);
             achievementsDAO.checkPracticeMode(user_id);
             achievementsDAO.checkPerfectionist(user_id, quiz_id);
             achievementsDAO.checkNightOwl(user_id, attempt_id);
             
             req.setAttribute("quiz", q);
             req.setAttribute("score", score);
             req.getRequestDispatcher("/quiz_result.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertAnswers(Map<Integer, String[]> userAnswers, AnswerDAO answerDAO, int attempt_id) throws SQLException {
        for(Map.Entry<Integer, String[]> entry : userAnswers.entrySet()){
            Integer question_id = entry.getKey();
            String[] answer = entry.getValue();
            for (String curr : answer) {
                boolean isCorrect = answerDAO.checkAnswer(question_id, curr);
                answerDAO.insertUserAnswer(curr, attempt_id, question_id, isCorrect);
            }
        }
    }
}
