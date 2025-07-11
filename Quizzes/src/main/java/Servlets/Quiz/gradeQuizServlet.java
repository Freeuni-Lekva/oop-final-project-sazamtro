package Servlets.Quiz;

import DAO.*;
import bean.*;
import bean.Questions.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/gradeAttempt")
public class gradeQuizServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int attemptId = Integer.parseInt(req.getParameter("attemptId"));

        Connection conn = (Connection) req.getServletContext().getAttribute("DBConnection");
        QuizDAO quizDAO = new QuizDAO(conn);
        QuestionsDAO questionsDAO = new QuestionsDAO(conn);
//        UserAnswersDAO userAnswersDAO = new UserAnswersDAO(conn);
        QuizAttemptDAO quizAttemptDAO = new QuizAttemptDAO(conn);

        try {
            // Get attempt info
            QuizAttempt attempt = quizAttemptDAO.getAttempt(attemptId);
            int quizId = attempt.getQuizId();

            // Get all questions in the quiz
            List<Question> questions = quizDAO.getQuizQuestions(quizId);
//            Set<Integer> attemptQuestions = quizAttemptDAO.getAttemptQuestions(attemptId);
//            questions.removeIf(question -> !attemptQuestions.contains(question.getId()));

            // Prepare maps to send to JSP
            Map<Integer, List<AnswerOption>> optionsMap = new HashMap<>();
            Map<Integer, List<String>> correctAnswersMap = new HashMap<>();
            Map<Integer, List<String>> userAnswersMap = new HashMap<>();

            for (Question q : questions) {
                int qid = q.getId();
                String type = q.getQuestionType().name();

                // Options for MCQ / Multi-Select
                if (q.hasChoices()) {
                    List<AnswerOption> options = questionsDAO.getOptions(qid);
                    optionsMap.put(qid, options);

                    List<String> correct = new ArrayList<>();
                    for (AnswerOption opt : options) {
                        if (opt.isCorrect()) correct.add(opt.getAnswerText());
                    }
                    correctAnswersMap.put(qid, correct);
                }
                // Correct answers for text/image/fill-in
                else {
                    List<String> corrects = new ArrayList<>();
                    corrects.add(questionsDAO.getCorrectAnswerText(qid));
                    correctAnswersMap.put(qid, corrects);
                }

                // User responses
                List<String> userResponses = quizAttemptDAO.getResponsesByAttemptAndQuestion(attemptId, qid);
                userAnswersMap.put(qid, userResponses);
            }

            // Send everything to JSP
            req.setAttribute("attemptId", attemptId);
            req.setAttribute("questions", questions);
            req.setAttribute("options", optionsMap);
            req.setAttribute("correctAnswers", correctAnswersMap);
            req.setAttribute("userAnswers", userAnswersMap);

            req.getRequestDispatcher("/gradeQuiz.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Failed to load grading data", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int attemptId = Integer.parseInt(req.getParameter("attemptId"));
        int score = Integer.parseInt(req.getParameter("score"));
        Connection conn = (Connection) req.getServletContext().getAttribute("DBConnection");
        QuizAttemptDAO attemptDAO = new QuizAttemptDAO(conn);
        try {
            attemptDAO.updateScore(attemptId, score);
            int quizId = attemptDAO.getAttempt(attemptId).getQuizId();
            resp.sendRedirect(req.getContextPath() + "/gradeOverview?quizId=" + quizId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
