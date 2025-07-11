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

@WebServlet("/quizzes/delete")
public class DeleteQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int quiz_id = Integer.parseInt(req.getParameter("id"));
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            String quizName = qDAO.getOneQuiz(quiz_id).getQuizTitle();
            qDAO.removeQuiz(quiz_id);
            req.setAttribute("quiz_title", quizName);
            req.getRequestDispatcher("/quiz_deleted.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
