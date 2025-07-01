package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.QuizDAO;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/quizzes/*")
public class DeleteQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        String path = req.getPathInfo();
        String[] pathParts = path.split("/");
        int quiz_id = Integer.parseInt(pathParts[1]);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            qDAO.deleteQuiz(quiz_id);
            resp.sendRedirect("/quiz_deleted.jsp");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
