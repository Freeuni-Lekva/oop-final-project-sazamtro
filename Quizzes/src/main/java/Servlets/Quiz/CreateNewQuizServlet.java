package Servlets.Quiz;

import DAO.DatabaseConnection;
import DAO.QuizDAO;
import bean.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/quizzes/new")
public class CreateNewQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        int creator_id = user.getUserId();
        boolean is_random = req.getParameter("random") != null;
        boolean is_multipage = req.getParameter("multipage") != null;
        boolean immediate_correction = req.getParameter("immediate_correction") != null;
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            QuizDAO qDAO = new QuizDAO(connection);
            int quiz_id =    qDAO.insertNewQuiz(title, description, creator_id,
                                                is_random, is_multipage, immediate_correction);
            req.setAttribute("quizId", quiz_id);
            int position = is_random?-1:1;
            req.setAttribute("position", position);
            RequestDispatcher rd = req.getRequestDispatcher("/AddQuestion.jsp");
            rd.forward(req, resp);
            //resp.sendRedirect("/quizzes/"+quiz_id+"/add-question");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
