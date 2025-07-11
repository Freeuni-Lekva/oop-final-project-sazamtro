package Servlets.Quiz;

import DAO.*;
import bean.Questions.AnswerOption;
import bean.Questions.Question;
import bean.Questions.QuestionType;
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

@WebServlet("/quizzes/submit")
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
        int quiz_id = Integer.parseInt(req.getParameter("id"));

        LocalDateTime startTime = (LocalDateTime) session.getAttribute("start_time");
        LocalDateTime endTime = LocalDateTime.now();
        double seconds = (double) Duration.between(startTime, endTime).getSeconds() / 60;

        boolean isPractice = (Boolean) session.getAttribute("practice");

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        QuizDAO quizDAO = new QuizDAO(connection);
        AnswerDAO answerDAO = new AnswerDAO(connection);
        AchievementsDAO achievementsDAO = new AchievementsDAO(connection);


        try {

            Quiz q = quizDAO.getOneQuiz(quiz_id);
            int score = 0;
            int maxScore = 0;
            int attempt_id = -1;

            if(!q.checkIfMultipage()){

                @SuppressWarnings("unchecked")
                List<Question> questions = (List<Question>)session.getAttribute("quiz_questions");  //quizDAO.getQuizQuestions(quiz_id);

                Map<Integer, String[]> userAnswers = new HashMap<>();

                for(Question curr : questions){
                    maxScore = maxScore + answerDAO.getCorrectAnswers(curr.getId()).size();
                    String[] userAnswer = req.getParameterValues("q_" + curr.getId());
                    if(userAnswer != null && userAnswer.length != 0){
                        for (String currAns : userAnswer) {
                            if (answerDAO.checkAnswer(curr.getId(), currAns)) {
                                score = score + 1;
                            }
                        }
                        userAnswers.put(curr.getId(), userAnswer);
                    }
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

            req.setAttribute("max_score", maxScore);
            req.setAttribute("score", score);
            req.setAttribute("quiz", q);
            req.getRequestDispatcher("/quiz_result.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertAnswers(Map<Integer, String[]> userAnswers, AnswerDAO answerDAO, int attempt_id) throws SQLException {
        for(Map.Entry<Integer, String[]> entry : userAnswers.entrySet()){
            Integer question_id = entry.getKey();
            String[] answer = entry.getValue();
            if (answer == null) continue;
            for (String curr : answer) {
                boolean isCorrect = answerDAO.checkAnswer(question_id, curr);
                answerDAO.insertUserAnswer(curr, attempt_id, question_id, isCorrect);
            }
        }
    }
}

