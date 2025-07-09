package Servlets.QuizAttempts;

import DAO.QuizAttemptDAO;
import bean.QuizAttempt;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/QuizAttemptsListForQuizServlet")
public class QuizAttemptsListForQuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String quizIdParam = request.getParameter("quizId");
        String filter = request.getParameter("filter");
        if (filter == null) filter = "all";

        if (quizIdParam == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp");
            return;
        }

        Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");
        QuizAttemptDAO dao = new QuizAttemptDAO(connection);

        try {
            List<QuizAttempt> attempts;

            switch (filter) {
                case "real":
                    attempts = dao.getRealAttemptsByQuiz(quizId);
                    break;

                case "practice":
                    attempts = dao.getPracticeAttemptsByQuiz(quizId);
                    break;

                case "all":
                default:
                    attempts = dao.getAttemptsByQuiz(quizId);
                    break;
            }

            request.setAttribute("attempts", attempts);
            request.setAttribute("quizId", quizId);
            request.setAttribute("filter", filter);

            request.getRequestDispatcher("/quizAttempts-quiz.jsp").forward(request, response);

        } catch (SQLException | javax.servlet.ServletException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
