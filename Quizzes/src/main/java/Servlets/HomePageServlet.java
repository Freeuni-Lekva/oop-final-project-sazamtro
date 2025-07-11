package Servlets;

import DAO.AnnouncementDAO;
import DAO.AchievementsDAO;
import DAO.QuizDAO;

import bean.Achievement;
import bean.Announcement;
import bean.Quiz;
import bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/HomePageServlet")
public class HomePageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get current user from session
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");

        // Get DB connection from context
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");

        // Determine which view to show
        String view = req.getParameter("view");
        if (view == null) {
            view = "announcements";  // default view
        }

        try {
            switch (view) {
                case "announcements":
                    AnnouncementDAO announcementDAO = new AnnouncementDAO(connection);
                    List<Announcement> announcements = announcementDAO.getAllAnnouncements();
                    req.setAttribute("announcements", announcements);
                    req.setAttribute("showAnnouncements", true);
                    break;

                case "achievements":
                    AchievementsDAO achievementsDAO = new AchievementsDAO(connection);
                    List<Achievement> friendsAchievements = achievementsDAO.getFriendsAchievements(user.getUserId());
                    req.setAttribute("friendsAchievements", friendsAchievements);
                    req.setAttribute("showFriendsAchievements", true);
                    break;

                case "quizzes":
                    QuizDAO quizzesDAO = new QuizDAO(connection);
                    List<Quiz> quizzes = quizzesDAO.getAllQuizzes();
                    req.setAttribute("quizzes", quizzes);
                    req.setAttribute("showAllQuizzes", true);
                    break;

                default:
                    // If unknown view param, fallback to announcements
                    AnnouncementDAO defaultDAO = new AnnouncementDAO(connection);
                    List<Announcement> defaultAnnouncements = defaultDAO.getAllAnnouncements();
                    req.setAttribute("announcements", defaultAnnouncements);
                    req.setAttribute("showAnnouncements", true);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // You can set empty lists or error messages here if you want
            req.setAttribute("errorMessage", "Failed to load data. Please try again later.");
        }

        // Forward to homepage.jsp with data set
        req.getRequestDispatcher("/homepage.jsp").forward(req, resp);
    }
}
