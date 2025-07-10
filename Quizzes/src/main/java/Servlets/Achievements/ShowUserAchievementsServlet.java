package Servlets.Achievements;

import DAO.AchievementsDAO;
import DAO.DatabaseConnection;
import bean.Achievement;
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
import java.util.List;

@WebServlet("/ShowUserAchievementsServlet")
public class ShowUserAchievementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) req.getSession().getAttribute("user");

        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");

        try{
            AchievementsDAO aDAO = new AchievementsDAO(connection);
            List<Achievement> achievements = aDAO.getUserAchievements(user.getUserId());
            req.setAttribute("achievements", achievements);
            RequestDispatcher rd = req.getRequestDispatcher("achievements-user.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();

            req.setAttribute("errorMessage", "Error loading achievements.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
