package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.QuizDAO;
import DAO.UserDAO;
import bean.Quiz;
import bean.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;


@WebServlet("/quizzes/show")
public class ShowOneQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        String path = req.getPathInfo();
        int quiz_id = Integer.parseInt(req.getParameter("quizId"));
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            Quiz q = qDAO.getOneQuiz(quiz_id);

            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.getUserById(q.getCreator_id());

            req.setAttribute("quiz", q);
            req.setAttribute("creator", user);
            RequestDispatcher rd = req.getRequestDispatcher("/single_quiz_page.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
