package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.FriendRequestDAO;
import bean.User;
import DAO.QuizDAO;
import bean.Quiz;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/showAllQuizzes")
public class ShowAllQuizzesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            List<Quiz> allQuizzes = qDAO.getAllQuizzes();
            req.setAttribute("quizzes", allQuizzes);

            User user = (User) req.getSession().getAttribute("user");
            FriendRequestDAO friendDAO = new FriendRequestDAO(connection);
            List<User> friends = friendDAO.getFriendsList(user);
            req.setAttribute("friends", friends);
            req.setAttribute("showAllQuizzes", true);
            RequestDispatcher rd = req.getRequestDispatcher("homepage.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
