package Servlets.Quiz;

import DAO.QuestionsDAO;
import DAO.QuizAttemptDAO;
import DAO.UserDAO;
import bean.QuizAttempt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/gradeOverview")
public class gradeOverviewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quizIdStr = req.getParameter("quizId");
        int quizId = Integer.parseInt(quizIdStr);
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");
        if (con == null) {
            throw new ServletException("Database connection not initialized.");
        }
        QuizAttemptDAO quizAttemptDAO = new QuizAttemptDAO(con);
        try {
            List<QuizAttempt> attempts = quizAttemptDAO.getAttemptsByQuiz(quizId);
            Map<QuizAttempt, String> map = new HashMap<>();
            UserDAO userDAO = new UserDAO(con);
            for(QuizAttempt attempt : attempts) {
                int userId = attempt.getUserId();
                String username = userDAO.getUserById(userId).getUsername();
                map.put(attempt, username);
            }
            req.setAttribute("attempts", map);
            req.getRequestDispatcher("gradeOverview.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
