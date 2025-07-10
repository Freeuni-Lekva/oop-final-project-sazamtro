package Servlets.Achievements;

import DAO.AchievementsDAO;
import DAO.DatabaseConnection;
import bean.Achievement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/users/*")
public class ShowUserAchievementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        String path = req.getPathInfo();
        int user_id = Integer.parseInt(path.substring(1));
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        try{
            AchievementsDAO aDAO = new AchievementsDAO(connection);
            List<Achievement> userAchievements = aDAO.getUserAchievements(user_id);
            req.setAttribute("userAchievements", userAchievements);
            RequestDispatcher rd = req.getRequestDispatcher("achievement_list.jsp");
            rd.forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
