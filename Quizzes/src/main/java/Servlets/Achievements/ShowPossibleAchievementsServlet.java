package Servlets.Achievements;

import DAO.AchievementsDAO;
import DAO.DatabaseConnection;
import bean.Achievement;

import java.sql.SQLException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class ShowPossibleAchievementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try {
            AchievementsDAO aDAO = new AchievementsDAO(connection);
            List<Achievement> allAchievements = aDAO.getAllAchievements();
            req.setAttribute("achievements", allAchievements);
            RequestDispatcher rd = req.getRequestDispatcher("achievement_list.jsp");
            rd.forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
