package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.QuizDAO;
import bean.Quiz;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class ShowAllQuizzesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);

        try(Connection connection = DatabaseConnection.getConnection()){
            List<Quiz> allQuizzes = QuizDAO.getAllQuizzes(connection);
            req.setAttribute("quizzes", allQuizzes);
            RequestDispatcher rd = req.getRequestDispatcher("quizzes_list.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
