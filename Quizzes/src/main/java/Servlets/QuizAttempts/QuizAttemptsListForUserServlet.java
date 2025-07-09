package Servlets.QuizAttempts;

import DAO.QuizAttemptDAO;
import bean.QuizAttempt;
import bean.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/QuizAttemptsListForUserServlet")
public class QuizAttemptsListForUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String filter = request.getParameter("filter");
        if (filter == null) filter = "all";

        int userId = ((User) session.getAttribute("user")).getUserId();
        Connection connection = (Connection) request.getServletContext().getAttribute("DBConnection");
        QuizAttemptDAO dao = new QuizAttemptDAO(connection);

        try {
            List<QuizAttempt> attempts;

            switch (filter) {
                case "real":
                    attempts = dao.getRealAttemptsByUser(userId);
                    break;

                case "practice":
                    attempts = dao.getPracticeAttemptsByUser(userId);
                    break;

                case "all":
                default:
                    attempts = dao.getAttemptsByUser(userId);
                    break;
            }

            request.setAttribute("attempts", attempts);
            request.setAttribute("filter", filter);

            request.getRequestDispatcher("/quizAttempts-user.jsp").forward(request, response);

        } catch (SQLException | javax.servlet.ServletException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
