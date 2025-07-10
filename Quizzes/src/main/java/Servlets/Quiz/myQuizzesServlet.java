package Servlets.Quiz;

import DAO.QuestionsDAO;
import DAO.QuizDAO;
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
import java.util.List;

@WebServlet("/myQuizzes")
public class myQuizzesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = (Connection) getServletContext().getAttribute("DBConnection");
        if (con == null) {
            throw new ServletException("Database connection not initialized.");
        }
        QuizDAO quizDAO = new QuizDAO(con);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();

        try {
            List<Quiz> quizzes = quizDAO.getUserQuizzes(userId);
            request.setAttribute("quizzes", quizzes);
            request.getRequestDispatcher("myQuizzes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException("couldn't get user quizzes");
        }
    }
}
